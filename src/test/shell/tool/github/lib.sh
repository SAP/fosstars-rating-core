#!/bin/bash

check_expected_output() {
  arr=("$@")
  for expected_string in "${arr[@]}"
  do
    echo "Check: $expected_string"
    grep "$expected_string" tmp.log > /dev/null 2>&1
    if [ $? != 0 ]; then
      echo "Failed: could not find in the output: $expected_string"
    fi
  done
}
