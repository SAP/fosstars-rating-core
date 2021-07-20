# openssl/openssl

https://github.com/openssl/openssl

Last updated on Jul 18, 2021

**Rating**: **MODERATE**

**Score**: **5.19**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **4.22** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **3.5** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **7.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
1.  **[Security awareness](#security-awareness)**: **5.0** (weight is 0.9)
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **4.22** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        1.  **[Static analysis](#static-analysis)**: **3.5** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **7.0** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
        1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
1.  **[Community commitment](#community-commitment)**: **0.0** (weight is 0.5)
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
1.  **[Security reviews](#security-reviews)**: **3.33** (weight is 0.2)


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
4.  You can fix the issues reported by LGTM for the project.
    More info:
    1.  [List of issues on LGTM](https://lgtm.com/projects/g/openssl/openssl)
5.  You can open a pull request to add a security policy for the project.
    More info:
    1.  [About adding a security policy to a repository on GitHub](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    2.  [An example of a security policy](https://github.com/apache/nifi/blob/main/SECURITY.md)
    3.  [Suggest a security policy for the project](https://github.com/openssl/openssl/security/policy)
6.  You can open a pull request to enable FindSecBugs for the project.
    More info:
    1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)
7.  You can open a pull request to enable AddressSanitizer for the project.
    More info:
    1.  [About AddressSanitizer](https://github.com/google/sanitizers/wiki/AddressSanitizer)
8.  You can open a pull request to enable MemorySanitizer for the project.
    More info:
    1.  [About MemorySanitizer](https://github.com/google/sanitizers/wiki/MemorySanitizer)
9.  You can open a pull request to enable UndefinedBehaviorSanitizer for the project.
    More info:
    1.  [About UndefinedBehaviorSanitizer](https://clang.llvm.org/docs/UndefinedBehaviorSanitizer.html)
10.  You can enable artifact signing in the project's build pipeline.
    More info:
    1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)
11.  You can enable NoHttp tool in the project's build pipeline.
    More info:
    1.  [NoHttp tool home page](https://github.com/spring-io/nohttp)


## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **4.22**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
1.  **[Static analysis](#static-analysis)**: **3.5** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **7.0** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)


### Security awareness

Score: **5.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00.



This sub-score is based on 17 features:

1.  Does it have a bug bounty program? **Yes**
1.  Does it have a security policy? **No**
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
1.  Is it included to OSS-Fuzz? **Yes**

### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:

1.  **[Security testing](#security-testing)**: **4.22** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **3.5** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **7.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)

This sub-score is based on 1 feature:

1.  Info about vulnerabilities: **222 vulnerabilities, [details below](#known-vulnerabilities)**

### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:

1.  Info about vulnerabilities: **222 vulnerabilities, [details below](#known-vulnerabilities)**

### Community commitment

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:

1.  Does it belong to Apache? **No**
1.  Does it belong to Eclipse? **No**
1.  Is it supported by a company? **No**

### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

1134 commits in the last 3 months results to 10.00 points
13 contributors increase the score value from 10.00 to 12.00

This sub-score is based on 2 features:

1.  Number of commits in the last three months: **1134**
1.  Number of contributors in the last three months: **13**

### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score is based on number of stars and watchers.
Here is how a number of stars contributes to the score:
0 -> 0.00 (min), 2500 -> 2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
Here is how a number of watchers contributes to the score:
0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50, 3000 -> 10.00 (max)



This sub-score is based on 2 features:

1.  Number of stars for a GitHub repository: **16088**
1.  Number of watchers for a GitHub repository: **924**

### Security reviews

Score: **3.33**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 1 feature:

1.  Info about security reviews: **1 security review**

### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)


### Static analysis

Score: **3.5**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:

1.  **[LGTM score](#lgtm-score)**: **7.0** (weight is 1.0)
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)


### Fuzzing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:

1.  Is it included to OSS-Fuzz? **Yes**
1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**

### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:

1.  Does it use AddressSanitizer? **No**
1.  Does it use MemorySanitizer? **No**
1.  Does it use UndefinedBehaviorSanitizer? **No**
1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**

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
1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**

### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:

1.  How is OWASP Dependency Check used? **Not used**
1.  Package managers: **None**
1.  What is the threshold for OWASP Dependency Check? **Not specified**

### LGTM score

Score: **7.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:

1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**
1.  The worst LGTM grade of the project: **B**

### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:

1.  Does it run CodeQL scans? **No**
1.  Does it use CodeQL checks for pull requests? **No**
1.  Does it use LGTM checks? **No**
1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**

### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:

1.  Does it use FindSecBugs? **No**
1.  Programming languages: **C, CPP, RUBY, PYTHON, OTHER**



## Known vulnerabilities

1.  [CVE-1999-0428](https://nvd.nist.gov/vuln/detail/CVE-1999-0428)
1.  [CVE-2000-0535](https://nvd.nist.gov/vuln/detail/CVE-2000-0535)
1.  [CVE-2001-1141](https://nvd.nist.gov/vuln/detail/CVE-2001-1141)
1.  [CVE-2002-0656](https://nvd.nist.gov/vuln/detail/CVE-2002-0656)
1.  [CVE-2003-0078](https://nvd.nist.gov/vuln/detail/CVE-2003-0078)
1.  [CVE-2003-0131](https://nvd.nist.gov/vuln/detail/CVE-2003-0131)
1.  [CVE-2003-0147](https://nvd.nist.gov/vuln/detail/CVE-2003-0147)
1.  [CVE-2003-0544](https://nvd.nist.gov/vuln/detail/CVE-2003-0544)
1.  [CVE-2003-0851](https://nvd.nist.gov/vuln/detail/CVE-2003-0851)
1.  [CVE-2004-0112](https://nvd.nist.gov/vuln/detail/CVE-2004-0112)
1.  [CVE-2004-0975](https://nvd.nist.gov/vuln/detail/CVE-2004-0975)
1.  [CVE-2005-1797](https://nvd.nist.gov/vuln/detail/CVE-2005-1797)
1.  [CVE-2005-2946](https://nvd.nist.gov/vuln/detail/CVE-2005-2946)
1.  [CVE-2005-2969](https://nvd.nist.gov/vuln/detail/CVE-2005-2969)
1.  [CVE-2006-4339](https://nvd.nist.gov/vuln/detail/CVE-2006-4339)
1.  [CVE-2006-3738](https://nvd.nist.gov/vuln/detail/CVE-2006-3738)
1.  [CVE-2007-3108](https://nvd.nist.gov/vuln/detail/CVE-2007-3108)
1.  [CVE-2007-5135](https://nvd.nist.gov/vuln/detail/CVE-2007-5135)
1.  [CVE-2007-4995](https://nvd.nist.gov/vuln/detail/CVE-2007-4995)
1.  [CVE-2007-5536](https://nvd.nist.gov/vuln/detail/CVE-2007-5536)
1.  [CVE-2008-0166](https://nvd.nist.gov/vuln/detail/CVE-2008-0166)
1.  [CVE-2008-0891](https://nvd.nist.gov/vuln/detail/CVE-2008-0891)
1.  [CVE-2008-1678](https://nvd.nist.gov/vuln/detail/CVE-2008-1678)
1.  [CVE-2008-5077](https://nvd.nist.gov/vuln/detail/CVE-2008-5077)
1.  [CVE-2009-0653](https://nvd.nist.gov/vuln/detail/CVE-2009-0653)
1.  [CVE-2009-0591](https://nvd.nist.gov/vuln/detail/CVE-2009-0591)
1.  [CVE-2009-1379](https://nvd.nist.gov/vuln/detail/CVE-2009-1379)
1.  [CVE-2009-1386](https://nvd.nist.gov/vuln/detail/CVE-2009-1386)
1.  [CVE-2009-1390](https://nvd.nist.gov/vuln/detail/CVE-2009-1390)
1.  [CVE-2009-2409](https://nvd.nist.gov/vuln/detail/CVE-2009-2409)
1.  [CVE-2009-3767](https://nvd.nist.gov/vuln/detail/CVE-2009-3767)
1.  [CVE-2009-3555](https://nvd.nist.gov/vuln/detail/CVE-2009-3555)
1.  [CVE-2009-4355](https://nvd.nist.gov/vuln/detail/CVE-2009-4355)
1.  [CVE-2010-0433](https://nvd.nist.gov/vuln/detail/CVE-2010-0433)
1.  [CVE-2010-0740](https://nvd.nist.gov/vuln/detail/CVE-2010-0740)
1.  [CVE-2010-1633](https://nvd.nist.gov/vuln/detail/CVE-2010-1633)
1.  [CVE-2010-2939](https://nvd.nist.gov/vuln/detail/CVE-2010-2939)
1.  [CVE-2010-3864](https://nvd.nist.gov/vuln/detail/CVE-2010-3864)
1.  [CVE-2010-4252](https://nvd.nist.gov/vuln/detail/CVE-2010-4252)
1.  [CVE-2011-0014](https://nvd.nist.gov/vuln/detail/CVE-2011-0014)
1.  [CVE-2011-1945](https://nvd.nist.gov/vuln/detail/CVE-2011-1945)
1.  [CVE-2011-3207](https://nvd.nist.gov/vuln/detail/CVE-2011-3207)
1.  [CVE-2011-4576](https://nvd.nist.gov/vuln/detail/CVE-2011-4576)
1.  [CVE-2012-0050](https://nvd.nist.gov/vuln/detail/CVE-2012-0050)
1.  [CVE-2011-4354](https://nvd.nist.gov/vuln/detail/CVE-2011-4354)
1.  [CVE-2006-7250](https://nvd.nist.gov/vuln/detail/CVE-2006-7250)
1.  [CVE-2012-0884](https://nvd.nist.gov/vuln/detail/CVE-2012-0884)
1.  [CVE-2012-1165](https://nvd.nist.gov/vuln/detail/CVE-2012-1165)
1.  [CVE-2012-2110](https://nvd.nist.gov/vuln/detail/CVE-2012-2110)
1.  [CVE-2012-2131](https://nvd.nist.gov/vuln/detail/CVE-2012-2131)
1.  [CVE-2012-2333](https://nvd.nist.gov/vuln/detail/CVE-2012-2333)
1.  [CVE-2011-1473](https://nvd.nist.gov/vuln/detail/CVE-2011-1473)
1.  [CVE-2011-5095](https://nvd.nist.gov/vuln/detail/CVE-2011-5095)
1.  [CVE-2012-2686](https://nvd.nist.gov/vuln/detail/CVE-2012-2686)
1.  [CVE-2013-6449](https://nvd.nist.gov/vuln/detail/CVE-2013-6449)
1.  [CVE-2013-6450](https://nvd.nist.gov/vuln/detail/CVE-2013-6450)
1.  [CVE-2013-4353](https://nvd.nist.gov/vuln/detail/CVE-2013-4353)
1.  [CVE-2014-0076](https://nvd.nist.gov/vuln/detail/CVE-2014-0076)
1.  [CVE-2014-0160](https://nvd.nist.gov/vuln/detail/CVE-2014-0160)
1.  [CVE-2010-5298](https://nvd.nist.gov/vuln/detail/CVE-2010-5298)
1.  [CVE-2014-0198](https://nvd.nist.gov/vuln/detail/CVE-2014-0198)
1.  [CVE-2014-0221](https://nvd.nist.gov/vuln/detail/CVE-2014-0221)
1.  [CVE-2014-3507](https://nvd.nist.gov/vuln/detail/CVE-2014-3507)
1.  [CVE-2014-3566](https://nvd.nist.gov/vuln/detail/CVE-2014-3566)
1.  [CVE-2014-3513](https://nvd.nist.gov/vuln/detail/CVE-2014-3513)
1.  [CVE-2014-3569](https://nvd.nist.gov/vuln/detail/CVE-2014-3569)
1.  [CVE-2015-0206](https://nvd.nist.gov/vuln/detail/CVE-2015-0206)
1.  [CVE-2015-0208](https://nvd.nist.gov/vuln/detail/CVE-2015-0208)
1.  [CVE-2015-4000](https://nvd.nist.gov/vuln/detail/CVE-2015-4000)
1.  [CVE-2014-8176](https://nvd.nist.gov/vuln/detail/CVE-2014-8176)
1.  [CVE-2015-3216](https://nvd.nist.gov/vuln/detail/CVE-2015-3216)
1.  [CVE-2015-1793](https://nvd.nist.gov/vuln/detail/CVE-2015-1793)
1.  [CVE-2015-1794](https://nvd.nist.gov/vuln/detail/CVE-2015-1794)
1.  [CVE-2015-3197](https://nvd.nist.gov/vuln/detail/CVE-2015-3197)
1.  [CVE-2016-0800](https://nvd.nist.gov/vuln/detail/CVE-2016-0800)
1.  [CVE-2016-0704](https://nvd.nist.gov/vuln/detail/CVE-2016-0704)
1.  [CVE-2016-0799](https://nvd.nist.gov/vuln/detail/CVE-2016-0799)
1.  [CVE-2016-2176](https://nvd.nist.gov/vuln/detail/CVE-2016-2176)
1.  [CVE-2016-2177](https://nvd.nist.gov/vuln/detail/CVE-2016-2177)
1.  [CVE-2016-2180](https://nvd.nist.gov/vuln/detail/CVE-2016-2180)
1.  [CVE-2016-2183](https://nvd.nist.gov/vuln/detail/CVE-2016-2183)
1.  [CVE-2016-2181](https://nvd.nist.gov/vuln/detail/CVE-2016-2181)
1.  [CVE-2016-6309](https://nvd.nist.gov/vuln/detail/CVE-2016-6309)
1.  [CVE-2016-7798](https://nvd.nist.gov/vuln/detail/CVE-2016-7798)
1.  [CVE-2016-7053](https://nvd.nist.gov/vuln/detail/CVE-2016-7053)
1.  [CVE-2017-3735](https://nvd.nist.gov/vuln/detail/CVE-2017-3735)
1.  [CVE-2017-3736](https://nvd.nist.gov/vuln/detail/CVE-2017-3736)
1.  [CVE-2016-8610](https://nvd.nist.gov/vuln/detail/CVE-2016-8610)
1.  [CVE-2017-3738](https://nvd.nist.gov/vuln/detail/CVE-2017-3738)
1.  [CVE-2018-0739](https://nvd.nist.gov/vuln/detail/CVE-2018-0739)
1.  [CVE-2018-0737](https://nvd.nist.gov/vuln/detail/CVE-2018-0737)
1.  [CVE-2018-0732](https://nvd.nist.gov/vuln/detail/CVE-2018-0732)
1.  [CVE-2016-7056](https://nvd.nist.gov/vuln/detail/CVE-2016-7056)
1.  [CVE-2018-0735](https://nvd.nist.gov/vuln/detail/CVE-2018-0735)
1.  [CVE-2018-0734](https://nvd.nist.gov/vuln/detail/CVE-2018-0734)
1.  [CVE-2018-5407](https://nvd.nist.gov/vuln/detail/CVE-2018-5407)
1.  [CVE-2018-16395](https://nvd.nist.gov/vuln/detail/CVE-2018-16395)
1.  [CVE-2019-0190](https://nvd.nist.gov/vuln/detail/CVE-2019-0190)
1.  [CVE-2019-1559](https://nvd.nist.gov/vuln/detail/CVE-2019-1559)
1.  [CVE-2019-1543](https://nvd.nist.gov/vuln/detail/CVE-2019-1543)
1.  [CVE-2019-1552](https://nvd.nist.gov/vuln/detail/CVE-2019-1552)
1.  [CVE-2018-20997](https://nvd.nist.gov/vuln/detail/CVE-2018-20997)
1.  [CVE-2019-1547](https://nvd.nist.gov/vuln/detail/CVE-2019-1547)
1.  [CVE-2019-1551](https://nvd.nist.gov/vuln/detail/CVE-2019-1551)
1.  [CVE-2020-7043](https://nvd.nist.gov/vuln/detail/CVE-2020-7043)
1.  [CVE-2020-1967](https://nvd.nist.gov/vuln/detail/CVE-2020-1967)
1.  [CVE-2020-1968](https://nvd.nist.gov/vuln/detail/CVE-2020-1968)
1.  [CVE-2020-1971](https://nvd.nist.gov/vuln/detail/CVE-2020-1971)
1.  [CVE-2021-23839](https://nvd.nist.gov/vuln/detail/CVE-2021-23839)
1.  [CVE-2021-3449](https://nvd.nist.gov/vuln/detail/CVE-2021-3449)


