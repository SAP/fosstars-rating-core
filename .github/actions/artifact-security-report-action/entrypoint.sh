#!/bin/bash

export CONFIG=$1
export REPORT_BRANCH=$2
export FOSSTARS_VERSION=$3
export TOKEN=$4
export CLEANUP=$5

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
OLD_BRANCH=$(git rev-parse --abbrev-ref HEAD)
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    echo "Could not switch to branch '$REPORT_BRANCH'"
    echo "Did you fortet to run 'actions/checkout' step in your workflow?"
    exit 1
fi

status=0
while true
do
	bash /opt/cleanup_for_config_if_necessary.sh

  bash /opt/build_fosstars.sh
  if [ $? -ne 0 ]; then
    status=1
    echo "Oops! Could not build Fosstars!"
    break
  fi

  # Generate a report
  echo "" > report.log
  java -jar -Xms2048M -Xmx2048M \
            fosstars-rating-core/target/fosstars-github-rating-calc.jar \
            --config $CONFIG \
            --token $TOKEN \
            --rating oss-artifact-security \
            --cleanup \
            --verbose 2>&1 | tee report.log

  if [ ${PIPESTATUS[0]} -ne 0 ]; then
    status=1
    echo "Oops! Fosstars failed!"
    break
  fi

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
  git commit -am "Update Fosstars security report for $CONFIG"
  if [ $? -ne 0 ]; then
    echo "Could not commit anything"
  else
    git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
    git push origin $REPORT_BRANCH
  fi

	break
done

# Restore the original branch
git checkout $OLD_BRANCH

exit $status
