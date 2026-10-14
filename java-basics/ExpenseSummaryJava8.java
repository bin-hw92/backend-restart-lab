import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 일반 클래스와 반복문으로 먼저 복습하는 Java 8 예제. */
public class ExpenseSummaryJava8 {
    static class Expense {
        private final LocalDate date;
        private final String category;
        private final BigDecimal amount;

        Expense(LocalDate date, String category, BigDecimal amount) {
            this.date = date;
            this.category = category;
            this.amount = amount;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getCategory() {
            return category;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }

    public static void main(String[] args) {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000")));

        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();

        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());

            String category = expense.getCategory();
            BigDecimal previous = byCategory.get(category);
            if (previous == null) {
                previous = BigDecimal.ZERO;
            }
            byCategory.put(category, previous.add(expense.getAmount()));
        }

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));

    }
}
