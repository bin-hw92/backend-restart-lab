import { execFileSync } from 'node:child_process';

const testUrl = process.env.TEST_DATABASE_URL;
if (!testUrl || new URL(testUrl).pathname !== '/nest_board_test') {
  throw new Error('테스트는 TEST_DATABASE_URL의 nest_board_test DB에서만 실행합니다.');
}
const env = { ...process.env, DATABASE_URL: testUrl, NODE_ENV: 'test' };
// 테스트용 DB에만 테이블을 준비합니다. reset/drop/truncate는 하지 않습니다.
execFileSync(process.execPath, ['node_modules/prisma/build/index.js', 'migrate', 'deploy'], { env, stdio: 'inherit' });
execFileSync(process.execPath, ['--test', 'test/board.e2e.mjs'], { env, stdio: 'inherit' });
