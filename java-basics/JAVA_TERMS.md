# 현재 예제의 Java 용어

현재 공부는 [문자열 금액 입력](lessons/02-amount-input/README.md)입니다. 숫자 변환·예외 종류·catch 범위 설명은 이 문서 마지막 절에 있습니다.

## 등록 결과 복습: void / boolean / return / 매개변수

[등록결과 연습문제](lessons/01-registration/README.md)와 해당 폴더의 ExpenseRegistrationJava8/21 파일 주석을 함께 봅니다.

| 표현 | 의미와 이번 예제 |
| --- | --- |
| 반환형 void | 호출한 쪽에 결과값을 돌려주지 않는 메서드 |
| 반환형 boolean | 호출한 쪽에 true/false를 돌려주는 메서드. 이번 registerExpense는 성공 여부를 알림 |
| return | 현재 메서드 호출을 끝냄. 반환값이 있으면 그 값도 돌려줌. 호출한 main의 반복문까지 끝내는 것은 아님 |
| 매개변수 | 메서드 선언의 전달받을 값 이름과 타입. 예: BigDecimal amount |
| 인자 | 호출할 때 전달하는 실제 값. 예: new BigDecimal("12000") |
| if (registered) | boolean 변수 registered가 true일 때 실행. if (registered == true)와 같은 조건 |

`static`은 반환형이 아닙니다. 지금 예제에서는 static main이 같은 클래스의 메서드를 인스턴스 생성 없이 호출하는 구조로 사용합니다. try-catch의 사용 가능 여부는 static/void 여부로 결정되지 않습니다.

