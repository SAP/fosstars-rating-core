# apache/struts

https://github.com/apache/struts

Last updated on Feb 16, 2023

**Rating**: **GOOD**

**Score**: **6.38**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **7.91** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **7.41** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **5.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **8.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **7.91** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **7.41** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **7.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **6.94** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **1.61** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can set a CVSS threshold for vulnerabilities reported by OWASP Dependency Check.
More info:
1.  [OWASP Dependnecy Check](https://jeremylong.github.io/DependencyCheck/)
1.  [Configuring OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html)


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


You can create Snyk account and configure your project.
More info:
1.  [Getting started with snyk for open source](https://docs.snyk.io/products/snyk-open-source/getting-started-snyk-open-source)


You can open a pull request to trigger GoSec scans job in the project using GitHub action workflow for every pull-request.
More info:
1.  [GitHub workflow action config to run GoSec code scanning job on every PR of a project.](https://github.com/securego/gosec#github-action)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **7.91**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **7.41** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **5.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** Yes
1.  **Does it have executable binaries?** Yes
1.  **Does it sign artifacts?** No
1.  **Does it use AddressSanitizer?** No
1.  **Does it use Dependabot?** Yes
1.  **Does it use FindSecBugs?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use OWASP ESAPI?** No
1.  **Does it use OWASP Java Encoder?** No
1.  **Does it use OWASP Java HTML Sanitizer?** No
1.  **Does it use Snyk?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Does it use nohttp?** No
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Mandatory
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **8.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **7.91** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **7.41** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 81 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 81 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **7.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** Yes
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **6.94**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

41 commits in the last 3 months results to 6.31 points
3 contributors increase the score value from 6.31 to 6.94

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 41
1.  **Number of contributors in the last three months:** 3


### Project popularity

Score: **1.61**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 1190
1.  **Number of watchers for a GitHub repository:** 125


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
    
1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
    


### Static analysis

Score: **7.41**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
    


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** MAVEN


### Dependabot score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** Yes
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** MAVEN
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** MAVEN
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### OWASP Dependency Check score

Score: **7.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Mandatory
1.  **Package managers:** MAVEN
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### Bandit score

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)



The score is N/A because the project uses languages that are not supported by Bandit.

This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### How a project uses Pylint

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)



The score is N/A because the project uses languages that are not supported by Pylint.

This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


### How a project uses MyPy

Score: **N/A**, confidence is 10.0 (max), weight is 0.2 (low)



The score is N/A because the project uses languages that are not supported by MyPy.

This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** JAVA, JAVASCRIPT, OTHER


## Known vulnerabilities

1.  [CVE-2005-3745](https://nvd.nist.gov/vuln/detail/CVE-2005-3745)
1.  [CVE-2006-1547](https://nvd.nist.gov/vuln/detail/CVE-2006-1547)
1.  [CVE-2008-6504](https://nvd.nist.gov/vuln/detail/CVE-2008-6504)
1.  [CVE-2008-2025](https://nvd.nist.gov/vuln/detail/CVE-2008-2025)
1.  [CVE-2010-1870](https://nvd.nist.gov/vuln/detail/CVE-2010-1870)
1.  [CVE-2011-2088](https://nvd.nist.gov/vuln/detail/CVE-2011-2088)
1.  [CVE-2012-0392](https://nvd.nist.gov/vuln/detail/CVE-2012-0392)
1.  [CVE-2012-1006](https://nvd.nist.gov/vuln/detail/CVE-2012-1006)
1.  [CVE-2012-0838](https://nvd.nist.gov/vuln/detail/CVE-2012-0838)
1.  [CVE-2012-4387](https://nvd.nist.gov/vuln/detail/CVE-2012-4387)
1.  [CVE-2013-1965](https://nvd.nist.gov/vuln/detail/CVE-2013-1965)
1.  [CVE-2013-2135](https://nvd.nist.gov/vuln/detail/CVE-2013-2135)
1.  [CVE-2013-2251](https://nvd.nist.gov/vuln/detail/CVE-2013-2251)
1.  [CVE-2013-4316](https://nvd.nist.gov/vuln/detail/CVE-2013-4316)
1.  [CVE-2013-6348](https://nvd.nist.gov/vuln/detail/CVE-2013-6348)
1.  [CVE-2014-0094](https://nvd.nist.gov/vuln/detail/CVE-2014-0094)
1.  [CVE-2014-0112](https://nvd.nist.gov/vuln/detail/CVE-2014-0112)
1.  [CVE-2014-0114](https://nvd.nist.gov/vuln/detail/CVE-2014-0114)
1.  [CVE-2014-0116](https://nvd.nist.gov/vuln/detail/CVE-2014-0116)
1.  [CVE-2014-7809](https://nvd.nist.gov/vuln/detail/CVE-2014-7809)
1.  [CVE-2015-1831](https://nvd.nist.gov/vuln/detail/CVE-2015-1831)
1.  [CVE-2016-4003](https://nvd.nist.gov/vuln/detail/CVE-2016-4003)
1.  [CVE-2016-3081](https://nvd.nist.gov/vuln/detail/CVE-2016-3081)
1.  [CVE-2016-3087](https://nvd.nist.gov/vuln/detail/CVE-2016-3087)
1.  [CVE-2016-1182](https://nvd.nist.gov/vuln/detail/CVE-2016-1182)
1.  [CVE-2016-4436](https://nvd.nist.gov/vuln/detail/CVE-2016-4436)
1.  [CVE-2017-5638](https://nvd.nist.gov/vuln/detail/CVE-2017-5638)
1.  [CVE-2017-9791](https://nvd.nist.gov/vuln/detail/CVE-2017-9791)
1.  [CVE-2017-9787](https://nvd.nist.gov/vuln/detail/CVE-2017-9787)
1.  [CVE-2015-5209](https://nvd.nist.gov/vuln/detail/CVE-2015-5209)
1.  [CVE-2017-9805](https://nvd.nist.gov/vuln/detail/CVE-2017-9805)
1.  [CVE-2016-6795](https://nvd.nist.gov/vuln/detail/CVE-2016-6795)
1.  [CVE-2015-5169](https://nvd.nist.gov/vuln/detail/CVE-2015-5169)
1.  [CVE-2016-4461](https://nvd.nist.gov/vuln/detail/CVE-2016-4461)
1.  [CVE-2016-3090](https://nvd.nist.gov/vuln/detail/CVE-2016-3090)
1.  [CVE-2017-15707](https://nvd.nist.gov/vuln/detail/CVE-2017-15707)
1.  [CVE-2018-1327](https://nvd.nist.gov/vuln/detail/CVE-2018-1327)
1.  [CVE-2018-11776](https://nvd.nist.gov/vuln/detail/CVE-2018-11776)
1.  [CVE-2011-3923](https://nvd.nist.gov/vuln/detail/CVE-2011-3923)
1.  [CVE-2012-1592](https://nvd.nist.gov/vuln/detail/CVE-2012-1592)
1.  [CVE-2015-2992](https://nvd.nist.gov/vuln/detail/CVE-2015-2992)
1.  [CVE-2019-0230](https://nvd.nist.gov/vuln/detail/CVE-2019-0230)
1.  [CVE-2020-17530](https://nvd.nist.gov/vuln/detail/CVE-2020-17530)
1.  [CVE-2021-31805](https://nvd.nist.gov/vuln/detail/CVE-2021-31805)


