#!/bin/bash

REPORT_BRANCH=$1
TOKEN=$2

PROJECT_SCM_URL=$GITHUB_SERVER_URL/$GITHUB_REPOSITORY

# Switch to the branch where the report should be stored
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    echo "Could not switch to branch '$REPORT_BRANCH'"
    echo "Did you fortet to run 'actions/checkout' step in your workflow?"
    exit 1
fi

# Generate a report
report_file="fosstars_security_rating.md"
raw_rating_file="fosstars_security_rating.json"
java -jar /opt/stuff/fosstars-rating-core/target/fosstars-github-rating-calc.jar \
          --url $PROJECT_SCM_URL \
          --token $TOKEN \
          --verbose \
          --report-file $report_file \
          --report-type markdown \
          --raw-rating-file $raw_rating_file

git add $report_file $raw_rating_file
if git status | grep "nothing to commit" > /dev/null ; then
    echo "No updates found"
    exit 0
fi

# Update the current badge
label=$(cat $raw_rating_file | jq -r .label[1] | tr '[:upper:]' '[:lower:]' | sed 's/ //g')
case $label in
    good|moderate|bad|unclear)
      suffix=$label
      ;;
    *)
      suffix="unknown"
      ;;
esac
current_badge_file="fosstars-security-rating.svg"
wget -O $current_badge_file https://raw.githubusercontent.com/SAP/fosstars-rating-core/master/.github/actions/fosstars-create-single-report/images/security-fosstars-$suffix.svg
git add $current_badge_file

# Commit the report and the badge
set -e
git config --global user.name "Fosstars"
git config --global user.email "fosstars@users.noreply.github.com"
git commit -m "Update Fosstars security rating report" $report_file $current_badge_file $raw_rating_file
git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
git push origin $REPORT_BRANCH
