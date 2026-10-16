import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 예외처리 연습문제 1·2번 답지 — Java 8 방식.
 * 먼저 [A] 생성자 → [B] 등록 시도 → [C] 집계 순서로 읽으세요.
 * 3번 메서드 분리 답지는 맨 아래 registerExpense에 있습니다.
 * 사용자 연습 파일과 독립된 답지이며, 자세한 설명은 예외처리_답지_해설.md 참고.
 */
public class ExpenseExceptionAnswerJava8 {
    static class Expense {
        private final LocalDate date;
        private final String category;
        private final BigDecimal amount;

        Expense(LocalDate date, String category, BigDecimal amount) {
            // [A] 생성자의 책임: 올바른 금액을 가진 지출만 만들기.
            // compareTo가 반환한 정수를 <= 0과 비교해 "금액이 0 이하인가?"를 판단합니다.
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                // new: 설명을 담은 예외 객체 만들기. throw: 그 예외를 실제로 던지기.
                // 여기서 생성자를 중단하고, 호출한 쪽의 맞는 catch를 찾아 이동합니다.
                // 따라서 아래 this.amount = amount까지 도달하지 않습니다.
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
            // 생성자 안에서 예외를 잡고 계속 진행하면 잘못된 금액도 저장될 수 있습니다.
            // 그래서 여기서는 거절하고, 등록하는 쪽에서 실패 후 무엇을 할지 결정합니다.
            this.date = date;
            this.category = category;
            this.amount = amount;
        }

        public LocalDate getDate() { return date; }
        public String getCategory() { return category; }
        public BigDecimal getAmount() { return amount; }
    }

    public static void main(String[] args) {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("5000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("3000")));

        // [B-1] 문제 1: try 안에는 "실패할 수 있는 등록 시도"를 넣습니다.
        try {
            // ① add의 인자를 구하려고 new Expense(...)부터 실행합니다.
            // ② 생성자가 0원을 보고 throw하므로 객체 생성은 완료되지 않습니다.
            // ③ 따라서 expenses.add도 실행되지 않고 catch로 이동합니다.
            expenses.add(new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("0")));
            // 여기에 다른 코드가 있어도, 위 줄에서 예외가 발생한 경우에는 실행하지 않습니다.
        } catch (IllegalArgumentException e) {
            // ④ 이 종류의 예외를 받습니다. e는 던져진 예외 객체를 가리키는 변수입니다.
            // getMessage()는 생성할 때 넣은 설명을 읽습니다. 인자 없이 호출합니다.
            // 메시지를 화면에 보이게 하는 일은 println이 합니다.
            System.out.println("등록 실패: " + e.getMessage());
        }
        // ⑤ catch가 정상적으로 끝나면 try를 다시 실행하지 않고 그 다음 코드로 갑니다.

        // [B-2] 문제 2: 별도 try이므로 0원 실패와 관계없이 음수도 시도합니다.
        // 두 등록을 한 try에 넣으면 첫 번째 throw 이후 두 번째 등록은 건너뜁니다.
        try {
            expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("-1000")));
        } catch (IllegalArgumentException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }

        // [C] 집계는 try-catch 바깥: 실패한 두 건은 없고 정상 5건만 남아 있습니다.
        // 생성자 실패는 이미 처리했으므로 아래 계산과 출력은 계속 실행됩니다.
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal highValueTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> byDate = new TreeMap<>();

        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());

            String category = expense.getCategory();
            BigDecimal previous = byCategory.get(category);
            if (previous == null) {
                previous = BigDecimal.ZERO;
            }
            byCategory.put(category, previous.add(expense.getAmount()));

            if (expense.getAmount().compareTo(new BigDecimal("10000")) >= 0) {
                highValueTotal = highValueTotal.add(expense.getAmount());
            }

            LocalDate date = expense.getDate();
            BigDecimal previousByDate = byDate.get(date);
            if (previousByDate == null) {
                previousByDate = BigDecimal.ZERO;
            }
            byDate.put(date, previousByDate.add(expense.getAmount()));
        }

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
        System.out.println("TRANSPORT 지출: " + byCategory.get("TRANSPORT"));
        System.out.println("1만원 이상 지출: " + highValueTotal);
        System.out.println("날짜별 지출: " + byDate);
    }

    // [D] 문제 3 답지: B-1·B-2가 이해된 뒤 읽으세요. 현재 main에서는 호출하지 않습니다.
    // main의 두 try-catch를 지우고, 같은 인자를 전달하는 registerExpense 두 줄로 바꿀 수 있습니다.
    // List<Expense>: 담을 목록, LocalDate: 날짜, String: 분류, BigDecimal: 금액을 받습니다.
    // static: static main에서 이 메서드를 직접 호출하기 위해 사용합니다.
    // void: 결과값을 반환하지 않고 목록 추가 또는 실패 출력까지 이 메서드에서 처리합니다.
    static void registerExpense(List<Expense> expenses, LocalDate date,
                                String category, BigDecimal amount) {
        try {
            expenses.add(new Expense(date, category, amount));
        } catch (IllegalArgumentException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
        // 실패도 여기서 처리하므로 호출한 main의 다음 줄로 돌아갑니다.
        // 전달받은 expenses는 main과 같은 목록을 가리켜 성공한 add가 main에도 반영됩니다.
    }
}
