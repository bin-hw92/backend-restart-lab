import java.util.ArrayList;
import java.util.List;

/** Java 8 성적 예제와 같은 입력·결과를 record와 Stream으로 표현합니다. */
public class GradeSummary {
    // 공통 용어 설명: ../JAVA_TERMS.md (이 java21 폴더 기준)
    // 학습자B의 65점도 이 기준값 60 이상이므로 통과합니다.
    private static final int PASS_SCORE = 60;

    // record는 Java 16부터 정식 제공. 필드·생성자·접근자·equals/hashCode/toString 생성.
    // 일반 클래스의 getScore()에 대응하는 접근자 이름은 score()입니다.
    // 필드는 final이라 생성 후 score에 새 값을 대입할 수 없습니다.
    record Grade(String studentName, int score) {}

    public static void main(String[] args) {
        // <>는 왼쪽의 Grade 타입을 추론합니다. Java 7부터 있는 문법이라 8에서도 가능.
        List<Grade> grades = new ArrayList<>();
        // Java 8 버전과 같은 입력을 유지하며 수정합니다.
        grades.add(new Grade("학습자A", 80));
        grades.add(new Grade("학습자B", 65));
        grades.add(new Grade("학습자C", 75));

        // Stream은 Java 8부터 제공되는 처리 API이며 Java 21 전용이 아닙니다.
        // stream(): 원본 목록을 유지하며 순차 처리 흐름을 만듭니다.
        // Grade::score는 grade -> grade.score()와 같은 메서드 참조입니다.
        // mapToInt: 각 Grade의 점수 int를 꺼내 IntStream으로 바꿉니다. 값은 80, 65, 75.
        // sum: 이 정수들을 더해 220 반환. 마지막 작업을 호출하면 실제 처리가 실행됩니다.
        int totalScore = grades.stream()
            .mapToInt(Grade::score)
            .sum();

        // 사용이 끝난 Stream은 재사용하지 않고 동일한 목록에서 새 Stream을 만듭니다.
        // filter: 조건이 true인 Grade만 남깁니다. 원본 목록에서 삭제하는 것은 아닙니다.
        // map: Grade를 이름 String으로 변환합니다. 따라서 결과는 List<String>입니다.
        // toList: 이름들을 List로 수집. Java 16부터 제공되며 결과 목록은 수정 불가입니다.
        // passedNames.add(...)는 예외가 납니다. 8 예제의 ArrayList와 이 성질은 다릅니다.
        // filter/map은 처리 방법을 구성하고 toList 호출 시 실제로 순회합니다.
        List<String> passedNames = grades.stream()
            .filter(grade -> grade.score() >= PASS_SCORE)
            .map(Grade::studentName)
            .toList();

        System.out.println("학습자 수: " + grades.size());
        System.out.println("점수 합계: " + totalScore);
        // 직접 추가한 코드. Stream의 forEach로 원소마다 점수를 출력합니다.
        // 단순 출력은 grades.forEach(grade -> ...)로도 가능. 두 방식 모두 Java 8부터 제공.
        // print는 줄바꿈하지 않아 다음 "통과" 출력과 같은 줄에 붙습니다.
        // 줄을 바꾸려면 이 순회 다음에 System.out.println();을 직접 추가해 보세요.
        grades.stream().forEach(grade -> System.out.print(grade.score() + ", "));
        System.out.println("통과: " + passedNames.size());
        System.out.println("미통과: " + (grades.size() - passedNames.size()));
        System.out.println("통과 학습자: " + passedNames);
    }
}
