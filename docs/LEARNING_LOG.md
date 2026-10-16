# 공부하면서 적어둘 것

## 2026-10-16 — 실습 폴더 정리와 문자열 금액 입력 문제 준비

- 사용자 요청: 현재 학습 내용과 다음 공부 준비를 함께 push·머지하고, Java·class 파일이 쌓이지 않도록 주제별 폴더에서 진행. 이번에는 머지까지 명시적으로 요청받음.
- 등록 실습 Java8/21 및 문제 문서를 lessons/01-registration에 모으고, 다음 문제·두 시작 파일은 lessons/02-amount-input에 준비. 기존 성적·지출·예외 답지는 보존. registration 실행 명령은 유지하고 amount-input 선택지를 추가. README·학습 안내·용어·AGENTS의 경로와 다음 순서 갱신.
- 다음 핵심: 문자열을 그대로 메서드에 전달하고 try 안에서 BigDecimal로 변환·지출 생성·추가·결과 반환. main에서 인자를 변환하다 실패하면 메서드 내부 catch가 처리하지 못하는 이유를 설명. 선택 2번은 NumberFormatException을 상위 IllegalArgumentException보다 먼저 catch해 사유 구분. 실제 문제 파일의 TODO는 미완성으로 유지.
- 검증: JDK21 --release8/21 runner에서 기존 registration은 각각 성공2/실패2/목록2/17000, 새 amount-input 시작 코드는 성공0/실패6/목록0/0. 임시 복사본의 1번 및 선택2번 참고 풀이를 두 버전에서 컴파일·실행해 성공2/실패4/목록2/17000, 숫자 변환 실패2회·금액 검증 실패2회·마지막5000원 등록 지속 확인. 사용자 새 문제 독립 풀이 완료와는 구분하며 실제JDK8 런타임 검증은 아님.
- bash -n, 상대 문서 링크44개, diff --check 통과. 소스 옆의 기존 ignored class4개는 로컬 임시 백업 폴더로 이동. runner는 종료 시 class 결과를 정리하므로 다시 쌓이지 않음. Git에서는 기존 *.class 제외 규칙 유지. 운영 사이트 편집·저장 없음. 실제 push·PR·머지 결과는 세션 메모리에 별도 기록.

## 2026-10-16 — Java 21 등록 결과 TODO 1·2 확인 완료

- 사용자가 Java21에도 등록 try/add/true, catch/사유/false 및 성공·실패 카운터를 작성. 두 버전 runner를 각각 실행해 실패 사유2회 및 성공2/실패2/목록2/합계17000 출력 일치 확인. JDK21에서 --release8/21 대상 컴파일·실행이며 실제JDK8 런타임 검증은 아님.
- 이번 21 비교 취지에 맞음: 지출 데이터는 record(생성자·접근자 자동 제공), 합계는 Stream의 amount 접근자·reduce 사용. 등록·예외처리·반환·건수는 같은 for/if/try-catch가 적합하고 21에서 새 문법으로 바꿀 필요 없음. Stream은 Java8부터, record는 Java16부터 정식 기능임을 코드 주석에 보완. 사용자 실행 로직 보존.
- 다음 선택 연습은 기존 문제의 1000원 입력 추가 후 성공3/실패2/목록3/합계18000을 예상·확인하는 것. 새 커밋·push·PR·관리자 편집 없음.

## 2026-10-16 — Java 8 등록 결과 TODO 2 확인 완료

- 사용자가 if (registered)의 true 분기에 successCount += 1, else 분기에 failureCount += 1을 직접 작성. 반환값에 따라 해당 카운터만 증가하므로 TODO2 구현이 맞음. 실행 로직은 수정하지 않고 += 1의 뜻과 분기 역할을 Java 주석에 보완.
- `bash run-sample.sh 8 registration`으로 JDK21의 --release8 컴파일·실행 통과. 실패 사유 2회, 성공2/실패2/등록목록2/합계17000으로 기대 결과 일치. 0원·-1000원은 제외되고 이후 5000원도 등록됨. 실제 JDK8 런타임 검증은 아님.
- Java21도 runner로 컴파일·실행 확인했으며 TODO1·2 미작성 시작 상태(네 출력0)를 유지. 다음은 Java21에 같은 등록·반환·카운터 흐름을 직접 적용. 새 커밋·push·PR·관리자 편집 없음.

## 2026-10-16 — Java 8 등록 결과 TODO 1 확인 완료

