#!/bin/bash

export INPUT_FILE=$1
export REPORT_BRANCH=$2
export FOSSTARS_VERSION=$3

if [ "$INPUT_FILE" = "" ]; then
    cowsay "Oops! No input file provided!"
    exit 1
fi

if [ "$REPORT_BRANCH" = "" ]; then
    cowsay "Oops! No branch provided!"
    exit 1
fi

if [ "$FOSSTARS_VERSION" = "" ]; then
    cowsay "Oops! No Fosstars version provided!"
    exit 1
fi

# Switch to the branch where the output should be stored
git config --global --add safe.directory /github/workspace
OLD_BRANCH=$(git rev-parse --abbrev-ref HEAD)
git fetch origin $REPORT_BRANCH || git branch $REPORT_BRANCH
git checkout $REPORT_BRANCH
if [ $? -ne 0 ]; then
    cowsay "Could not switch to branch '$REPORT_BRANCH'. Did you forget to run 'actions/checkout' step in your workflow?"
    exit 1
fi

ROOT_DIR=$(pwd)

status=0
while true
do
  export INPUT_FILE=$ROOT_DIR/$INPUT_FILE
  if [ ! -f $INPUT_FILE ]; then
    status=1
    cowsay "$INPUT_FILE does not exist!"
    break
  fi

  git clone https://github.com/SAP/fosstars-rating-core && \
      cd fosstars-rating-core && \
      git checkout $FOSSTARS_VERSION

  if [ $? -ne 0 ]; then
    status=1
    cowsay "Oops! Could not build Fosstars!"
    break
  fi

  cd src/main/jupyter/oss/security
  export OUTPUT_FILE=$ROOT_DIR/OssSecurityRatingThresholds.json
  jupyter nbconvert --to notebook --execute SecurityRatingAnalysis.ipynb

  if [ $? -ne 0 ]; then
    status=1
    cowsay "Oops! Jupyter notebook failed!"
    break
  fi

  mv SecurityRatingAnalysis.nbconvert.ipynb $ROOT_DIR/SecurityRatingAnalysis.ipynb

  # Commit the report
  cd $ROOT_DIR
  git add SecurityRatingAnalysis.ipynb OssSecurityRatingThresholds.json
  git config --global user.name "Fosstars"
  git config --global user.email "fosstars@users.noreply.github.com"
  git commit -m "Update label thresholds" SecurityRatingAnalysis.ipynb OssSecurityRatingThresholds.json
  if [ $? -ne 0 ]; then
    cowsay "Could not commit anything"
  else
    git remote set-url origin https://x-access-token:$TOKEN@github.com/$GITHUB_REPOSITORY
    git push origin $REPORT_BRANCH
  fi

	break
done

# Restore the original branch
git checkout $OLD_BRANCH

exit $status
