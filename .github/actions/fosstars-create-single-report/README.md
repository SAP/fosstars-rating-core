# Fosstars rating for a project

This action calculates [a security rating for an open source project](https://sap.github.io/fosstars-rating-core/oss_security_rating.html).
The action creates a detailed report that explains how the rating was calculated.
The report contains recommendations for improving the security rating.

The action also generates a badge with security rating:

*  ![Good security rating](https://github.com/SAP/fosstars-rating-core/blob/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-good.svg)
*  ![Moderate security rating](https://github.com/SAP/fosstars-rating-core/blob/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-moderate.svg)
*  ![Bad security rating](https://github.com/SAP/fosstars-rating-core/blob/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-bad.svg)
*  ![Unclear security rating](https://github.com/SAP/fosstars-rating-core/blob/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-unclear.svg)
*  ![Unknown security rating](https://github.com/SAP/fosstars-rating-core/blob/fosstars-single-report-action/.github/actions/fosstars-create-single-report/images/security-fosstars-unknown.svg)

The action stores report and badge to a specified branch.

## Inputs

### `report-branch`

**Required** A branch where the report and badge should be stored. Default `fosstars`.

### `token`

**Required** A token for fetching data about the project via GitHub API,
and for committing the report and badge to the specified branch.

## How to use it

Here is an example workflow that updates the report every day, or when a commit is pushed.
The report is stored to the `fosstars-report` branch.

```
name: "Fosstars"
on:
  push:
    branches:
      - main
  schedule:
    - cron: "0 0 * * *"

jobs:
  create_fosstars_report:
    runs-on: ubuntu-latest
    name: "Security rating"
    steps:
      - uses: actions/checkout@v2.3.4
      - uses: ./.github/actions/fosstars-create-single-report
        with:
          report-branch: fosstars-report
          token: ${{ secrets.GITHUB_TOKEN }}
```

The badge will be stored to the same branch. It can then be used in README file:

```
[![Fosstars security rating](https://raw.githubusercontent.com/your-organisation/your-project/fosstars-report/fosstars-security-rating.svg)](https://github.com/your-organisation/your-project/blob/fosstars-report/fosstars_security_rating.md)
```

## Links

1.  [Fosstars home page](https://github.com/SAP/fosstars-rating-core)
1.  [Fosstars documentation](https://sap.github.io/fosstars-rating-core/)
1.  [Open source security rating](https://sap.github.io/fosstars-rating-core/oss_security_rating.html)
1.  [Security ratings for well-known open source projects](https://sap.github.io/fosstars-rating-core/oss/security/)
