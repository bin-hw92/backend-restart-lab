import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** 일반 클래스와 반복문으로 먼저 복습하는 Java 8 예제. */
// 문제 위치: 같은 폴더의 지출_연습문제.md. 각 문제를 Java21 파일에도 똑같이 적용합니다.
// 순서: FOOD 5000 추가 → TRANSPORT 3000 추가/출력 → 1만원 이상 합계 → 날짜별 합계.
public class ExpenseSummaryJava8 {
    static class Expense {
        private final LocalDate date;
        private final String category;
        private final BigDecimal amount;

        Expense(LocalDate date, String category, BigDecimal amount) {
            // [5번 첨삭 적용] 검증은 for문이 아니라 객체를 만드는 이 생성자에서 합니다.
            // compareTo는 boolean이 아니라 int 반환: 작은 값은 음수, 같은 값은 0, 큰 값은 양수.
            // <= 0 비교를 붙여 "금액이 0 이하인가?"라는 boolean 조건으로 만듭니다.
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                // new: 예외 객체 생성. throw: 그 객체로 실제 오류 발생. catch가 없으면 종료됩니다.
                // println은 반환값이 없는 void 함수라 throw 뒤에 사용할 수 없습니다.
                // [2026-10-16 업로드 정리] 작성했던 시도는 아래에 보존합니다.
                // 빈 try는 금액 오류를 발생시키지 않고, getMessage()에는 인자를 넣지 않습니다.
                // 생성자는 throw로 거절하고 등록하는 쪽에서 catch합니다. 별도 answers 답지 참고.
                /*
                try {

                }catch (IllegalArgumentException e){
                    e.getMessage("금액은 0보다 커야 합니다.");
                }
                */
                throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
            }
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
        // [최종 확인] 조건 합계·날짜별 누적·날짜 정렬·생성자 검증이 구현됐습니다.
        // 기본 실행은 정상 합계 확인용. 5번 오류 확인은 아래 0원 테스트 줄을 활성화합니다.
        // 자세한 채점 이력은 지출_채점_2026-10-14.md 참고.
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "FOOD", new BigDecimal("12000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 1), "BOOK", new BigDecimal("25000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("9000")));
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "FOOD", new BigDecimal("5000"))); //1번 푸드 추가.
        expenses.add(new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("3000"))); //2번 카테고리 추가.
        // [5번 테스트] 생성자 검증을 넣으면 이 줄에서 예외로 종료되는 것이 정상입니다.
        // 1~4번 합계를 확인할 때는 0원 테스트 줄을 잠시 주석 처리하고 실행하세요.
        // [다음 실습] 예외처리_연습문제.md 1번: 이 등록 시도를 try-catch로 처리하고 합계는 계속 출력.
        //expenses.add(new Expense(LocalDate.of(2026, 10, 2), "TRANSPORT", new BigDecimal("0"))); //추가 문제 관련.

        BigDecimal total = BigDecimal.ZERO;
        // LinkedHashMap: 카테고리가 처음 등장한 순서(FOOD → BOOK → TRANSPORT)를 유지합니다.
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
        BigDecimal total2 = BigDecimal.ZERO;
        // TreeMap: 입력 순서와 관계없이 날짜 키를 오름차순으로 정렬합니다.
        Map<LocalDate, BigDecimal> byCategory3 = new TreeMap<>();

        for (Expense expense : expenses) {

            total = total.add(expense.getAmount());

            String category = expense.getCategory();
            BigDecimal previous = byCategory.get(category);
            if (previous == null) {
                previous = BigDecimal.ZERO;
            }
            byCategory.put(category, previous.add(expense.getAmount()));

            // [3번 완료] 별도 BigDecimal total2에 조건에 맞는 금액만 누적해 중복 합산을 해결했습니다.
            // >= 0으로 바꿨으므로 정확히 10000원도 포함됩니다.
            if(expense.getAmount().compareTo(new BigDecimal("10000")) >= 0){

                total2 = total2.add(expense.getAmount());
            }

            LocalDate date = expense.getDate();
            // [4번] 날짜는 String 변환 없이 LocalDate를 Map 키로 사용할 수 있습니다.
            BigDecimal previous3 = byCategory3.get(date);
            // [4번 완료] 처음 보는 날짜만 0으로 초기화하고 put은 매번 실행해 누적합니다.
            // TreeMap이 날짜순을 보장하지만 합계를 자동 계산하지는 않습니다.
            if (previous3 == null) {
                previous3 = BigDecimal.ZERO;
            }
            byCategory3.put(date, previous3.add(expense.getAmount()));
        }

        System.out.println("전체 지출: " + total);
        System.out.println("카테고리별 지출: " + byCategory);
        System.out.println("FOOD 지출: " + byCategory.get("FOOD"));
        System.out.println("TRANSPORT 지출: " + byCategory.get("TRANSPORT"));
        System.out.println("1만원 이상 지출: " + total2); //3번 1만원 이상 지출 합계.
        System.out.println("날짜별 지출: " + byCategory3); //4번 날짜별 합계 출력.

    }

    /*
     * [3·4번 참고 풀이 — 위에서 구현한 코드와 비교]
     * 현재 total2는 조건합계, byCategory3는 날짜별 합계를 담습니다.
     *
     * // for문 전에: 숫자 하나와 날짜별 Map을 따로 준비
     * BigDecimal highValueTotal = BigDecimal.ZERO;
     * Map<LocalDate, BigDecimal> byDate = new TreeMap<>();
     *
     * // 기존 for문 안에: 카테고리 집계는 유지하고 이 계산을 추가
     * if (expense.getAmount().compareTo(new BigDecimal("10000")) >= 0) {
     *     highValueTotal = highValueTotal.add(expense.getAmount());
     * }
     * LocalDate date = expense.getDate();
     * BigDecimal previousByDate = byDate.get(date);
     * if (previousByDate == null) {
     *     previousByDate = BigDecimal.ZERO;
     * }
     * byDate.put(date, previousByDate.add(expense.getAmount())); // 날짜가 있어도 매번 누적
     *
     * // for문이 끝난 뒤: 기존 3·4번 출력 교체
     * System.out.println("1만원 이상 지출: " + highValueTotal);
     * System.out.println("날짜별 지출: " + byDate);
     */
}
