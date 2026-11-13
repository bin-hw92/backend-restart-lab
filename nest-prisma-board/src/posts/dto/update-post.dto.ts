import { Transform } from 'class-transformer';
import { IsString, Length, ValidateIf } from 'class-validator';

export class UpdatePostDto {
  // undefined(미전달)는 허용하지만 null과 빈 문자열은 거절합니다.
  @ValidateIf((_, value) => value !== undefined)
  @Transform(({ value }) => typeof value === 'string' ? value.trim() : value)
  @IsString()
  @Length(1, 100)
  title?: string;

  @ValidateIf((_, value) => value !== undefined)
  @Transform(({ value }) => typeof value === 'string' ? value.trim() : value)
  @IsString()
  @Length(1, 10000)
  content?: string;

  @ValidateIf((_, value) => value !== undefined)
  @Transform(({ value }) => typeof value === 'string' ? value.trim() : value)
  @IsString()
  @Length(1, 40)
  author?: string;
}
