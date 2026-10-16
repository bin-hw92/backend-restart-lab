# React에서 익숙했던 오류 처리를 Java의 try-catch로 다시 이해하기

## React에서는 자주 직접 쓰지 않았던 문법

Java를 다시 공부하다가 try-catch에서 잠깐 막혔다. 처음 보는 문법은 아닌데, 막상 지출 예제에 어디부터 넣어야 하는지 생각하니 헷갈렸다.

JavaScript와 React를 사용할 때는 초기에 비동기 요청이나 콜백을 다루면서 오류 처리를 신경 썼던 기억이 있다. 이후에는 React Query와 Axios 같은 도구를 쓰면서, 모든 요청마다 직접 try-catch를 적는 일이 많지 않았다. 라이브러리가 제공하는 실패 상태나 공통 오류 처리 흐름을 사용하는 쪽에 더 익숙해져 있었다.

그래서 이번에는 문법을 먼저 외우기보다, React에서 사용하던 오류 처리와 Java의 작은 지출 예제를 연결해서 정리해 보기로 했다.

## React Query를 쓰면 어떤 부분을 맡기는 걸까?

아래는 React 컴포넌트 안에서 사용한다고 가정한 간단한 예시다. API 주소는 설명용이다.

```jsx
const expenseQuery = useQuery({
  queryKey: ['expenses'],
  queryFn: async () => {
    const response = await axios.get('/api/expenses');
    return response.data;
  },
});

if (expenseQuery.isError) {
  return <p>지출을 불러오지 못했습니다.</p>;
}
```

여기에는 직접 쓴 try-catch가 없다. 대신 요청이 실패했을 때 React Query가 오류 상태를 관리하고, 화면에서는 isError를 보고 안내 문구를 보여준다. 요청과 상태 관리를 도구에 맡기고, 사용자에게 무엇을 보여줄지는 내가 정하는 형태다.

