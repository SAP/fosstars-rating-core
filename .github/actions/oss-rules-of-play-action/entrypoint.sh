#!/bin/bash

REPORT_BRANCH=$1
REPORT_FILE=$2
FOSSTARS_VERSION=$3
TOKEN=$4

if [ "$REPORT_BRANCH" = "" ]; then
    echo "Oops! No branch provided!"
    exit 1
fi

if [ "$FOSSTARS_VERSION" = "" ]; then
    echo "Oops! No Fosstars version provided!"
    exit 1
fi

if [ "$REPORT_FILE" = "" ]; then
    REPORT_FILE="README.md"
fi

PROJECT_SCM_URL=$GITHUB_SERVER_URL/$GITHUB_REPOSITORY

# Switch to the branch where the report should be stored
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    echo "Could not switch to branch '$REPORT_BRANCH'"
    echo "Did you forget to run 'actions/checkout' step in your workflow?"
    exit 1
fi

# Build Fosstars
git clone https://github.com/SAP/fosstars-rating-core && \
    cd fosstars-rating-core && \
    git checkout $FOSSTARS_VERSION && \
    mvn package -DskipTests && \
if [ $? -ne 0 ]; then
    echo "Oops! Could not build Fosstars!"
    exit 1
fi
cd ..
echo fosstars-rating-core >> .gitignore

# Generate a report
java -jar fosstars-rating-core/target/fosstars-github-rating-calc.jar \
          --token $TOKEN \
          --verbose \
          --config config.yml \
          --rating oss-rules-of-play
if [ $? -ne 0 ]; then
    echo "Oops! Could not run Fosstars!"
    exit 1
fi

# Commit the report
git add --all
git config --global user.name "Fosstars"
git config --global user.email "fosstars@users.noreply.github.com"

git commit -am "Update Fosstars report"
if [ $? -ne 0 ]; then
    echo "Could not commit anything"
    exit 0
fi

git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
git push origin $REPORT_BRANCH
