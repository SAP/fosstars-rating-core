#!/bin/bash

echo "The test is disabled since the tool may hang when an anonymous connection is used"
exit 70

JAVA="java"
if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
fi

JAR=${JAR:-"target/fosstars-github-rating-calc.jar"}

# check if the tool works with an anonymous connection
$JAVA -jar $JAR \
  --url https://github.com/apache/poi 2>&1 | tee tmp.log

grep "Rating" tmp.log > /dev/null 2>&1 || exit 1
grep "Confidence" tmp.log > /dev/null 2>&1 || exit 1
grep "Sub-score" tmp.log > /dev/null 2>&1 || exit 1
grep "Value" tmp.log > /dev/null 2>&1 || exit 1
grep "Importance" tmp.log > /dev/null 2>&1 || exit 1
grep "https://github.com/apache/poi" tmp.log > /dev/null 2>&1 || exit 1

rm tmp.log
