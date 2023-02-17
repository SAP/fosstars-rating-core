# curl/curl

https://github.com/curl/curl

Last updated on Feb 17, 2023

**Rating**: **GOOD**

**Score**: **5.55**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **4.77** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **5.26** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **7.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **4.77** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **5.26** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **0.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can open a pull request to enable AddressSanitizer for the project.
More info:
1.  [About AddressSanitizer](https://github.com/google/sanitizers/wiki/AddressSanitizer)


You can open a pull request to enable MemorySanitizer for the project.
More info:
1.  [About MemorySanitizer](https://github.com/google/sanitizers/wiki/MemorySanitizer)


You can open a pull request to enable UndefinedBehaviorSanitizer for the project.
More info:
1.  [About UndefinedBehaviorSanitizer](https://clang.llvm.org/docs/UndefinedBehaviorSanitizer.html)


You can enable artifact signing in the project's build pipeline.
More info:
1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)


You can enable NoHttp tool in the project's build pipeline.
More info:
1.  [NoHttp tool home page](https://github.com/spring-io/nohttp)


You can open a pull request to run Bandit scans in the project using GitHub action workflow.
More info:
1.  [GitHub workflow action job config to run Bandit code scanning for a repository.](https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idstepsrun)
1.  [An example to run Bandit scan check as part of GitHub action workflow.](https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L28)


It would be good to have SecGo as analysis step for all commits, yes initiating it with PR would be great.
More info:
1.  [GitHub workflow action config to run Bandit code scanning job on every PR of a project.](https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#example-using-a-list-of-events)
1.  [An eample to trigger Bandit scan check on every pull-request.](https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L3)


You can open a pull request to trigger GoSec scans job in the project using GitHub action workflow for every pull-request.
More info:
1.  [GitHub workflow action config to run GoSec code scanning job on every PR of a project.](https://github.com/securego/gosec#github-action)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **4.77**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **5.26** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **7.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** Yes
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** No
1.  **Does it have executable binaries?** No
1.  **Does it sign artifacts?** No
1.  **Does it use AddressSanitizer?** No
1.  **Does it use Dependabot?** No
1.  **Does it use FindSecBugs?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use OWASP ESAPI?** No
1.  **Does it use OWASP Java Encoder?** No
1.  **Does it use OWASP Java HTML Sanitizer?** No
1.  **Does it use Snyk?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Does it use nohttp?** No
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** Yes


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **4.77** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **5.26** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 101 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 101 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

396 commits in the last 3 months results to 10.00 points
7 contributors increase the score value from 10.00 to 12.00

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 396
1.  **Number of contributors in the last three months:** 7


### Project popularity

Score: **10.0**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 28479
1.  **Number of watchers for a GitHub repository:** 753


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    
1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **5.26**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
    


### Fuzzing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** Yes
1.  **Programming languages:** C, PYTHON, OTHER


### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C, PYTHON, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** None


### Dependabot score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** No
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** None
1.  **Programming languages:** C, PYTHON, OTHER


### Snyk score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** None
1.  **Programming languages:** C, PYTHON, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** None
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Programming languages:** C, PYTHON, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** C, PYTHON, OTHER


### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C, PYTHON, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** C, PYTHON, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** C, PYTHON, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** C, PYTHON, OTHER


## Known vulnerabilities

1.  [CVE-2000-0973](https://nvd.nist.gov/vuln/detail/CVE-2000-0973)
1.  [CVE-2005-0490](https://nvd.nist.gov/vuln/detail/CVE-2005-0490)
1.  [CVE-2005-3185](https://nvd.nist.gov/vuln/detail/CVE-2005-3185)
1.  [CVE-2005-4077](https://nvd.nist.gov/vuln/detail/CVE-2005-4077)
1.  [CVE-2006-1061](https://nvd.nist.gov/vuln/detail/CVE-2006-1061)
1.  [CVE-2009-0037](https://nvd.nist.gov/vuln/detail/CVE-2009-0037)
1.  [CVE-2010-3842](https://nvd.nist.gov/vuln/detail/CVE-2010-3842)
1.  [CVE-2011-3389](https://nvd.nist.gov/vuln/detail/CVE-2011-3389)
1.  [CVE-2012-0036](https://nvd.nist.gov/vuln/detail/CVE-2012-0036)
1.  [CVE-2013-0249](https://nvd.nist.gov/vuln/detail/CVE-2013-0249)
1.  [CVE-2013-2617](https://nvd.nist.gov/vuln/detail/CVE-2013-2617)
1.  [CVE-2013-1944](https://nvd.nist.gov/vuln/detail/CVE-2013-1944)
1.  [CVE-2013-2174](https://nvd.nist.gov/vuln/detail/CVE-2013-2174)
1.  [CVE-2013-4545](https://nvd.nist.gov/vuln/detail/CVE-2013-4545)
1.  [CVE-2014-0015](https://nvd.nist.gov/vuln/detail/CVE-2014-0015)
1.  [CVE-2014-0139](https://nvd.nist.gov/vuln/detail/CVE-2014-0139)
1.  [CVE-2014-2522](https://nvd.nist.gov/vuln/detail/CVE-2014-2522)
1.  [CVE-2014-3620](https://nvd.nist.gov/vuln/detail/CVE-2014-3620)
1.  [CVE-2015-3145](https://nvd.nist.gov/vuln/detail/CVE-2015-3145)
1.  [CVE-2015-3153](https://nvd.nist.gov/vuln/detail/CVE-2015-3153)
1.  [CVE-2015-3237](https://nvd.nist.gov/vuln/detail/CVE-2015-3237)
1.  [CVE-2016-0755](https://nvd.nist.gov/vuln/detail/CVE-2016-0755)
1.  [CVE-2016-3739](https://nvd.nist.gov/vuln/detail/CVE-2016-3739)
1.  [CVE-2016-4802](https://nvd.nist.gov/vuln/detail/CVE-2016-4802)
1.  [CVE-2017-7407](https://nvd.nist.gov/vuln/detail/CVE-2017-7407)
1.  [CVE-2017-9502](https://nvd.nist.gov/vuln/detail/CVE-2017-9502)
1.  [CVE-2017-1000101](https://nvd.nist.gov/vuln/detail/CVE-2017-1000101)
1.  [CVE-2017-8816](https://nvd.nist.gov/vuln/detail/CVE-2017-8816)
1.  [CVE-2018-1000007](https://nvd.nist.gov/vuln/detail/CVE-2018-1000007)
1.  [CVE-2017-2628](https://nvd.nist.gov/vuln/detail/CVE-2017-2628)
1.  [CVE-2018-1000121](https://nvd.nist.gov/vuln/detail/CVE-2018-1000121)
1.  [CVE-2016-9586](https://nvd.nist.gov/vuln/detail/CVE-2016-9586)
1.  [CVE-2018-1000300](https://nvd.nist.gov/vuln/detail/CVE-2018-1000300)
1.  [CVE-2018-0500](https://nvd.nist.gov/vuln/detail/CVE-2018-0500)
1.  [CVE-2017-2629](https://nvd.nist.gov/vuln/detail/CVE-2017-2629)
1.  [CVE-2016-8624](https://nvd.nist.gov/vuln/detail/CVE-2016-8624)
1.  [CVE-2016-8623](https://nvd.nist.gov/vuln/detail/CVE-2016-8623)
1.  [CVE-2003-1605](https://nvd.nist.gov/vuln/detail/CVE-2003-1605)
1.  [CVE-2018-16842](https://nvd.nist.gov/vuln/detail/CVE-2018-16842)
1.  [CVE-2019-5435](https://nvd.nist.gov/vuln/detail/CVE-2019-5435)
1.  [CVE-2019-5443](https://nvd.nist.gov/vuln/detail/CVE-2019-5443)
1.  [CVE-2019-5481](https://nvd.nist.gov/vuln/detail/CVE-2019-5481)
1.  [CVE-2016-4606](https://nvd.nist.gov/vuln/detail/CVE-2016-4606)
1.  [CVE-2020-8284](https://nvd.nist.gov/vuln/detail/CVE-2020-8284)
1.  [CVE-2021-22901](https://nvd.nist.gov/vuln/detail/CVE-2021-22901)
1.  [CVE-2021-22922](https://nvd.nist.gov/vuln/detail/CVE-2021-22922)
1.  [CVE-2021-22946](https://nvd.nist.gov/vuln/detail/CVE-2021-22946)
1.  [CVE-2022-22576](https://nvd.nist.gov/vuln/detail/CVE-2022-22576)
1.  [CVE-2022-27779](https://nvd.nist.gov/vuln/detail/CVE-2022-27779)
1.  [CVE-2022-32205](https://nvd.nist.gov/vuln/detail/CVE-2022-32205)
1.  [CVE-2022-35252](https://nvd.nist.gov/vuln/detail/CVE-2022-35252)
1.  [CVE-2022-42915](https://nvd.nist.gov/vuln/detail/CVE-2022-42915)
1.  [CVE-2022-35260](https://nvd.nist.gov/vuln/detail/CVE-2022-35260)
1.  [CVE-2022-43551](https://nvd.nist.gov/vuln/detail/CVE-2022-43551)


