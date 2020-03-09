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

The tool can be run with a command like the following:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/apache/poi --no-questions
```

The `TOKEN` variable contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.

The output is going to look like the following:

```
[+] Project: https://github.com/apache/poi
[+] Let's get info about the project and calculate a security rating
[+] Counting how many commits have been done in the last three months ...
[+] Counting how many people contributed to the project in the last three months ...
[+] Counting how many stars the project has ...
[+] Counting how many watchers the project has ...
[+] Figuring out when the first commit was done ...
[+] Figuring out when the project started ...
[+] Figuring out if the project has a security team ...
[+] Figuring out if the project is supported by a company ...
[+] Figuring out if the project has a security policy ...
[+] Figuring out if any security review has been done for the project ...
[+] Figuring out if the project has any unpatched vulnerability ...
[+] Looking for vulnerabilities in NVD ...
[+] Figuring out if the project belongs to the Apache Software Foundation ...
[+] Figuring out if the project belongs to the Eclipse Software Foundation ...
[+] Figuring out if the project uses OWASP Dependency Check ...
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If an open-source project is regularly scanned for vulnerable dependencies: false
[+]    If an open-source project has a security team: true
[+]    Number of watchers for a GitHub repository: 78
[+]    Number of contributors in the last three months: 5
[+]    If an open-source project has a security policy: false
[+]    Info about vulnerabilities in open-source project: 8 vulnerabilities
[+]    Number of stars for a GitHub repository: 923
[+]    When first commit was done: Thu Jan 31 03:22:28 CET 2002
[+]    Number of commits in the last three months: 143
[+]    Security reviews for an open-source project: 0 security reviews
[+]    When a project started: Thu Jan 31 03:22:28 CET 2002
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If an open-source project is supported by a company: false
[+] Rating: 5.43 out of 10.00 -> OKAY
[+] Confidence: 10.00 out of 10.00
[+] Here is how the rating was calculated:
[+]   Score:........Security score for open-source projects
[+]   Value:........5.43  out of 10.00
[+]   Confidence:...10.00 out of 10.00
[+]   Based on:.....7 sub-scores:
[+]       Score:........How well open-source community commits to support an open-source project
[+]       Value:........7.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...3 features:
[+]           If an open-source project is supported by a company:.......false
[+]           If an open-source project belongs to Apache Foundation:....true
[+]           If an open-source project belongs to Eclipse Foundation:...false
[+]
[+]       Score:........Open-source project activity score
[+]       Value:........9.07  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...2 features:
[+]           Number of commits in the last three months:........143
[+]           Number of contributors in the last three months:...5
[+]
[+]       Score:........How well vulnerabilities are patched
[+]       Value:........10.00 out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...1 features:
[+]           Info about vulnerabilities in open-source project:...8 vulnerabilities
[+]
[+]       Score:........How well security testing is done for an open-source project
[+]       Value:........0.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...2 features:
[+]           Security reviews for an open-source project:..................................0 security reviews
[+]           If an open-source project is regularly scanned for vulnerable dependencies:...false
[+]
[+]       Score:........How fast vulnerabilities are patched
[+]       Value:........7.17  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...3 features:
[+]           Info about vulnerabilities in open-source project:...8 vulnerabilities
[+]           When a project started:..............................Thu Jan 31 03:22:28 CET 2002
[+]           When first commit was done:..........................Thu Jan 31 03:22:28 CET 2002
[+]
[+]       Score:........How well open-source community is aware about security
[+]       Value:........8.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...2 features:
[+]           If an open-source project has a security policy:...false
[+]           If an open-source project has a security team:.....true
[+]
[+]       Score:........Open-source project popularity score
[+]       Value:........1.18  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]         Based on:...2 features:
[+]           Number of stars for a GitHub repository:......923
[+]           Number of watchers for a GitHub repository:...78
[+]
[+] Bye!
```

If no `--no-questions` option is specified, the tool becomes a bit interactive,
and may ask the user a couple of questions.

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
