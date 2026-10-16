import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Java 8 예제와 같은 입력·출력을 최신 문법으로 비교합니다. */
// 문제 위치: 같은 폴더의 지출_연습문제.md. 각 문제를 Java8 파일에도 똑같이 적용합니다.
// 순서: FOOD 5000 추가 → TRANSPORT 3000 추가/출력 → 1만원 이상 합계 → 날짜별 합계.
public class ExpenseSummaryJava21 {
    // [5번 구현 확인] record의 생성자에서 0 이하 금액을 거절합니다. 아래 주석은 참고 형태입니다.
    // Expense { ... }는 compact constructor: 선언된 date/category/amount 인자를 검증합니다.
    // 블록이 정상 종료하면 필드에 값이 자동 대입되므로 this.amount = amount는 쓰지 않습니다.
    // record Expense(LocalDate date, String category, BigDecimal amount) {
    //     Expense {
    //         if (amount.compareTo(BigDecimal.ZERO) <= 0) {
    //             throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
    //         }
    //     }
    // }
    record Expense(LocalDate date, String category, BigDecimal amount) {
        Expense {
            if(amount.compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
        }
    }

    public static void main(String[] args) {
        // [최종 확인] 조건 합계·날짜별 집계·생성자 검증이 구현됐습니다.
        // 기본 실행은 정상 합계 확인용. 0원 테스트는 별도로 활성화합니다.
        // ArrayList로 감싸서 Java 8 예제처럼 add()로 연습할 수 있습니다.
        List<Expense> expenses = new ArrayList<>(List.of(
            new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")),
            new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("5000")),  //1번 푸드 추가.
            new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("3000")) //2번 카테고리 추가.
            // [5번 테스트] 생성자 검증 후 이 줄은 예외로 종료됩니다.
            // 1~4번 정상 결과를 확인할 때는 이 원소와 앞 원소 끝의 쉼표를 함께 정리하세요.
            //new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("0")) //5번 추가 문제.
        ));

        // [다음 실습] 예외처리_연습문제.md 1번: 이 위치에서 0원 지출을 add하려는 시도를 try-catch로 처리.
        // List.of 안의 0원 주석은 유지합니다. 생성 중 오류가 나면 목록 전체가 완성되지 않습니다.

        // map: Expense를 금액 BigDecimal로 바꿉니다. reduce: 0에서 시작해 금액을 하나씩 더합니다.
        // Expense::amount는 expense -> expense.amount(), BigDecimal::add는 (a, b) -> a.add(b).
        BigDecimal total = expenses.stream()
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // groupingBy의 세 인자: 묶을 기준 / 결과 Map 종류 / 각 그룹 안의 계산 방식.
        // reducing은 여기서 "각 카테고리 안의 금액을 0부터 더한다"는 계산 방법을 제공합니다.
        Map<String, BigDecimal> byCategory = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::category,
                LinkedHashMap::new,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        // [3번 구현 확인] Map이 아닌 BigDecimal 한 개의 조건합계를 구합니다. 아래는 참고 풀이.
        // BigDecimal highValueTotal = expenses.stream()
        //     .filter(expense -> expense.amount().compareTo(new BigDecimal("10000")) >= 0)
        //     .map(Expense::amount)
        //     .reduce(BigDecimal.ZERO, BigDecimal::add);
        // filter는 Java 8 코드의 if, map은 getAmount(), reduce는 합계 누적에 대응합니다.
        BigDecimal byCategory2 = expenses.stream()
                .filter(expense -> expense.amount().compareTo(new BigDecimal("10000")) >= 0)
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // [4번 참고 풀이] 아래 실제 구현과 비교: 날짜 기준 + TreeMap + 그룹별 금액 누적.
        // Map<LocalDate, BigDecimal> byDate = expenses.stream()
        //     .collect(Collectors.groupingBy(
        //         Expense::date, // Expense::category 대신 날짜로 묶기
        //         TreeMap::new,  // LinkedHashMap의 입력 순서 대신 날짜 오름차순
        //         Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
        //     ));

        Map<LocalDate, BigDecimal> byCategory3 = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::date,
                TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
        System.out.println("TRANSPORT 지출: " + byCategory.get("TRANSPORT"));

        // [3번 완료] byCategory2는 조건합계 숫자입니다. highValueTotal 같은 이름도 가능합니다.
        // System.out.println("1만원 이상 지출: " + highValueTotal); // 정상 5건이면 37000
        System.out.println("1만원 이상 지출: " + byCategory2); //3번 1만원 이상 지출
        // [4번 완료] byCategory3는 TreeMap의 날짜별 금액 집계입니다.
        // System.out.println("날짜별 지출: " + byDate); // {2026-10-01=37000, 2026-10-02=17000}
        System.out.println("날짜별 지출: " + byCategory3); //4번 날짜별.
    }
}
