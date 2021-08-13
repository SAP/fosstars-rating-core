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

$JAVA -jar $JAR \
  --url https://github.com/apache/poi --verbose --rating security-risk-from-oss \
  $TOKEN_OPTION > tmp.log 2>&1

if [ $? -ne 0 ]; then
  cat tmp.log
  echo "Unexpected exit code"
  exit 1
fi

cat tmp.log

check_expected_output "${project_security_default_expected_strings[@]}" | tee | grep Failed
if [ $? -eq 0 ]; then
  echo "check_expected_output() failed"
  exit 1
fi

declare -a expected_strings=(
  'https://github.com/apache/poi'
  'How many components use it?'
  'What kind of functionality does it provide?'
  'How likely does it handle untrusted data?'
  'Is it adopted by any team?'
  'What kind of data does it process?'
  'What is potential confidentiality impact in case of a security problem?'
  'What is potential integrity impact in case of a security problem?'
  'What is potential availability impact in case of a security problem?'
  'Likelihood score for security risk of open source project'
  'Likelihood coefficient for security risk of open source project'
  'Security of project'
  'Aggregated likelihood factors for security risk of open source project'
  'Aggregated impact factors for security risk of open source project'
)

check_expected_output "${expected_strings[@]}" | tee | grep Failed
if [ $? -eq 0 ]; then
  echo "check_expected_output() failed"
  exit 1
fi

if grep Exception tmp.log > /dev/null 2>&1 ; then
  echo "Exceptions found"
  exit 1
fi
