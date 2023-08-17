# Getting the OSS RIsk Likelihood Rating

The page describes how a Risk Likelihood of an OSS Project may be calculated.

## Configuration

Several of the rules need to be configured as the detailed guidelines differ from project to project,
for example, which licenses may be allowed in your organization, or which sections you want to enforce in the README file.
Thus, in order to get proper OSS rules of play ratings, you need to provide some details.
These details are usually passed to Fosstars via YAML configuration files for the respective data providers. 
Below you find the configurable data providers and associated scores, information how to name the configuration files and the possible options in the respective configuration file.
The section about the command-line tool further below explains how to pass these configuration files to Fosstars.

### Risk Likelihood
The Risk Likelihood score, that is used in calculating security risk introduced by an open-source project.
It is based on `Risk Likelihood co-efficient * Risk Likelihood factors`

### Risk Likelihood Co-efficient
The score is a combination of 
- `OssSecurityScore` of the project. More information can be found [OSS Security Rating](./oss_security_rating.md).
- `Adopted RiskLikelihood Factor` informs whether the OSS project under consideration has been adopted by another project or not.
  - This is a boolean value and can be set to `true` or `false`. Expected manual input. 

### Risk Likelihood Factors
- `Usage Risk Likelihood Factor`
- `Functionality Risk Likelihood Factor`
- `Handling Untrusted Data Risk Likelihood Factor`

### Usage Risk Likelihood Factor
The score depends on how much the OSS project is used. For example a company.
Spring Boot is widely used within SAP. Hence, the score is high.
Expected manual input.

### Functionality Risk Likelihood Factor
The score determines the functionality of an OSS project. This will indicate how tightly coupled the OSS project could be when adopted.
Few functionalities categorized so far:
- Application framework
- SDK
- Security
- Networking
- Parser
- Logger
- Annotations
- Testing
- Other

Expected manual input.

### Handling Untrusted Data Risk Likelihood Factor
The score determines to what degree the OSS project handles untrusted data.
- NEGLIGIBLE
- LOW
- MEDIUM
- HIGH

Expected manual input.

## How to build
```
git clone https://github.com/SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
```

To calculate an OSS rules of play rating for an open source project, the command-line tool needs a URL to its source code management system (SCM). Currently, the tool works best with projects that stay on GitHub.

### Calculating the rating by providing command-line parameters

A URL to the repository can be passed to the tool by using `--url` command-line parameter.
For example, here is how an Risk Likelihood rating may be calculated for the repository `fosstars-rating-core` in the GitHub organization `SAP`:

```
java -jar target/fosstars-github-rating-calc.jar --url https://github.com/SAP/fosstars-rating-core --token ${TOKEN} --rating security-risk-from-oss
```

