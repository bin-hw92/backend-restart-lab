import { BadRequestException, Injectable, NotFoundException } from '@nestjs/common';
import { Prisma } from '../generated/prisma/client';
import { PrismaService } from '../prisma/prisma.service';
import { CreatePostDto } from './dto/create-post.dto';
import { ListPostsDto } from './dto/list-posts.dto';
import { UpdatePostDto } from './dto/update-post.dto';

@Injectable()
export class PostsService {
  constructor(private readonly db: PrismaService) {}

  create(dto: CreatePostDto) {
    // Prisma가 INSERT SQL을 만들어 DB에 저장합니다.
    return this.db.post.create({ data: dto });
  }

  async list(query: ListPostsDto) {
    const { page, limit, q } = query;
    // MySQL의 대소문자 비교는 collation에 따릅니다. 이 posts 테이블은 utf8mb4_unicode_ci입니다.
    // PostgreSQL에서 대소문자를 무시하려면 contains와 mode: 'insensitive' 등을 검토합니다.
    const where: Prisma.PostWhereInput = q ? {
      OR: [{ title: { contains: q } }, { content: { contains: q } }],
    } : {};

    const [items, total] = await Promise.all([
      this.db.post.findMany({
        where,
        skip: (page - 1) * limit,
        take: limit,
        orderBy: [{ createdAt: 'desc' }, { id: 'desc' }],
      }),
      this.db.post.count({ where }),
    ]);
    // 두 조회를 함께 기다립니다. 동시 수정까지 같은 스냅샷으로 묶는 트랜잭션은 아닙니다.
    return { items, total, page, limit };
  }

  async findOne(id: number) {
    this.checkId(id);
    const post = await this.db.post.findUnique({ where: { id } });
    if (!post) throw new NotFoundException('게시글을 찾을 수 없습니다.');
    return post;
  }

  async update(id: number, dto: UpdatePostDto) {
    this.checkId(id);
    if (dto.title === undefined && dto.content === undefined && dto.author === undefined) {
      throw new BadRequestException('수정할 내용을 하나 이상 보내주세요.');
    }
    try {
      return await this.db.post.update({ where: { id }, data: dto });
    } catch (error) {
      this.handleMissing(error);
    }
  }

  async remove(id: number) {
    this.checkId(id);
    try {
      await this.db.post.delete({ where: { id } });
    } catch (error) {
      this.handleMissing(error);
    }
  }

  private checkId(id: number) {
    if (!Number.isInteger(id) || id < 1 || id > 2147483647) {
      throw new BadRequestException('id는 양의 정수(INT 범위)여야 합니다.');
    }
  }

  private handleMissing(error: unknown): never {
    // 변경 도중 글이 삭제된 경우도 Prisma의 P2025를 HTTP 404로 바꿉니다.
    if (error instanceof Prisma.PrismaClientKnownRequestError && error.code === 'P2025') {
      throw new NotFoundException('게시글을 찾을 수 없습니다.');
    }
    throw error;
  }
}