Map 순서 비교는 [지출 연습문제의 TreeMap/LinkedHashMap 설명](지출_연습문제.md#treemap과-linkedhashmap-차이)에 있습니다. 카테고리는 입력 순서를 유지하고 날짜는 오름차순으로 정렬하는 이유를 같은 날짜 입력 예제로 비교합니다.

## 키 조회와 순회 순서, 성능

키에 맞는 값을 넣고 그 키로 조회하는 기능은 둘 다 제공합니다. 같은 날짜와 금액을 넣었다면 `get(날짜)`의 결과도 같습니다. 이번 예제에서 TreeMap을 선택한 이유는 **전체 날짜별 결과를 언제나 날짜순으로 보여주기 위해서**입니다.

```java
// 동일한 코드를 각각 LinkedHashMap과 TreeMap에서 실행한다고 가정
map.put(2, 17000);
map.put(1, 37000);
System.out.println(map.get(1)); // 두 Map 모두 37000
System.out.println(map);
// LinkedHashMap: {2=17000, 1=37000} — 처음 입력한 키 순서
// TreeMap:       {1=37000, 2=17000} — 키 정렬 순서
```

먼저 데이터를 정렬해 1→2 순서로 put하면 LinkedHashMap도 1→2 순서로 나옵니다. 다만 나중에 더 이른 날짜가 추가되면 입력 순서를 다시 정리해야 날짜순이 유지됩니다. TreeMap은 데이터가 어떤 순서로 들어와도 정렬을 관리합니다.

| 작업 | LinkedHashMap | TreeMap |
| --- | --- | --- |
| 기본 순회 순서 | 처음 입력한 키 순서 | 키 정렬 순서 |
| 키 조회·추가 비용 | 해시가 잘 분산되는 경우 평균 O(1) | O(log n) |
| 이번 목적 | 카테고리 등장 순서 유지 | 날짜순 집계 결과 보장 |

속도만으로 TreeMap을 고른 것은 아닙니다. 정렬된 키 순서가 필요한 경우 그 비용을 들여 유지하는 선택입니다. 키 조회만 필요하면 해시 기반 Map을 사용할 수 있고, 마지막 출력에서만 정렬하는 방법도 있습니다. O 표기는 데이터가 늘 때의 처리 비용이며 실제 실행 시간 벤치마크는 아닙니다.

공식 근거: [LinkedHashMap의 입력 순서와 기본 연산](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html), [TreeMap의 정렬과 log(n) 연산](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html).

## 지출 집계에서 추가로 만난 함수

| 표현 | 의미 | 반복문에서 대응되는 부분 |
| --- | --- | --- |
| `amount.compareTo(기준금액)` | 작은 값은 음수, 같으면 0, 큰 값은 양수 | 금액 조건 비교. 이상은 >= 0, 이하는 <= 0 |
| `total.add(amount)` | 두 금액을 더한 새 BigDecimal 반환 | 결과를 total에 다시 대입해 누적 |
| `map(Expense::amount)` | Expense마다 금액을 꺼냄 | getAmount() |
| `reduce(BigDecimal.ZERO, BigDecimal::add)` | 0부터 금액을 차례로 더해 숫자 하나 반환 | 합계 변수와 add 반복 |
| `groupingBy(기준, Map생성방법, 그룹계산)` | 기준별로 나누고 각 그룹 결과를 Map에 저장 | Map 키별로 이전 금액을 가져와 누적 |
| `reducing(0, 금액꺼내기, 더하기)` | 그룹 안에서 어떻게 합칠지 정하는 Collector 생성 | 카테고리/날짜 하나 안의 합계 계산 |
| `TreeMap::new` | 정렬된 Map을 만드는 생성자 참조 | 날짜 키를 오름차순으로 보관 |

`reduce`는 Stream을 마무리해 합계 결과를 직접 구하는 함수이고, `Collectors.reducing`은 collect/groupingBy에 넘기는 계산 방법을 만듭니다. 그래서 숫자 하나의 전체/조건 합계는 reduce, 카테고리별·날짜별 Map은 groupingBy 안의 reducing으로 작성할 수 있습니다. 이 함수들도 Java 8에서 제공됩니다.

`intValue()`는 비교 함수가 아닙니다. 소수 부분을 버리고 int 범위로 변환하므로 0.50은 0이 됩니다. 값 자체를 비교하려면 compareTo를 사용하세요.

`System.out.println("IllegalArgumentException")`은 이름을 출력할 뿐입니다. `throw new IllegalArgumentException("설명")`은 실제 예외를 발생시켜, 처리하는 catch가 없다면 실행을 중단시킵니다.

record에서는 `Expense { 검증 코드 }` 형태의 compact constructor를 사용할 수 있습니다. date/category/amount 인자가 이미 선언되어 있고, 검증 블록을 정상 종료하면 필드에 값이 자동 대입됩니다. 일반 클래스 생성자처럼 this.amount에 직접 대입하지 않습니다.

공식 참고: [BigDecimal 비교와 intValue](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html), [Collectors](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Collectors.html), [Record 생성자](https://docs.oracle.com/en/java/javase/21/language/records.html).

---

현재 GradeSummary의 함수와 문법을 기준으로 정리합니다. 기존 반복문 방식도 Java 21에서 계속 사용할 수 있습니다.

## 도입 버전

| 표현 | 사용 가능 버전 | 역할 |
| --- | --- | --- |
| `new ArrayList<>()`의 `<>` | Java 7부터 | 왼쪽 선언에서 원소 타입 추론 |
| `stream()`, `forEach(...)` | Java 8부터 | 처리 흐름 생성, 원소별 동작 수행 |
| `grade -> ...` | Java 8부터 | 원소를 받아 수행할 동작 표현(람다) |
| `Grade::score` | Java 8부터 있는 문법 | 메서드 참조. 해당 메서드가 클래스에 있어야 함 |
| `filter`, `map`, `mapToInt`, `sum` | Java 8부터 | 조건 선택, 값 변환, 합계 |
| `List.of(...)` | Java 9부터 | 수정 불가 목록 생성(지출 예제) |
| `record` | Java 16부터 정식 제공 | 데이터 클래스 간결하게 선언 |
| `Stream.toList()` | Java 16부터 | 결과를 수정 불가 List로 수집 |

Java 8로 실행하는 프로젝트에서도 기존 for문·일반 클래스를 계속 사용할 수 있습니다. 당시 Stream을 쓰지 않았다는 사실과 Java 8에서 지원되지 않는다는 것은 다릅니다.

## 사용자가 추가한 forEach

```java
// List 자체를 순회: Java 8 파일
grades.forEach(grade -> System.out.print(grade.getScore() + ", "));

// Stream을 생성한 뒤 순회: Java 21 파일
grades.stream().forEach(grade -> System.out.print(grade.score() + ", "));
```

두 코드 모두 현재 각 원소의 점수를 출력합니다. forEach는 반환 결과가 없고 원소마다 출력 같은 동작을 수행합니다. 단순 출력에는 List.forEach로 충분하며 조건 선택·값 변환을 연결할 때 Stream을 사용할 수 있습니다.

`->` 왼쪽의 grade는 원소 한 개입니다. 람다는 아래 반복문의 본문처럼, 원소를 받았을 때 무엇을 할지 적는 문법입니다.

```java
for (Grade grade : grades) {
    System.out.print(grade.getScore() + ", ");
}
```

print는 줄바꿈하지 않고 println은 출력 후 줄을 바꿉니다. `System.out.println();`은 내용 없이 줄만 바꿉니다.

## mapToInt와 sum

```java
int totalScore = grades.stream()
    .mapToInt(Grade::score)
    .sum();
```

Grade 객체 3개에서 점수 int만 꺼내는 것이 mapToInt입니다. 반환형은 정수들을 처리하는 IntStream이고, sum이 80 + 65 + 75 = 220을 계산해 int로 반환합니다.

이 위치에서 `.mapToInt(Grade::score)`와 `.mapToInt(grade -> grade.score())`는 같은 동작입니다. Java 8의 일반 클래스에서도 `.mapToInt(Grade::getScore).sum()`으로 합계를 낼 수 있습니다. 메서드 이름은 실제 클래스에 존재하는 이름을 사용합니다.

## filter와 map과 toList

```java
List<String> passedNames = grades.stream()
    .filter(grade -> grade.score() >= PASS_SCORE)
    .map(Grade::studentName)
    .toList();
```

- filter는 60점 이상인 Grade만 다음 처리로 넘깁니다. 원본 목록에서 원소를 삭제하지 않습니다.
- map은 Grade에서 이름 String을 꺼내 다음 처리가 이름을 대상으로 하게 만듭니다. 원본 Grade를 변경하지 않습니다.
- toList는 이름들을 하나의 목록으로 모읍니다.

filter/map은 중간 처리이고 sum/toList/forEach는 마지막 처리입니다. 마지막 처리를 호출할 때 실제 순회가 시작됩니다. stream()은 기본적으로 순차 처리이며 자동으로 여러 스레드가 실행되는 것이 아닙니다. 사용이 끝난 Stream은 재사용하지 않고 원본 목록에서 다시 만듭니다.

Java 8에서 Stream으로 이름 목록을 만드는 방법도 있습니다. 기존 연습 파일은 반복문 비교용이므로 아래 코드로 덮어쓰지 않았습니다.

```java
// 파일 위쪽에 import java.util.stream.Collectors; 추가
// 아래 코드는 main 등 메서드 안에 작성
List<String> passedNames = grades.stream()
    .filter(grade -> grade.getScore() >= PASS_SCORE)
    .map(Grade::getStudentName)
    .collect(Collectors.toCollection(ArrayList::new));
```

이 코드는 수정 가능한 ArrayList를 명시적으로 생성합니다. 21 예제의 toList 결과는 수정 불가라 add/remove하면 UnsupportedOperationException이 발생합니다. 두 버전의 출력이 같아도 결과 목록의 수정 가능 여부는 다릅니다.

## record와 getter

일반 클래스에서는 필드·생성자·getter를 직접 작성했습니다. record는 이 선언을 줄이고 접근자 등을 제공합니다.

```java
record Grade(String studentName, int score) {}
```

new Grade로 생성하고 studentName()/score()로 값을 읽습니다. get 접두사가 없어도 getter와 값 읽기 역할이 대응합니다. equals/hashCode/toString도 생성되므로 생성자/getter만 있는 일반 클래스와 모든 동작이 같지는 않습니다. record 필드는 재대입 불가이며, 이 예제의 점수를 바꾸려면 새 Grade를 만들거나 입력 코드를 수정합니다.

## 이번 실습 결과 — 2026-10-14

사용자가 두 버전에서 학습자B 점수 55→65 변경, 점수 출력 forEach 추가. 두 버전 컴파일·실행 성공, 출력 일치:

```text
학습자 수: 3
점수 합계: 220
80, 65, 75, 통과: 3
미통과: 0
통과 학습자: [학습자A, 학습자B, 학습자C]
```

합계는 210→220으로 늘고 B도 60 이상이 되어 전원 통과합니다. 점수와 통과 문구가 붙는 것은 print의 줄바꿈 동작 때문이며 계산 오류가 아닙니다. 실습 코드는 보존하고 설명 주석만 추가했습니다.

공식 참고: [Java 8 Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html), [Java 8 Iterable.forEach](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html#forEach-java.util.function.Consumer-), [Stream.toList의 도입 버전과 수정 불가 규칙](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html#toList()).

## try / catch / throw / getMessage — 별도 답지로 보기

2026-10-15. 예외를 발생시키는 생성자와 실패를 처리하는 호출부의 차이는 [예외처리 답지 해설](answers/예외처리_답지_해설.md)에 정리했습니다. [Java 8](answers/ExpenseExceptionAnswerJava8.java)·[Java 21](answers/ExpenseExceptionAnswerJava21.java) 답지에도 실행 순서와 이유를 주석으로 적었습니다.

- `throw new IllegalArgumentException("설명")`: 설명을 담은 예외를 던집니다.
- `try`: 예외가 생길 수 있는 등록 코드를 실행합니다.
- `catch (IllegalArgumentException e)`: 해당 종류의 예외가 발생했을 때 처리합니다.
- `e.getMessage()`: 담겨 있는 설명을 문자열로 읽습니다. 인자는 넣지 않으며 출력은 println이 합니다.

## 문자열 숫자 변환과 catch 범위 — 다음 실습

[02. 문자열 금액 입력](lessons/02-amount-input/README.md)에서 직접 적용합니다.

- 변환(파싱): `new BigDecimal("12000")`처럼 문자열을 계산할 수 있는 금액 객체로 바꾸는 일입니다. `"abc"`나 `""`는 NumberFormatException이 발생합니다.
- 호출 전 인자 계산: `registerExpense(..., new BigDecimal(amountText))`에서는 숫자 변환이 메서드 진입 전에 실행됩니다. 그 예외는 등록 메서드 내부 catch가 잡지 못합니다. 이번에는 문자열을 그대로 전달한 뒤 메서드의 try 안에서 변환합니다.
- NumberFormatException: 문자열의 숫자 형식이 잘못된 경우입니다. IllegalArgumentException의 하위 예외라 상위 타입 catch에서도 처리할 수 있습니다.
- catch 순서: 종류를 나눠 처리하려면 NumberFormatException을 먼저, IllegalArgumentException을 나중에 둡니다. 해당하는 catch 하나가 선택되어 실행됩니다.

공식 참고: [BigDecimal(String)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html#%3Cinit%3E(java.lang.String)), [NumberFormatException](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/NumberFormatException.html).
