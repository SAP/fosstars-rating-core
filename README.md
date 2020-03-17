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
[+] Figuring out if the project uses any signed commits ...
[+] Figuring out if the project uses OWASP Dependency Check ...
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If a project uses verified signed commits: false
[+]    If an open-source project is regularly scanned for vulnerable dependencies: false
[+]    If an open-source project has a security team: true
[+]    Number of watchers for a GitHub repository: 77
[+]    If an open-source project has a security policy: false
[+]    Info about vulnerabilities in open-source project: 8 vulnerabilities
[+]    Number of stars for a GitHub repository: 926
[+]    When first commit was done: Thu Jan 31 03:22:28 CET 2002
[+]    Number of contributors in the last three months: 5
[+]    When a project started: Thu Jan 31 03:22:28 CET 2002
[+]    Security reviews for an open-source project: 0 security reviews
[+]    Number of commits in the last three months: 133
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If an open-source project is supported by a company: false
[+] Rating: 5.08 out of 10.00 -> MODERATE
[+] Confidence: 10.00 out of 10.00
[+]
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........5.08  out of 10.00
[+]   Confidence:...10.00 out of 10.00
[+]   Based on:.....7 sub-scores:
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 0.95 out of 1.00)
[+]       Value:........0.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...2 features:
[+]           Does it scan for vulnerable dependencies?....false
[+]           Security reviews:............................0 security reviews
[+]       Explanation:..If the project had security reviews, the score would be higher (+7.00)
[+]                     If the project scanned dependencies, the score would be higher (+3.00)
[+]
[+]       Sub-score:....Project activity
[+]       Description:..The score is based on number of commits and contributors.
[+]                     Here is how a number of commits contributes to the score:
[+]                     0 -> 0.85 (min), 35 -> 2.50, 72 -> 5.00, 500 -> 7.10 (max)
[+]                     Here is how a number of contributors contributes to the score:
[+]                     0 -> 1.40 (min), 1 -> 1.50, 8 -> 2.50, 500 -> 3.10 (max)
[+]       Importance:...High (weight 0.91 out of 1.00)
[+]       Value:........8.99  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...2 features:
[+]           Number of commits in the last three months:........133
[+]           Number of contributors in the last three months:...5
[+]
[+]       Sub-score:....Project popularity
[+]       Description:..The score is based on number of stars and watchers.
[+]                     Here is how a number of stars contributes to the score:
[+]                     0 -> 0.00 (min), 2500 -> 2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
[+]                     Here is how a number of watchers contributes to the score:
[+]                     0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50, 3000 -> 10.00 (max)
[+]       Importance:...High (weight 0.84 out of 1.00)
[+]       Value:........1.18  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...2 features:
[+]           Number of stars for a GitHub repository:......926
[+]           Number of watchers for a GitHub repository:...77
[+]
[+]       Sub-score:....Community commitment
[+]       Importance:...High (weight 0.73 out of 1.00)
[+]       Value:........7.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...3 features:
[+]           Does it belong to Apache?........true
[+]           Does it belong to Eclipse?.......false
[+]           Is it supported by a company?....false
[+]
[+]       Sub-score:....Vulnerability lifetime
[+]       Importance:...Medium (weight 0.59 out of 1.00)
[+]       Value:........7.17  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...3 features:
[+]           Info about vulnerabilities:...8 vulnerabilities
[+]           When a project started:.......Thu Jan 31 03:22:28 CET 2002
[+]           When first commit was done:...Thu Jan 31 03:22:28 CET 2002
[+]
[+]       Sub-score:....Security awareness
[+]       Description:..The score checks if a project has a security policy and a security team.
[+]                     If the project has a security policy, then the score adds 3.00.
[+]                     If the project has a security team, then the score adds 5.00.
[+]                     If the project uses verified signed commits, then the score adds 2.00.
[+]       Importance:...Medium (weight 0.54 out of 1.00)
[+]       Value:........5.00  out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...3 features:
[+]           Does it have a security policy?.........false
[+]           Does it have a security team?...........true
[+]           Does it use verified signed commits?....false
[+]
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...Medium (weight 0.40 out of 1.00)
[+]       Value:........10.00 out of 10.00
[+]       Confidence:...10.00 out of 10.00
[+]       Based on:...1 features:
[+]           Info about vulnerabilities:...8 vulnerabilities
[+]       Explanation:..No unpatched vulnerabilities found which is good
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
