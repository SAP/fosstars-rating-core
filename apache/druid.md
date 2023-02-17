# apache/druid

https://github.com/apache/druid

Last updated on Feb 17, 2023

**Rating**: **GOOD**

**Score**: **6.77**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.9** (weight is 1.0)
            
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
        
1.  **[Security awareness](#security-awareness)**: **7.5** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.9** (weight is 1.0)
                
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
    1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.9** (weight is 1.0)
        
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

Score: **7.5**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** Yes
1.  **Does it have executable binaries?** No
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
1.  **Does it use verified signed commits?** Yes
1.  **How is OWASP Dependency Check used?** Mandatory
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.9** (weight is 1.0)
            
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

214 commits in the last 3 months results to 10.00 points

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 214
1.  **Number of contributors in the last three months:** 1


### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 1
1.  **Number of stars for a GitHub repository:** 12394
1.  **Number of watchers for a GitHub repository:** 609


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
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **7.9** (weight is 1.0)
    


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
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** MAVEN, NPM, YARN


### Dependabot score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** Yes
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** MAVEN, NPM, YARN
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** MAVEN, NPM, YARN
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### OWASP Dependency Check score

Score: **7.9**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Mandatory
1.  **Package managers:** MAVEN, NPM, YARN
1.  **What is the threshold for OWASP Dependency Check?** 7.0


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, OTHER


## Known vulnerabilities

1.  [CVE-2020-1958](https://nvd.nist.gov/vuln/detail/CVE-2020-1958): When LDAP authentication is enabled in Apache Druid 0.17.0, callers of Druid APIs with a valid set of LDAP credentials can bypass the credentialsValidator.userSearch filter barrier that determines if a valid LDAP user is allowed to authenticate with Druid. They are still subject to role-based authorization checks, if configured. Callers of Druid APIs can also retrieve any LDAP attribute values of users that exist on the LDAP server, so long as that information is visible to the Druid server. This information disclosure does not require the caller itself to be a valid LDAP user.
1.  [CVE-2021-25646](https://nvd.nist.gov/vuln/detail/CVE-2021-25646): Apache Druid includes the ability to execute user-provided JavaScript code embedded in various types of requests. This functionality is intended for use in high-trust environments, and is disabled by default. However, in Druid 0.20.0 and earlier, it is possible for an authenticated user to send a specially-crafted request that forces Druid to run user-provided JavaScript code for that request, regardless of server configuration. This can be leveraged to execute code on the target machine with the privileges of the Druid server process.
1.  [CVE-2021-26919](https://nvd.nist.gov/vuln/detail/CVE-2021-26919): Apache Druid allows users to read data from other database systems using JDBC. This functionality is to allow trusted users with the proper permissions to set up lookups or submit ingestion tasks. The MySQL JDBC driver supports certain properties, which, if left unmitigated, can allow an attacker to execute arbitrary code from a hacker-controlled malicious MySQL server within Druid server processes. This issue was addressed in Apache Druid 0.20.2
1.  [CVE-2021-26920](https://nvd.nist.gov/vuln/detail/CVE-2021-26920): In the Druid ingestion system, the InputSource is used for reading data from a certain data source. However, the HTTP InputSource allows authenticated users to read data from other sources than intended, such as the local file system, with the privileges of the Druid server process. This is not an elevation of privilege when users access Druid directly, since Druid also provides the Local InputSource, which allows the same level of access. But it is problematic when users interact with Druid indirectly through an application that allows users to specify the HTTP InputSource, but not the Local InputSource. In this case, users could bypass the application-level restriction by passing a file URL to the HTTP InputSource.
1.  [CVE-2021-36749](https://nvd.nist.gov/vuln/detail/CVE-2021-36749): In the Druid ingestion system, the InputSource is used for reading data from a certain data source. However, the HTTP InputSource allows authenticated users to read data from other sources than intended, such as the local file system, with the privileges of the Druid server process. This is not an elevation of privilege when users access Druid directly, since Druid also provides the Local InputSource, which allows the same level of access. But it is problematic when users interact with Druid indirectly through an application that allows users to specify the HTTP InputSource, but not the Local InputSource. In this case, users could bypass the application-level restriction by passing a file URL to the HTTP InputSource. This issue was previously mentioned as being fixed in 0.21.0 as per CVE-2021-26920 but was not fixed in 0.21.0 or 0.21.1.
1.  [CVE-2021-33800](https://nvd.nist.gov/vuln/detail/CVE-2021-33800): In Druid 1.2.3, visiting the path with parameter in a certain function can lead to directory traversal.
1.  [CVE-2022-28889](https://nvd.nist.gov/vuln/detail/CVE-2022-28889): In Apache Druid 0.22.1 and earlier, the server did not set appropriate headers to prevent clickjacking. Druid 0.23.0 and later prevent clickjacking using the Content-Security-Policy header.


