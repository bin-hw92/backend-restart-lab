# 6주 학습 로드맵

기준: 주 8~10시간. 채용 지원과 병행할 수 있도록 한 번에 작은 결과를 남깁니다. 실제 속도에 따라 한 주를 늘려도 됩니다.

| 주차 | 집중할 내용 | 저장소에 남길 결과 | 완료 기준 |
| --- | --- | --- | --- |
| 1 | Java 21: 컬렉션, record, Optional, Stream, 예외, 날짜/시간 | `java-basics` 예제 2~3개 수정 | 각 자료구조를 고른 이유와 예외 처리 방식을 설명 |
| 2 | HTTP·REST, Spring Boot 구조, Controller/Service | `study-api` CRUD와 오류 응답 | 각 요청의 상태 코드와 요청·응답 JSON을 설명 |
| 3 | SQL 재복습, 테이블 설계, JDBC/JPA 비교 | 메모리 저장을 DB로 교체 | 재시작 후 데이터 유지, 조회 쿼리 설명 |
| 4 | 트랜잭션, 입력 검증, 페이지네이션, 동시성 기초 | 수정·삭제·목록 API 개선 | 잘못된 입력과 동시 수정 상황을 설명 |
| 5 | 인증·권한, 환경 변수, 로그, 운영 기본 | 사용자별 데이터 접근 | 권한 오류와 비밀값 관리 방법을 설명 |
| 6 | React 연동과 배포 문서 | 작은 전체 흐름과 아키텍처 문서 | 브라우저 → API → DB 흐름을 시연하고 본인 기여를 설명 |

## 매주 반복할 작업

1. 공식 문서의 관련 절을 읽고 작은 예제를 실행합니다.
2. 본인 코드에서 한 기능만 추가합니다.
3. 왜 그렇게 구현했는지, 다른 방법의 장단점은 무엇인지 `LEARNING_LOG.md`에 적습니다.
4. 다음 주에 다시 열었을 때 재현할 수 있도록 실행 명령과 요청 예시를 갱신합니다.

## Node.js를 시작할 시점

Spring API의 CRUD, DB 저장, 오류 처리, 인증 흐름을 본인 말로 설명할 수 있을 때 시작합니다. 그때 같은 학습 항목 API를 **TypeScript + Node.js**로 다시 만들고, 비동기 처리·타입·DI·트랜잭션 구현 방식을 비교합니다. 여러 기술 이름을 나열하는 것보다 한 서비스를 두 방식으로 구현하고 차이를 설명하는 것이 목표입니다.

## 참고 문서

- [Java 학습 자료](https://dev.java/learn/)
- [Spring Boot 첫 애플리케이션](https://docs.spring.io/spring-boot/tutorial/first-application/index.html)
- [Spring REST 서비스 가이드](https://spring.io/guides/gs/rest-service/)
- [Node.js 릴리스 현황](https://nodejs.org/en/about/previous-releases)

