# NestJS + Prisma + MySQL 게시판

2026-11-13 면접 준비용으로 AI와 함께 만든 완성 샘플입니다. 사용자가 직접 풀어 완성한 과제나 회사의 실제 CRM 구현은 아닙니다. 기존 Java·Spring 실습과 별도 폴더로 구성했습니다.

## 먼저 이름 구분하기

| 이름 | 읽는 법 | 하는 일 |
| --- | --- | --- |
| NestJS | 네스트 제이에스, 줄여서 네스트 | Node.js 기반 백엔드 프레임워크. API·검증·업무 로직을 모듈로 구성 |
| Nuxt | 넉스트 | Vue 기반 웹 프레임워크. 화면·SSR·서버 API 등 제공 |
| Next.js | 넥스트 제이에스 | React 기반 웹 프레임워크. 화면·SSR·서버 기능 등 제공 |
| Prisma | 프리즈마 | 코드로 DB 조회·저장·스키마 변경을 다루는 ORM 도구 |
| PostgreSQL | 포스트그레스큐엘 등으로 읽음 | MySQL과 같은 관계형 DB의 한 종류 |

질문의 `nust`는 Nuxt로 추정했습니다. Nest와 Nuxt는 서로 다른 제품이며, Vue가 익숙하지 않아도 이 Nest 실습은 진행할 수 있습니다.

## 바로 실행하기

Node.js 22.12 이상인 22 계열 또는 24 이상, Docker가 필요합니다. 실제 검증은 Node 24에서 합니다. NestJS는 11, Prisma CLI·Client·MySQL 어댑터는 모두 7.10.1을 사용합니다. 정확한 설치 버전은 package-lock.json에 고정합니다.

저장소 루트에서:

```sh
cd nest-prisma-board
npm ci
npm run db:setup
npm run db:migrate
npm run start:dev
```

브라우저에서 <http://127.0.0.1:3100>을 열면 글 등록·목록·상세·수정·삭제를 눌러볼 수 있습니다. 서버 종료는 터미널에서 `Ctrl+C`입니다.

- `db:setup`: 기존 Docker의 `news-backend-mysql-1`을 사용합니다. 정지되어 있으면 시작하고, MySQL 준비를 기다립니다.
- 이 PC의 MySQL 포트는 **3307**입니다. 컨테이너 내부 포트는 3306이며, 스크립트가 호스트 연결 포트를 조회합니다.
- 새 DB `nest_board`, 테스트 DB `nest_board_test`, 두 DB에만 권한이 있는 계정을 만듭니다. 기존 서비스 DB를 초기화하지 않습니다.
- 접속값은 Git에서 제외한 `.env`에 저장합니다. 이미 `.env`가 있으면 계정과 설정을 덮어쓰지 않습니다.
- 다른 컨테이너라면 `MYSQL_CONTAINER=컨테이너이름 npm run db:setup`으로 지정합니다. 스크립트는 MySQL 8의 `MYSQL_ROOT_PASSWORD` 설정을 가진 로컬 컨테이너를 기준으로 합니다.
- 자동 설정을 쓰지 않으면 `.env.example`을 참고해 별도 DB·계정과 `.env`를 준비합니다. 예시의 `CHANGE_ME`는 실제 비밀번호가 아닙니다.

서버는 인증 없는 학습용으로 **127.0.0.1에만 연결**합니다. 이 예제의 작성자 이름은 로그인 계정이나 수정 권한 확인 기능이 아닙니다.

## 읽는 순서

```text
브라우저에서 글 저장
  → POST /api/posts
  → ValidationPipe + DTO: 실제 입력값 검사
  → PostsController: URL과 Service 연결
  → PostsService: 게시판 처리 규칙
  → PrismaService: DB 연결과 Prisma Client
  → MySQL의 posts 테이블
  → JSON 응답 → 화면 표시
```

