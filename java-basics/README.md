# Java 다시 보기: 지출 합계

`ExpenseSummary.java`는 지출 목록의 전체 금액과 카테고리별 금액을 계산하는 작은 예제입니다. Java를 다시 보려고 `record`, `List`, `Stream`, `BigDecimal`을 사용했습니다.

```powershell
javac ExpenseSummary.java
java ExpenseSummary
```

실행한 다음에는 아래 내용을 직접 바꿔 보려고 합니다.

1. `FOOD` 지출만 골라 합계 내기
2. 날짜별로 묶고 날짜순으로 출력하기
3. 금액이 0 이하일 때 어떻게 처리할지 정하기
4. Stream을 쓰지 않고 반복문으로도 만들어 보기

같은 결과를 내더라도 코드가 어떻게 다른지 학습 기록에 적어두면 나중에 다시 보기 좋을 것 같습니다.