The environment variable `TOKEN` contains a token for accessing the GitHub API. You can create a personal token in the [settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub. **Please note:** In order to work correctly, the token needs to have elevated permissions. To get the status of vulnerability alerts, the user who issued the token [must have admin access to the respective repository](https://docs.github.com/en/rest/reference/repos#check-if-vulnerability-alerts-are-enabled-for-a-repository). To get the correct team assignment status, the token [requires the `read:org` scope](https://docs.github.com/en/rest/reference/teams).

Be sure to use the option `--rating` with the value `security-risk-from-oss` as otherwise you won't get the OSS rules of OSS Risk Likelihood rating, but the security rating for the repository.

If everything is correctly set up, the output is going to look like the following:

```
[+] Let's gather info and calculate a rating for:
[+]   https://github.com/SAP/fosstars-rating-core
[+] Figuring out if the project belongs to the Eclipse Software Foundation ...
[+] Checking how the project uses Dependabot ...
[+] Cloning https://github.com/SAP/fosstars-rating-core ...
[+] Figuring out if the project uses GitHub for development ...
[+] Checking how the project uses Snyk ...
[+] Figuring out how many projects on GitHub uses this project ...
[+] Looking for vulnerabilities in the project ...
[+] Figuring out if the project has any unpatched vulnerability ...
[+] Looking for vulnerabilities in NVD ...
[+] Found 0 vulnerabilities
[+] Counting how many stars the project has ...
[+] Figuring out how the project uses GoSec ...
[+] Counting how many people contributed to the project in the last three months ...
[+] Counting how many commits have been done in the last three months ...
[+] Figuring out if the project uses OWASP security libraries ...
[+] Figuring out if the project uses signed commits ...
[+] Not enough info about vulnerabilities to estimate potential CIA impact
[+] Figuring out how the project uses Bandit ...
[+] Figuring out if the project uses OWASP Dependency Check ...
[+] Figuring out if the project uses sanitizers ...
[+] Figuring out how the project uses CodeQL ...
[+] Figuring out if the project is supported by a company ...
[+] Figuring out how the project uses pylint ...
[+] Figuring out if the project has executable binaries...
[+] Figuring out if the project uses FindSecBugs ...
[+] Figuring out if the project signs jar files ...
[+] Figuring out if the project belongs to the Apache Software Foundation ...
[+] Figuring out how the project uses mypy ...
[+] Not enough info about vulnerabilities to estimate potential CIA impact
[+] Not enough info about vulnerabilities to estimate potential CIA impact
[+] Figuring out if the project has a security team ...
[+] Figuring out if the project uses nohttp ...
[+] Figuring out if the project has a bug bounty program ...
[+] Counting how many watchers the project has ...
[+] Looking for programming languages that are used in the project...
[+] Figuring out if the project has a security policy ...
[+] Figuring out if the project is fuzzed in OSS-Fuzz ...
[+] Looking for package managers ...
[+] Looking for programming languages that are used in the project...
[+] Here is what we know about the project:
[+]    What is the threshold for OWASP Dependency Check? Not specified
[+]    Package managers: MAVEN
[+]    Programming languages: JAVA, PYTHON, OTHER
[+]    Is it adopted by any team? No
[+]    What kind of data does it process? CONFIDENTIAL
[+]    How is OWASP Dependency Check used? Not used
[+]    How likely does it handle untrusted data? HIGH
[+]    How many components use it? A_LOT
[+]    Does it have a bug bounty program? No
[+]    Does it have executable binaries? No
[+]    Does the project have open pull requests from Dependabot? No
[+]    Does the project have open pull requests from Snyk? No
[+]    If a project runs Bandit scan checks for commits: No
[+]    If a project runs Bandit scans: No
[+]    Does it use CodeQL checks for pull requests? Yes
[+]    Does it run CodeQL scans? Yes
[+]    Does it run GoSec scans on all pull requests? No
[+]    Does it run GoSec scans? No
[+]    Does it run GoSec scans with rules? No
[+]    Does it run MyPy scans on all commits? No
[+]    Does it run MyPy scans? Yes
[+]    Does it run Pylint scans on all commits? No
[+]    Does it run Pylint scans? Yes
[+]    Does it sign artifacts? Yes
[+]    Does it use Dependabot? Yes
[+]    Does it use GitHub as the main development platform? Yes
[+]    Does it use OWASP ESAPI? No
[+]    Does it use OWASP Java Encoder? No
[+]    Does it use OWASP Java HTML Sanitizer? No
[+]    Does it use Snyk? No
[+]    Does it use nohttp? No
[+]    Does it use verified signed commits? Yes
[+]    Does it belong to Apache? No
[+]    Does it belong to Eclipse? No
[+]    Does it have a security policy? Yes
[+]    Does it have a security team? No
[+]    Is it included to OSS-Fuzz? No
[+]    Is it supported by a company? Yes
[+]    Does it use AddressSanitizer? No
[+]    Does it use FindSecBugs? No
[+]    Does it use MemorySanitizer? No
[+]    Does it use UndefinedBehaviorSanitizer? No
[+]    Info about vulnerabilities in the project: 0 vulnerabilities
[+]    Number of commits in the last three months: 20
[+]    Number of contributors in the last three months: 1
[+]    Number of projects on GitHub that use an open source project: unknown
[+]    Number of stars for a GitHub repository: 50
[+]    Number of watchers for a GitHub repository: 11
[+]    What is potential availability impact in case of a security problem? unknown
[+]    What is potential confidentiality impact in case of a security problem? unknown
[+]    What is potential integrity impact in case of a security problem? HIGH
[+]    Info about security reviews: 0 security reviews
[+]    What kind of functionality does it provide? OTHER
[+] Here is how the rating was calculated:
[+]   Score:........Security risk introduced by an open source project
[+]   Value:........4.51 out of 10.0
[+]   Confidence:...Low (7.3  out of 10.0)
[+]   Based on:.....2 sub-scores
[+]       Sub-score:....Likelihood score for security risk of open source project
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........4.51 out of 10.0
[+]       Confidence:...High (9.86 out of 10.0)
[+]       Based on:.....2 sub-scores
[+]           Sub-score:....Likelihood coefficient for security risk of open source project
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........5.33 out of 10.0
[+]           Confidence:...High (9.72 out of 10.0)
[+]           Based on:.....2 sub-scores
[+]               Sub-score:....Security of project
[+]               Importance:...High (weight 0.8  out of  1.0)
[+]               Value:........5.83 out of 10.0
[+]               Confidence:...High (9.65 out of 10.0)
[+]               Based on:.....8 sub-scores
[+]                   Sub-score:....Security testing
[+]                   Importance:...High (weight 1.0  out of  1.0)
[+]                   Value:........7.23 out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:.....5 sub-scores
[+]                       Sub-score:....Dependency testing
[+]                       Importance:...High (weight 1.0  out of  1.0)
[+]                       Value:........10.0 out of 10.0
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                       Based on:.....3 sub-scores
[+]                           Sub-score:....Dependabot score
[+]                           Importance:...High (weight 1.0  out of  1.0)
[+]                           Value:........10.0 out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...4 features
[+] 
[+]                           Sub-score:....Snyk score
[+]                           Importance:...High (weight 1.0  out of  1.0)
[+]                           Value:........5.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...4 features
[+] 
[+]                           Sub-score:....OWASP Dependency Check score
[+]                           Importance:...High (weight 1.0  out of  1.0)
[+]                           Value:........0.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...3 features
[+] 
[+]                       Sub-score:....Fuzzing
[+]                       Importance:...High (weight 1.0  out of  1.0)
[+]                       Value:........N/A  
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                       Based on:...2 features
[+] 
[+]                       Sub-score:....Memory-safety testing
[+]                       Importance:...High (weight 1.0  out of  1.0)
[+]                       Value:........N/A  
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                       Based on:...4 features
[+] 
[+]                       Sub-score:....Static analysis
[+]                       Importance:...High (weight 1.0  out of  1.0)
[+]                       Value:........5.91 out of 10.0
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                       Based on:.....6 sub-scores
[+]                           Sub-score:....How a project uses CodeQL
[+]                           Importance:...High (weight 1.0  out of  1.0)
[+]                           Value:........10.0 out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...3 features
[+] 
[+]                           Sub-score:....Bandit score
[+]                           Importance:...Medium (weight 0.35 out of  1.0)
[+]                           Value:........0.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...3 features
[+] 
[+]                           Sub-score:....FindSecBugs score
[+]                           Importance:...Medium (weight 0.35 out of  1.0)
[+]                           Value:........0.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...2 features
[+] 
[+]                           Sub-score:....How a project uses Pylint
[+]                           Importance:...Medium (weight 0.35 out of  1.0)
[+]                           Value:........6.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...3 features
[+] 
[+]                           Sub-score:....GoSec score
[+]                           Importance:...Medium (weight 0.3  out of  1.0)
[+]                           Value:........N/A  
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...4 features
[+] 
[+]                           Sub-score:....How a project uses MyPy
[+]                           Importance:...Low (weight 0.2  out of  1.0)
[+]                           Value:........6.0  out of 10.0
[+]                           Confidence:...Max (10.0 out of 10.0)
[+]                           Based on:...3 features
[+] 
[+]                       Sub-score:....nohttp tool
[+]                       Importance:...Low (weight 0.2  out of  1.0)
[+]                       Value:........0.0  out of 10.0
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                       Based on:...2 features
[+] 
[+]                   Sub-score:....Security awareness
[+]                   Importance:...High (weight 0.9  out of  1.0)
[+]                   Value:........4.0  out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...18 features
[+] 
[+]                   Sub-score:....Vulnerability discovery and security testing
[+]                   Importance:...Medium (weight 0.6  out of  1.0)
[+]                   Value:........10.0 out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:.....1 sub-scores
[+]                       Sub-score:....Security testing
[+]                       Importance:...High (weight 1.0  out of  1.0)
[+]                       Value:........7.23 out of 10.0
[+]                       Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...1 features
[+] 
[+]                   Sub-score:....Unpatched vulnerabilities
[+]                   Importance:...Medium (weight 0.5  out of  1.0)
[+]                   Value:........10.0 out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...1 features
[+] 
[+]                   Sub-score:....Community commitment
[+]                   Importance:...Medium (weight 0.5  out of  1.0)
[+]                   Value:........8.0  out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...3 features
[+] 
[+]                   Sub-score:....Project activity
[+]                   Importance:...Medium (weight 0.5  out of  1.0)
[+]                   Value:........3.08 out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...2 features
[+] 
[+]                   Sub-score:....Project popularity
[+]                   Importance:...Medium (weight 0.5  out of  1.0)
[+]                   Value:........0.09 out of 10.0
[+]                   Confidence:...Low (6.67 out of 10.0)
[+]                   Based on:...3 features
[+] 
[+]                   Sub-score:....Security reviews
[+]                   Importance:...Low (weight 0.2  out of  1.0)
[+]                   Value:........0.0  out of 10.0
[+]                   Confidence:...Max (10.0 out of 10.0)
[+]                   Based on:...1 features
[+] 
[+]               Sub-score:....Likelihood factor of adoption of an open source project by a team
[+]               Importance:...Low (weight 0.2  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...1 features
[+] 
[+]           Sub-score:....Aggregated likelihood factors for security risk of open source project
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........8.46 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....3 sub-scores
[+]               Sub-score:....Likelihood factor of usage of an open source project
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........10.0 out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...1 features
[+] 
[+]               Sub-score:....Likelihood factor of functionality of an open source project
[+]               Importance:...High (weight 0.8  out of  1.0)
[+]               Value:........5.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...1 features
[+] 
[+]               Sub-score:....Likelihood factor of handling untrusted data
[+]               Importance:...High (weight 0.8  out of  1.0)
[+]               Value:........10.0 out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...1 features
[+] 
[+]       Sub-score:....Aggregated impact factors for security risk of open source project
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Low (4.74 out of 10.0)
[+]       Based on:.....4 sub-scores
[+]           Sub-score:....Impact factor of confidentiality of data that an open source project likely processes
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........10.0 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...1 features
[+] 
[+]           Sub-score:....Integrity impact factor
[+]           Importance:...High (weight 0.8  out of  1.0)
[+]           Value:........10.0 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...1 features
[+] 
[+]           Sub-score:....Confidentiality impact factor
[+]           Importance:...High (weight 0.8  out of  1.0)
[+]           Value:........unknown
[+]           Confidence:...Min (0.0  out of 10.0)
[+]           Based on:...1 features
[+] 
[+]           Sub-score:....Availability impact factor
[+]           Importance:...High (weight 0.8  out of  1.0)
[+]           Value:........unknown
[+]           Confidence:...Min (0.0  out of 10.0)
[+]           Based on:...1 features
[+] 
[+] Rating:     4.51 out of 10.0 -> MEDIUM
[+] Confidence: Low (7.3  out of 10.0)
[+] 
[+] Bye!
```

The tool prints out the gathered data and a status for the project.

---

Next: [Notes](notes.md)
