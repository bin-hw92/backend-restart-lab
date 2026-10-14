import java.util.ArrayList;
import java.util.List;

/** 교육 서비스 상황을 가정한 독립 연습 예제. 회사 소스·DB를 사용하지 않습니다. */
public class GradeSummary {
    // 공통 용어 설명: ../JAVA_TERMS.md (이 java8 폴더 기준)
    // static: 클래스에서 함께 사용하는 값. final: 초기화 후 재대입 불가.
    private static final int PASS_SCORE = 60;

    // 한 명의 이름과 점수를 담는 일반 클래스. static 내부 클래스라 바깥 객체 없이 생성 가능.
    static class Grade {
        private final String studentName;
        private final int score;

        // 생성자: new Grade("학습자A", 80)의 값을 이 객체의 필드에 저장합니다.
        Grade(String studentName, int score) {
            this.studentName = studentName;
            this.score = score;
        }

        // getter: private 필드를 읽는 일반 메서드. get 접두사는 이름 관례입니다.
        public String getStudentName() {
            return studentName;
        }

        // Java 21 record의 score() 접근자와 값을 읽는 역할이 대응합니다.
        public int getScore() {
            return score;
        }
    }

    public static void main(String[] args) {
        // List<Grade>: Grade만 담는 목록 타입. ArrayList: 실제 저장 구현, add/remove 가능.
        List<Grade> grades = new ArrayList<Grade>();
        // 연습할 때 이 입력 부분을 먼저 바꿉니다.
        grades.add(new Grade("학습자A", 80));
        grades.add(new Grade("학습자B", 65));
        grades.add(new Grade("학습자C", 75));

        int totalScore = 0;
        List<String> passedNames = new ArrayList<String>();

        // Athena에서 확인한 인덱스 for문과 if 분기 방식으로 복습합니다.
        // size()는 원소 수, get(i)는 i번째 원소. 인덱스는 0부터 시작합니다.
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 각 점수를 꺼내 누적: 최신 예제의 mapToInt(...).sum()에 대응합니다.
            totalScore = totalScore + grade.getScore();

            // 조건에 맞는 항목 선택: 최신 예제의 filter에 대응합니다.
            if (grade.getScore() >= PASS_SCORE) {
                // 이름을 꺼내 결과 목록에 추가: 최신 예제의 map(...).toList()에 대응합니다.
                passedNames.add(grade.getStudentName());
            }
        }

        System.out.println("학습자 수: " + grades.size());
        System.out.println("점수 합계: " + totalScore);
        // 직접 추가한 코드. List.forEach는 Java 8부터 제공되며 Stream을 만들지 않습니다.
        // grade -> ... 는 람다: 목록 원소 하나를 grade로 받아 오른쪽 동작을 수행합니다.
        // for (Grade grade : grades) { System.out.print(grade.getScore() + ", "); }와 같은 순회.
        // print는 줄바꿈하지 않아 다음 "통과" 출력과 같은 줄에 붙습니다.
        // 줄을 바꾸려면 이 순회 다음에 System.out.println();을 직접 추가해 보세요.
        grades.forEach(grade -> System.out.print(grade.getScore() + ", "));
        System.out.println("통과: " + passedNames.size());
        System.out.println("미통과: " + (grades.size() - passedNames.size()));
        System.out.println("통과 학습자: " + passedNames);
    }
}
