#!/bin/bash
set -euo pipefail

sample_version="${1:-}"
sample_name="${2:-grade}"
sample_root="$(cd "$(dirname "$0")" && pwd)"

case "$sample_version" in
  8|21) ;;
  *) echo '사용법: bash run-sample.sh <8|21> [grade|expense]' >&2; exit 2 ;;
esac

case "$sample_name" in
  grade)
    sample_source="$sample_root/java$sample_version/GradeSummary.java"
    sample_class=GradeSummary
    ;;
  expense)
    sample_source="$sample_root/ExpenseSummaryJava$sample_version.java"
    sample_class="ExpenseSummaryJava$sample_version"
    ;;
  *) echo '예제 이름은 grade 또는 expense입니다.' >&2; exit 2 ;;
esac

# 두 버전 모두 JDK 21에서 실행하되 컴파일 대상 문법/API를 구분합니다.
sample_java_home="${JAVA_HOME:-/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home}"
if [ ! -x "$sample_java_home/bin/javac" ] || [ ! -x "$sample_java_home/bin/java" ]; then
  echo 'JDK 21 경로를 JAVA_HOME에 설정해주세요.' >&2
  exit 1
fi

# 실행마다 새 디렉토리에 컴파일하여 이전 class 파일을 실행하지 않습니다.
sample_build="$(mktemp -d "${TMPDIR:-/tmp}/java-sample.XXXXXX")"
trap 'rm -rf "$sample_build"' EXIT
"$sample_java_home/bin/javac" -encoding UTF-8 --release "$sample_version" -d "$sample_build" "$sample_source"
"$sample_java_home/bin/java" -cp "$sample_build" "$sample_class"
