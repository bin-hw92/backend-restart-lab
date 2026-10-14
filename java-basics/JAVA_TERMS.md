# 현재 예제의 Java 용어

Map 순서 비교는 [지출 연습문제의 TreeMap/LinkedHashMap 설명](지출_연습문제.md#treemap과-linkedhashmap-차이)에 있습니다. 카테고리는 입력 순서를 유지하고 날짜는 오름차순으로 정렬하는 이유를 같은 날짜 입력 예제로 비교합니다.

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
