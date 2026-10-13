import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseSummary {
    record Expense(LocalDate date, String category, BigDecimal amount) {}

    public static void main(String[] args) {
        List<Expense> expenses = List.of(
            new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")),
            new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")),
            new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000"))
        );

        BigDecimal total = expenses.stream()
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> byCategory = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::category,
                Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)
            ));

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);

        
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
    }
}

