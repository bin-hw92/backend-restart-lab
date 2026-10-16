import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 같은 예외처리 답지 — Java 21에서 record·Stream과 함께 쓰는 형태.
 * try / catch / throw / getMessage()의 사용법은 Java 8 답지와 같습니다.
 * 먼저 8 답지의 [A]~[C]를 읽고, 이 파일에서는 같은 위치를 비교하세요.
 */
public class ExpenseExceptionAnswerJava21 {
    record Expense(LocalDate date, String category, BigDecimal amount) {
        // [A] record의 compact constructor에서도 같은 검증을 합니다.
        Expense {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                // 여기서는 거절만 합니다. 등록하는 쪽의 catch가 실패 메시지를 출력합니다.
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
            // 정상 종료한 경우 record가 인자를 필드에 자동으로 대입합니다.
            // throw한 경우에는 정상 객체 생성이 완료되지 않습니다.
        }
    }

    public static void main(String[] args) {
        // List.of는 수정 불가 목록이므로 add하려면 ArrayList로 감쌉니다.
        // 이 안에 0원 지출을 넣으면 인자를 만드는 도중 예외가 나서 목록도 완성되지 않습니다.
        // 정상 목록을 먼저 만들고, 실패를 다룰 추가 등록은 아래 try에서 따로 합니다.
        List<Expense> expenses = new ArrayList<>(List.of(
            new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")),
            new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("5000")),
            new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("3000"))
        ));

        // [B-1] 문제 1: 이 부분은 Java 8 답지와 동일합니다.
        try {
            // new Expense → 생성자에서 throw → add 실행 없이 catch로 이동.
            expenses.add(new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("0")));
        } catch (IllegalArgumentException e) {
            // e.getMessage()로 설명을 읽고 println으로 출력합니다.
            System.out.println("등록 실패: " + e.getMessage());
        }

        // [B-2] 문제 2: try를 분리했으므로 앞 등록이 실패해도 이 등록은 시도합니다.
        try {
            expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("-1000")));
        } catch (IllegalArgumentException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }

        // [C] 집계는 try-catch 뒤에 그대로 둡니다. 정상 5건으로 계산합니다.
        // map은 금액을 꺼내고, reduce는 0부터 금액을 누적합니다.
        // Stream 자체는 Java 8부터 사용할 수 있습니다.
        BigDecimal total = expenses.stream()
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 묶을 기준 / 결과 Map / 각 그룹의 금액 합계를 지정합니다.
        Map<String, BigDecimal> byCategory = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::category,
                LinkedHashMap::new,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        // 8 답지의 if + 누적에 대응합니다. 정확히 10000원도 포함합니다.
        BigDecimal highValueTotal = expenses.stream()
            .filter(expense -> expense.amount().compareTo(new BigDecimal("10000")) >= 0)
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<LocalDate, BigDecimal> byDate = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::date,
                TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
        System.out.println("TRANSPORT 지출: " + byCategory.get("TRANSPORT"));
        System.out.println("1만원 이상 지출: " + highValueTotal);
        System.out.println("날짜별 지출: " + byDate);
    }

    // [D] 문제 3 답지: 현재 main에서는 호출하지 않는 메서드 분리 형태입니다.
    // main의 두 try-catch를 이 메서드 두 번 호출로 바꾸면 같은 결과가 나옵니다.
    // 매개변수·static·void의 뜻은 Java 8 답지의 같은 메서드 주석을 참고하세요.
    static void registerExpense(List<Expense> expenses, LocalDate date,
                                String category, BigDecimal amount) {
        try {
            expenses.add(new Expense(date, category, amount));
        } catch (IllegalArgumentException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
    }
}
