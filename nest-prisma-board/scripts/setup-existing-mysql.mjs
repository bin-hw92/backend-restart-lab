import { execFileSync } from 'node:child_process';
import { randomBytes } from 'node:crypto';
import { existsSync, writeFileSync } from 'node:fs';

const docker = process.env.DOCKER_BIN ?? (existsSync('/Applications/Docker.app/Contents/Resources/bin/docker')
  ? '/Applications/Docker.app/Contents/Resources/bin/docker' : 'docker');
const container = process.env.MYSQL_CONTAINER ?? 'news-backend-mysql-1';
if (!/^[a-zA-Z0-9_.-]+$/.test(container)) throw new Error('컨테이너 이름을 확인하세요.');

if (existsSync('.env')) {
  console.log('.env가 이미 있어 설정과 기존 계정을 보존했습니다.');
  process.exit(0);
}

const runDocker = (args, options = {}) => execFileSync(docker, args, {
  encoding: 'utf8', stdio: ['pipe', 'pipe', 'pipe'], ...options,
});
if (runDocker(['inspect', '--format', '{{.State.Running}}', container]).trim() !== 'true') {
  runDocker(['start', container]);
}

// 기존 컨테이너 안의 root 환경변수를 사용합니다. 비밀번호를 출력하거나 파일에 복사하지 않습니다.
const sql = (text) => runDocker(['exec', '-i', container, 'sh', '-c',
  'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" exec mysql --protocol=socket --user=root --batch --skip-column-names --default-character-set=utf8mb4',
], { input: text });

let ready = false;
for (let attempt = 0; attempt < 20; attempt++) {
  try { sql('SELECT 1;'); ready = true; break; }
  catch { await new Promise(resolve => setTimeout(resolve, 1000)); }
}
if (!ready) throw new Error('MySQL 시작 상태와 컨테이너 root 설정을 확인하세요.');

const existing = sql("SELECT schema_name FROM information_schema.schemata WHERE schema_name IN ('nest_board', 'nest_board_test');").trim();
if (existing) throw new Error('같은 이름의 DB가 이미 있습니다. 기존 DB와 .env를 확인하세요. 자동 변경하지 않습니다.');

const ports = JSON.parse(runDocker(['inspect', '--format', '{{json .NetworkSettings.Ports}}', container]));
const port = ports['3306/tcp']?.[0]?.HostPort;
if (!port || !/^\d+$/.test(port)) throw new Error('MySQL의 호스트 연결 포트가 필요합니다.');
const username = `nest_board_${randomBytes(4).toString('hex')}`;
const password = randomBytes(24).toString('hex');

// 기존 서비스 DB는 건드리지 않고, 실습 DB 두 개에만 이 계정의 권한을 줍니다.
sql(`
CREATE DATABASE nest_board CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE nest_board_test CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE USER '${username}'@'%' IDENTIFIED BY '${password}';
GRANT ALL PRIVILEGES ON nest_board.* TO '${username}'@'%';
GRANT ALL PRIVILEGES ON nest_board_test.* TO '${username}'@'%';
`);
const base = `mysql://${username}:${password}@127.0.0.1:${port}`;
writeFileSync('.env', `DATABASE_URL="${base}/nest_board"\nTEST_DATABASE_URL="${base}/nest_board_test"\nPORT=3100\n`, { flag: 'wx', mode: 0o600 });
console.log(`전용 DB nest_board / nest_board_test와 .env를 만들었습니다. MySQL 포트: ${port}`);
