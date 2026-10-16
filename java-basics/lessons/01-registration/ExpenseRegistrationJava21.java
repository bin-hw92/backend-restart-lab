import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 같은 등록 결과 실습 — Java 21 방식.
 * boolean / return / try-catch는 Java 8 파일과 같은 방식으로 작성할 수 있습니다.
 * record와 합계 계산만 비교하세요. 문제: 같은 폴더의 README.md
 * 2026-10-16 확인: 사용자가 TODO 1·2를 작성했고, 8과 동일한 성공 2건·실패 2건·합계 17000을 확인했습니다.
 */
public class ExpenseRegistrationJava21 {
    record Expense(LocalDate date, String category, BigDecimal amount) {
        // record는 생성자·amount() 같은 접근자 등을 만들어 줍니다(Java 16부터 정식 기능).
        // 아래는 간결한 생성자입니다. 검증을 통과하면 각 필드에 인자가 자동으로 대입됩니다.
        Expense {
            // 이미 배운 검증입니다. 그대로 둡니다.
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
        }
    }

    public static void main(String[] args) {
        List<Expense> expenses = new ArrayList<>();
        String[] amounts = {"12000", "0", "-1000", "5000"};
        int successCount = 0;
        int failureCount = 0;

        // 21에서도 순서대로 등록하고 결과별 건수를 세는 데 for/if를 그대로 사용할 수 있습니다.
        for (String amountText : amounts) {
            // 메서드가 반환한 true/false로 성공·실패를 판단합니다.
            boolean registered = registerExpense(
                expenses, LocalDate.of(2026, 10, 16), "FOOD", new BigDecimal(amountText)
            );

            // TODO 2 확인: 등록 메서드의 true/false에 따라 성공 또는 실패 건수만 증가합니다.
            if (registered) {
                // TODO 2-success: 성공 건수 누적
                successCount += 1;
            } else {
                // TODO 2-failure: 실패 건수 누적
                failureCount += 1;
            }
        }

        System.out.println("등록 성공: " + successCount);
        System.out.println("등록 실패: " + failureCount);
        System.out.println("등록된 지출 수: " + expenses.size());
        System.out.println("전체 지출: " + calculateTotal(expenses));
    }

    // TODO 1: 8과 같은 try-catch로 작성하세요. record여도 등록 흐름은 같습니다.
    // 성공한 add 뒤 true 반환 / IllegalArgumentException이면 사유 출력 후 false 반환.
    // 실패 메시지: "등록 실패 사유: " + e.getMessage()
    // return은 현재 메서드 호출을 끝내고 호출한 main의 다음 처리로 돌아갑니다.
    static boolean registerExpense(List<Expense> expenses, LocalDate date,
                                   String category, BigDecimal amount) {
        // TODO 1-start: 사용자가 작성한 풀이입니다. try-catch·return은 8과 같은 문법입니다.
        try {
            // record 생성자에서 0원·음수 검증 예외가 발생하면 add와 return true는 실행되지 않습니다.
            expenses.add(new Expense(date, category, amount));
            return true;
        } catch (IllegalArgumentException e) {
            // 생성자의 예외를 이 메서드에서 처리하고, main에는 실패 결과 false를 전달합니다.
            System.out.println("등록 실패 사유: " + e.getMessage());
            return false;
        }
        // TODO 1-end
    }

    // 완성된 참고 코드. 8의 getter + 반복 누적에 대응합니다.
    // Stream은 Java 8부터 있고, 여기서는 record 접근자 amount()를 사용합니다.
    static BigDecimal calculateTotal(List<Expense> expenses) {
        return expenses.stream()
            .map(Expense::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