| 순서 | 파일 | 확인할 것 |
| --- | --- | --- |
| 1 | [src/main.ts](src/main.ts), [src/app.ts](src/app.ts) | 서버 시작, 공통 검증, API 접두사 |
| 2 | [posts.controller.ts](src/posts/posts.controller.ts) | GET·POST·PATCH·DELETE가 어느 함수로 들어오는지 |
| 3 | [create-post.dto.ts](src/posts/dto/create-post.dto.ts) | 제목·본문·작성자 형식과 길이 검사 |
| 4 | [posts.service.ts](src/posts/posts.service.ts) | 저장·조회·수정·삭제, 없는 글 404 |
| 5 | [schema.prisma](prisma/schema.prisma) | 테이블 필드·자료형·인덱스 |
| 6 | [prisma.service.ts](src/prisma/prisma.service.ts) | 연결 설정, 시작·종료 때 연결 관리 |
| 7 | [posts.module.ts](src/posts/posts.module.ts) | Controller·Service·Prisma 모듈을 묶는 방법 |

`@Controller`는 요청 입구, `@Injectable`은 주입 가능한 서비스, `@Module`은 이들을 묶는 설정입니다. 생성자의 `private readonly posts: PostsService`는 Nest가 필요한 Service를 넣어주는 **DI(의존성 주입)**예요. 직접 매번 `new PostsService(...)`를 하지 않습니다.

MVC와 역할을 비교하면 Controller와 데이터 모델은 있지만, 이 샘플은 HTML 템플릿 대신 JSON API를 반환합니다. 업무 로직을 Service로 분리하는 패턴부터 보면 됩니다. `public/index.html`은 API를 호출해보는 간단한 화면이며 React·Nuxt 프로젝트가 아닙니다.

## API

| 기능 | 요청 | 정상 응답 |
| --- | --- | --- |
| 글 등록 | `POST /api/posts` | 201 + 게시글 |
| 목록 | `GET /api/posts?page=1&limit=10&q=검색어` | 200 + items·total·page·limit |
| 상세 | `GET /api/posts/1` | 200 + 게시글 |
| 일부 수정 | `PATCH /api/posts/1` | 200 + 수정된 게시글 |
| 삭제 | `DELETE /api/posts/1` | 204, 응답 본문 없음 |

```sh
curl -i http://127.0.0.1:3100/api/posts \
  -H 'Content-Type: application/json' \
  -d '{"title":"첫 글","content":"NestJS를 공부합니다.","author":"학습자"}'

curl 'http://127.0.0.1:3100/api/posts?page=1&limit=10'
```

등록 응답의 `id`로 [requests.http](requests.http)의 `@id`를 바꿔 조회·수정·삭제해도 됩니다. `.http` 실행에는 해당 기능을 제공하는 VS Code 확장이 필요하고, 확장 없이도 브라우저 실습 화면이나 curl을 사용할 수 있습니다.

- DTO의 타입 표기만으로 입력이 검사되는 것은 아닙니다. `ValidationPipe`와 `class-validator`가 실행 중 검사합니다.
- 빈 제목·공백만 있는 값·잘못된 타입·추가 필드는 400, 없는 글은 404입니다.
- PATCH에서 미전달 필드는 유지하지만 `null`, 빈 값, 수정할 필드가 전혀 없는 요청은 거절합니다.
- API 시간은 UTC의 ISO 문자열로 받고, 실습 화면은 `toLocaleString()`으로 사용자 시간대에 표시합니다.
- Prisma의 `create`, `findMany`, `update`, `delete`가 INSERT·SELECT·UPDATE·DELETE에 대응합니다. ORM을 사용해도 SQL·인덱스·DB 자료형은 알아야 합니다.

## MySQL과 PostgreSQL 차이를 어디에 표시했나

