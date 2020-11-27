![Java CI](https://github.com/SAP/fosstars-rating-core/workflows/Java%20CI/badge.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/SAP/fosstars-rating-core.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/SAP/fosstars-rating-core/context:java)
[![REUSE status](https://api.reuse.software/badge/github.com/SAP/fosstars-rating-core)](https://api.reuse.software/info/github.com/SAP/fosstars-rating-core)

# Ratings for open-source projects

This is a framework for defining and calculating ratings for open-source projects.
See [docs](https://sap.github.io/fosstars-rating-core/) for more details.

## Security rating for open-source projects

Open-source software helps a lot, but it also may bring new security issues
and therefore increase security risks.
Is it safe to use a particular open-source component?
Sometimes answering this question is not easy.
The security rating for open-source projects helps to answer this question.
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

The [jars](https://mvnrepository.com/artifact/com.sap.oss.phosphor/fosstars-rating-core) are available on the Maven Central repository:

```
<dependency>
    <groupId>com.sap.oss.phosphor</groupId>
    <artifactId>fosstars-rating-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or, the project can be built and installed with the following command:

```
mvn clean install
```

## Command line tool for calculating security ratings

There is a command line tool that takes a URL to a project on GitHub,
gathers data about it, and calculates a security rating.

The tool can be run with commands like the following:

```
git clone git@github.com:SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
TOKEN=xyz # use your personal token, see below
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/apache/beam
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
[+] Counting how many commits have been done in the last three months ...
[+] Pulling updates from https://github.com/apache/beam ...
[+] Counting how many people contributed to the project in the last three months ...
[+] Counting how many stars the project has ...
[+] Counting how many watchers the project has ...
[+] Figuring out if the project has a security team ...
[+] Figuring out if the project is supported by a company ...
[+] Figuring out if the project has a security policy ...
[+] Figuring out if the project has a bug bounty program ...
[+] Looking for vulnerabilities in the project ...
[+] Figuring out if the project has any unpatched vulnerability ...
[+] Looking for vulnerabilities in NVD ...
[+] Figuring out if the project belongs to the Apache Software Foundation ...
[+] Figuring out if the project belongs to the Eclipse Software Foundation ...
[+] Figuring out how the project uses CodeQL ...
[+] Figuring out how the project uses LGTM ...
[+] Figuring out if the project uses signed commits ...
[+] Checking if the project uses Dependabot ...
[+] Looking for programming languages that are used in the project...
[+] Looking for package managers ...
[+] Looking for programming languages that are used in the project...
[+] Figuring out if the project uses nohttp ...
[+] Figuring out if the project uses GitHub for development ...
[+] Figuring out if the project uses OWASP Dependency Check ...
[+] Figuring out if the project uses sanitizers ...
[+] Figuring out if the project uses FindSecBugs ...
[+] Figuring out if the project is fuzzed in OSS-Fuzz ...
[+] Figuring out if the project signs jar files ...
[+] Figuring out if the project uses OWASP security libraries ...
[+] Here is what we know about the project:
[+]    If an open-source project belongs to Eclipse Foundation: false
[+]    If a project runs CodeQL scans: false
[+]    If a project uses LGTM checks for commits: false
[+]    If a project uses OWASP Java Encoder: false
[+]    If a project uses Dependabot: false
[+]    If a project uses GitHub as the main development platform: true
[+]    If an open-source project uses AddressSanitizer: false
[+]    If an open-source project uses FindSecBugs: false
[+]    If a project signs artifacts: false
[+]    Info about vulnerabilities in open-source project: 1 vulnerability
[+]    Number of stars for a GitHub repository: 4373
[+]    Number of contributors in the last three months: 70
[+]    Number of commits in the last three months: 1126
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If a project uses OWASP Enterprise Security API (ESAPI): false
[+]    If a project uses signed commits: false
[+]    If an open-source project has a security team: true
[+]    If an open-source project uses MemorySanitizer: false
[+]    How OWASP Dependency Check is used: NOT_USED
[+]    If a project uses nohttp tool: false
[+]    If a project has a bug bounty program: false
[+]    Number of watchers for a GitHub repository: 254
[+]    A set of programming languages: C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]    If an open-source project has a security policy: false
[+]    If an open-source project is included to OSS-Fuzz project: false
[+]    If a project uses OWASP Java HTML Sanitizer: false
[+]    A CVSS threshold for OWASP Dependency Check to fail the build: 0.0
[+]    If an open-source project uses UndefinedBehaviorSanitizer: false
[+]    If a project runs CodeQL checks for commits: false
[+]    A set of package managers: GRADLE
[+]    The worst LGTM grade of a project: C
[+]    If an open-source project is supported by a company: false
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........4.64 out of 10.0
[+]   Confidence:...Max (10.0 out of 10.0)
[+]   Based on:.....7 sub-scores:
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........0.44 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....5 sub-scores:
[+]           Sub-score:....Dependency testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores:
[+]               Sub-score:....Dependabot score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features:
[+]                   A set of package managers:..............................GRADLE
[+]                   A set of programming languages:.........................C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]                   Does it use Dependabot?.................................No
[+]                   Does it use GitHub as the main development platform?....Yes
[+] 
[+]               Sub-score:....OWASP Dependency Check score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   How is OWASP Dependency Check used?..................Not used
[+]                   What is the threshold for OWASP Dependency Check?....Not specified
[+] 
[+] 
[+]           Sub-score:....Fuzzing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features:
[+]               A set of programming languages:...C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]               Is it included to OSS-Fuzz?.......No
[+] 
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........2.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....3 sub-scores:
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........5.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...1 features:
[+]                   The worst LGTM grade of the project:...C
[+] 
[+]               Sub-score:....How a project uses CodeQL
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...3 features:
[+]                   Does it use LGTM checks?.......................No
[+]                   If a project runs CodeQL checks for commits:...No
[+]                   If a project runs CodeQL scans:................No
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   A set of programming languages:...C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]                   Does it use FindSecBugs?..........No
[+] 
[+] 
[+]           Sub-score:....Memory-safety testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...4 features:
[+]               A set of programming languages:............C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]               Does it use AddressSanitizer?..............No
[+]               Does it use MemorySanitizer?...............No
[+]               Does it use UndefinedBehaviorSanitizer?....No
[+] 
[+]           Sub-score:....nohttp tool
[+]           Importance:...Medium (weight 0.5  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
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
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...17 features:
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
[+]           How is OWASP Dependency Check used?........Not used
[+]           Is it included to OSS-Fuzz?................No
[+] 
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...High (weight 0.8  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
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
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....1 sub-scores:
[+]           Sub-score:....Security testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.44 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
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
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features:
[+]           Number of commits in the last three months:........1126
[+]           Number of contributors in the last three months:...70
[+] 
[+]       Sub-score:....Community commitment
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........7.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
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
[+]       Value:........5.22 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features:
[+]           Number of stars for a GitHub repository:......4373
[+]           Number of watchers for a GitHub repository:...254
[+] 
[+] Rating:     4.64 out of 10.0 -> MODERATE
[+] Confidence: Max (10.0 out of 10.0)
[+] Bye!
```

If `--interactive` option is specified, the tool becomes a bit interactive,
and may ask the user a couple of questions.

## Known issues

Please see [GitHub issues](https://github.com/SAP/fosstars-rating-core/issues).

## Support

Please create a new [GitHub issue](https://github.com/SAP/fosstars-rating-core/issues)
if you found a bug, or you'd like to propose an enhancement.
If you think you found a security issue, please follow [this guideline](SECURITY.md).

We currently don't have a support channel.
If you have a question, please also ask it via GitHub issues.

# Contributing

We appreciate feedback, ideas for improvements and, of course, pull requests.

Please follow [this guideline](CONTRIBUTING.md) if you'd like to contribute to the project.
