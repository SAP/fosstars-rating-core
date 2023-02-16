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

1.  [CVE-2005-3745](https://nvd.nist.gov/vuln/detail/CVE-2005-3745): Cross-site scripting (XSS) vulnerability in Apache Struts 1.2.7, and possibly other versions allows remote attackers to inject arbitrary web script or HTML via the query string, which is not properly quoted or filtered when the request handler generates an error message.
1.  [CVE-2006-1547](https://nvd.nist.gov/vuln/detail/CVE-2006-1547): ActionForm in Apache Software Foundation (ASF) Struts before 1.2.9 with BeanUtils 1.7 allows remote attackers to cause a denial of service via a multipart/form-data encoded form with a parameter name that references the public getMultipartRequestHandler method, which provides further access to elements in the CommonsMultipartRequestHandler implementation and BeanUtils.
1.  [CVE-2008-6504](https://nvd.nist.gov/vuln/detail/CVE-2008-6504): ParametersInterceptor in OpenSymphony XWork 2.0.x before 2.0.6 and 2.1.x before 2.1.2, as used in Apache Struts and other products, does not properly restrict # (pound sign) references to context objects, which allows remote attackers to execute Object-Graph Navigation Language (OGNL) statements and modify server-side context objects, as demonstrated by use of a \u0023 representation for the # character.
1.  [CVE-2008-2025](https://nvd.nist.gov/vuln/detail/CVE-2008-2025): Cross-site scripting (XSS) vulnerability in Apache Struts before 1.2.9-162.31.1 on SUSE Linux Enterprise (SLE) 11, before 1.2.9-108.2 on SUSE openSUSE 10.3, before 1.2.9-198.2 on SUSE openSUSE 11.0, and before 1.2.9-162.163.2 on SUSE openSUSE 11.1 allows remote attackers to inject arbitrary web script or HTML via unspecified vectors related to "insufficient quoting of parameters."
1.  [CVE-2010-1870](https://nvd.nist.gov/vuln/detail/CVE-2010-1870): The OGNL extensive expression evaluation capability in XWork in Struts 2.0.0 through 2.1.8.1, as used in Atlassian Fisheye, Crucible, and possibly other products, uses a permissive whitelist, which allows remote attackers to modify server-side context objects and bypass the "#" protection mechanism in ParameterInterceptors via the (1) #context, (2) #_memberAccess, (3) #root, (4) #this, (5) #_typeResolver, (6) #_classResolver, (7) #_traceEvaluations, (8) #_lastEvaluation, (9) #_keepLastEvaluation, and possibly other OGNL context variables, a different vulnerability than CVE-2008-6504.
1.  [CVE-2011-2088](https://nvd.nist.gov/vuln/detail/CVE-2011-2088): XWork 2.2.1 in Apache Struts 2.2.1, and OpenSymphony XWork in OpenSymphony WebWork, allows remote attackers to obtain potentially sensitive information about internal Java class paths via vectors involving an s:submit element and a nonexistent method, a different vulnerability than CVE-2011-1772.3.
1.  [CVE-2012-0392](https://nvd.nist.gov/vuln/detail/CVE-2012-0392): The CookieInterceptor component in Apache Struts before 2.3.1.1 does not use the parameter-name whitelist, which allows remote attackers to execute arbitrary commands via a crafted HTTP Cookie header that triggers Java code execution through a static method.
1.  [CVE-2012-1006](https://nvd.nist.gov/vuln/detail/CVE-2012-1006): Multiple cross-site scripting (XSS) vulnerabilities in Apache Struts 2.0.14 and 2.2.3 allow remote attackers to inject arbitrary web script or HTML via the (1) name or (2) lastName parameter to struts2-showcase/person/editPerson.action, or the (3) clientName parameter to struts2-rest-showcase/orders.
1.  [CVE-2012-0838](https://nvd.nist.gov/vuln/detail/CVE-2012-0838): Apache Struts 2 before 2.2.3.1 evaluates a string as an OGNL expression during the handling of a conversion error, which allows remote attackers to modify run-time data values, and consequently execute arbitrary code, via invalid input to a field.
1.  [CVE-2012-4387](https://nvd.nist.gov/vuln/detail/CVE-2012-4387): Apache Struts 2.0.0 through 2.3.4 allows remote attackers to cause a denial of service (CPU consumption) via a long parameter name, which is processed as an OGNL expression.
1.  [CVE-2013-1965](https://nvd.nist.gov/vuln/detail/CVE-2013-1965): Apache Struts Showcase App 2.0.0 through 2.3.13, as used in Struts 2 before 2.3.14.3, allows remote attackers to execute arbitrary OGNL code via a crafted parameter name that is not properly handled when invoking a redirect.
1.  [CVE-2013-2135](https://nvd.nist.gov/vuln/detail/CVE-2013-2135): Apache Struts 2 before 2.3.14.3 allows remote attackers to execute arbitrary OGNL code via a request with a crafted value that contains both "${}" and "%{}" sequences, which causes the OGNL code to be evaluated twice.
1.  [CVE-2013-2251](https://nvd.nist.gov/vuln/detail/CVE-2013-2251): Apache Struts 2.0.0 through 2.3.15 allows remote attackers to execute arbitrary OGNL expressions via a parameter with a crafted (1) action:, (2) redirect:, or (3) redirectAction: prefix.
1.  [CVE-2013-4316](https://nvd.nist.gov/vuln/detail/CVE-2013-4316): Apache Struts 2.0.0 through 2.3.15.1 enables Dynamic Method Invocation by default, which has unknown impact and attack vectors.
1.  [CVE-2013-6348](https://nvd.nist.gov/vuln/detail/CVE-2013-6348): Multiple cross-site scripting (XSS) vulnerabilities in Apache Struts 2.3.15.3 allow remote attackers to inject arbitrary web script or HTML via the namespace parameter to (1) actionNames.action and (2) showConfig.action in config-browser/.
1.  [CVE-2014-0094](https://nvd.nist.gov/vuln/detail/CVE-2014-0094): The ParametersInterceptor in Apache Struts before 2.3.16.2 allows remote attackers to "manipulate" the ClassLoader via the class parameter, which is passed to the getClass method.
1.  [CVE-2014-0112](https://nvd.nist.gov/vuln/detail/CVE-2014-0112): ParametersInterceptor in Apache Struts before 2.3.20 does not properly restrict access to the getClass method, which allows remote attackers to "manipulate" the ClassLoader and execute arbitrary code via a crafted request. NOTE: this vulnerability exists because of an incomplete fix for CVE-2014-0094.
1.  [CVE-2014-0114](https://nvd.nist.gov/vuln/detail/CVE-2014-0114): Apache Commons BeanUtils, as distributed in lib/commons-beanutils-1.8.0.jar in Apache Struts 1.x through 1.3.10 and in other products requiring commons-beanutils through 1.9.2, does not suppress the class property, which allows remote attackers to "manipulate" the ClassLoader and execute arbitrary code via the class parameter, as demonstrated by the passing of this parameter to the getClass method of the ActionForm object in Struts 1.
1.  [CVE-2014-0116](https://nvd.nist.gov/vuln/detail/CVE-2014-0116): CookieInterceptor in Apache Struts 2.x before 2.3.20, when a wildcard cookiesName value is used, does not properly restrict access to the getClass method, which allows remote attackers to "manipulate" the ClassLoader and modify session state via a crafted request. NOTE: this vulnerability exists because of an incomplete fix for CVE-2014-0113.
1.  [CVE-2014-7809](https://nvd.nist.gov/vuln/detail/CVE-2014-7809): Apache Struts 2.0.0 through 2.3.x before 2.3.20 uses predictable <s:token/> values, which allows remote attackers to bypass the CSRF protection mechanism.
1.  [CVE-2015-1831](https://nvd.nist.gov/vuln/detail/CVE-2015-1831): The default exclude patterns (excludeParams) in Apache Struts 2.3.20 allow remote attackers to "compromise internal state of an application" via unspecified vectors.
1.  [CVE-2016-4003](https://nvd.nist.gov/vuln/detail/CVE-2016-4003): Cross-site scripting (XSS) vulnerability in the URLDecoder function in JRE before 1.8, as used in Apache Struts 2.x before 2.3.28, when using a single byte page encoding, allows remote attackers to inject arbitrary web script or HTML via multi-byte characters in a url-encoded parameter.
1.  [CVE-2016-3081](https://nvd.nist.gov/vuln/detail/CVE-2016-3081): Apache Struts 2.3.19 to 2.3.20.2, 2.3.21 to 2.3.24.1, and 2.3.25 to 2.3.28, when Dynamic Method Invocation is enabled, allow remote attackers to execute arbitrary code via method: prefix, related to chained expressions.
1.  [CVE-2016-3087](https://nvd.nist.gov/vuln/detail/CVE-2016-3087): Apache Struts 2.3.19 to 2.3.20.2, 2.3.21 to 2.3.24.1, and 2.3.25 to 2.3.28, when Dynamic Method Invocation is enabled, allow remote attackers to execute arbitrary code via vectors related to an ! (exclamation mark) operator to the REST Plugin.
1.  [CVE-2016-1182](https://nvd.nist.gov/vuln/detail/CVE-2016-1182): ActionServlet.java in Apache Struts 1 1.x through 1.3.10 does not properly restrict the Validator configuration, which allows remote attackers to conduct cross-site scripting (XSS) attacks or cause a denial of service via crafted input, a related issue to CVE-2015-0899.
1.  [CVE-2016-4436](https://nvd.nist.gov/vuln/detail/CVE-2016-4436): Apache Struts 2 before 2.3.29 and 2.5.x before 2.5.1 allow attackers to have unspecified impact via vectors related to improper action name clean up.
1.  [CVE-2017-5638](https://nvd.nist.gov/vuln/detail/CVE-2017-5638): The Jakarta Multipart parser in Apache Struts 2 2.3.x before 2.3.32 and 2.5.x before 2.5.10.1 has incorrect exception handling and error-message generation during file-upload attempts, which allows remote attackers to execute arbitrary commands via a crafted Content-Type, Content-Disposition, or Content-Length HTTP header, as exploited in the wild in March 2017 with a Content-Type header containing a #cmd= string.
1.  [CVE-2017-9791](https://nvd.nist.gov/vuln/detail/CVE-2017-9791): The Struts 1 plugin in Apache Struts 2.1.x and 2.3.x might allow remote code execution via a malicious field value passed in a raw message to the ActionMessage.
1.  [CVE-2017-9787](https://nvd.nist.gov/vuln/detail/CVE-2017-9787): When using a Spring AOP functionality to secure Struts actions it is possible to perform a DoS attack. Solution is to upgrade to Apache Struts version 2.5.12 or 2.3.33.
1.  [CVE-2015-5209](https://nvd.nist.gov/vuln/detail/CVE-2015-5209): Apache Struts 2.x before 2.3.24.1 allows remote attackers to manipulate Struts internals, alter user sessions, or affect container settings via vectors involving a top object.
1.  [CVE-2017-9805](https://nvd.nist.gov/vuln/detail/CVE-2017-9805): The REST Plugin in Apache Struts 2.1.1 through 2.3.x before 2.3.34 and 2.5.x before 2.5.13 uses an XStreamHandler with an instance of XStream for deserialization without any type filtering, which can lead to Remote Code Execution when deserializing XML payloads.
1.  [CVE-2016-6795](https://nvd.nist.gov/vuln/detail/CVE-2016-6795): In the Convention plugin in Apache Struts 2.3.x before 2.3.31, and 2.5.x before 2.5.5, it is possible to prepare a special URL which will be used for path traversal and execution of arbitrary code on server side.
1.  [CVE-2015-5169](https://nvd.nist.gov/vuln/detail/CVE-2015-5169): Cross-site scripting (XSS) vulnerability in Apache Struts before 2.3.20.
1.  [CVE-2016-4461](https://nvd.nist.gov/vuln/detail/CVE-2016-4461): Apache Struts 2.x before 2.3.29 allows remote attackers to execute arbitrary code via a "%{}" sequence in a tag attribute, aka forced double OGNL evaluation.  NOTE: this vulnerability exists because of an incomplete fix for CVE-2016-0785.
1.  [CVE-2016-3090](https://nvd.nist.gov/vuln/detail/CVE-2016-3090): The TextParseUtil.translateVariables method in Apache Struts 2.x before 2.3.20 allows remote attackers to execute arbitrary code via a crafted OGNL expression with ANTLR tooling.
1.  [CVE-2017-15707](https://nvd.nist.gov/vuln/detail/CVE-2017-15707): In Apache Struts 2.5 to 2.5.14, the REST Plugin is using an outdated JSON-lib library which is vulnerable and allow perform a DoS attack using malicious request with specially crafted JSON payload.
1.  [CVE-2018-1327](https://nvd.nist.gov/vuln/detail/CVE-2018-1327): The Apache Struts REST Plugin is using XStream library which is vulnerable and allow perform a DoS attack when using a malicious request with specially crafted XML payload. Upgrade to the Apache Struts version 2.5.16 and switch to an optional Jackson XML handler as described here http://struts.apache.org/plugins/rest/#custom-contenttypehandlers. Another option is to implement a custom XML handler based on the Jackson XML handler from the Apache Struts 2.5.16.
1.  [CVE-2018-11776](https://nvd.nist.gov/vuln/detail/CVE-2018-11776): Apache Struts versions 2.3 to 2.3.34 and 2.5 to 2.5.16 suffer from possible Remote Code Execution when alwaysSelectFullNamespace is true (either by user or a plugin like Convention Plugin) and then: results are used with no namespace and in same time, its upper package have no or wildcard namespace and similar to results, same possibility when using url tag which doesn't have value and action set and in same time, its upper package have no or wildcard namespace.
1.  [CVE-2011-3923](https://nvd.nist.gov/vuln/detail/CVE-2011-3923): Apache Struts before 2.3.1.2 allows remote attackers to bypass security protections in the ParameterInterceptor class and execute arbitrary commands.
1.  [CVE-2012-1592](https://nvd.nist.gov/vuln/detail/CVE-2012-1592): A local code execution issue exists in Apache Struts2 when processing malformed XSLT files, which could let a malicious user upload and execute arbitrary files.
1.  [CVE-2015-2992](https://nvd.nist.gov/vuln/detail/CVE-2015-2992): Apache Struts before 2.3.20 has a cross-site scripting (XSS) vulnerability.
1.  [CVE-2019-0230](https://nvd.nist.gov/vuln/detail/CVE-2019-0230): Apache Struts 2.0.0 to 2.5.20 forced double OGNL evaluation, when evaluated on raw user input in tag attributes, may lead to remote code execution.
1.  [CVE-2020-17530](https://nvd.nist.gov/vuln/detail/CVE-2020-17530): Forced OGNL evaluation, when evaluated on raw user input in tag attributes, may lead to remote code execution. Affected software : Apache Struts 2.0.0 - Struts 2.5.25.
1.  [CVE-2021-31805](https://nvd.nist.gov/vuln/detail/CVE-2021-31805): The fix issued for CVE-2020-17530 was incomplete. So from Apache Struts 2.0.0 to 2.5.29, still some of the tag’s attributes could perform a double evaluation if a developer applied forced OGNL evaluation by using the %{...} syntax. Using forced OGNL evaluation on untrusted user input can lead to a Remote Code Execution and security degradation.


