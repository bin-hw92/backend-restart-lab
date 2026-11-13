import { Body, Controller, Delete, Get, HttpCode, Param, ParseIntPipe, Patch, Post, Query } from '@nestjs/common';
import { CreatePostDto } from './dto/create-post.dto';
import { ListPostsDto } from './dto/list-posts.dto';
import { UpdatePostDto } from './dto/update-post.dto';
import { PostsService } from './posts.service';

@Controller('posts')
export class PostsController {
  // Nest가 Service 객체를 만들어 주입합니다(DI: 의존성 주입).
  constructor(private readonly posts: PostsService) {}

  @Post()
  create(@Body() dto: CreatePostDto) {
    return this.posts.create(dto);
  }

  @Get()
  list(@Query() query: ListPostsDto) {
    return this.posts.list(query);
  }

  @Get(':id')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.posts.findOne(id);
  }

  @Patch(':id')
  update(@Param('id', ParseIntPipe) id: number, @Body() dto: UpdatePostDto) {
    return this.posts.update(id, dto);
  }

  @Delete(':id')
  @HttpCode(204)
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.posts.remove(id);
  }
}
