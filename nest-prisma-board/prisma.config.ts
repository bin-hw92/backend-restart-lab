import 'dotenv/config';
import { defineConfig, env } from 'prisma/config';

export default defineConfig({
  schema: 'prisma/schema.prisma',
  migrations: { path: 'prisma/migrations' },
  // Prisma 7에서는 연결 주소를 schema.prisma 대신 이 설정에서 읽습니다.
  datasource: { url: env('DATABASE_URL') },
});
