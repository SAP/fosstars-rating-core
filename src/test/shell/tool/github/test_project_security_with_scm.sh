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
  --url https://github.com/apache/poi --verbose \
  $TOKEN_OPTION > tmp.log 2>&1

if [ $? != 0 ]; then
  cat tmp.log
  echo "Unexpected exit code"
  exit 1
fi

cat tmp.log

declare -a expected_strings=(
  'Figuring out if the project belongs to the Eclipse Software Foundation'
  'Figuring out how the project uses CodeQL'
  'Figuring out how the project uses LGTM'
  'Figuring out if the project uses OWASP security libraries'
  'Checking how the project uses Dependabot'
  'Figuring out if the project uses GitHub for development'
  'Figuring out if the project uses sanitizers'
  'Figuring out if the project uses FindSecBugs'
  'Figuring out if the project signs jar files'
  'Looking for vulnerabilities in the project'
  'Figuring out if the project has any unpatched vulnerability'
  'Looking for vulnerabilities in NVD'
  'Counting how many stars the project has'
  'Counting how many people contributed to the project in the last three months'
  'Counting how many commits have been done in the last three months'
  'Figuring out if the project belongs to the Apache Software Foundation'
  'Figuring out if the project uses signed commits'
  'Figuring out if the project has a security team'
  'Figuring out if the project uses nohttp'
  'Figuring out if the project uses OWASP Dependency Check'
  'Figuring out if the project has a bug bounty program'
  'Counting how many watchers the project has'
  'Looking for programming languages that are used in the project'
  'Figuring out if the project has a security policy'
  'Figuring out if the project is fuzzed in OSS-Fuzz'
  'Looking for package managers'
  'Looking for programming languages that are used in the project'
  'Figuring out if the project is supported by a company'
  'https://github.com/apache/poi'
  'Here is how the rating was calculated'
  'Rating'
  'Confidence'
  'Sub-score:....Security testing'
  'Sub-score:....Static analysis'
  'Sub-score:....LGTM score'
  'Sub-score:....How a project uses CodeQL'
  'Sub-score:....FindSecBugs score'
  'Sub-score:....Dependency testing'
  'Sub-score:....Dependabot score'
  'Sub-score:....OWASP Dependency Check score'
  'Sub-score:....Fuzzing'
  'Sub-score:....Memory-safety testing'
  'Sub-score:....nohttp tool'
  'Sub-score:....Security awareness'
  'Sub-score:....Unpatched vulnerabilities'
  'Sub-score:....Vulnerability discovery and security testing'
  'Sub-score:....Security testing'
  'Sub-score:....Community commitment'
  'Sub-score:....Project activity'
  'Sub-score:....Project popularity'
  'Sub-score:....Security reviews'
)

check_expected_output "${expected_strings[@]}" | tee | grep Failed
if [ $? == 0 ]; then
  echo "check_expected_output() failed"
  exit 1
fi

if [ grep Exception tmp.log > /dev/null 2>&1 ]; then
  echo "Exceptions found"
  exit 1
fi
