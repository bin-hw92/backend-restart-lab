# Study API — 1단계

메모리에 학습 항목을 저장하는 Spring Boot REST API입니다. 서비스가 재시작되면 데이터가 사라집니다.

## 실행

```powershell
mvn spring-boot:run
```

## API

| 메서드 | 경로 | 결과 |
| --- | --- | --- |
| GET | `/api/study-tasks` | 전체 목록, 200 |
| GET | `/api/study-tasks/{id}` | 한 항목, 200 / 없으면 404 |
| POST | `/api/study-tasks` | 생성, 201 / 제목이 비면 400 |
| PATCH | `/api/study-tasks/{id}/complete` | 완료, 200 / 없으면 404 |
| DELETE | `/api/study-tasks/{id}` | 삭제, 204 / 없으면 404 |

POST 요청 본문:

```json
{"title":"Java collection 복습"}
```

## 다음에 직접 구현할 기능

1. 제목 길이 제한과 오류 응답 형식을 정합니다.
2. 완료 항목만 필터링하는 조회를 만듭니다.
3. DB 테이블을 설계하고 메모리 저장을 교체합니다.
4. React 화면에서 목록·생성·완료 흐름을 연결합니다.

각 기능을 추가할 때 `Controller → Service → 저장소`의 책임을 왜 나누는지 설명할 수 있어야 합니다.

