# 공부 순서

기간을 딱 정해두기보다, 앞 단계의 코드를 직접 고칠 수 있을 때 다음으로 넘어가려고 합니다. 취업 준비와 같이 진행하면서 공부한 내용은 작은 커밋으로 남길 생각입니다.

## 1. Java 다시 보기

Java를 오래 쓰지 않아서 컬렉션, 객체 설계, 예외 처리, 날짜/시간, Stream부터 다시 보려고 합니다. `java-basics`의 지출 합계 예제에 필터링과 입력 검증을 직접 추가해 봅니다.

여기서는 “왜 List를 썼는지”, “금액에 BigDecimal을 쓴 이유가 뭔지” 정도를 코드 보고 설명할 수 있으면 좋겠습니다.

## 2. Spring Boot로 API 만들기

`study-api`의 목록 조회·등록·완료·삭제 흐름을 먼저 이해하고 수정합니다. Controller와 Service가 각각 무슨 일을 하는지, 잘못된 요청이나 없는 ID에 어떤 응답을 주는지도 확인합니다.

지금은 메모리 저장이라 서버를 다시 켜면 데이터가 사라집니다. 기능이 익숙해지면 SQL과 테이블 설계를 다시 보고 DB 저장으로 바꿔 볼 생각입니다. 그 과정에서 JDBC와 JPA가 어떻게 다른지도 비교해 보려고 합니다.

## 3. 실제 서비스에 가까운 부분 붙이기

입력 검증, 목록 조회 조건, 트랜잭션, 로그인과 권한을 순서대로 붙입니다. 기능을 추가할 때마다 정상 요청만 확인하지 않고, 잘못된 입력이나 권한이 없는 요청도 어떻게 처리할지 정해 봅니다.

그다음 익숙한 React 화면을 연결해서 브라우저에서 등록한 데이터가 API와 DB까지 어떻게 흐르는지 직접 확인하려고 합니다.

## 4. Node.js와 비교하기

Java와 Spring으로 만든 API의 흐름을 설명하고 수정할 수 있게 되면, 같은 기능을 TypeScript와 Node.js로 만들어 볼 생각입니다. 이미 만든 기능을 다시 구현하면서 비동기 처리, 타입, DB 접근 방식이 어떻게 다른지 살펴보려고 합니다.

## 공부할 때 참고할 문서

- [Java 학습 자료](https://dev.java/learn/)
- [Spring Boot 첫 애플리케이션](https://docs.spring.io/spring-boot/tutorial/first-application/index.html)
- [Spring REST 서비스 가이드](https://spring.io/guides/gs/rest-service/)
- [Node.js 릴리스 현황](https://nodejs.org/en/about/previous-releases)

