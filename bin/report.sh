#!/bin/bash

if [ "${TOKEN}" = "" ]; then
  echo "Achtung! No GitHub token! Set TOKEN environment variable!"
  exit 1
fi

set -e

if [ "${BUILD}" = "yes" ]; then
  mvn clean package -DskipTests
fi

if [ "${CLEAN}" = "yes" ]; then
  rm -rf .fosstars/github_project_value_cache.json
  rm -rf .fosstars/project_rating_cache.json
  echo "[]" > docs/oss/security/github_projects.json

  for file in $(find docs/oss/security -name "*.md" | grep -v README | grep -v improvements)
  do
    rm $file
  done
fi

configs=$(ls docs/oss/security/*.yml)

echo "" > report.log
for config in ${configs}
do
  java -jar target/fosstars-github-rating-calc.jar \
      --verbose --token ${TOKEN} --config ${config} 2>&1 | tee report.log
done

if grep -i exception report.log > /dev/null 2>&1; then
  echo "Achtung! Looks like there were some errors, check out report.log"
fi

