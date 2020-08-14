![Java CI](https://github.com/SAP/fosstars-rating-core/workflows/Java%20CI/badge.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/SAP/fosstars-rating-core.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/SAP/fosstars-rating-core/context:java)
[![REUSE status](https://api.reuse.software/badge/github.com/SAP/fosstars-rating-core)](https://api.reuse.software/info/github.com/SAP/fosstars-rating-core)

# Ratings for open-source projects

This is a framework for defining and calculating ratings for open-source projects.
See [docs](https://sap.github.io/fosstars-rating-core/) for more details.

## Security rating for open-source projects

Using open-source software helps a lot but it also may bring new security issues
and therefore increase security risks.
Is it safe to use a particular open-source component?
Sometimes answering this question is not easy.
The security rating for open-source projects helps answering this question.
First, it gathers various data about an open-source project.
Then, it calculates a security rating for it.
The rating helps to assess the security risk that comes with this open-source project.

More details about the security rating
can be found in the [docs](https://sap.github.io/fosstars-rating-core/oss_security_rating.html).

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

The project offers a command line tool that takes a URL to a project on GitHub,
gathers data about it, and calculates a security rating.

The tool can be run with a command like the following:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/apache/beam --no-questions
```

The `TOKEN` variable contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.

The output is going to look like the following:

```
[+] Okay, we have a GitHub token, let's try to use it
[+] Project: https://github.com/apache/beam
[+] Let's gather info and calculate a security rating for:
[+]   https://github.com/apache/beam
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If a project uses LGTM checks for commits: false
[+]    If a project uses OWASP Java Encoder: false
[+]    If a project uses Dependabot: false
[+]    If a project uses GitHub as the main development platform: true
[+]    If an open-source project uses AddressSanitizer: false
[+]    If an open-source project uses FindSecBugs: false
[+]    If a project signs artifacts: false
[+]    Info about vulnerabilities in open-source project: 1 vulnerability
[+]    Number of stars for a GitHub repository: 4172
[+]    Number of contributors in the last three months: 91
[+]    Number of commits in the last three months: 1390
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If a project uses OWASP Enterprise Security API (ESAPI): false
[+]    If a project uses signed commits: false
[+]    If an open-source project is regularly scanned for vulnerable dependencies: false
[+]    If an open-source project has a security team: true
[+]    If an open-source project uses MemorySanitizer: false
[+]    If a project uses nohttp tool: false
[+]    If a project has a bug bounty program: false
[+]    Number of watchers for a GitHub repository: 257
[+]    A set of programming languages: C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]    If an open-source project has a security policy: false
[+]    If an open-source project is included to OSS-Fuzz project: false
[+]    If a project uses OWASP Java HTML Sanitizer: false
[+]    If an open-source project uses UndefinedBehaviorSanitizer: false
[+]    The worst LGTM grade of a project: B
[+]    A set of package managers: GRADLE
[+]    If an open-source project is supported by a company: false
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........4.71 out of 10.0
[+]   Confidence:...High (10.0 out of 10.0)
[+]   Based on:.....7 sub-scores:
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........0.89 out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:.....5 sub-scores:
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........4.0  out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores:
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........4.0  out of 10.0
[+]               Confidence:...High (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   Does it use LGTM checks?...............No
[+]                   The worst LGTM grade of the project:...B
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...High (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   A set of programming languages:...C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]                   Does it use FindSecBugs?..........No
[+] 
[+] 
[+]           Sub-score:....Fuzzing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+]           Based on:...2 features:
[+]               A set of programming languages:...C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]               Is it included to OSS-Fuzz?.......No
[+] 
[+]           Sub-score:....Dependency testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+]           Based on:...5 features:
[+]               A set of package managers:..............................GRADLE
[+]               A set of programming languages:.........................C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]               Does it scan for vulnerable dependencies?...............No
[+]               Does it use Dependabot?.................................No
[+]               Does it use GitHub as the main development platform?....Yes
[+] 
[+]           Sub-score:....Memory-safety testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+]           Based on:...4 features:
[+]               A set of programming languages:............C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]               Does it use AddressSanitizer?..............No
[+]               Does it use MemorySanitizer?...............No
[+]               Does it use UndefinedBehaviorSanitizer?....No
[+] 
[+]           Sub-score:....nohttp tool
[+]           Importance:...Medium (weight 0.5  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+]           Based on:...2 features:
[+]               A set of package managers:...GRADLE
[+]               Does it use nohttp?..........No
[+] 
[+] 
[+]       Sub-score:....Security awareness
[+]       Description:..The score shows how a project is aware of
[+]                     security. If the project has a security policy,
[+]                     then the score adds 2.00. If the project has a
[+]                     security team, then the score adds 3.00. If the
[+]                     project uses verified signed commits, then the
[+]                     score adds 0.50. If the project has a bug bounty
[+]                     program, then the score adds 4.00. If the project
[+]                     signs its artifacts, then the score adds 0.50. If
[+]                     the project uses a security tool or library, then
[+]                     the score adds 1.00.
[+]       Importance:...High (weight 0.9  out of  1.0)
[+]       Value:........3.0  out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:...16 features:
[+]           Does it have a bug bounty program?.........No
[+]           Does it have a security policy?............No
[+]           Does it have a security team?..............Yes
[+]           Does it sign artifacts?....................No
[+]           Does it use AddressSanitizer?..............No
[+]           Does it use Dependabot?....................No
[+]           Does it use FindSecBugs?...................No
[+]           Does it use LGTM checks?...................No
[+]           Does it use MemorySanitizer?...............No
[+]           Does it use OWASP ESAPI?...................No
[+]           Does it use OWASP Java Encoder?............No
[+]           Does it use OWASP Java HTML Sanitizer?.....No
[+]           Does it use UndefinedBehaviorSanitizer?....No
[+]           Does it use nohttp?........................No
[+]           Does it use verified signed commits?.......No
[+]           Is it included to OSS-Fuzz?................No
[+] 
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...High (weight 0.8  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:...1 features:
[+]           Info about vulnerabilities:...1 vulnerability
[+]       Explanation:..No unpatched vulnerabilities found which is good
[+] 
[+]       Sub-score:....Vulnerability discovery and security testing
[+]       Description:..The scores checks how security testing is done and
[+]                     how many vulnerabilities were recently discovered.
[+]                     If testing is good, and there are no recent
[+]                     vulnerabilities, then the score value is max. If
[+]                     there are vulnerabilities, then the score value is
[+]                     high. If testing is bad, and there are no recent
[+]                     vulnerabilities, then the score value is low. If
[+]                     there are vulnerabilities, then the score is min.
[+]       Importance:...Medium (weight 0.6  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:.....1 sub-scores:
[+]           Sub-score:....Security testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.89 out of 10.0
[+]           Confidence:...High (10.0 out of 10.0)
[+] 
[+]       Based on:...1 features:
[+]           Info about vulnerabilities:...1 vulnerability
[+] 
[+]       Sub-score:....Project activity
[+]       Description:..The score is based on number of commits and
[+]                     contributors.
[+]                     Here is how the number of commits
[+]                     contributes to the score (up to 5.10):
[+]                     0 -> 0.10,
[+]                     200 -> 2.55, 310 -> 4.59
[+]                     Here is how the number of
[+]                     contributors contributes to the score (up to
[+]                     5.10):
[+]                     0 -> 0.10, 5 -> 2.55, 10 -> 4.59
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:...2 features:
[+]           Number of commits in the last three months:........1390
[+]           Number of contributors in the last three months:...91
[+] 
[+]       Sub-score:....Community commitment
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........7.0  out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:...3 features:
[+]           Does it belong to Apache?........Yes
[+]           Does it belong to Eclipse?.......No
[+]           Is it supported by a company?....No
[+] 
[+]       Sub-score:....Project popularity
[+]       Description:..The score is based on number of stars and
[+]                     watchers.
[+]                     Here is how a number of stars
[+]                     contributes to the score:
[+]                     0 -> 0.00 (min), 2500 ->
[+]                     2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
[+]                     Here is
[+]                     how a number of watchers contributes to the
[+]                     score:
[+]                     0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50,
[+]                     3000 -> 10.00 (max)
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........5.03 out of 10.0
[+]       Confidence:...High (10.0 out of 10.0)
[+]       Based on:...2 features:
[+]           Number of stars for a GitHub repository:......4172
[+]           Number of watchers for a GitHub repository:...257
[+] 
[+] Rating:     4.71 out of 10.0 -> GOOD
[+] Confidence: High (10.0 out of 10.0)
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

