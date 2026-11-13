import { Injectable, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaMariaDb } from '@prisma/adapter-mariadb';
import { PrismaClient } from '../generated/prisma/client';

@Injectable()
export class PrismaService extends PrismaClient implements OnModuleInit, OnModuleDestroy {
  constructor(config: ConfigService) {
    const url = new URL(config.getOrThrow<string>('DATABASE_URL'));
    if (url.protocol !== 'mysql:' || url.pathname.length < 2) {
      throw new Error('DATABASE_URL에 MySQL 연결 주소와 DB 이름이 필요합니다.');
    }

    // 이름에 MariaDb가 있어도 이 어댑터는 MySQL에도 연결할 수 있습니다.
    const adapter = new PrismaMariaDb({
      host: url.hostname,
      port: Number(url.port || 3306),
      user: decodeURIComponent(url.username),
      password: decodeURIComponent(url.password),
      database: decodeURIComponent(url.pathname.slice(1)),
      connectionLimit: 5,
      timezone: 'Z',
      initSql: "SET time_zone = '+00:00'",
    });
    // PostgreSQL은 @prisma/adapter-pg의 PrismaPg({ connectionString })로 교체합니다.
    super({ adapter });
  }

  async onModuleInit() {
    await this.$connect(); // 시작할 때 연결을 확인
  }

  async onModuleDestroy() {
    await this.$disconnect(); // 종료할 때 연결 풀을 닫음
  }
}
