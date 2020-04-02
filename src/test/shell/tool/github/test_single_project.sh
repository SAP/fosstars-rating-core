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

$JAVA -jar $JAR \
  --url https://github.com/apache/poi \
  --no-questions \
  $TOKEN_OPTION 2>&1 | tee tmp.log

grep "Rating" tmp.log || exit 1
grep "Confidence" tmp.log || exit 1
grep "Sub-score" tmp.log || exit 1
grep "Value" tmp.log || exit 1
grep "Importance" tmp.log || exit 1
grep "https://github.com/apache/poi" tmp.log || exit 1

rm tmp.log
