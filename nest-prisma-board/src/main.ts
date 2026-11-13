import { createApp } from './app';

async function bootstrap() {
  const app = await createApp();
  const port = Number(process.env.PORT ?? 3100);
  // 로그인 없는 학습 예제이므로 내 PC에서만 접속하도록 엽니다.
  await app.listen(port, '127.0.0.1');
  console.log(`게시판: http://127.0.0.1:${port}`);
}

bootstrap().catch(() => {
  console.error('서버 시작 실패: MySQL 실행 여부와 .env 설정을 확인하세요.');
  process.exitCode = 1;
});
