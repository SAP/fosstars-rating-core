#!/bin/bash

TOKEN_OPTION=""
if [ "$TOKEN" != "" ]; then
  TOKEN_OPTION="--token $TOKEN"
fi

JAVA="java"
if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
fi

JAR=${JAR:-"target/fosstars-github-rating-calc.jar"}

source lib.sh

clean_cache

rm -rf .fosstars/report > /dev/null 2>&1
mkdir -p .fosstars/report

$JAVA -jar $JAR \
  --rating security \
  --pom test_project_security_with_pom.xml \
  --report-file .fosstars/report \
  $TOKEN_OPTION > tmp.log 2>&1

if [ $? -ne 0 ]; then
  cat tmp.log
  echo "Unexpected exit code"
  exit 1
fi

cat tmp.log

declare -a expected_strings=(
  'Looking for dependencies'
  'Found a GitHub project for com.fasterxml.jackson.core:jackson-databind: https://github.com/FasterXML/jackson-databind'
  'Found a GitHub project for org.kohsuke:github-api: https://github.com/hub4j/github-api'
  'Starting calculating ratings'
  'Storing a report to .fosstars'
)

check_expected_output "${expected_strings[@]}" | grep Failed
if [ $? -eq 0 ]; then
  echo "check_expected_output() failed"
  exit 1
fi

if grep Exception tmp.log > /dev/null 2>&1 ; then
  echo "Exceptions found"
  exit 1
fi

ls .fosstars/report/hub4j/github-api.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/FasterXML/jackson-databind.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/README.md > /dev/null 2>&1 || exit 1
