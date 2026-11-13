import 'reflect-metadata';
import { ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { NestExpressApplication } from '@nestjs/platform-express';
import { join } from 'node:path';
import { AppModule } from './app.module';

export async function createApp(logger: false | undefined = undefined) {
  const app = await NestFactory.create<NestExpressApplication>(AppModule, { logger });
  app.setGlobalPrefix('api');
  app.useGlobalPipes(new ValidationPipe({
    transform: true,             // 요청 값을 DTO 클래스와 숫자 등으로 변환
    whitelist: true,             // DTO에 적힌 필드만 허용
    forbidNonWhitelisted: true,  // 예상하지 못한 필드는 400으로 거절
  }));
  app.useStaticAssets(join(__dirname, '..', 'public'));
  app.enableShutdownHooks();
  return app;
}
