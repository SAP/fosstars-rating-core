#!/bin/bash

CONFIG=$1
REPORT_BRANCH=$2
FOSSTARS_VERSION=$3
TOKEN=$4
CLEANUP=$5

if [ "$CONFIG" = "" ]; then
    echo "Oops! No config file provided!"
    exit 1
fi

if [ "$REPORT_BRANCH" = "" ]; then
    echo "Oops! No branch provided!"
    exit 1
fi

if [ "$FOSSTARS_VERSION" = "" ]; then
    echo "Oops! No Fosstars version provided!"
    exit 1
fi

if [ "$TOKEN" = "" ]; then
    echo "Oops! No token provided!"
    exit 1
fi

# Switch to the branch where the report should be stored
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    echo "Could not switch to branch '$REPORT_BRANCH'"
    echo "Did you fortet to run 'actions/checkout' step in your workflow?"
    exit 1
fi

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

# Build Fosstars
git clone https://github.com/SAP/fosstars-rating-core && \
    cd fosstars-rating-core && \
    git checkout $FOSSTARS_VERSION && \
    mvn package -DskipTests -Dcheckstyle.skip -Dmaven.javadoc.skip && \
    #mvn package -ntp -DskipTests -Dcheckstyle.skip -Dmaven.javadoc.skip && \
if [ $? -ne 0 ]; then
    echo "Oops! Could not build Fosstars!"
    exit 1
fi
cd ..

# Generate a report
echo "" > report.log
java -jar fosstars-rating-core/target/fosstars-github-rating-calc.jar \
          --config $CONFIG \
          --token $TOKEN \
          --verbose 2>&1 | tee report.log

if grep -i exception report.log > /dev/null 2>&1; then
  echo "Achtung! Looks like there were some errors, check out report.log"
fi

rm -rf .fosstars > /dev/null 2>&1
rm -rf report.log > /dev/null 2>&1
rm -rf fosstars-rating-core > /dev/null 2>&1

# Commit the report
git config --global user.name "Fosstars"
git config --global user.email "fosstars@users.noreply.github.com"

git add --all
git commit -m "Update Fosstars report" $REPORT_FILE $BADGE_FILE $RAW_RATING_FILE
if [ $? -ne 0 ]; then
    echo "Could not commit anything"
    exit 0
fi

git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
git push origin $REPORT_BRANCH
