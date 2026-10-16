import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 02. 문자열 금액 입력 — Java 8 방식.
 * 문제와 단계별 기대 결과: 같은 폴더의 README.md
 * 처음에는 TODO 1만 작성합니다. main의 호출·건수 계산은 완성된 참고 코드입니다.
 */
public class ExpenseInputJava8 {
    static class Expense {
        private final LocalDate date;
        private final String category;
        private final BigDecimal amount;

        Expense(LocalDate date, String category, BigDecimal amount) {
            // 숫자로 변환할 수 있어도 0원·음수는 지출로 등록하지 않는 기존 규칙입니다.
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
        // 숫자로 바꿀 수 없는 입력 뒤에도 마지막 5000원 등록을 시도해야 합니다.
        String[] amounts = {"12000", "abc", "0", "-1000", "", "5000"};
        int successCount = 0;
        int failureCount = 0;

        for (String amountText : amounts) {
            // 이전 실습과 달리 main에서 new BigDecimal(amountText)를 실행하지 않습니다.
            // 인자는 메서드에 들어가기 전에 계산되므로, 변환을 여기서 하면 메서드 안의 catch가 처리할 수 없습니다.
            boolean registered = registerExpense(
                expenses, LocalDate.of(2026, 10, 16), "FOOD", amountText
            );
            if (registered) {
                successCount += 1;
            } else {
                failureCount += 1;
            }
        }

        System.out.println("등록 성공: " + successCount);
        System.out.println("등록 실패: " + failureCount);
        System.out.println("등록된 지출 수: " + expenses.size());
        System.out.println("전체 지출: " + calculateTotal(expenses));
    }

    // 매개변수가 BigDecimal amount에서 String amountText로 바뀌었습니다.
    // 숫자 변환과 지출 생성·등록을 같은 try 안에서 처리하는 것이 이번 핵심입니다.
    static boolean registerExpense(List<Expense> expenses, LocalDate date,
                                   String category, String amountText) {
        try {
            // TODO 1-start: 이 임시 return을 아래 세 단계의 코드로 교체하세요.
            // ① amountText를 BigDecimal로 변환해 amount 변수에 저장합니다.
            // ② amount로 Expense를 만들어 expenses에 추가합니다.
            // ③ 추가에 성공한 뒤 true를 반환합니다.
            return false;
            // TODO 1-end
        } catch (IllegalArgumentException e) {
            // NumberFormatException은 IllegalArgumentException의 하위 예외이므로 여기서도 잡힙니다.
            System.out.println("등록 실패 사유: " + e.getMessage());
            return false;
        }
        // TODO 2 (선택, 1번 확인 후): 숫자 변환 실패를 따로 catch해 메시지를 구분합니다.
        // 구체적인 NumberFormatException의 catch를 IllegalArgumentException보다 먼저 배치해야 합니다.
        // 자세한 출력 형식과 이유는 README.md의 2번을 읽어보세요.
    }

    // 이전 실습의 완성된 합계 코드입니다. 이번에는 바꾸지 않아도 됩니다.
    static BigDecimal calculateTotal(List<Expense> expenses) {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        return total;
    }
}