- 사용자가 `new registerExpense(...)`를 메서드 호출 `registerExpense(...)`로, `new expenses(...)`를 실제 클래스명 `new Expense(...)`로 수정. Java 8 방식 TODO1의 try/add 뒤 true 반환, 생성자 예외 catch·사유 출력·false 반환 흐름이 맞음.
- `bash run-sample.sh 8 registration` 실행 성공. 실패 사유 2회, 등록 건수 2, 합계 17000. 실패 입력 뒤 마지막 5000원도 등록되어 예외를 처리한 뒤 반복이 계속됨을 확인. 실행 때마다 JDK21로 `--release 8` 컴파일했으며 실제 JDK8 런타임 검증은 아님.
- TODO2는 아직 미작성이라 성공·실패 카운터가 각각 0으로 출력되는 것이 현재 단계에서는 정상. Java21 파일도 시작 상태로 컴파일·실행되며 네 출력이 모두0인 미완성 상태. 사용자의 다음 연습은 Java8 TODO2 건수 누적 또는 Java21 TODO1 적용.
- 검증을 위해 기존 학습 소스에서 잘못된 두 수정 안내 주석을 최신 상태로 바꾸고, 출력에 기대 형식의 콜론 뒤 공백을 추가함. 세션 메모리 갱신. 새 커밋/push/PR·관리자 편집 없음.

## 2026-10-16 — PR #4 정리와 등록 결과 반환 실습 준비

- 사용자가 PR #4 머지 완료 보고. fetch --prune로 origin/main=eaf9eeb 및 원격 feat/java-exception-study-notes 삭제 확인. a588708이 main에 포함됨을 merge-base로 확인하고 로컬 main fast-forward, 로컬 병합 브랜치 -d 삭제. 기존 작업 트리 깨끗한 상태에서 다음 실습 시작. 무관한 origin/docs/README-tone 브랜치는 보존.
- 이전 요청에 따라 새 ExpenseRegistrationJava8/21.java와 등록결과_연습문제.md 작성. void 등록 메서드에서 boolean 성공/실패 반환으로 발전시키고, main에서 반환값으로 건수를 세는 단계. 생성자 검증·호출·합계는 제공하고 TODO1 등록/반환 → TODO2 건수 → 선택 입력추가 순서. 기존 연습 파일과 답지 보존.
- Java마다 매개변수/인자/반환형/return/if(boolean) 이유를 주석, 공통 용어는 JAVA_TERMS에 설명. runner의 registration 선택지, README·두버전 학습안내 현재 단계 갱신.
- JDK21 --release8/21에서 시작 코드 각각 컴파일·실행 성공. 시작 상태는 미완성이라 네 출력 모두0. 임시 복사본의 에이전트 참고 풀이로 1번 실패사유2회/목록2/합계17000(건수0), 2번 성공2·실패2, 선택3번1000추가 시 성공3·실패2·합계18000 확인. 실제 사용자 파일에는 TODO를 유지했으며 사용자 풀이·이해 완료로 기록하지 않음. 실제Java8 런타임 검증 아님.
- 새 실습 변경은 main의 로컬 미커밋 상태로 제공. 새 커밋·push·PR·관리자 편집 없음. 다음은 사용자가 우선 Java8 TODO1을 작성하면 반환값·잘못된 입력 제외·실패 뒤 정상 등록을 확인할 것.

## 2026-10-16 — 예외처리 학습 내용 GitHub 정리

- 사용자 요청: 현재 이해한 범위까지 GitHub에 반영하고, 이후 브랜치 정리가 끝나면 새로운 Java 파일과 공부할 내용 안내. 기존 방식대로 에이전트는 커밋·push·PR 생성까지, 머지는 사용자가 진행. 다음 문제는 머지·브랜치 정리 후 별도 작업으로 남김.
- 포함 범위: 예외처리 문제, Java8/21 별도 답지·해설, 실행 선택지 exception-answer, Map 조회/순회 순서 설명, React 경험과 연결한 회고 초안·학습 기록.
- 업로드 검증에서 원본8의 빈 try/getMessage 인자 오류 확인. 사용자 시도를 같은 위치의 주석에 보존하고 기존 생성자 throw 검증 복구. main에 정답 try-catch를 덮어쓰지 않았으며 완성 풀이와 메서드 분리 형태는 answers에 유지.
- JDK21에서 grade·expense·exception-answer를 각각 --release8/21로 컴파일·실행해 여섯 실행 통과 및 버전별 출력 일치. 성적220, 지출54000, 답지 실패2회 후 정상집계. 원본8 생성자도 0/음수 거절·0.50 허용 확인. bash -n·diff --check 통과. 실제 Java8 런타임 검증은 아님.
- 사용자는 현재 설명 범위까지 알겠다고 보고함. 새 문제 독립 풀이·전체 Java 학습 완료와는 구분. 관리자 회고는 이전 요청대로 입력·미리보기까지만 진행했으며 이번 GitHub 작업에서 저장/공개하지 않음.

