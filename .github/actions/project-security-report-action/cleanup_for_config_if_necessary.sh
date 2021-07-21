#!/bin/bash

if [ "$CLEANUP" == "Yes" ]; then
  echo "Remove the old report and data"
  echo "Remove .fosstars"
  rm -rf .fosstars > /dev/null 2>&1

  echo "Remove Markdown files"
  for file in $(find . -name "*.md")
  do
    rm $file > /dev/null 2>&1
  done

  echo "Remove JSON files"
  for file in $(find . -name "*.json")
  do
    rm $file > /dev/null 2>&1
  done
fi