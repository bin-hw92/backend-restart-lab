import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { PostsModule } from './posts/posts.module';

@Module({
  // .env를 읽고, 하위 모듈에서 설정값을 주입받을 수 있게 합니다.
  imports: [ConfigModule.forRoot({ isGlobal: true }), PostsModule],
})
export class AppModule {}
