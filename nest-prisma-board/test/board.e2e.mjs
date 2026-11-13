import assert from 'node:assert/strict';
import { after, before, test } from 'node:test';
import { randomUUID } from 'node:crypto';
import { createRequire } from 'node:module';

if (new URL(process.env.DATABASE_URL).pathname !== '/nest_board_test') {
  throw new Error('실습 DB를 보호하기 위해 테스트 DB 이름을 확인하세요.');
}
const require = createRequire(import.meta.url);
const { createApp } = require('../dist/app.js');
const { PrismaService } = require('../dist/prisma/prisma.service.js');
const tag = `test-${randomUUID()}`;
const createdIds = new Set();
let app;
let base;
let post;

async function request(path, method = 'GET', body) {
  const response = await fetch(`${base}${path}`, {
    method,
    headers: body === undefined ? {} : { 'Content-Type': 'application/json' },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  const data = response.status === 204 ? null : await response.json();
  return { status: response.status, data };
}

before(async () => {
  app = await createApp(false);
  await app.listen(0, '127.0.0.1');
  base = await app.getUrl();
});

after(async () => {
  if (!app) return;
  try {
    // 이번 실행에서 만든 행만 지웁니다. 이전 데이터는 보존합니다.
    await app.get(PrismaService).post.deleteMany({ where: { id: { in: [...createdIds] } } });
  } finally {
    await app.close();
  }
});

test('등록 → 한글·공백 정리·UTC 날짜 응답', async () => {
  const result = await request('/api/posts', 'POST', { title: `  ${tag} 게시판  `, content: '본문입니다.', author: ' 학습자 ' });
  assert.equal(result.status, 201);
  post = result.data;
  createdIds.add(post.id);
  assert.equal(post.title, `${tag} 게시판`);
  assert.equal(post.author, '학습자');
  assert.match(post.createdAt, /Z$/);
});

test('상세 조회 → DB에 저장된 내용 반환', async () => {
  const result = await request(`/api/posts/${post.id}`);
  assert.equal(result.status, 200);
  assert.equal(result.data.content, '본문입니다.');
});

test('서버를 다시 만들어도 DB의 게시글 유지', async () => {
  await app.close();
  app = await createApp(false);
  await app.listen(0, '127.0.0.1');
  base = await app.getUrl();
  const result = await request(`/api/posts/${post.id}`);
  assert.equal(result.status, 200);
  assert.equal(result.data.title, post.title);
});

test('목록 → 검색·페이지 나누기·MySQL 대소문자 비교', async () => {
  for (let i = 0; i < 2; i++) {
    const result = await request('/api/posts', 'POST', { title: `${tag} ABC ${i}`, content: '검색용', author: '테스트' });
    assert.equal(result.status, 201);
    createdIds.add(result.data.id);
  }
  const first = await request(`/api/posts?q=${tag}&limit=2&page=1`);
  const second = await request(`/api/posts?q=${tag}&limit=2&page=2`);
  assert.equal(first.data.total, 3);
  assert.equal(first.data.items.length, 2);
  assert.equal(second.data.items.length, 1);
  assert.equal(new Set([...first.data.items, ...second.data.items].map(item => item.id)).size, 3);
  const folded = await request(`/api/posts?q=${encodeURIComponent(tag.toUpperCase() + ' abc')}`);
  assert.equal(folded.data.total, 2);
});

test('일부 수정 → 보내지 않은 필드는 유지', async () => {
  const result = await request(`/api/posts/${post.id}`, 'PATCH', { title: `${tag} 수정` });
  assert.equal(result.status, 200);
  assert.equal(result.data.title, `${tag} 수정`);
  assert.equal(result.data.content, '본문입니다.');
  assert.equal(result.data.author, '학습자');
});

test('입력 검증 → 공백·필수값 누락·타입 오류·초과 길이·추가 필드 거절', async () => {
  const valid = { title: '제목', content: '본문', author: '학습자' };
  for (const body of [
    { ...valid, title: '   ' },
    { title: '제목', content: '본문' },
    { ...valid, title: 123 },
    { ...valid, title: 'a'.repeat(101) },
    { ...valid, author: 'a'.repeat(41) },
    { ...valid, content: 'a'.repeat(10001) },
    { ...valid, admin: true },
  ]) {
    assert.equal((await request('/api/posts', 'POST', body)).status, 400);
  }
});

test('수정 검증 → null·빈 내용·빈 수정 요청 거절', async () => {
  for (const body of [{ title: null }, { content: ' ' }, {}, { id: post.id }]) {
    assert.equal((await request(`/api/posts/${post.id}`, 'PATCH', body)).status, 400);
  }
});

test('요청 검증 → 잘못된 ID·페이지·검색 타입 거절', async () => {
  for (const path of ['/api/posts/abc', '/api/posts/0', '/api/posts/-1', '/api/posts/2147483648',
    '/api/posts?page=0', '/api/posts?limit=101', '/api/posts?page=1.2', '/api/posts?q[a]=b']) {
    assert.equal((await request(path)).status, 400, path);
  }
});

test('삭제 → 204, 이후 조회·수정·삭제는 404', async () => {
  assert.equal((await request(`/api/posts/${post.id}`, 'DELETE')).status, 204);
  assert.equal((await request(`/api/posts/${post.id}`)).status, 404);
  assert.equal((await request(`/api/posts/${post.id}`, 'PATCH', { title: '수정' })).status, 404);
  assert.equal((await request(`/api/posts/${post.id}`, 'DELETE')).status, 404);
});

test('실습용 HTML 제공', async () => {
  const response = await fetch(base);
  assert.equal(response.status, 200);
  assert.match(await response.text(), /NestJS 게시판 실습/);
});
