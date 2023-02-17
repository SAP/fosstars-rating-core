# apache/linkis

https://github.com/apache/linkis

Last updated on Feb 17, 2023

**Rating**: **GOOD**

**Score**: **5.84**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
            
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
        
1.  **[Security awareness](#security-awareness)**: **6.5** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
                
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
    
1.  **[Project activity](#project-activity)**: **9.21** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **3.84** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can add OWASP Dependency Check to the project's build pipeline.
More info:
1.  [OWASP Dependnecy Check](https://jeremylong.github.io/DependencyCheck/)
1.  [How to use OWASP Dependency Check with Maven](https://jeremylong.github.io/DependencyCheck/dependency-check-maven)
1.  [How to use OWASP Dependnecy Check with Gradle](https://github.com/dependency-check/dependency-check-gradle)


You can set a CVSS threshold for vulnerabilities reported by OWASP Dependency Check.
More info:
1.  [OWASP Dependnecy Check](https://jeremylong.github.io/DependencyCheck/)
1.  [Configuring OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html)


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
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
        
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

Score: **6.5**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** Yes
1.  **Does it have executable binaries?** No
1.  **Does it sign artifacts?** Yes
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
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **6.57** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
            
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



1.  **Info about vulnerabilities in the project:** 17 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 17 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **7.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** Yes
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **9.21**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

57 commits in the last 3 months results to 8.77 points
2 contributors increase the score value from 8.77 to 9.21

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 57
1.  **Number of contributors in the last three months:** 2


### Project popularity

Score: **3.84**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 2950
1.  **Number of watchers for a GitHub repository:** 266


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
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
    


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
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


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
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** MAVEN, NPM, YARN
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### OWASP Dependency Check score

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** MAVEN, NPM, YARN
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** JAVA, SCALA, PYTHON, JAVASCRIPT, OTHER


## Known vulnerabilities

1.  [CVE-2002-2060](https://nvd.nist.gov/vuln/detail/CVE-2002-2060): Buffer overflow in Links 2.0 pre4 allows remote attackers to crash client browsers and possibly execute arbitrary code via gamma tables in large 16-bit PNG images.
1.  [CVE-2002-1405](https://nvd.nist.gov/vuln/detail/CVE-2002-1405): CRLF injection vulnerability in Lynx 2.8.4 and earlier allows remote attackers to inject false HTTP headers into an HTTP request that is provided on the command line, via a URL containing encoded carriage return, line feed, and other whitespace characters.
1.  [CVE-2004-1616](https://nvd.nist.gov/vuln/detail/CVE-2004-1616): Links allows remote attackers to cause a denial of service (memory consumption) via a web page or HTML email that contains a table with a td element and a large rowspan value,as demonstrated by mangleme.
1.  [CVE-2006-5925](https://nvd.nist.gov/vuln/detail/CVE-2006-5925): Links web browser 1.00pre12 and Elinks 0.9.2 with smbclient installed allows remote attackers to execute arbitrary code via shell metacharacters in an smb:// URI, as demonstrated by using PUT and GET statements.
1.  [CVE-2008-3319](https://nvd.nist.gov/vuln/detail/CVE-2008-3319): admin/index.php in Maian Links 3.1 and earlier allows remote attackers to bypass authentication and gain administrative access by sending an arbitrary links_cookie cookie.
1.  [CVE-2008-3329](https://nvd.nist.gov/vuln/detail/CVE-2008-3329): Unspecified vulnerability in Links before 2.1, when "only proxies" is enabled, has unknown impact and attack vectors related to providing "URLs to external programs."
1.  [CVE-2008-3555](https://nvd.nist.gov/vuln/detail/CVE-2008-3555): Directory traversal vulnerability in index.php in (1) WSN Forum 4.1.43 and earlier, (2) Gallery 4.1.30 and earlier, (3) Knowledge Base (WSNKB) 4.1.36 and earlier, (4) Links 4.1.44 and earlier, and possibly (5) Classifieds before 4.1.30 allows remote attackers to include and execute arbitrary local files via a .. (dot dot) in the TID parameter, as demonstrated by uploading a .jpg file containing PHP sequences.
1.  [CVE-2008-6032](https://nvd.nist.gov/vuln/detail/CVE-2008-6032): SQL injection vulnerability in comments.php in WSN Links Free 4.0.34P allows remote attackers to execute arbitrary SQL commands via the id parameter.
1.  [CVE-2009-3915](https://nvd.nist.gov/vuln/detail/CVE-2009-3915): Cross-site scripting (XSS) vulnerability in the "Separate title and URL" formatter in the Link module 5.x before 5.x-2.6 and 6.x before 6.x-2.7, a module for Drupal, allows remote attackers to inject arbitrary web script or HTML via the link title field.
1.  [CVE-2010-4006](https://nvd.nist.gov/vuln/detail/CVE-2010-4006): Multiple SQL injection vulnerabilities in search.php in WSN Links 5.0.x before 5.0.81, 5.1.x before 5.1.51, and 6.0.x before 6.0.1 allow remote attackers to execute arbitrary SQL commands via the (1) namecondition or (2) namesearch parameter.
1.  [CVE-2013-6050](https://nvd.nist.gov/vuln/detail/CVE-2013-6050): Integer overflow in Links before 2.8 allows remote attackers to cause a denial of service (crash) via crafted HTML tables.
1.  [CVE-2017-11114](https://nvd.nist.gov/vuln/detail/CVE-2017-11114): The put_chars function in html_r.c in Twibright Links 2.14 allows remote attackers to cause a denial of service (buffer over-read) via a crafted HTML file.
1.  [CVE-2012-6709](https://nvd.nist.gov/vuln/detail/CVE-2012-6709): ELinks 0.12 and Twibright Links 2.3 have Missing SSL Certificate Validation.
1.  [CVE-2018-19592](https://nvd.nist.gov/vuln/detail/CVE-2018-19592): The "CLink4Service" service is installed with Corsair Link 4.9.7.35 with insecure permissions by default. This allows unprivileged users to take control of the service and execute commands in the context of NT AUTHORITY\SYSTEM, leading to total system takeover, a similar issue to CVE-2018-12441.
1.  [CVE-2022-39944](https://nvd.nist.gov/vuln/detail/CVE-2022-39944): In Apache Linkis <=1.2.0 when used with the MySQL Connector/J, a deserialization vulnerability with possible remote code execution impact exists when an attacker has write access to a database and configures a JDBC EC with a MySQL data source and malicious parameters. Therefore, the parameters in the jdbc url should be blacklisted. Versions of Apache Linkis <= 1.2.0 will be affected, We recommend users to update to 1.3.0.
1.  [CVE-2022-44645](https://nvd.nist.gov/vuln/detail/CVE-2022-44645): In Apache Linkis <=1.3.0 when used with the MySQL Connector/J, a deserialization vulnerability with possible remote code execution impact exists when an attacker has write access to a database and configures new datasource with a MySQL data source and malicious parameters. Therefore, the parameters in the jdbc url should be blacklisted. Versions of Apache Linkis <= 1.3.0 will be affected. We recommend users to upgrade the version of Linkis to version 1.3.1.


