# Configuring the OSS security rating procedure

This section describes how to tune the open source security procedure.

## Tuning thresholds for labels

The label thresholds in the open source security rating procedure depend on scores
of a set of well-known open source projects.
YAML Config files in [`docs/oss/securty`](oss/security) define the set of projects.

First, build Fosstars Core:

```
mvn clean install
```

Next, generate a report for the projects. Make sure you provide a token for accessing GitHub API.
A personal token can be created in the [Developer settings](https://github.com/settings/tokens).

```
export TOKEN=xyz
CLEAN=yes bash bin/report.sh
```

This can take quite long. The report will be stored in [`docs/oss/security/README.md`](oss/security/README.md).

Then, make sure that the report doesn't have many unknown and unclear ratings.
Ideally, the report should not contain such ratings.
Those ratings may be caused by bugs in data providers,
or some issues with the environment (networking, rate limits for GitHub API, etc).
You can look for errors in `report.log`:

```
grep Exception report.log
```

Triage errors and [open issues](https://github.com/SAP/fosstars-rating-core/issues/new).
If the report contains many unknown and unclear ratings due to bugs in the data providers,
then the issues need to be fixed, and the report needs to be re-created.

Besides the Markdown report, `report.sh` stores info about the projects and their ratings
to `docs/oss/security/github_projects.json`. This data is used for calculating the thresholds.

To calculate the thresholds,
run [SecurityRatingAnalysis Jupyter notebook](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/jupyter/oss/security/SecurityRatingAnalysis.ipynb).

```
cd src/main/jupyter/oss/security
jupyter notebook
```

Select `SecurityRatingAnalysis.ipynb` and re-run the whole notebook.
It loads `github_projects.json`, calculate stats and draw diagrams that may be help to find issues
in data providers and scoring functions.
Make sure that all cells ran without errors. All problems have to be fixed.
In the end, the notebook calculates the thresholds, and stores them to 
[`OssSecurityRatingThresholds.json`](../src/main/resources/com/sap/oss/phosphor/fosstars/model/rating/oss/OssSecurityRatingThresholds.json).

Since the thresholds have been updated, some test vectors may start failing. They need to be updated.

Finally, commit updates in `docs/oss/securty` and `OssSecurityRatingThresholds.json`.
