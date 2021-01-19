#!/bin/bash

REPORT_BRANCH=$1
TOKEN=$2

PROJECT_SCM_URL=$GITHUB_SERVER_URL/$GITHUB_REPOSITORY

# Switch to the Fosstars branch
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    echo "Could not switch to branch '$REPORT_BRANCH'"
    echo "Did you fortet to run 'actions/checkout' step in your workflow?"
    exit 1
fi

# Generate a report
java -jar /opt/stuff/fosstars-rating-core/target/fosstars-github-rating-calc.jar \
          --verbose --url $PROJECT_SCM_URL --token $TOKEN 2>&1 | tee fosstars_security_rating.txt

# Commit the report
set -e
git config --global user.name "Fosstars"
git config --global user.email "fosstars@users.noreply.github.com"
git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
git add fosstars_security_rating.txt
git commit -m "Update Fosstars report" fosstars_security_rating.txt
git push origin $REPORT_BRANCH
