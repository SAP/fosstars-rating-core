# Ratings for open-source projects

This is a framework for defining and calculating ratings for open-source projects.
See more details in [docs](docs).

## Security rating for open-source projects

Using open-source software helps a lot but it also may bring new security issues
and therefore increase security risks.
Is it safe to use a particular open-source component?
Sometimes answering this question is not easy.
The security rating for open-source projects helps answering this question.
First, it gathers various data about an open-source project.
Then, it calculates a security rating for it.
The rating helps to assess the security risk which comes with this open-source project.

Such a rating can be based on multiple factors such as:

1.  **Data which can be automatically collected**.
    For example, known vulnerabilities, statistics from code repositories,
    statistics from artifact repositories, data from mailing lists, etc.
1.  **Data which can be manually entered by users**.
    For example, results of security testing and review,
    companies which committed to support the project, etc.
1.  **User's and expert's opinion**.
    When developers and security experts evaluate security of open-source projects,
    they should be able to share their results and opinions,
    so that other users can benefit from this information.

## Requirements

*  Java 8+
*  Maven 3.6.0+
*  Python 3.6.8+
*  Jupyter Notebook 4.4.0+

## Download and installation

The project can be built and installed with the following command:

```
mvn clean install
```

## Open-source security rating calculator

There is a command line tool which takes a URL to a project on GitHub, fetches data about it,
and calculates a security rating.
The tool can interact with a user to get more data about the project.

The tool can be run with a command like the following:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/apache/poi
```

The `TOKEN` variable contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.

The tool is a bit interactive, and can ask several question. The dialog looks like the following:

```
[+] Project: https://github.com/apache/poi
[+] Let's get info about the project and calculate a rating
[?] Are you aware about any unpatched vulnerability in the project? (yes/no)
>>> no
Downloading files at Wed Oct 23 13:35:52 CEST 2019
Using local NVD cache as last update was within two hours
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If an open-source project is regularly scanned for vulnerable dependencies: false
[+]    If an open-source project has a security team: true
[+]    Number of watchers for a GitHub repository: 78
[+]    Number of contributors last three months: 4
[+]    If an open-source project has a security policy: false
[+]    Info about vulnerabilities in open-source project: 8 vulnerabilities
[+]    Security reviews for an OSS project: 0 security reviews
[+]    Number of stars for a GitHub repository: 861
[+]    When first commit was done: Thu Jan 31 03:22:28 CET 2002
[+]    Number of commits last three months: 91
[+]    When a project started: Thu Jan 31 03:22:28 CET 2002
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If an open-source project is supported by a company: false
[+] Rating: 5.92 out of 10.00, OKAY
[+] Confidence: 10.00 out of 10.00
[+] Bye!
```

A user can ask the tool to be silent by passing `--no-questions`.
Then, the tool is going to use only data which may be collected automatically.
Here is an example:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --no-questions \
        --url https://github.com/apache/poi
```

The output looks like the following:

```
[+] Project: https://github.com/apache/poi
[+] Let's get info about the project and calculate a rating
Downloading files at Wed Oct 23 13:41:21 CEST 2019
Using local NVD cache as last update was within two hours
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If an open-source project is regularly scanned for vulnerable dependencies: false
[+]    If an open-source project has a security team: true
[+]    Number of watchers for a GitHub repository: 78
[+]    Number of contributors last three months: 4
[+]    If an open-source project has a security policy: false
[+]    Info about vulnerabilities in open-source project: 8 vulnerabilities
[+]    Security reviews for an OSS project: 0 security reviews
[+]    Number of stars for a GitHub repository: 861
[+]    When first commit was done: Thu Jan 31 03:22:28 CET 2002
[+]    Number of commits last three months: 91
[+]    When a project started: Thu Jan 31 03:22:28 CET 2002
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If an open-source project is supported by a company: false
[+] Rating: 5.92 out of 10.00, OKAY
[+] Confidence: 10.00 out of 10.00
[+] Bye!
```

## Known issues

Please see [GitHub issues](https://github.com/SAP/fosstars-rating-core/issues).

## How to obtain support

Please create a new [GitHub issue](https://github.com/SAP/fosstars-rating-core/issues)
if you found a bug, or you'd like to propose an enhancement.
If you think you found a security issue, please follow [this guideline](SECURITY.md).

We currently don't have a support channel.
If you have a question, please also ask it via GitHub issues.

# Contributing to the project

We welcome ideas for improvements and pull requests.
Please follow [this guideline](CONTRIBUTING.md) if you'd like to contribute to the project.

## License

Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
This file is licensed under the Apache License Version 2.0
except as noted otherwise in [the LICENSE file](LICENSE).
