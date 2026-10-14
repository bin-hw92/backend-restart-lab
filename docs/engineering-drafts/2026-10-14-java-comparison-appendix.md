
---

## 이번에 비교한 핵심 코드

같은 성적 입력으로 데이터 선언, 합계, 조건 선택, 이름 목록, 출력을 비교했다. 아래 코드는 전체 프로그램에서 핵심 부분만 발췌한 것이다.

| 공부한 부분 | Java 8 예제 | Java 21 예제 |
| --- | --- | --- |
| 데이터 한 건 | 일반 클래스·생성자·getter | record·자동 접근자 |
| 점수 합계 | for문으로 누적 | mapToInt → sum |
| 통과 이름 | if → 이름을 목록에 추가 | filter → map → toList |
| 점수 출력 | List.forEach | Stream.forEach |

### 1. 학습자 한 명을 표현하기

**Java 8 — 필드, 생성자, getter를 직접 작성**

```java
static class Grade {
    private final String studentName;
    private final int score;

    Grade(String studentName, int score) {
        this.studentName = studentName;
        this.score = score;
    }

    public String getStudentName() { return studentName; }
    public int getScore() { return score; }
}
// 읽기: grade.getScore()
```

**Java 21 — record로 같은 데이터를 선언**

```java
// 생성자·접근자 등을 자동 제공한다. record는 Java 16부터 정식 제공.
record Grade(String studentName, int score) {}
// 읽기: grade.score() — get 접두사 없이 값을 읽는다.
```

### 2. 같은 입력에서 합계와 통과 이름 구하기

**두 버전에서 직접 수정한 입력**

```java
List<Grade> grades = new ArrayList<>();
grades.add(new Grade("학습자A", 80));
grades.add(new Grade("학습자B", 65)); // 55 → 65로 직접 수정
grades.add(new Grade("학습자C", 75));
// 통과 기준: PASS_SCORE = 60
```

**Java 8 — 순회·누적·조건 분기를 직접 작성**

```java
int totalScore = 0;
List<String> passedNames = new ArrayList<>();

for (int i = 0; i < grades.size(); i++) {
    Grade grade = grades.get(i);
    totalScore += grade.getScore(); // 한 명씩 점수를 더한다.

    if (grade.getScore() >= PASS_SCORE) { // 60점 이상만 선택
        passedNames.add(grade.getStudentName()); // 이름을 모은다.
    }
}
```

**Java 21 — 처리 단계를 연결해서 작성**

```java
int totalScore = grades.stream()
    .mapToInt(Grade::score) // 점수 int를 꺼낸다.
    .sum();               // 80 + 65 + 75 = 220

List<String> passedNames = grades.stream()
    .filter(grade -> grade.score() >= PASS_SCORE) // 조건: if와 대응
    .map(Grade::studentName)                     // Grade → 이름 String
    .toList();                                  // 이름들을 목록으로 모은다.
```

`Grade::score`는 이 위치에서 `grade -> grade.score()`와 같은 동작을 뜻한다. `toList()`는 Java 16부터 제공되며 결과 목록은 수정할 수 없다. Java 8 예제의 `ArrayList`는 수정할 수 있어, 출력이 같아도 결과 목록의 성질은 다르다.

### 3. 직접 추가한 점수 출력

```java
// Java 8: 목록 자체를 순회하며 getter로 점수를 읽는다.
grades.forEach(grade -> System.out.print(grade.getScore() + ", "));

// Java 21: Stream으로 순회하며 record 접근자로 점수를 읽는다.
grades.stream().forEach(grade -> System.out.print(grade.score() + ", "));
```

`->`는 원소 하나를 받아 수행할 동작을 적는 람다 문법이다. 단순 출력은 Stream 없이 `List.forEach`로도 가능하다. `print()`에는 줄바꿈이 없어 점수 뒤에 다음 문구가 붙었다.

### 4. 두 버전에서 확인한 결과

```text
학습자 수: 3
점수 합계: 220
80, 65, 75, 통과: 3
미통과: 0
통과 학습자: [학습자A, 학습자B, 학습자C]
```

Stream·람다·메서드 참조·forEach는 Java 8에도 있다. 이번 비교는 익숙한 반복문 방식과 최신 예제의 표현 방식을 비교한 것이며, Stream 자체가 Java 21에서 새로 생긴 기능은 아니다.
