# 01. 메서드가 등록 결과를 돌려주게 하기

2026-10-16. Java 8·21의 TODO 1·2를 사용자가 작성했고, 성공2/실패2/목록2/합계17000을 실행 확인했습니다. 아래는 복습용 문제이며, 다음 공부는 [02. 문자열 금액 입력](../02-amount-input/README.md)입니다. 선택 3번은 아직 수행하지 않았습니다.

| 버전 | 수정할 새 파일 |
| --- | --- |
| Java 8 방식 — 먼저 작성 | [ExpenseRegistrationJava8.java](ExpenseRegistrationJava8.java) |
| Java 21 방식 — 같은 로직 적용 | [ExpenseRegistrationJava21.java](ExpenseRegistrationJava21.java) |

## 이번에 바뀌는 한 가지

이전 답지의 `void registerExpense(...)`는 등록과 실패 출력까지 처리하고 결과값을 돌려주지 않았습니다. 이번에는 `boolean registerExpense(...)`로 만들어, 호출한 쪽이 등록 성공 여부를 알 수 있도록 합니다.

```java
boolean registered = registerExpense(expenses, date, category, amount);
```

위는 호출 구조를 설명하는 코드입니다. 실제 파일의 main에는 날짜·분류·금액을 전달하는 호출을 넣어뒀습니다.

- 매개변수: 메서드 선언에 적은 `List<Expense> expenses` 같은, 전달받을 값의 타입과 이름.
- 인자: 메서드를 호출하면서 실제로 전달하는 목록·날짜·분류·금액.
- 반환형 `boolean`: 이 메서드가 호출한 쪽에 true 또는 false를 돌려준다는 약속.
- `return`: 현재 메서드 호출을 끝내고 값을 돌려주는 문장. 예를 들어 합계 메서드의 `return total;`은 계산한 금액을 돌려줍니다.

return을 쓰는 이유는 **호출한 쪽도 처리 결과를 가지고 다음 일을 결정하기 위해서**입니다. registerExpense의 return이 main의 for문까지 끝내는 것은 아닙니다. main은 반환값으로 건수를 세고 다음 금액을 시도합니다.

## 준비된 부분과 직접 채울 부분

생성자의 금액 검증, 등록할 금액 4개, 메서드 호출, 합계 계산·출력은 준비되어 있습니다. 새로 만든 두 파일의 `TODO 1`, `TODO 2`만 채웁니다.

| 순서 | 금액 | 목표 동작 |
| --- | --- | --- |
| 1 | 12000 | 등록 성공, true 반환 |
| 2 | 0 | 등록 실패 처리, false 반환 |
| 3 | -1000 | 등록 실패 처리, false 반환 |
| 4 | 5000 | 앞선 실패와 관계없이 등록 성공, true 반환 |

이 문제는 금액 검증과 반환값에 집중합니다. 날짜·분류 검증이나 숫자로 바꿀 수 없는 문자열 처리는 이번 범위에 포함하지 않습니다.

처음 제공했던 시작 코드는 컴파일되지만 등록을 시도하지 않고 임시로 false만 반환했습니다. 당시에는 건수도 누적하지 않아 네 출력이 모두 0이었습니다. 현재는 사용자 풀이가 반영되어 2/2/2/17000이 출력됩니다.

## 1. registerExpense 완성 — 먼저 여기까지만

두 파일의 `TODO 1` 위치를 찾고 아래 순서로 작성합니다. 처음에는 8만 고친 뒤 21에도 같은 코드를 적용해도 됩니다.

1. try 안에서 전달받은 값으로 `new Expense(...)`를 만들고 목록에 add합니다.
2. **추가에 성공한 뒤** true를 반환합니다.
3. IllegalArgumentException을 catch합니다.
4. `등록 실패 사유: ` 뒤에 `e.getMessage()`를 붙여 출력합니다.
5. 실패했으면 false를 반환합니다.

기존 [8 답지의 registerExpense](../../answers/ExpenseExceptionAnswerJava8.java)와 비교해 보세요. 그 답지는 반환형이 void이고, 이번에는 성공/실패에 따라 boolean을 반환하는 부분이 추가됩니다. 생성자의 throw 검증은 그대로 둡니다. 임시 `return false;`는 자신의 처리 코드로 바꿉니다.

**1번만 마쳤을 때 확인할 것:** 실패 사유가 두 번 출력되고, 등록된 지출 수는 2, 전체 지출은 17000입니다. 성공·실패 건수는 2번을 아직 채우지 않았으므로 0으로 남습니다.

메서드 안에서 catch하므로 0원 실패 뒤 -1000원과 5000원도 순서대로 시도할 수 있어야 합니다. 실패한 금액은 목록에 들어가면 안 됩니다.

## 2. 호출한 main에서 성공·실패 건수 세기

1번 확인 뒤 `TODO 2-success`, `TODO 2-failure`에 각각 건수를 1 늘리는 코드를 넣습니다. `registered`가 true이면 성공 건수, false이면 실패 건수를 늘립니다.

이 단계까지 끝낸 출력:

```text
등록 실패 사유: 금액은 0보다 커야 합니다.
등록 실패 사유: 금액은 0보다 커야 합니다.
등록 성공: 2
등록 실패: 2
등록된 지출 수: 2
전체 지출: 17000
```

실패 메시지 출력은 registerExpense가, 건수 누적과 전체 출력은 main이 담당합니다. 등록된 목록의 금액을 더하는 일은 calculateTotal이 담당합니다. 메서드마다 맡은 일을 나눈 이유도 함께 읽어보세요.

## 3. 입력 하나를 바꿔 결과 예상하기 — 선택

1·2번 확인 후 amounts 배열 맨 뒤에 `"1000"`을 추가해 보세요. 실행하기 전에 성공 3건/실패 2건/등록된 지출 3건/전체 18000이 되는 이유를 생각해 봅니다. 추가한 입력은 두 버전에 똑같이 적용합니다.

## 두 버전에서 비교할 곳

이번 registerExpense와 main은 두 버전에 같은 코드로 작성할 수 있습니다. 21이라고 모든 코드를 Stream으로 바꾸는 것은 아닙니다.

| 부분 | 8 방식 | 21 방식 |
| --- | --- | --- |
| 지출 한 건 | 일반 클래스·생성자·getAmount | record·생성자·amount |
| 등록과 결과 반환 | try-catch + boolean return | 같은 코드 |
| 여러 금액 시도 | for문 | 같은 for문 |
| 합계 메서드 | 반복문 누적 후 return | Stream reduce 결과 return |

## 실행과 확인 요청

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 registration
bash run-sample.sh 21 registration
```

두 버전 모두 설치된 JDK 21에서 실행하며, 컴파일 대상만 --release 8/21로 구분합니다. 실제 Java 8 런타임 실행과는 구분합니다.

현재 1·2번은 사용자 풀이 확인을 마쳤습니다. 복습 후에는 다음 폴더의 [문자열 금액 입력 문제](../02-amount-input/README.md)에서 Java 8 TODO 1부터 진행합니다.
