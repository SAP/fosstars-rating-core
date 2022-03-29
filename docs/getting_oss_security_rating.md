# Getting the security ratings

The page describes how a security rating may be calculated for an open source project.

## Fosstars GitHub action

For projects on GitHub, there is a [GitHub action](https://github.com/SAP/fosstars-rating-core-action)
that calculates a security rating and generates a badge.

## Command-line tool for calculating security ratings

There is a command-line tool for gathering data about an open source project and calculating
a security rating for it. 
Currently, there is not an [installer](https://github.com/SAP/fosstars-rating-core/issues/399) for the tool,
and the tool has to be built locally from the source code.

The following commands download the source code and build the command-line tool with Maven:

```
git clone https://github.com/SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
```

To calculate a security rating for an open source project,
the command-line tool needs a URL to its source code management system (SCM).
Currently, the tool works best with projects that stay on GitHub.

### Calculating the security rating by providing a URL to the source code

A URL to SCM can be passed to the tool by using `--url` command-line parameter.
For example, here is how a security rating may be calculated for Apache Beam:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/FasterXML/jackson-databind --verbose
```

The environment variable `TOKEN` contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.
It's okay to run the tool without the token, but the result will be a bit less precise.

First, the tool will try to gather info about the project.
In particular, it will try to download the source code,
make a number of requests to GitHub and other services, fetch data from NVD and so on.
The output is going to look like the following:

```
[+] Okay, we have a GitHub token, let's try to use it
[+] Let's gather info and calculate a rating for:
[+]   https://github.com/FasterXML/jackson-databind
[+] Here is what we know about the project:
[+]    What is the threshold for OWASP Dependency Check? Not specified
[+]    Package managers: MAVEN
[+]    Programming languages: JAVA, OTHER
[+]    How is OWASP Dependency Check used? Not used
[+]    Does it have a bug bounty program? No
[+]    Does the project have open pull requests from Dependabot? No
[+]    If a project runs Bandit scan checks for commits: No
[+]    If a project runs Bandit scans: No
[+]    Does it use CodeQL checks for pull requests? Yes
[+]    Does it run CodeQL scans? Yes
[+]    Does it sign artifacts? No
[+]    Does it use Dependabot? No
[+]    Does it use GitHub as the main development platform? Yes
[+]    Does it use LGTM checks? No
[+]    Does it use OWASP ESAPI? No
[+]    Does it use OWASP Java Encoder? No
[+]    Does it use OWASP Java HTML Sanitizer? No
[+]    Does it use nohttp? No
[+]    Does it use verified signed commits? No
[+]    Does it belong to Apache? No
[+]    Does it belong to Eclipse? No
[+]    Does it have a security policy? Yes
[+]    Does it have a security team? No
[+]    Is it included to OSS-Fuzz? Yes
[+]    Is it supported by a company? No
[+]    Does it use AddressSanitizer? No
[+]    Does it use FindSecBugs? No
[+]    Does it use MemorySanitizer? No
[+]    Does it use UndefinedBehaviorSanitizer? No
[+]    Info about vulnerabilities in the project: 64 vulnerabilities
[+]    Number of commits in the last three months: 60
[+]    Number of contributors in the last three months: 2
[+]    Number of projects on GitHub that use an open source project: 453913
[+]    Number of stars for a GitHub repository: 2963
[+]    Number of watchers for a GitHub repository: 162
[+]    Info about security reviews: 0 security reviews
[+]    The worst LGTM grade of the project: A
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........5.05 out of 10.0
[+]   Confidence:...Max (10.0 out of 10.0)
[+]   Based on:.....8 sub-scores
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........6.18 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....5 sub-scores
[+]           Sub-score:....Dependency testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........6.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores
[+]               Sub-score:....Dependabot score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........6.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+]                   Does it use Dependabot?.................................No
[+]                   Does it use GitHub as the main development platform?....Yes
[+]                   Package managers:.......................................MAVEN
[+]                   Programming languages:..................................JAVA, OTHER
[+] 
[+]               Sub-score:....OWASP Dependency Check score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...3 features
[+]                   How is OWASP Dependency Check used?..................Not used
[+]                   Package managers:....................................MAVEN
[+]                   What is the threshold for OWASP Dependency Check?....Not specified
[+] 
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........7.6  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....4 sub-scores
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........9.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+]                   Programming languages:.................JAVA, OTHER
[+]                   The worst LGTM grade of the project:...A
[+] 
[+]               Sub-score:....How a project uses CodeQL
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........10.0 out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+]                   Does it run CodeQL scans?.......................Yes
[+]                   Does it use CodeQL checks for pull requests?....Yes
[+]                   Does it use LGTM checks?........................No
[+]                   Programming languages:..........................JAVA, OTHER
[+] 
[+]               Sub-score:....How a project uses Bandit
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........N/A  
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...3 features
[+]                   If a project runs Bandit scan checks for commits:...No
[+]                   If a project runs Bandit scans:.....................No
[+]                   Programming languages:..............................JAVA, OTHER
[+]               Explanation:..The score is N/A because the project uses languages that are not supported by Bandit.
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+]                   Does it use FindSecBugs?....No
[+]                   Programming languages:......JAVA, OTHER
[+] 
[+]           Sub-score:....Fuzzing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........N/A  
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+]               Is it included to OSS-Fuzz?....Yes
[+]               Programming languages:.........JAVA, OTHER
[+] 
[+]           Sub-score:....Memory-safety testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........N/A  
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...4 features
[+]               Does it use AddressSanitizer?..............No
[+]               Does it use MemorySanitizer?...............No
[+]               Does it use UndefinedBehaviorSanitizer?....No
[+]               Programming languages:.....................JAVA, OTHER
[+] 
[+]           Sub-score:....nohttp tool
[+]           Importance:...Low (weight 0.2  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+]               Does it use nohttp?....No
[+]               Package managers:......MAVEN
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
[+]       Based on:...17 features
[+]           Does it have a bug bounty program?.........No
[+]           Does it have a security policy?............Yes
[+]           Does it have a security team?..............No
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
[+]           Is it included to OSS-Fuzz?................Yes
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
[+]       Based on:.....1 sub-scores
[+]           Sub-score:....Security testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........6.18 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about vulnerabilities in the project:...64 vulnerabilities
[+] 
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about vulnerabilities in the project:...64 vulnerabilities
[+]       Explanation:..No unpatched vulnerabilities found which is good
[+] 
[+]       Sub-score:....Community commitment
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...3 features
[+]           Does it belong to Apache?........No
[+]           Does it belong to Eclipse?.......No
[+]           Is it supported by a company?....No
[+] 
[+]       Sub-score:....Project activity
[+]       Description:..The score evaluates how active a project is. It's
[+]                     based on number of commits and contributors in the
[+]                     last 3 months.
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........9.69 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features
[+]           Number of commits in the last three months:........60
[+]           Number of contributors in the last three months:...2
[+]       Explanation:..60 commits in the last 3 months results to 9.23 points
[+]                     2 contributors increase the score value from 9.23 to 9.69
[+] 
[+]       Sub-score:....Project popularity
[+]       Description:..This scoring function is based on number of stars,
[+]                     watchers and dependent projects.
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...3 features
[+]           Number of projects on GitHub that use an open source project:...453913
[+]           Number of stars for a GitHub repository:........................2963
[+]           Number of watchers for a GitHub repository:.....................162
[+] 
[+]       Sub-score:....Security reviews
[+]       Importance:...Low (weight 0.2  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about security reviews:...0 security reviews
[+]       Explanation:..No security reviews have been done
[+] 
[+] Rating:     5.05 out of 10.0 -> MODERATE
[+] Confidence: Max (10.0 out of 10.0)
[+] 
[+] Here is how the rating may be improved:
[+] 1. You can ask the project maintainers to enable LGTM
[+]    checks for pull requests in the project.
[+]    More info:
[+]    1. How to enable LGTM checks for pull requests:
[+]       https://lgtm.com/help/lgtm/about-automated-code-review
[+] 2. You can fix the issues reported by LGTM for the
[+]    project.
[+]    More info:
[+]    1. List of issues on LGTM:
[+]       https://lgtm.com/projects/g/FasterXML/jackson-databind
[+] 3. You can open a pull request to enable FindSecBugs
[+]    for the project.
[+]    More info:
[+]    1. FindSecBugs home page:
[+]       https://find-sec-bugs.github.io/
[+] 4. You can configure Dependabot by creating a
[+]    configuration file.
[+]    More info:
[+]    1. Configuration options for dependency updates:
[+]       https://docs.github.com/en/github/administering-a-repository/configuration-options-for-dependency-updates
[+] 5. You can add OWASP Dependency Check to the
[+]    project's build pipeline.
[+]    More info:
[+]    1. OWASP Dependnecy Check:
[+]       https://jeremylong.github.io/DependencyCheck/
[+]    2. How to use OWASP Dependency Check with Maven:
[+]       https://jeremylong.github.io/DependencyCheck/dependency-check-maven
[+]    3. How to use OWASP Dependnecy Check with Gradle:
[+]       https://github.com/dependency-check/dependency-check-gradle
[+] 6. You can set a CVSS threshold for vulnerabilities
[+]    reported by OWASP Dependency Check.
[+]    More info:
[+]    1. OWASP Dependnecy Check:
[+]       https://jeremylong.github.io/DependencyCheck/
[+]    2. Configuring OWASP Dependency Check:
[+]       https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html
[+] 7. You can enable artifact signing in the project's
[+]    build pipeline.
[+]    More info:
[+]    1. Apache Maven Jarsigner Plugin:
[+]       https://maven.apache.org/plugins/maven-jarsigner-plugin/
[+] 8. You can enable NoHttp tool in the project's build
[+]    pipeline.
[+]    More info:
[+]    1. NoHttp tool home page:
[+]       https://github.com/spring-io/nohttp
[+] 9. You can open a pull request to run Bandit scans in
[+]    the project using GitHub action workflow.
[+]    More info:
[+]    1. GitHub workflow action job config to run Bandit code scanning for a repository.:
[+]       https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idstepsrun
[+]    2. An example to run Bandit scan check as part of GitHub action workflow.:
[+]       https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L28
[+] 10. You can open a pull request to trigger Bandit
[+]    scans job in the project using GitHub action
[+]    workflow for every pull-request.
[+]    More info:
[+]    1. GitHub workflow action config to run Bandit code scanning job on every PR of a project.:
[+]       https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#example-using-a-list-of-events
[+]    2. An eample to trigger Bandit scan check on every pull-request.:
[+]       https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L3
[+] 
[+] Bye!
```

The tool prints out the gathered data, the structure of the overall security score, used sub-scores,
their weights and so on. This info may be useful if one would like to understand how the tool
calculated the rating. In the end, the tool print a list of possible improvement in the project
that would positively impact the rating.

If `--interactive` option is specified, the tool becomes a bit interactive,
and may ask the user a couple of questions about the project.

### Calculating the security rating by providing GAV coordinates

The command-line tool also accepts GAV coordinates of Maven artifacts (group id, artifact id and version).
If the coordinates are provided,
then the tool will try to figure out which open source project produced the artifact.
In particular, it will try to find a URL to the project's SCM.
If the URL is found, the tool will use it to calculate the security rating.
Otherwise, the tool exits with an error.

The tool has a command-line option `--gav` that accepts either GAV coordinates or just group id and artifact id.
Below is an example of getting a security rating for Apache Commons Text by passing its group and artifact ids:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --gav org.apache.commons:commons-text --verbose
```

### Building a report for multiple projects

The tool can calculate ratings for multiple projects and generate a report.
To do that, the tool needs a configuration file that describes which projects should be included
to the report, and which format should be used for the report.

Here is an example of such a configuration file:

```
# this is a configuration for generating a report for a number of open source projects

# a cache to store fetched data about the projects
cache: .fosstars/project_rating_cache.json

# the following sections lists projects for which the ratings should be calculated
finder:

  # search for projects in FasterXML organisation on GitHub
  organizations:
    - name: FasterXML

      # consider projects with at least the specified number of stars
      stars: 5000

      # skip projects that contains the following words in its names
      exclude:
        - docs
        - test
        - benchmark

  # include the following projects
  repositories:
    - organization: netty
      name: netty
    - organization: openssl
      name: openssl
    - organization: curl
      name: curl
    - organization: google
      name: guava
    - organization: google
      name: gson

# the following section describes which reports should be generated.
reports:

  # first, store the gathered data and calculated ratings in a JSON file
  # then, the file will be used to generate other reports
  - type: json
    where: fosstars/github_projects.json

  # generate a report in Markdown format
  # first, it will read gathered data and calculated ratings from the specified JSON file
  # then, it will build the report and store it in the specified directory
  - type: markdown
    source: fosstars/github_projects.json
    where: fosstars/report
```

The config gives the following instructions to the tool:

*  Calculate security ratings for Netty, OpenSSL, Guava, Gson, curl 
   and projects from FasterXML organisation which have at least 5K stars.
*  Store gathered data and calculated ratings in a JSON file.
*  Create a report in Markdown format.
*  Store the results in the specified directories.

Here is how the tool may be run with the config above:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --config conf.yml --verbose
```

The Markdown report is going to be available in `fosstars/report` directory.

## Further ideas

1.  [Add an installer for the command-line tool.](https://github.com/SAP/fosstars-rating-core/issues/399)
1.  [Add a Maven plugin](https://github.com/SAP/fosstars-rating-core/issues/344) that looks for dependencies in an application and calculate security ratings for them.
1.  Add a GitHub App that looks for dependencies in a repository, calculates security ratings for them,
    and report them via GitHub issues or comments in pull requests.

---

Next: [Alternatives](alternatives.md)
