# Getting the security ratings

The page describes how a security rating may be calculated for an open-source project.

## Command line tool for calculating security ratings

There is a command line tool for gathering data about an open-source project and calculating
the security rating. Currently, there is not an installer for the tool (should we create one?),
and the tool should be built locally from the sources.

The following commands download the sources and build the command line tool with Maven:

```
git clone git@github.com:SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
```

To calculate the security rating for an open-source project,
the command line tool needs a URL to its source code management system (SCM).
Currently, the tool works best with projects what stay on GitHub.

### Calculating the security rating by providing a URL to the source code

A URL to SCM can be passed to the tool by using `--url` command line parameter.
For example, here is how a security rating may be calculated for Apache Beam:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --url https://github.com/apache/beam --no-questions
```

The environment variable `TOKEN` contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.
It's okay to run the tool without the token, but the result will be a bit less precise.

The tool will try to gather info about the project.
In particular, it will try to download the source code,
make a number of requests to GitHub and other services, fetch data from NVD and so on.
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
[+]    Number of stars for a GitHub repository: 4199
[+]    Number of contributors in the last three months: 93
[+]    Number of commits in the last three months: 1388
[+]    If an open-source project belongs to Apache Foundation: true
[+]    If a project uses OWASP Enterprise Security API (ESAPI): false
[+]    If a project uses signed commits: false
[+]    If an open-source project has a security team: true
[+]    If an open-source project uses MemorySanitizer: false
[+]    How OWASP Dependency Check is used: NOT_USED
[+]    If a project uses nohttp tool: false
[+]    If a project has a bug bounty program: false
[+]    Number of watchers for a GitHub repository: 257
[+]    A set of programming languages: C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]    If an open-source project has a security policy: false
[+]    If an open-source project is included to OSS-Fuzz project: false
[+]    If a project uses OWASP Java HTML Sanitizer: false
[+]    A CVSS threshold for OWASP Dependency Check to fail the build: 0.0
[+]    If an open-source project uses UndefinedBehaviorSanitizer: false
[+]    A set of package managers: GRADLE
[+]    The worst LGTM grade of a project: B
[+]    If an open-source project is supported by a company: false
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........4.71 out of 10.0
[+]   Confidence:...Max (10.0 out of 10.0)
[+]   Based on:.....7 sub-scores:
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........0.89 out of 10.0
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
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........4.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores:
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........4.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   Does it use LGTM checks?...............No
[+]                   The worst LGTM grade of the project:...B
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features:
[+]                   A set of programming languages:...C, JAVA, PYTHON, JAVASCRIPT, OTHER
[+]                   Does it use FindSecBugs?..........No
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
[+]           Value:........0.89 out of 10.0
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
[+]           Number of commits in the last three months:........1388
[+]           Number of contributors in the last three months:...93
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
[+]       Value:........5.06 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features:
[+]           Number of stars for a GitHub repository:......4199
[+]           Number of watchers for a GitHub repository:...257
[+] 
[+] Rating:     4.71 out of 10.0 -> GOOD
[+] Confidence: Max (10.0 out of 10.0)
[+] Bye!
```

The tool prints out the gathered data, the structure of the overall security score, used sub-scores,
their weights and so on. This info may be useful if one would like to understand how the tool
calculated the rating. It may also give an idea for possible improvements that can improve the rating.

If no `--no-questions` option is specified, the tool becomes a bit interactive,
and may ask the user a couple of questions about the project.

### Calculating the security rating by providing GAV coordinates

The command line tool also accepts GAV coordinates of Maven artifacts (group id, artifact id and version).
If the coordinates are provided,
then the tool will try to figure out which open-source project produces the artifact.
In particular, it will try to find a URL to the project's SCM.
If the URL is found, the tool will use it to calculate the security rating.
Otherwise, the tool exits with an error.

The tool has a command line option `--gav`, that accepts either GAV coordinates or just group id and artifact id.
Below is an example of getting a rating for Apache Commons Text by passing its group and artifact ids:

```
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --gav org.apache.commons:commons-text --no-questions
```

### Building a report for multiple projects

The tool can calculate ratings for multiple projects and generate a report.
To do that, the tool needs a configuration file that describes which projects should be included
to the report, and which format should be used for the report.

Here is an example of such a configuration file:

```
# this is a configuration for generating a report for a number of open-source projects

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
java -jar target/fosstars-github-rating-calc.jar --token ${TOKEN} --config conf.yml --no-questions
```

The Markdown report is going to be available in `fosstars/report` directory.

## Further ideas

1.  Add an installer for the command line tool.
1.  Add a Maven plugin that looks for dependencies in an application and calculate security ratings for them.
1.  Add a GitHub App that looks for dependencies in a repository, calculates security ratings for them,
    and report them via GitHub issues or comments in pull requests.
1.  Offer a GitHub action that regularly generates reports.

---

Next: [Alternatives](alternatives.md)
