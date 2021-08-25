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
  --rating oss-rules-of-play \
  --config test_oss_rop_config.yml \
  --verbose \
  $TOKEN_OPTION > tmp.log 2>&1

if [ $? -ne 0 ]; then
  cat tmp.log
  echo "Unexpected exit code"
  exit 1
fi

cat tmp.log

declare -a expected_strings=(
  'Loading config from test_oss_rop_config.yml'
  'Found 2 projects'
  'https://github.com/SAP/fosstars-rating-core'
  'https://github.com/SAP/openui5'
  'Found config for LicenseInfo data provider: LicenseInfo.config.yml'
  'Found config for ContributingGuidelineInfo data provider: ContributingGuidelineInfo.config.yml'
  'Found config for CodeOfConductGuidelineInfo data provider: CodeOfConductGuidelineInfo.config.yml'
)

check_expected_output "${expected_strings[@]}" | tee | grep Failed
if [ $? -eq 0 ]; then
  echo "check_expected_output() failed"
  exit 1
fi

cat tmp.log | grep -v list-repository-teams | grep -v "Caused by" | grep -v getChainedException | grep Exception
if [ $? -eq 0 ]; then
  echo "Exceptions found"
  exit 1
fi

ls .fosstars/report/SAP/fosstars-rating-core.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/SAP/openui5.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/README.md > /dev/null 2>&1 || exit 1