## 2026-10-16 — React 경험과 연결한 try-catch 회고 초안

- 사용자 요청: React에서 초기에 비동기/콜백 오류 처리를 다룬 뒤 React Query·Axios 사용으로 직접 try-catch를 적을 일이 적었던 경험과 Java 예제를 쉽게 연결해 관리자에 작성. 저장하지 않고 미리보기까지만 확인.
- Chrome의 기존 marry-mobile.site/admin 탭은 빈 새 글 화면이었음. 기존 공개 글 2개를 유지하고 제목 `React에서 익숙했던 오류 처리를 Java의 try-catch로 다시 이해하기`, slug `java-try-catch-from-react-experience`, 요약·본문6280자 입력. 공개하기 미선택, 저장하지 않은 변경 표시 확인. 저장/공개 버튼 미클릭.
- React Query의 queryFn 오류 전달, Axios 실패 전달과 UI 처리 구분, JS await/try-catch와 나중에 실행되는 콜백의 범위, Java 생성자 throw/호출부 catch/getMessage, void·별도 클래스 오해, 메서드 분리, Java8/21 비교를 주석 코드·표로 정리. TanStack/Axios/MDN 공식 문서 확인·본문 링크 첨부. 코드 실행 결과는 10월15일 답지 검증 사실이며 이번 사용자 독립 풀이 완료를 뜻하지 않음.
- 미리보기에서 본문·코드8개·표2개 렌더링 및 비교 표 화면 확인. 원문은 `docs/engineering-drafts/2026-10-16-try-catch-from-react.md`, 화면은 `/private/tmp/try-catch-admin-preview-2026-10-16.png`. 사용자 검토·저장 대기. 기존 Java 파일·기존 게시글·공개 상태는 변경하지 않음. 커밋/PR 없음.

## 2026-10-15 — 예외처리 별도 답지와 해설

- 사용자 요청: try-catch가 어려워 이번 문제의 별도 Java 답지와 작성 이유를 먼저 공부하고, 새 문제는 이후 요청하기로 함.
- 에이전트가 `java-basics/answers/ExpenseExceptionAnswerJava8.java`·`ExpenseExceptionAnswerJava21.java` 작성. 생성자 검증 → 호출부 try-catch → 집계로 이어지는 흐름을 주석으로 설명. 기본 main은 1·2번 직접 풀이, 파일 하단 registerExpense는 3번 메서드 분리 풀이이며 기본 main에서는 호출하지 않음.
- `answers/예외처리_답지_해설.md`에 역할·실행 순서·작성 위치의 이유·사용자 getMessage 인자 오류·메서드 분리 대체 호출·버전 비교 정리. JAVA_TERMS/README 연결, run-sample.sh에 exception-answer 선택 추가.
- 검증: JDK21에서 --release8/21로 각각 컴파일·실행. 두 버전 모두 실패 메시지2회 후 전체54000/조건합계37000/날짜37000·17000 출력 일치. 메서드 분리 형태도 임시 복사본에서 동일 출력, 0/음수 제외 후 정상 등록 계속 확인. 실제 JDK8 런타임 실행은 아님.
- 사용자 원본 ExpenseSummaryJava8/21 파일은 작업 전후 SHA-256 일치로 보존 확인. 현재 원본8의 빈 try/getMessage 인자 오류는 답지 해설에 설명하고 직접 수정하지 않음. 사용자 풀이 완료·이해 완료로 기록하지 않음. 새 문제·커밋·PR·사이트 편집은 이번 범위에 없음.

## 2026-10-14 — 지출 최종 구현·Map 비교와 회고 추가

