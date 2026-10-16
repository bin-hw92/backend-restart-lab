import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 다음 실습: 등록 결과를 boolean으로 돌려주기 — Java 8 방식.
 * 문제와 단계별 기대 결과: 같은 폴더의 README.md
 * TODO 1 → TODO 2 순서로 고칩니다.
 * 2026-10-16 확인: 사용자가 TODO 1·2를 작성했고, 성공 2건·실패 2건·합계 17000 실행 결과를 확인했습니다.
 */
public class ExpenseRegistrationJava8 {
    static class Expense {
        private final LocalDate date;
        private final String category;
        private final BigDecimal amount;

        Expense(LocalDate date, String category, BigDecimal amount) {
            // 이미 배운 검증입니다. 이번 실습에서는 그대로 둡니다.
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
            this.date = date;
            this.category = category;
            this.amount = amount;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }

    public static void main(String[] args) {
        List<Expense> expenses = new ArrayList<>();
        // 배열은 이번에 시도할 금액들입니다. 0원·음수 뒤의 5000원도 시도해야 합니다.
        String[] amounts = {"12000", "0", "-1000", "5000"};
        int successCount = 0;
        int failureCount = 0;

        for (String amountText : amounts) {
            // 인자: 호출할 때 전달하는 실제 값. amountText로 금액 객체를 만듭니다.
            // boolean 반환값: registerExpense가 끝난 뒤 돌려주는 true 또는 false.
            // 메서드의 return은 이 for문을 끝내지 않고, 호출한 이 줄로 돌아옵니다.
            // 메서드는 괄호와 함께 호출하고, new는 BigDecimal 같은 클래스의 객체를 만들 때 사용합니다.
            boolean registered = registerExpense(
                expenses, LocalDate.of(2026, 10, 16), "FOOD", new BigDecimal(amountText)
            );

            // TODO 2 확인: 메서드가 돌려준 등록 결과에 따라 해당 건수만 1씩 늘립니다.
            // if (registered)는 if (registered == true)와 같은 조건입니다.
            if (registered) {
                // TODO 2-success: += 1은 successCount = successCount + 1과 같습니다.
                successCount += 1;
            } else {
                // TODO 2-failure: false가 반환된 경우 실패 건수만 늘립니다.
                failureCount += 1;
            }
        }

        System.out.println("등록 성공: " + successCount);
        System.out.println("등록 실패: " + failureCount);
        System.out.println("등록된 지출 수: " + expenses.size());
        System.out.println("전체 지출: " + calculateTotal(expenses));
    }

    // TODO 1: 이전 답지의 void registerExpense를 참고해 이 메서드를 완성하세요.
    // ① try에서 new Expense와 expenses.add를 실행합니다.
    // ② 추가까지 성공했을 때 true를 반환합니다. add 전에 성공을 반환하면 안 됩니다.
    // ③ IllegalArgumentException을 catch해 "등록 실패 사유: " + e.getMessage()를 출력합니다.
    // ④ 실패했다면 false를 반환합니다. 생성자의 throw 검증은 유지합니다.
    // boolean은 반환값의 타입이고, static은 main에서 직접 호출하는 이 예제의 구조입니다.
    // 매개변수 expenses는 main의 목록과 같은 객체를 가리키므로, 성공한 add가 반영됩니다.
    static boolean registerExpense(List<Expense> expenses, LocalDate date,
                                   String category, BigDecimal amount) {
        // TODO 1-start: 사용자가 작성한 풀이입니다.
        try {
            // Expense 객체 생성에 성공하면 main에서 전달받은 목록에 추가합니다.
            expenses.add(new Expense(date, category, amount));

            // [확인] add가 성공한 뒤 true를 반환하는 위치는 맞습니다.
            return true;
        } catch (IllegalArgumentException e) {
            // 등록 실패 사유를 보여주고 false를 돌려줍니다.
            System.out.println("등록 실패 사유: " + e.getMessage());
            return false;
        }
        // TODO 1-end
    }

    // 반환형이 BigDecimal인 메서드: 합계를 계산해 호출한 쪽에 돌려줍니다.
    // 이번에는 완성된 참고 코드이므로 고치지 않아도 됩니다.
    static BigDecimal calculateTotal(List<Expense> expenses) {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        return total;
    }
}
