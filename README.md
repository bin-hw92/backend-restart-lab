# Backend Restart Lab

Java·Spring 백엔드 감각을 다시 익히기 위한 개인 학습 저장소입니다. 기존 Java/JSP와 SQL 경험을 출발점으로 삼고, 현재 익숙한 React와 연결할 수 있는 API까지 단계적으로 만듭니다.

## 왜 Java → Spring → Node 순서인가

- **Java:** 문법보다 컬렉션, 예외, 객체 설계, 날짜/시간, Stream, SQL 연동의 감각을 되찾습니다.
- **Spring Boot:** HTTP API, 계층 분리, 입력 검증, DB 트랜잭션, 인증, 배포를 하나의 작은 서비스에서 연습합니다.
- **Node.js:** Java API를 직접 설명하고 수정할 수 있게 된 뒤 같은 기능을 TypeScript/Node로 비교 구현합니다. 처음부터 두 서버 스택을 동시에 공부하지 않습니다.

현재 예제는 **메모리에 저장하는 학습 항목 API**입니다. 서버를 다시 시작하면 데이터가 사라집니다. DB, 인증, 배포를 이미 구현했다고 보이지 않도록 단계별로 확장합니다.

## 준비

- JDK 21
- Maven 3.6.3 이상
- 편집기 또는 IDE

이 저장소는 Spring Boot 4.1.1과 Java 21을 기준으로 작성했습니다. [Spring Boot 시스템 요구 사항](https://docs.spring.io/spring-boot/system-requirements.html)에서 현재 지원 범위를 확인할 수 있습니다.

```powershell
java -version
mvn -version
```

## 폴더

| 경로 | 내용 |
| --- | --- |
| `java-basics/` | Java 문법과 컬렉션·Stream 복습용 단독 예제 |
| `study-api/` | Spring Boot REST API 첫 단계 |
| `docs/ROADMAP.md` | 6주 학습 순서와 완료 기준 |
| `docs/LEARNING_LOG.md` | 공부한 내용과 막힌 부분을 기록하는 양식 |

## 시작하기

### 1. Java 복습

```powershell
cd java-basics
javac ExpenseSummary.java
java ExpenseSummary
```

코드를 읽은 뒤 `java-basics/README.md`의 과제를 본인이 직접 수정합니다.

### 2. Spring API 실행

```powershell
cd study-api
mvn spring-boot:run
```

다른 PowerShell 창에서:

```powershell
Invoke-RestMethod http://localhost:8080/api/study-tasks
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/study-tasks -ContentType 'application/json' -Body '{"title":"Java collection 복습"}'
Invoke-RestMethod http://localhost:8080/api/study-tasks
```

API 계약과 다음 구현 과제는 [`study-api/README.md`](study-api/README.md)에 있습니다.

## 학습 원칙

1. 기능을 만들기 전에 입력·출력·오류를 짧게 적습니다.
2. AI가 제안한 코드는 각 계층의 역할과 변경 이유를 본인 말로 설명한 뒤 반영합니다.
3. 완료한 기능은 요청 예시와 화면 또는 응답 결과를 README에 남깁니다.
4. 미완료 기능을 이력서의 구현 경험으로 적지 않습니다.

## 현재 상태

- Java 단독 복습 예제 1개와 메모리 기반 Spring CRUD API의 시작 코드가 들어 있습니다.
- DB, JPA, 인증, Docker 배포, Node 비교 구현은 학습 과제로 남겨 두었습니다.
- 이 환경에 JDK·Maven이 설치되지 않아 실행 결과는 아직 확인하지 않았습니다.

