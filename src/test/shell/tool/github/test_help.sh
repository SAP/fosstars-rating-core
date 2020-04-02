#!/bin/bash

JAVA="java"
if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
fi

JAR=${JAR:-"target/fosstars-github-rating-calc.jar"}

# check if the usage message is printed out
$JAVA -jar $JAR -h | grep -i "usage" || exit 1
$JAVA -jar $JAR --help | grep -i "usage" || exit 1
