import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Java 8 예제와 같은 입력·출력을 최신 문법으로 비교합니다. */
public class ExpenseSummaryJava21 {
    record Expense(LocalDate date, String category, BigDecimal amount) {}

    public static void main(String[] args) {
        // ArrayList로 감싸서 Java 8 예제처럼 add()로 연습할 수 있습니다.
        List<Expense> expenses = new ArrayList<>(List.of(
            new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")),
            new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000"))
        ));

        BigDecimal total = expenses.stream()
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> byCategory = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::category,
                LinkedHashMap::new,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
    }
}
