# 공부하면서 적어둘 것

## 2026-10-14 — 첫 성적 수정 확인과 설명 주석

- 사용자 직접 수정 확인: 두 GradeSummary의 학습자B 55→65, Java 8의 List.forEach와 Java 21의 Stream.forEach로 점수 출력 추가.
- 양쪽 실제 컴파일·실행 성공. 합계220/통과3/미통과0, 출력 문자열 일치. 점수 print에 줄바꿈이 없어 `80, 65, 75, 통과: 3`으로 같은 줄에 출력됨. 계산 오류 아님.
- 사용자가 각 Java에 주석과 별도 용어 파일 요청. 실행 코드·입력은 보존하고 두 GradeSummary에 설명 주석 추가, JAVA_TERMS.md 작성.
- Stream/람다/메서드 참조/forEach는 Java 8부터, record와 Stream.toList는 Java 16부터 정식 제공됨을 구분해 설명. 사용자는 당시 Java 8 업무에서 Stream을 접하지 않았다고 설명. 이해 완료로 단정하지 않음.
- 다음 연습 후보: print 뒤 줄바꿈 추가, 이후 통과 기준/경계값 변경. 사용자 요청 전에 실습 정답으로 덮어쓰지 않음.

## 2026-10-14 — Athena 작성 방식과 최신 방식 나란히 연습

- 사용자 방향: 아이맥스소프트 때 익숙한 Java 8과 Java 21 방식의 예제를 직접 수정하고, 에이전트가 양쪽 실행 결과와 문법 대응을 설명하는 방식으로 공부. 이력서 후속 작업은 기록만 유지.
- 로컬 `../athena` 폴더 확인. Sorting/Course/Lecture class major 52 확인. 일반 클래스·인덱스 for문·if·자체 DBQuery 패턴 참고. 원본 수정·회사 DB 접속·회사 코드 복사 없음.
- 성적 집계 `java-basics/java8/GradeSummary.java`, `java-basics/java21/GradeSummary.java` 추가. 기존 지출 Java 8 예제에 대응하는 ExpenseSummaryJava21 추가. 원래 ExpenseSummary 및 기존 사용자 변경 보존.
- JDK 21에서 각각 --release 8/21로 컴파일·실행 성공. 성적: 3명/합계210/통과2/미통과1. 지출: 전체46000/FOOD21000/BOOK25000. 두 버전 출력 문자열도 일치.
- run-sample.sh: 버전/예제 선택, 매번 임시 폴더에서 컴파일 후 실행. 두버전_학습안내.md와 AGENTS.md에 사용자 수정·에이전트 결과 확인 절차 기록.
- Java 8 런타임 실검증은 아님. Stream/람다/메서드 참조는 Java 8에도 있고 record/List.of/Stream.toList는 이후 도입된 기능임을 구분.
- 첫 연습 제안: 두 성적 파일의 학습자B 점수 55→65 변경 후 결과 확인 요청. 아직 사용자 수정·이해 여부는 확인하지 않았음.

## 2026-10-14 — 맥 환경 확인 및 Java 8 비교 예제 준비

- 에이전트 작업: 기존 예제를 보존하고 일반 클래스·ArrayList·반복문을 사용하는 `java-basics/ExpenseSummaryJava8.java`와 비교 설명 추가.
- 직접 확인: 현재 터미널에서 Java Runtime을 찾지 못했고 `javac`도 실행되지 않음. `mvn`, `node`, `npm`은 PATH에서 찾지 못함. Homebrew는 `/opt/homebrew/bin/brew`, CPU는 arm64.
- 검증 상태: JDK가 없어 Java 컴파일·실행은 미완료. 예상 합계는 전체 46000, FOOD 21000, BOOK 25000.
- 다음 작업: JDK 21과 Maven 실행 환경 준비 → `--release 8`로 새 예제 컴파일 → 기존 예제 실행 및 비교 → Spring API 실행.
- 사용자 학습 완료·이해 여부는 아직 확인하지 않음.

### 같은 날 후속 검증

- 사용자 JDK 설치 후 `/opt/homebrew/opt/openjdk@21/bin`의 Java 21.0.12.1로 두 예제 컴파일·실행 성공.
- Java 8 비교 예제는 `--release 8`로 컴파일. obsolete 대상 옵션 경고는 있지만 실행 성공.
- 두 예제 모두 전체 46000, FOOD 21000, BOOK 25000으로 일치. Map 구현 차이로 카테고리 출력 순서는 다름.
- Maven 3.10.0 확인. 기본 Java가 27이어서 Spring 실행 시 JAVA_HOME으로 21 지정 예정. Spring API는 아직 실행하지 않음.

### 개발 환경 준비 후 최종 검증

- Java 21로 Maven package 성공. 현재 저장소에 테스트 파일은 없어 Maven 결과는 빌드 검증임.
- 8080에서 Spring API 실행 후 임시 항목 등록·완료·삭제(204) 검증. 테스트 서버 종료.
- `~/.zshrc`에 Java 21 JAVA_HOME 설정 추가. 새 터미널에서 적용됨.

날짜별로 아래 내용을 복사해서 적으려고 합니다. 코드가 돌아갔는지와 제가 이해한 내용은 따로 남기겠습니다.

## YYYY-MM-DD — 공부한 주제

- 오늘 해본 것:
- 새로 알게 된 것:
- 막혔던 부분과 해결한 방법:
- AI 도움을 받았다면, 직접 확인한 부분:
- 다음에 해볼 것:
- 관련 커밋:

