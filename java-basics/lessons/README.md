# 주제별 Java 실습

한 주제마다 폴더 하나에 문제와 Java 8·21 파일을 함께 둡니다. 다음 문제도 이 아래에 번호를 붙여 추가합니다.

| 순서 | 주제 | 상태 |
| --- | --- | --- |
| [01-registration](01-registration/README.md) | boolean으로 등록 결과 반환, 성공·실패 건수 | 두 버전 TODO 1·2 사용자 작성·실행 확인 |
| [02-amount-input](02-amount-input/README.md) | 문자열을 금액으로 바꿀 때 예외처리 범위 | 다음 실습, Java 8 TODO 1부터 |

폴더 안의 Java 파일을 직접 수정하고, 실행은 `java-basics`에서 합니다.

```sh
cd ~/Desktop/project/backend-restart-lab/java-basics
bash run-sample.sh 8 registration
bash run-sample.sh 21 registration
bash run-sample.sh 8 amount-input
bash run-sample.sh 21 amount-input
```

`run-sample.sh`는 매번 임시 디렉토리에서 컴파일하고 종료할 때 결과를 지웁니다. 소스 폴더에 `.class` 파일을 만들지 않습니다. Git에서는 `*.class`를 이미 제외합니다. 기존 성적·지출·답지의 위치와 실행 명령은 유지합니다.
