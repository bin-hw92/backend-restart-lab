
---

## 지출 예제에서 다시 막혔던 부분

성적 예제를 수정한 다음에는 지출 예제로 넘어갔다. 같은 카테고리의 지출을 추가하고, 새로운 카테고리를 넣고, 1만원 이상인 지출과 날짜별 합계를 구하는 문제였다. 마지막에는 0원이나 음수인 금액을 거절하도록 만들었다.

처음에는 Java 8 쪽을 먼저 풀고 Java 21 쪽은 잘 모르겠어서, Codex에 채점과 코드별 주석 설명을 요청했다. 실행 결과를 확인해 보니 데이터를 추가한 부분은 맞았지만, 조건 합계와 날짜별 누적에서 오류가 있었다.

1만원 이상인 지출을 계산하면서 이미 금액을 더한 카테고리 합계를 다시 읽어 같은 금액을 한 번 더 더했다. 이 문제는 카테고리별 Map이 아니라 숫자 하나의 합계가 필요했다. 별도 BigDecimal 변수를 만들어 조건에 맞는 금액만 더하도록 수정했다.

날짜별 합계에서는 날짜가 처음 나왔을 때만 put을 실행해 같은 날짜의 다음 지출들이 빠졌다. 날짜가 없으면 초기값을 0으로 정하고, 금액을 더해 저장하는 부분은 매번 실행하도록 바꿨다.

### 반복문과 Stream으로 같은 조건 합계 구하기

```java
// Java 8: 조건에 맞는 지출만 합계 변수에 누적
BigDecimal total2 = BigDecimal.ZERO;
for (Expense expense : expenses) {
    if (expense.getAmount().compareTo(new BigDecimal("10000")) >= 0) {
        total2 = total2.add(expense.getAmount());
    }
}

// Java 21: 조건 선택 → 금액 꺼내기 → 합계
BigDecimal total2 = expenses.stream()
    .filter(expense -> expense.amount().compareTo(new BigDecimal("10000")) >= 0)
    .map(Expense::amount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

compareTo는 true/false가 아니라 정수를 반환한다. 기준과 같으면 0이므로 ‘1만원 이상’에는 `>= 0` 조건이 필요했다.

### LinkedHashMap과 TreeMap은 순서가 다르다

| 선택 | 순서 | 이번 예제의 사용 위치 |
| --- | --- | --- |
| LinkedHashMap | 키가 처음 들어온 순서 | 카테고리별 합계 |
| TreeMap | 키의 정렬 순서 | 날짜별 합계 |

```java
// 카테고리는 처음 등장한 FOOD → BOOK → TRANSPORT 순서
Map<String, BigDecimal> byCategory = new LinkedHashMap<>();

// 날짜는 입력 순서가 섞여도 10월 1일 → 10월 2일 순서
Map<LocalDate, BigDecimal> byDate = new TreeMap<>();
```

TreeMap으로 바꾼다고 합계까지 자동으로 계산되는 것은 아니다. 금액을 누적하는 처리와 결과를 어떤 순서로 보여줄지는 따로 봐야 했다.

```java
// Java 8: 초기값만 조건 안에 두고, 저장은 매번 실행
LocalDate date = expense.getDate();
BigDecimal previous = byDate.get(date);
if (previous == null) {
    previous = BigDecimal.ZERO;
}
byDate.put(date, previous.add(expense.getAmount()));

// Java 21: 날짜로 묶고 각 그룹의 금액을 더한 뒤 TreeMap으로 정렬
Map<LocalDate, BigDecimal> byDate = expenses.stream()
    .collect(Collectors.groupingBy(
        Expense::date,
        TreeMap::new,
        Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
    ));
```

위 코드는 각각의 버전에서 해당 위치를 비교하기 위해 발췌한 부분이다.

### 출력하는 것과 예외를 발생시키는 것도 달랐다

5번은 혼자서는 잘 모르겠어서 풀이를 직접 보여 달라고 요청했다. 처음에는 오류 이름을 출력하고 반복문을 멈추면 되는 것으로 작성했다. 다음에는 compareTo의 결과를 그대로 if에 넣고 println을 throw하려다가 컴파일 오류가 났다.

Codex의 첨삭을 받아 생성자에서 잘못된 금액을 거절하는 코드로 확인했다. Java 21 쪽도 설명 주석을 보며 record 생성자 검증을 적용했다.

```java
// Java 8: 일반 생성자 안에서 필드 저장 전에 검증
Expense(LocalDate date, String category, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
    }
    this.date = date;
    this.category = category;
    this.amount = amount;
}

// Java 21: record의 compact constructor에서 검증
record Expense(LocalDate date, String category, BigDecimal amount) {
    Expense {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }
    }
}
```

println은 화면에 글자를 출력하고, throw는 예외 객체로 실행 흐름을 중단시킨다. 지금 예제에는 catch가 없어서 0원이나 음수 입력은 생성자에서 종료되고 합계 출력까지 가지 않는다.

정상 지출 5건의 결과는 두 버전에서 같게 확인했다.

```text
전체 지출: 54000
카테고리별 지출: {FOOD=26000, BOOK=25000, TRANSPORT=3000}
FOOD 지출: 26000
TRANSPORT 지출: 3000
1만원 이상 지출: 37000
날짜별 지출: {2026-10-01=37000, 2026-10-02=17000}
```

이번에도 처음부터 혼자 완성한 것은 아니다. 먼저 작성한 코드를 채점받고, 5번은 직접 풀이와 주석을 보며 확인했다. 다음에는 같은 조건을 조금 바꿔서 이전 값이 어떻게 누적되는지, 생성자에서 왜 검증하는지 다시 설명해 보려고 한다.
