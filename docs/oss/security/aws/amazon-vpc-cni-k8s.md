# aws/amazon-vpc-cni-k8s

https://github.com/aws/amazon-vpc-cni-k8s

Last updated on Jul 18, 2021

**Rating**: **BAD**

**Score**: **3.36**, max score value is 10.0

**Confidence**: High (9.87, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
1.  **[Security awareness](#security-awareness)**: **2.0** (weight is 0.9)
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **2.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
1.  **[Community commitment](#community-commitment)**: **8.0** (weight is 0.5)
1.  **[Project activity](#project-activity)**: **5.69** (weight is 0.5)
1.  **[Project popularity](#project-popularity)**: **1.93** (weight is 0.5)
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)


## How to improve the rating

1.  You can ask the project maintainers to enable LGTM checks for pull requests in the project.
    More info:
    1.  [How to enable LGTM checks for pull requests](https://lgtm.com/help/lgtm/about-automated-code-review)
2.  You can open a pull request to enable CodeQL scans in the project. Make sure that the scans are run on pull requests.
    More info:
    1.  [How to enable CodeQL checks for pull requests](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)
3.  You can open a pull request to enable CodeQL scans in the project.
    More info:
    1.  [How to enable CodeQL checks](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)
4.  You can open a pull request to enable FindSecBugs for the project.
    More info:
    1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)
5.  You can enable artifact signing in the project's build pipeline.
    More info:
    1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)
6.  You can enable NoHttp tool in the project's build pipeline.
    More info:
    1.  [NoHttp tool home page](https://github.com/spring-io/nohttp)


## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **0.0**, confidence is 9.52 (high), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)


### Security awareness

Score: **2.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00.



This sub-score is based on 17 features:

1.  Does it have a bug bounty program? **No**
1.  Does it have a security policy? **Yes**
1.  Does it have a security team? **No**
1.  Does it sign artifacts? **No**
1.  Does it use AddressSanitizer? **No**
1.  Does it use Dependabot? **No**
1.  Does it use FindSecBugs? **No**
1.  Does it use LGTM checks? **No**
1.  Does it use MemorySanitizer? **No**
1.  Does it use OWASP ESAPI? **No**
1.  Does it use OWASP Java Encoder? **No**
1.  Does it use OWASP Java HTML Sanitizer? **No**
1.  Does it use UndefinedBehaviorSanitizer? **No**
1.  Does it use nohttp? **No**
1.  Does it use verified signed commits? **No**
1.  How is OWASP Dependency Check used? **Not used**
1.  Is it included to OSS-Fuzz? **No**

### Vulnerability discovery and security testing

Score: **2.0**, confidence is 9.76 (high), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:

1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)

This sub-score is based on 1 feature:

1.  Info about vulnerabilities: **Not found**

### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:

1.  Info about vulnerabilities: **Not found**

### Community commitment

Score: **8.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:

1.  Does it belong to Apache? **No**
1.  Does it belong to Eclipse? **No**
1.  Is it supported by a company? **Yes**

### Project activity

Score: **5.69**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

37 commits in the last 3 months results to 5.69 points

This sub-score is based on 2 features:

1.  Number of commits in the last three months: **37**
1.  Number of contributors in the last three months: **1**

### Project popularity

Score: **1.93**, confidence is 10.0 (max), weight is 0.5 (medium)

The score is based on number of stars and watchers.
Here is how a number of stars contributes to the score:
0 -> 0.00 (min), 2500 -> 2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
Here is how a number of watchers contributes to the score:
0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50, 3000 -> 10.00 (max)



This sub-score is based on 2 features:

1.  Number of stars for a GitHub repository: **1556**
1.  Number of watchers for a GitHub repository: **111**

### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:

1.  Info about security reviews: **0 security reviews**

### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)


### Static analysis

Score: **0.0**, confidence is 8.0 (low), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:

1.  Is it included to OSS-Fuzz? **No**
1.  Programming languages: **GO, OTHER**

### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:

1.  Does it use AddressSanitizer? **No**
1.  Does it use MemorySanitizer? **No**
1.  Does it use UndefinedBehaviorSanitizer? **No**
1.  Programming languages: **GO, OTHER**

### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:

1.  Does it use nohttp? **No**
1.  Package managers: **None**

### Dependabot score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:

1.  Does it use Dependabot? **No**
1.  Does it use GitHub as the main development platform? **Yes**
1.  Package managers: **None**
1.  Programming languages: **GO, OTHER**

### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:

1.  How is OWASP Dependency Check used? **Not used**
1.  Package managers: **None**
1.  What is the threshold for OWASP Dependency Check? **Not specified**

### LGTM score

Score: **0.0**, confidence is 5.0 (low), weight is 1.0 (high)





This sub-score is based on 2 features:

1.  Programming languages: **GO, OTHER**
1.  The worst LGTM grade of the project: **unknown**

### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:

1.  Does it run CodeQL scans? **No**
1.  Does it use CodeQL checks for pull requests? **No**
1.  Does it use LGTM checks? **No**
1.  Programming languages: **GO, OTHER**

### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:

1.  Does it use FindSecBugs? **No**
1.  Programming languages: **GO, OTHER**



## Known vulnerabilities

No vulnerabilities found