- 사용자 요청: 문제 작업 커밋·PR까지만 생성, TreeMap과 ‘listmap’ 차이를 문제 하단 설명, 오늘 marry-mobile 관리자 회고에 추가. listmap은 현재 코드의 LinkedHashMap으로 해석.
- 사용자 8 날짜 누적/TreeMap, 21 조건합계/날짜집계/record 검증 적용 확인. PR 정리에서 누락된 TRANSPORT 출력 추가, 8의 활성 0원 입력은 주석 처리해 기본 정상 실행을 양쪽 비교 가능하게 함. 오래된 첨삭 주석 갱신.
- JDK21의 --release8/21 컴파일. 두 버전 각각 정상·10000·0.50·0·-1000·날짜순서역전 확인. 실제 run-sample.sh 정상 출력 일치. 기본 전체54000/1만원이상37000/날짜37000·17000.
- 지출_연습문제.md 하단에 LinkedHashMap 입력순서/TreeMap 키정렬, 같은 키 put은 값 교체, 누적은 별도 계산이라는 설명·표·예제 추가. JAVA_TERMS 링크 추가.
- docs/engineering-drafts/2026-10-14-expense-followup.md에 실수·첨삭·조건합계·Map·생성자검증 비교 회고 작성. 오늘 관리자 기존 본문을 유지하며 추가, 사이트 저장은 사용자가 진행하므로 에이전트는 누르지 않음.

## 2026-10-14 — Java 8 지출 두 번째 채점

- 사용자 수정: 3번 total2에 조건합계 누적, compareTo >=0, 4번 날짜 키 LocalDate, 0원 입력 주석 처리. 3번37000 및 정확히10000 포함 확인.
- 사용자 5번의 두 오류: if에 compareTo int 직접 전달, throw 뒤 println void. 컴파일 진단으로 확인.
- 첨삭 적용: 생성자에서 amount.compareTo(ZERO)<=0이면 new IllegalArgumentException을 throw. 이전 시도는 주석 보존. 정상·10000·0.50 실행, 0/-1000 생성 시 예외 확인.
- 4번은 첫 금액만 누적하는 조건 구조가 남아12000/9000 출력. 날짜별put을 조건 밖으로 옮기고 TreeMap 정렬이 남음. 2번 TRANSPORT 단독 출력도 미완료. 관련 주석을 최신 상태로 갱신. Java21 미변경, 커밋/PR 미진행.

## 2026-10-14 — 지출 첫 풀이 채점

- 사용자는 8 풀이를 작성하고 21은 어려워 채점·주석 첨삭 요청. 양쪽 FOOD5000/TRANSPORT3000/0원 입력을 확인.
- 실제 실행: 전체54000/카테고리26000·25000·3000 정상. 2번 TRANSPORT 단독 출력 누락. 8의 3번은 byCategory에서 현재 합계를 읽어 다시 더함·10000 제외, 4번은 날짜 첫 값만 기록. 5번 println+break로 정상 종료. 21의 3·4번은 FOOD합계26000을 출력하며 검증 없음.
- 사용자의 실행 로직은 보존하고 양쪽 오류 위치·미적용 수정안을 주석으로 추가. 지출_채점_2026-10-14.md와 JAVA_TERMS.md에 상세 설명.
- 현재8 임시 복사본에서 10000 경계값/0.50 검증으로 추가 오류 확인. 주석 수정안은 별도 임시 프로그램에서 8/21 각각 정상값·10000·0.50·0·-1000 확인 통과. 사용자 코드 수정 완료·이해 완료로 기록하지 않음.
- 다음은 0원 테스트를 잠시 제외하고 3번을 양쪽 수정 → 날짜별4번 → 생성자검증5번 순서. 커밋/PR 미진행.

## 2026-10-14 — 첫 성적 수정 확인과 설명 주석

- 사용자 직접 수정 확인: 두 GradeSummary의 학습자B 55→65, Java 8의 List.forEach와 Java 21의 Stream.forEach로 점수 출력 추가.
- 양쪽 실제 컴파일·실행 성공. 합계220/통과3/미통과0, 출력 문자열 일치. 점수 print에 줄바꿈이 없어 `80, 65, 75, 통과: 3`으로 같은 줄에 출력됨. 계산 오류 아님.
- 사용자가 각 Java에 주석과 별도 용어 파일 요청. 실행 코드·입력은 보존하고 두 GradeSummary에 설명 주석 추가, JAVA_TERMS.md 작성.
- Stream/람다/메서드 참조/forEach는 Java 8부터, record와 Stream.toList는 Java 16부터 정식 제공됨을 구분해 설명. 사용자는 당시 Java 8 업무에서 Stream을 접하지 않았다고 설명. 이해 완료로 단정하지 않음.
- 다음 연습 후보: print 뒤 줄바꿈 추가, 이후 통과 기준/경계값 변경. 사용자 요청 전에 실습 정답으로 덮어쓰지 않음.

