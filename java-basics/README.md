# Java 8·21 복습

## 현재 공부할 곳

새 실습은 [lessons](lessons/README.md) 아래에 주제별로 묶습니다.

| 순서 | 내용 | 진행 상태 |
| --- | --- | --- |
| [01-registration](lessons/01-registration/README.md) | boolean 등록 결과와 성공·실패 건수 | 두 버전 TODO 1·2 작성·실행 확인 |
| [02-amount-input](lessons/02-amount-input/README.md) | 문자열 금액 변환과 예외처리 범위 | **다음 공부: Java 8 TODO 1부터** |

02 폴더의 ExpenseInputJava8.java와 ExpenseInputJava21.java를 수정합니다. 먼저 Java 8에서 변환·등록을 try 안에 넣어보고, 확인 후 21에 적용합니다. 기존 성적·지출·답지는 복습용으로 보존했습니다.

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 amount-input
bash run-sample.sh 21 amount-input
```

시작 코드는 TODO가 비어 있어 성공0/실패6/목록0/합계0입니다. 완성 목표는 성공2/실패4/목록2/합계17000입니다. 문제·입력별 기대 결과·설명은 02의 README에 있습니다.

직전 등록 실습은 `bash run-sample.sh 8 registration`, `bash run-sample.sh 21 registration`으로 복습합니다. **실행 스크립트는 임시 폴더에서 컴파일하고 결과를 정리하므로 소스 옆에 .class가 쌓이지 않습니다.**

답지는 `bash run-sample.sh 8 exception-answer`, `bash run-sample.sh 21 exception-answer`로 실행합니다. 기존 [예외처리 연습문제](예외처리_연습문제.md)와 [지출 연습문제](지출_연습문제.md)는 복습 자료입니다.

처음 시작하는 경우 [두 버전 학습 안내](두버전_학습안내.md)를 읽고 성적 집계부터 비교할 수 있습니다. `java8/GradeSummary.java`와 `java21/GradeSummary.java`는 같은 입력과 출력을 가지며, 일반 클래스/반복문과 record/Stream을 비교할 수 있습니다.

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 grade
bash run-sample.sh 21 grade
```

지출 예제도 `ExpenseSummaryJava8.java` / `ExpenseSummaryJava21.java`를 나란히 수정하고 `bash run-sample.sh 8 expense`, `bash run-sample.sh 21 expense`로 실행합니다. 기존 `ExpenseSummary.java`는 보존합니다.

## 맥에서 시작하기 — 2026-10-14

Java 8 방식부터 복습하려면 새로 추가한 `ExpenseSummaryJava8.java`를 먼저 읽습니다. 기존 `ExpenseSummary.java`와 사용자가 추가한 FOOD 출력은 보존했습니다.

JDK 21이 준비되면 맥 터미널에서 아래 명령으로 Java 8 API·문법 범위에 맞춰 컴파일합니다.

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 expense
```

2026-10-14 맥에서 JDK 21로 컴파일·실행해 아래 출력과 일치하는 것을 확인했습니다. `--release 8`의 오래된 대상 옵션 경고는 있었지만 컴파일은 성공했습니다.

```text
전체 지출: 46000
카테고리별 지출: {FOOD=21000, BOOK=25000}
FOOD 지출: 21000
```

복습 순서:

1. `Expense`는 지출 한 건을 담는 클래스입니다. 생성자가 값을 받고 `getAmount()` 같은 메서드가 값을 꺼냅니다.
2. `ArrayList`에 `add()`로 지출을 추가합니다. 기존 `List.of()`는 Java 9부터 제공되며 수정할 수 없는 목록을 만듭니다.
3. `for`문으로 한 건씩 읽어 전체 합계와 카테고리별 합계를 구합니다. `LinkedHashMap`으로 입력 순서를 유지합니다.
4. `BigDecimal.add()`는 새로운 값을 반환하므로 `total = total.add(...)`처럼 결과를 다시 저장합니다. 금액은 문자열로 생성합니다.
5. 기존 예제의 `record`는 Java 16부터 정식 제공되는 데이터 표현 문법입니다. 일반 클래스의 필드·생성자·접근자를 간결하게 표현하지만, 일반 클래스와 완전히 같은 기능은 아닙니다.
6. 기존 예제의 Stream과 메서드 참조는 Java 8에도 있습니다. 반복문을 이해한 다음 비교합니다.

먼저 FOOD 지출 한 건을 추가하고 전체 합계와 FOOD 합계가 함께 바뀌는지 확인해 보세요.

## 기존 예제

`ExpenseSummary.java`는 지출 목록의 전체 금액과 카테고리별 금액을 계산하는 작은 예제입니다. Java를 다시 보려고 `record`, `List`, `Stream`, `BigDecimal`을 사용했습니다.

```sh
bash run-sample.sh 21 expense
```

실행한 다음에는 아래 내용을 직접 바꿔 보려고 합니다.

1. `FOOD` 지출만 골라 합계 내기
2. 날짜별로 묶고 날짜순으로 출력하기
3. 금액이 0 이하일 때 어떻게 처리할지 정하기
4. Stream을 쓰지 않고 반복문으로도 만들어 보기

같은 결과를 내더라도 코드가 어떻게 다른지 학습 기록에 적어두면 나중에 다시 보기 좋을 것 같습니다.

