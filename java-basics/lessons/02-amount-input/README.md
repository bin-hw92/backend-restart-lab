# 02. 문자열 금액 입력과 예외처리 범위

2026-10-16. [이전 등록 결과 실습](../01-registration/README.md)의 다음 단계입니다. 이번에는 숫자로 바꿀 수 없는 입력도 처리합니다. **먼저 Java 8의 TODO 1만 작성하세요.**

| 버전 | 수정할 파일 |
| --- | --- |
| Java 8 — 먼저 작성 | [ExpenseInputJava8.java](ExpenseInputJava8.java) |
| Java 21 — 같은 흐름 적용 | [ExpenseInputJava21.java](ExpenseInputJava21.java) |

## 이번에 달라지는 부분

화면의 금액 입력을 문자열로 받았다고 가정합니다. `"12000"`을 계산하려면 BigDecimal로 바꾸어야 하지만, `"abc"`와 빈 문자열은 변환할 수 없습니다.

이전 실습의 main은 `new BigDecimal(amountText)`를 먼저 실행해서 그 결과를 등록 메서드에 전달했습니다. 변환 중에 예외가 발생하면 아직 등록 메서드에 들어가지 않았으므로, 그 안의 catch는 해당 예외를 처리할 수 없습니다.

이번 파일은 main에서 **문자열 그대로 전달**하고, 등록 메서드 안에서 변환하도록 준비했습니다.

```java
// 호출하는 쪽: 문자열을 그대로 전달합니다. 이 줄은 제공된 코드입니다.
boolean registered = registerExpense(expenses, date, category, amountText);
```

등록 메서드의 매개변수도 `BigDecimal amount`에서 `String amountText`로 바뀌었습니다. 변환 → 지출 생성 → 목록 추가를 같은 try 안에 넣어보는 것이 핵심입니다. main의 건수 계산과 생성자의 금액 검증, 합계 코드는 준비되어 있습니다.

## 1. try 안에서 변환하고 등록하기

`registerExpense`의 `TODO 1`에서 임시 `return false;` 한 줄을 다음 처리로 바꿉니다.

1. `new BigDecimal(amountText)` 결과를 `BigDecimal amount` 변수에 담습니다.
2. 그 amount로 Expense를 만들고 expenses 목록에 추가합니다.
3. 추가에 성공하면 true를 반환합니다.

catch의 사유 출력과 false 반환은 그대로 사용합니다. 이전 풀이를 참고하되, **문자열을 숫자로 바꾸는 위치**를 생각해 보세요.

| 입력 순서 | 금액 문자열 | 기대 결과 | 실패 이유 |
| --- | --- | --- | --- |
| 1 | `"12000"` | 성공, true | |
| 2 | `"abc"` | 실패, false | 숫자로 변환할 수 없음 |
| 3 | `"0"` | 실패, false | 숫자 변환은 성공하지만 0원 등록은 불가 |
| 4 | `"-1000"` | 실패, false | 숫자 변환은 성공하지만 음수 등록은 불가 |
| 5 | `""` | 실패, false | 빈 문자열을 숫자로 변환할 수 없음 |
| 6 | `"5000"` | 성공, true | 앞선 실패 뒤에도 정상 등록 |

완성 후 실패 사유가 4회 출력되고, 마지막 네 줄은 아래와 같아야 합니다. 숫자 변환 예외의 원문 메시지는 입력·JDK 구현에 따라 달라질 수 있으므로 문구보다 처리 결과를 먼저 확인합니다.

```text
등록 성공: 2
등록 실패: 4
등록된 지출 수: 2
전체 지출: 17000
```

**지금 제공된 시작 코드**는 등록을 시도하지 않고 false를 반환하므로 `성공0/실패6/목록0/합계0`입니다. 이는 임시 반환값을 main이 센 결과이며, 예외처리 완성을 뜻하지 않습니다. 처음에는 실패 사유도 출력되지 않습니다.

이번 입력에는 null, 공백 제거, 날짜·분류 검증은 포함하지 않습니다.

## 2. 실패 종류별로 catch 나누기 — 1번 확인 후 선택

1번은 `IllegalArgumentException` 하나로 네 실패를 모두 처리할 수 있습니다. 숫자 변환 실패인 `NumberFormatException`도 이 예외의 하위 종류이기 때문입니다.

실패 이유를 사용자가 읽기 쉽게 구분하고 싶다면 다음으로 바꿔보세요.

- `NumberFormatException`의 catch를 추가해 `숫자 입력 실패: [abc]` 또는 빈 문자열이면 `숫자 입력 실패: []`를 출력하고 false를 반환합니다. 입력은 amountText로 읽습니다.
- 기존 `IllegalArgumentException`의 catch는 0원·음수 등록 실패를 처리하도록 유지합니다.
- 숫자 변환용 catch를 먼저 적습니다. 상위 예외 catch가 앞에 있으면 하위 예외까지 먼저 잡기 때문에, 뒤쪽 하위 예외 catch는 도달할 수 없어 컴파일 오류가 납니다.

이 단계에서는 실패 메시지만 구분되며 건수와 합계는 1번과 같습니다. 두 catch가 하나의 실패를 동시에 처리하지는 않습니다.

## 두 버전의 비교와 실행

등록·변환·예외처리·카운터는 두 버전에 동일하게 작성할 수 있습니다. 8은 일반 클래스/getAmount/반복문 합계, 21은 record/amount/Stream 합계를 사용합니다. Stream은 Java 8부터, record는 Java 16부터 정식 기능입니다.

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 amount-input
bash run-sample.sh 21 amount-input
```

JDK21에서 --release8/21로 컴파일하고 실행합니다. 실제 JDK8 런타임에서의 실행은 아닙니다. 결과는 임시 디렉토리에만 생성되어 소스 옆에 class 파일이 쌓이지 않습니다.

먼저 `금액 입력 Java 8 TODO 1 수정했어. 확인해줘`라고 알려주세요. 파일을 읽고 숫자 변환 실패·금액 검증 실패·마지막 정상 입력까지 확인합니다. 완성 답지는 시작 파일에 넣지 않았습니다.

공통 용어는 [JAVA_TERMS.md](../../JAVA_TERMS.md)에 있습니다. 공식 근거: [BigDecimal(String)의 NumberFormatException](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html#%3Cinit%3E(java.lang.String)), [NumberFormatException의 상위 클래스](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/NumberFormatException.html).
