# apache/dolphinscheduler

https://github.com/apache/dolphinscheduler

Last updated on Feb 17, 2023

**Rating**: **GOOD**

**Score**: **6.19**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **5.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **4.44** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **4.5** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **5.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **4.44** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **7.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can configure Dependabot by creating a configuration file.
More info:
1.  [Configuration options for dependency updates](https://docs.github.com/en/github/administering-a-repository/configuration-options-for-dependency-updates)


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

Score: **6.57**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **5.0** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **4.44** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **4.5**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** Yes
1.  **Does it have executable binaries?** Yes
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
1.  **Does it use verified signed commits?** Yes
1.  **How is OWASP Dependency Check used?** Mandatory
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **5.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **4.44** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 8 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 8 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **7.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** Yes
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

186 commits in the last 3 months results to 10.00 points

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 186
1.  **Number of contributors in the last three months:** 1


### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 2
1.  **Number of stars for a GitHub repository:** 9818
1.  **Number of watchers for a GitHub repository:** 318


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **5.0** (weight is 1.0)
    
1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.0** (weight is 1.0)
    


### Static analysis

Score: **4.44**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
    


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** MAVEN


### Dependabot score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** No
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** MAVEN
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** MAVEN
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


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
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** JAVA, PYTHON, TYPESCRIPT, OTHER


## Known vulnerabilities

1.  [CVE-2020-11974](https://nvd.nist.gov/vuln/detail/CVE-2020-11974)
1.  [CVE-2020-13922](https://nvd.nist.gov/vuln/detail/CVE-2020-13922)
1.  [CVE-2021-27644](https://nvd.nist.gov/vuln/detail/CVE-2021-27644)
1.  [CVE-2022-25598](https://nvd.nist.gov/vuln/detail/CVE-2022-25598)
1.  [CVE-2022-26884](https://nvd.nist.gov/vuln/detail/CVE-2022-26884)
1.  [CVE-2022-34662](https://nvd.nist.gov/vuln/detail/CVE-2022-34662)
1.  [CVE-2022-26885](https://nvd.nist.gov/vuln/detail/CVE-2022-26885)
1.  [CVE-2022-45875](https://nvd.nist.gov/vuln/detail/CVE-2022-45875)


