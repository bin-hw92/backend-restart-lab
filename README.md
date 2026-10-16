# 백엔드 다시 공부하기

예전에 Java/JSP와 SQL로 교육 서비스를 개발했는데, 최근에는 React 쪽 일을 주로 했습니다. 풀스택으로 다시 취업을 준비하면서 백엔드에서 잊어버린 부분을 하나씩 다시 공부하려고 만든 저장소입니다.

우선 Java를 복습하고 Spring Boot로 작은 API를 만들어 보려고 합니다. Node.js도 관심이 있지만, 당장은 여러 가지를 한꺼번에 하기보다 Java와 Spring에 집중할 생각입니다.

## 지금 들어 있는 것

| 폴더 | 내용 |
| --- | --- |
| [`java-basics`](java-basics) | 같은 성적/지출 기능의 Java 8·Java 21 비교 예제와 실행 스크립트 |
| [`study-api`](study-api) | 학습할 일을 등록하고 완료 처리하는 Spring Boot API |
| [`docs/ROADMAP.md`](docs/ROADMAP.md) | 앞으로 공부할 순서 |
| [`docs/LEARNING_LOG.md`](docs/LEARNING_LOG.md) | 공부하면서 알게 된 점을 적어둘 곳 |

`study-api`는 아직 메모리에만 저장합니다. 서버를 다시 켜면 등록한 내용이 사라집니다. DB 연결과 인증은 다음 단계에서 붙여 볼 예정입니다.

## 실행해 보기

JDK 21과 Maven 3.6.3 이상이 필요합니다. Spring Boot는 4.1.1을 사용했습니다. 버전별 지원 범위는 [Spring Boot 문서](https://docs.spring.io/spring-boot/system-requirements.html)를 참고했습니다.

먼저 Java 예제부터 실행합니다.

```sh
cd java-basics
bash run-sample.sh 8 amount-input
bash run-sample.sh 21 amount-input
```

현재 공부는 [문자열 금액 입력](java-basics/lessons/02-amount-input/README.md)입니다. 주제별 실습은 [lessons](java-basics/lessons/README.md)에 모으며, 스크립트는 임시 폴더에서 컴파일하므로 소스 옆에 class 파일이 생기지 않습니다.

Spring API는 이렇게 실행합니다.

```powershell
cd study-api
mvn spring-boot:run
```

서버가 켜지면 다른 PowerShell 창에서 목록 조회와 등록을 해볼 수 있습니다.

```powershell
Invoke-RestMethod http://localhost:8080/api/study-tasks
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/study-tasks -ContentType 'application/json' -Body '{"title":"Java 컬렉션 복습"}'
Invoke-RestMethod http://localhost:8080/api/study-tasks
```

API 경로와 다음에 만들어 볼 기능은 [`study-api/README.md`](study-api/README.md)에 적었습니다.

## 어떻게 공부할지

예제를 그대로 따라 치고 끝내지 않고, 작은 기능을 직접 바꿔 보면서 왜 그렇게 동작하는지 기록하려고 합니다. AI의 도움을 받더라도 이해하지 못한 코드는 그냥 넘기지 않고, 실행 결과와 수정한 이유를 같이 남겨 보겠습니다.

2026-10-14 맥에서 JDK 21과 Maven 3.10.0으로 Java 예제 실행과 Spring API 빌드를 확인했습니다. API는 임시 항목 등록·완료·삭제까지 검증한 후 종료했습니다. Java 8 방식의 비교 예제와 실행 방법은 [`java-basics/README.md`](java-basics/README.md)에 있고, 직접 수정한 내용과 실행 결과는 [`docs/LEARNING_LOG.md`](docs/LEARNING_LOG.md)에 기록했습니다.