## 2026-10-14 — Athena 작성 방식과 최신 방식 나란히 연습

- 사용자 방향: 아이맥스소프트 때 익숙한 Java 8과 Java 21 방식의 예제를 직접 수정하고, 에이전트가 양쪽 실행 결과와 문법 대응을 설명하는 방식으로 공부. 이력서 후속 작업은 기록만 유지.
- 로컬 `../athena` 폴더 확인. Sorting/Course/Lecture class major 52 확인. 일반 클래스·인덱스 for문·if·자체 DBQuery 패턴 참고. 원본 수정·회사 DB 접속·회사 코드 복사 없음.
- 성적 집계 `java-basics/java8/GradeSummary.java`, `java-basics/java21/GradeSummary.java` 추가. 기존 지출 Java 8 예제에 대응하는 ExpenseSummaryJava21 추가. 원래 ExpenseSummary 및 기존 사용자 변경 보존.
- JDK 21에서 각각 --release 8/21로 컴파일·실행 성공. 성적: 3명/합계210/통과2/미통과1. 지출: 전체46000/FOOD21000/BOOK25000. 두 버전 출력 문자열도 일치.
- run-sample.sh: 버전/예제 선택, 매번 임시 폴더에서 컴파일 후 실행. 두버전_학습안내.md와 AGENTS.md에 사용자 수정·에이전트 결과 확인 절차 기록.
- Java 8 런타임 실검증은 아님. Stream/람다/메서드 참조는 Java 8에도 있고 record/List.of/Stream.toList는 이후 도입된 기능임을 구분.
- 첫 연습 제안: 두 성적 파일의 학습자B 점수 55→65 변경 후 결과 확인 요청. 아직 사용자 수정·이해 여부는 확인하지 않았음.

## 2026-10-14 — 맥 환경 확인 및 Java 8 비교 예제 준비

- 에이전트 작업: 기존 예제를 보존하고 일반 클래스·ArrayList·반복문을 사용하는 `java-basics/ExpenseSummaryJava8.java`와 비교 설명 추가.
- 직접 확인: 현재 터미널에서 Java Runtime을 찾지 못했고 `javac`도 실행되지 않음. `mvn`, `node`, `npm`은 PATH에서 찾지 못함. Homebrew는 `/opt/homebrew/bin/brew`, CPU는 arm64.
- 검증 상태: JDK가 없어 Java 컴파일·실행은 미완료. 예상 합계는 전체 46000, FOOD 21000, BOOK 25000.
- 다음 작업: JDK 21과 Maven 실행 환경 준비 → `--release 8`로 새 예제 컴파일 → 기존 예제 실행 및 비교 → Spring API 실행.
- 사용자 학습 완료·이해 여부는 아직 확인하지 않음.

### 같은 날 후속 검증

- 사용자 JDK 설치 후 `/opt/homebrew/opt/openjdk@21/bin`의 Java 21.0.12.1로 두 예제 컴파일·실행 성공.
- Java 8 비교 예제는 `--release 8`로 컴파일. obsolete 대상 옵션 경고는 있지만 실행 성공.
- 두 예제 모두 전체 46000, FOOD 21000, BOOK 25000으로 일치. Map 구현 차이로 카테고리 출력 순서는 다름.
- Maven 3.10.0 확인. 기본 Java가 27이어서 Spring 실행 시 JAVA_HOME으로 21 지정 예정. Spring API는 아직 실행하지 않음.

### 개발 환경 준비 후 최종 검증

- Java 21로 Maven package 성공. 현재 저장소에 테스트 파일은 없어 Maven 결과는 빌드 검증임.
- 8080에서 Spring API 실행 후 임시 항목 등록·완료·삭제(204) 검증. 테스트 서버 종료.
- `~/.zshrc`에 Java 21 JAVA_HOME 설정 추가. 새 터미널에서 적용됨.

날짜별로 아래 내용을 복사해서 적으려고 합니다. 코드가 돌아갔는지와 제가 이해한 내용은 따로 남기겠습니다.

## YYYY-MM-DD — 공부한 주제

- 오늘 해본 것:
- 새로 알게 된 것:
- 막혔던 부분과 해결한 방법:
- AI 도움을 받았다면, 직접 확인한 부분:
- 다음에 해볼 것:
- 관련 커밋:

