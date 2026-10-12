# 공부할 일 기록 API

Spring Boot를 다시 익히려고 만든 작은 API입니다. 공부할 일을 등록하고, 목록을 보거나 완료 처리할 수 있습니다. 지금은 메모리에 저장하기 때문에 서버를 끄면 데이터가 사라집니다.

```powershell
mvn spring-boot:run
```

## 현재 API

| 메서드 | 주소 | 동작 |
| --- | --- | --- |
| GET | `/api/study-tasks` | 전체 목록 보기 |
| GET | `/api/study-tasks/{id}` | 한 건 보기 |
| POST | `/api/study-tasks` | 새 항목 등록 |
| PATCH | `/api/study-tasks/{id}/complete` | 완료 처리 |
| DELETE | `/api/study-tasks/{id}` | 삭제 |

등록할 때 보내는 JSON은 아래와 같습니다.

```json
{"title":"Java 컬렉션 복습"}
```

없는 ID는 404, 빈 제목은 400으로 응답하도록 해두었습니다. 등록하면 201, 삭제하면 204를 반환합니다.

## 다음에 해볼 것

1. 제목 길이 제한과 오류 응답 형식 정하기
2. 완료한 항목만 조회하기
3. DB 테이블을 만들고 메모리 저장 교체하기
4. React 화면에서 목록·등록·완료 처리 연결하기

기능을 추가하면서 Controller와 Service를 왜 나눴는지도 같이 정리해 보려고 합니다.

