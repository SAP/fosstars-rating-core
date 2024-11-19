#!/bin/bash

# debug output
#set -x

ME=$(basename "$0")
TEST_DIRECTORY=$(dirname $0)
echo "Test directory: $TEST_DIRECTORY"

JAR=${JAR:-"$(pwd)/target/fosstars-github-rating-calc.jar"}
export JAR

TEST_REPORT="test_report"

OLD_DIRECTORY=$(pwd)
cd $TEST_DIRECTORY
rm -rf $TEST_REPORT > /dev/null 2>&1
touch $TEST_REPORT

n=$(ls -la test_*.sh | wc -l)
echo "Found $n tests"
selected_test=$1
if [ "x$selected_test" != "x" ]; then
  echo "Run only $selected_test"
fi
for test in test_*.sh; do
  if [ "x$selected_test" != "x" -a "$selected_test" != "$test" ]; then
    continue
  fi
  log="${test}.log"
  echo "Running $test ... "
  bash $test > $log 2>&1
  code=$?
  cat $log
  if [ $code -eq 0 ]; then
    status="passed"
  elif [ $code -eq 70 ]; then
    status="ignore"
  else
    status="failed"
  fi
  echo $status
  echo "$status $test" >> $TEST_REPORT
  rm tmp.log > /dev/null 2>&1
done

echo "Here are the results:"
cat $TEST_REPORT

code=0
if grep failed $TEST_REPORT > /dev/null 2>&1 ; then
  echo "Some test failed"
  code=1
fi

rm $TEST_REPORT
cd $OLD_DIRECTORY

exit $code