| 위치 | MySQL 예제 | PostgreSQL로 변경할 때 |
| --- | --- | --- |
| schema.prisma의 provider | `mysql` | `postgresql` |
| .env의 연결 URL | `mysql://...:3307/...` | `postgresql://...:5432/...` 등 실제 주소 |
| PrismaService 어댑터 | `PrismaMariaDb` — MySQL도 지원 | `@prisma/adapter-pg`의 `PrismaPg` |
| ID | MySQL AUTO_INCREMENT | Prisma 표현은 같아도 생성되는 증가 ID SQL은 다름 |
| 날짜 | `@db.DateTime(3)`, UTC 세션 사용 | `@db.Timestamptz(3)` 등 시점 저장 자료형 검토 |
| 검색 | posts 테이블의 `utf8mb4_unicode_ci`는 대소문자를 구분하지 않음 | 기본 비교 방식·collation을 확인하고 `mode: 'insensitive'` 등 검토 |
| 마이그레이션 | MySQL용 SQL | 별도의 PostgreSQL용 이력과 데이터 이관 계획 필요 |

관련 코드에 짧은 주석도 넣었습니다. **provider만 바꿔 기존 DB와 데이터가 자동으로 옮겨지지는 않습니다.** MySQL 마이그레이션을 PostgreSQL에 그대로 적용할 수 없으며, 기존 이력을 보존한 별도 작업에서 새 DB용 마이그레이션과 데이터 이전을 준비해야 합니다.

여기서 사용한 일반 CRUD의 Controller·DTO·Service 흐름은 대부분 유지할 수 있지만, DB별 자료형·검색·SQL·어댑터는 검토해야 합니다. PostgreSQL 서버 연결과 이관은 이 예제에서 실행하지 않습니다.

## 검증과 다음 연습

```sh
npm run build
npm test
npm run db:status
```

`npm test`는 실제 MySQL의 `nest_board_test`에 마이그레이션을 적용하고 HTTP 요청으로 확인합니다. 실습 DB와 테스트 DB를 분리하며, 테스트가 만든 글 ID만 정리합니다. DB 전체 reset·drop·truncate는 하지 않습니다.

검사 범위: CRUD, 서버 재시작 뒤 데이터 유지, 한글·공백 정리, 검색·페이지 나누기, 잘못된 값·null 수정 거절, 없는 글 404, HTML 제공. 브라우저 버튼의 실제 클릭 동작과 PostgreSQL은 별도 검증 범위입니다.

`db:migrate`는 이미 제공한 SQL을 적용하는 **migrate deploy**입니다. schema.prisma를 수정했다고 이 명령이 새 SQL을 만들어주지는 않습니다. 새 스키마 변경을 연습할 때는 별도 개발·shadow DB와 권한을 준비한 뒤 `migrate dev`를 사용하는 흐름을 추가로 학습하면 됩니다.

다음에는 한 번에 하나씩 바꿔보세요.

1. Controller → Service → Prisma에 `console.log`를 하나씩 넣어 저장 요청 순서 확인.
2. 목록 기본 개수를 10개에서 5개로 변경하고 DTO와 응답 확인.
3. 작성자 검색 조건을 Service의 OR에 추가.
4. 이후 로그인·작성자 권한·댓글 관계·스키마 변경을 별도 단계로 확장.

## 공식 참고

- [NestJS의 Prisma 연동](https://docs.nestjs.com/recipes/prisma): Prisma 7·생성 클라이언트·드라이버 어댑터 설정. 이 샘플은 학습을 위해 CommonJS로 모듈 형식을 명시했습니다.
- [NestJS 입력 검증](https://docs.nestjs.com/techniques/validation)
- [Prisma MySQL](https://www.prisma.io/docs/orm/v7/core-concepts/supported-databases/mysql)
- [Prisma 마이그레이션 DB별 제한](https://www.prisma.io/docs/orm/v7/prisma-migrate/understanding-prisma-migrate/limitations-and-known-issues)
- [Prisma 검색의 대소문자 구분](https://www.prisma.io/docs/orm/v6/prisma-client/queries/case-sensitivity): 해당 링크는 v6 문서이므로 원리 참고용이며, 실제 v7 동작은 MySQL 통합 테스트로 확인합니다.
- [Nuxt 소개](https://nuxt.com/docs/4.x/getting-started/introduction)
- [Next.js 소개](https://nextjs.org/docs)
