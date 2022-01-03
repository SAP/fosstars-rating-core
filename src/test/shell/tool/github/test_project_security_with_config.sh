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
  --config test_security_config.yml \
  $TOKEN_OPTION > tmp.log 2>&1

if [ $? -ne 0 ]; then
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
  'Figuring out how the project uses Bandit'
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
  'Found 2 projects'
  'https://github.com/netty/netty'
  'https://github.com/FasterXML/jackson-databind'
  'Starting calculating ratings'
  'Storing info about projects to'
  'Storing a report to'
  'Storing the cache to'
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

ls .fosstars/report/github_projects.json > /dev/null 2>&1 || exit 1
ls .fosstars/report/netty/netty.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/FasterXML/jackson-databind.md > /dev/null 2>&1 || exit 1
ls .fosstars/report/README.md > /dev/null 2>&1 || exit 1
