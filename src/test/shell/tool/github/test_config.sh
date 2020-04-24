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

rm -rf .fosstars/report > /dev/null 2>&1
mkdir -p .fosstars/report

# check if --config works (smoke test)
$JAVA -jar $JAR \
  --config test_config.yml \
  --no-questions \
  $TOKEN_OPTION 2>&1 | tee tmp.log

grep "Found 3 projects" tmp.log || exit 1
grep "https://github.com/netty/netty" tmp.log || exit 1
grep "https://github.com/netty/netty-tcnative" tmp.log || exit 1
grep "https://github.com/FasterXML/jackson-databind" tmp.log || exit 1
grep "Starting calculating ratings" tmp.log || exit 1
grep "Okay, we've done calculating the ratings" tmp.log || exit 1
grep "Storing info about projects to" tmp.log || exit 1

rm tmp.log