React Query가 실패를 알아보려면 queryFn에서 예외가 던져지거나, 반환한 Promise가 거부되어야 한다. 실패를 catch한 뒤 정상 데이터처럼 빈 배열을 반환하면, 그 결과는 성공으로 다뤄질 수 있다. 오류를 잡는 위치가 중요한 이유다. [TanStack Query의 Query Functions 설명](https://tanstack.com/query/latest/docs/framework/react/guides/query-functions?from=reactQueryV3)

Axios는 HTTP 요청을 보내고, 기본 설정에서는 2xx 밖의 응답이나 네트워크 문제 등을 Promise의 실패로 전달한다. 성공으로 볼 HTTP 상태 범위는 validateStatus 설정에 따라 달라질 수 있다. 알림이나 실패 화면까지 자동으로 완성해 주는 것은 아니므로, 그 이후 동작은 애플리케이션에서 정해야 한다. [Axios의 오류 처리 설명](https://axios-http.com/docs/handling_errors)

## try-catch는 비동기 전용 문법이 아니었다

JavaScript에서는 async/await와 함께 보는 일이 많아서 비동기 처리 문법처럼 기억하고 있었던 것 같다. 하지만 try-catch 자체는 실행 중 던져진 예외를 처리하는 문법이다. JSON.parse처럼 바로 실행되는 코드에서도 사용할 수 있다. [MDN의 try-catch 설명](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/try...catch)

비동기 요청을 직접 처리한다면 아래처럼 쓸 수 있다.

```js
async function loadExpenses() {
  try {
    const response = await axios.get('/api/expenses');
    console.log(response.data); // 요청 성공 시 실행
  } catch (error) {
    console.log('지출을 불러오지 못했습니다.');
  }
}
```

await는 Promise가 거부되면 그 지점에서 예외를 던지므로, 주변 catch에서 처리할 수 있다. 다만 나중에 별도로 실행되는 콜백 안의 예외까지 바깥 try가 자동으로 잡아주는 것은 아니다. Promise의 .catch()와 try-catch 문법도 구분해서 봐야 한다. [MDN의 await 설명](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/await)

이번 Java 지출 예제는 비동기 요청이 없다. 객체를 만드는 생성자가 잘못된 금액을 거절하는 상황에서 try-catch를 사용한다.

## Java에서는 금액을 검사하는 곳부터 보기

지출 한 건을 나타내는 Expense에는 “금액이 0보다 커야 한다”는 규칙이 있다. 아래는 Java 8 방식 답지에 있는 생성자 부분이다.

```java
Expense(LocalDate date, String category, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        // 잘못된 금액이면 정상 객체 생성을 중단한다.
        throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
    }

    this.date = date;
    this.category = category;
    this.amount = amount;
}
```

if는 금액이 조건에 맞는지 검사한다. new IllegalArgumentException은 설명을 담은 예외 객체를 만들고, throw는 그 예외를 실제로 던진다. 0원을 넣으면 생성자가 중단되어 아래 필드 대입까지 진행하지 않는다.

그다음 등록하는 쪽에서 실패했을 때 무엇을 할지 정한다. 아래 코드는 기존 정상 지출 목록이 준비된 뒤에 실행하는 부분이다.

```java
try {
    expenses.add(new Expense(
        LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("0")
    ));
} catch (IllegalArgumentException e) {
    System.out.println("등록 실패: " + e.getMessage());
}

// 실패한 등록을 처리한 뒤, 기존 정상 지출의 합계를 계산한다.
```

순서를 풀어보면 이렇다.

1. add에 넣을 값을 구하려고 new Expense부터 실행한다.
2. 생성자에서 0원을 확인하고 예외를 던진다.
3. 정상 객체 생성이 완료되지 않아 add도 호출되지 않는다.
4. 해당 예외를 처리하는 catch로 이동해 실패 메시지를 출력한다.
5. catch가 정상적으로 끝나면 그다음 합계 계산을 계속한다.

예외가 발생한 뒤 남아 있는 try 내부 코드는 건너뛴다. catch가 끝나도 실패한 줄로 돌아가 다시 실행하지 않는다. 정상 금액이면 생성과 등록을 마치고 catch를 건너뛴다.

여기서는 IllegalArgumentException을 처리한다. 모든 종류의 오류를 무조건 잡는다는 뜻은 아니다. 또한 catch 안에서 return하거나 예외를 다시 던지는 경우에는 이후 흐름도 달라진다.

## 내가 헷갈렸던 부분

처음에는 “void 메서드에서 try-catch를 쓰고, 별도 클래스를 만들어 각 데이터를 거치게 하는 건가?”라고 생각했다. 정리해 보니 반환형과 예외처리는 별개의 문제였다. 반환값이 있는 메서드나 생성자 안에서도 try-catch를 사용할 수 있다.

| 이번 예제의 위치 | 맡은 역할 |
| --- | --- |
| Expense 생성자 | 금액 규칙 검사. 잘못된 값이면 throw |
| 등록하는 코드의 try-catch | 생성과 추가를 시도하고, 실패 후 동작 결정 |
| registerExpense 메서드 | 반복되는 등록과 실패 처리를 한곳에 모음 |
| main 메서드 | 등록할 데이터를 정하고 이후 집계 진행 |

모든 new Expense가 같은 생성자를 거치므로, 금액 검증을 등록할 때마다 복사하지 않아도 된다. try-catch를 사용하려고 별도 클래스를 반드시 만들어야 하는 것은 아니다. 지금 답지의 registerExpense도 같은 답지 클래스 안에 만든 메서드다.

또 e.getMessage("설명")처럼 메시지를 넣으려고 했던 부분도 있었다. 메시지는 예외를 만들 때 넣고, catch에서는 인자 없는 e.getMessage()로 읽는다. 읽은 문자열을 화면에 보여주는 역할은 println이 한다.

## 반복되는 등록을 메서드로 옮기면

처음에는 0원과 음수를 각각 별도 try-catch로 시도하는 흐름부터 봤다. 두 등록을 한 try 안에 두면 첫 등록에서 예외가 발생했을 때 두 번째 등록을 건너뛰기 때문이다.

흐름을 본 다음에는 반복 부분을 아래 메서드로 모을 수 있다. 이 메서드는 기존 Expense를 사용하는 답지 클래스 안에 둔다.

```java
static void registerExpense(List<Expense> expenses, LocalDate date,
                            String category, BigDecimal amount) {
    try {
        expenses.add(new Expense(date, category, amount));
    } catch (IllegalArgumentException e) {
        System.out.println("등록 실패: " + e.getMessage());
    }
}
```

main에서는 이렇게 호출한다.

```java
registerExpense(expenses, LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("0"));
registerExpense(expenses, LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("-1000"));
```

첫 호출의 실패를 메서드 안에서 처리하고 돌아오므로 두 번째 호출도 실행된다. 같은 목록을 전달하므로 정상 등록에 성공하면 그 목록에 지출이 추가된다. 여기서 void는 반환값이 없다는 뜻이다.

React에서 공통 요청 함수나 훅으로 반복 코드를 모았던 경험과 연결해서 보니, 왜 메서드로 나누는지 조금 더 쉽게 접근할 수 있었다. 다만 금액의 규칙을 검사하는 일과 실패를 사용자에게 알리는 일은 각각 어디에 둘지 생각해야 했다.

## Java 8과 21에서도 예외처리 흐름은 같다

Java 21 방식 답지에서는 일반 클래스 대신 record를 사용한다. 아래는 해당 선언과 검증 부분이다.

```java
record Expense(LocalDate date, String category, BigDecimal amount) {
    Expense {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }
    }
}
```

| 비교할 부분 | Java 8 방식 답지 | Java 21 방식 답지 |
| --- | --- | --- |
| 지출 데이터 표현 | 일반 클래스와 getter | record와 접근자 |
| 생성할 때 검증 | 생성자에서 if + throw | record 생성자에서 if + throw |
| 등록 실패 처리 | 호출하는 쪽의 try-catch | 같은 try-catch |
| 정상 지출 집계 | 반복문으로 누적 | Stream으로 집계 |

try-catch는 두 버전에서 같은 방식으로 사용할 수 있다. Stream도 Java 8부터 있는 기능이다. 이번에는 최신 문법을 더 익히는 것보다, 예외가 어디서 발생하고 어디서 처리되는지부터 따라가려고 한다.

## 이번에 확인한 범위

기존 연습 파일은 보존하고, AI가 별도로 작성한 Java 8·21 답지와 해설을 먼저 보고 있다. 에이전트가 JDK 21에서 각각 --release 8과 --release 21로 컴파일하고 실행한 결과는 아래와 같았다. 실제 Java 8 런타임 실행과는 구분한다.

```text
등록 실패: 금액은 0보다 커야 합니다.
등록 실패: 금액은 0보다 커야 합니다.
전체 지출: 54000
```

0원과 음수는 등록에 실패하고, 기존 정상 지출 5건의 합계는 계속 출력된다. 메서드로 나눈 형태도 에이전트가 같은 결과를 확인했다.

아직 혼자 문제를 다 풀었다고 하기는 어렵다. 우선 답지에서 생성자 → throw → catch → 다음 코드 순서를 읽어보고, 이후에 다른 문제로 직접 적용해 볼 생각이다.
