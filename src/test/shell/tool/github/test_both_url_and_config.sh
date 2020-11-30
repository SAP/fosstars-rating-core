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

# check if --url and --config can't be used together
$JAVA -jar $JAR \
  --url https://github.com/apache/poi \
  --config test_config.yml \
  $TOKEN_OPTION

if [ $? -eq 0 ]; then
  exit 1
fi
