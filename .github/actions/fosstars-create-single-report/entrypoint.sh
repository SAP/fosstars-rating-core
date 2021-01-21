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
report_file="fosstars_security_rating.txt"
java -jar /opt/stuff/fosstars-rating-core/target/fosstars-github-rating-calc.jar \
          --verbose --url $PROJECT_SCM_URL --token $TOKEN 2>&1 | tee $report_file

label=$(cat fosstars_security_rating.txt | grep "Rating: " | cut -d ">" -f 2 | tr '[:upper:]' '[:lower:]' | sed 's/ //g')
case $label in
good|moderate|bad|unclear)
  suffix=$label
  ;;
*)
  suffix="unknown"
  ;;
esac

current_badge_file="fosstars-security-rating.svg"
wget -O $current_badge_file https://raw.githubusercontent.com/artem-smotrakov/fosstars-rating-core/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-$suffix.svg
git add $current_badge_file

# Commit the report
set -e
git config --global user.name "Fosstars"
git config --global user.email "fosstars@users.noreply.github.com"
git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
git add $report_file
git commit -m "Update Fosstars security rating report" $report_file $current_badge_file
git push origin $REPORT_BRANCH
