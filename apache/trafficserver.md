# apache/trafficserver

https://github.com/apache/trafficserver

Last updated on Feb 17, 2023

**Rating**: **MODERATE**

**Score**: **4.77**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **3.82** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **2.22** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **5.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **4.5** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **3.82** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **2.22** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **5.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **7.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **2.12** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable CodeQL scans in the project. Make sure that the scans are run on pull requests.
More info:
1.  [How to enable CodeQL checks for pull requests](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


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

Score: **3.82**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **2.22** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **5.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    
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
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** Yes


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **3.82** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **2.22** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **5.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 58 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 58 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **7.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** Yes
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

149 commits in the last 3 months results to 10.00 points
2 contributors increase the score value from 10.00 to 10.50

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 149
1.  **Number of contributors in the last three months:** 2


### Project popularity

Score: **2.12**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 1561
1.  **Number of watchers for a GitHub repository:** 167


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

Score: **2.22**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **5.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **N/A** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
    


### Fuzzing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** Yes
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


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
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### Snyk score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** None
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** None
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### GoSec score

Score: **N/A**, confidence is 10.0 (max), weight is 0.3 (medium)



The score is N/A because the project uses languages that are not supported by GoSec.

This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** C, CPP, JAVA, PYTHON, OTHER


## Known vulnerabilities

1.  [CVE-2002-1013](https://nvd.nist.gov/vuln/detail/CVE-2002-1013): Buffer overflow in traffic_manager for Inktomi Traffic Server 4.0.18 through 5.2.2, Traffic Edge 1.1.2 and 1.5.0, and Media-IXT 3.0.4 allows local users to gain root privileges via a long -path argument.
1.  [CVE-2010-2952](https://nvd.nist.gov/vuln/detail/CVE-2010-2952): Apache Traffic Server before 2.0.1, and 2.1.x before 2.1.2-unstable, does not properly choose DNS source ports and transaction IDs, and does not properly use DNS query fields to validate responses, which makes it easier for man-in-the-middle attackers to poison the internal DNS cache via a crafted response.
1.  [CVE-2012-0256](https://nvd.nist.gov/vuln/detail/CVE-2012-0256): Apache Traffic Server 2.0.x and 3.0.x before 3.0.4 and 3.1.x before 3.1.3 does not properly allocate heap memory, which allows remote attackers to cause a denial of service (daemon crash) via a long HTTP Host header.
1.  [CVE-2014-3525](https://nvd.nist.gov/vuln/detail/CVE-2014-3525): Unspecified vulnerability in Apache Traffic Server 3.x through 3.2.5, 4.x before 4.2.1.1, and 5.x before 5.0.1 has unknown impact and attack vectors, possibly related to health checks.
1.  [CVE-2014-10022](https://nvd.nist.gov/vuln/detail/CVE-2014-10022): Apache Traffic Server before 5.1.2 allows remote attackers to cause a denial of service via unspecified vectors, related to internal buffer sizing.
1.  [CVE-2017-5659](https://nvd.nist.gov/vuln/detail/CVE-2017-5659): Apache Traffic Server before 6.2.1 generates a coredump when there is a mismatch between content length and chunked encoding.
1.  [CVE-2015-5206](https://nvd.nist.gov/vuln/detail/CVE-2015-5206): Unspecified vulnerability in the HTTP/2 experimental feature in Apache Traffic Server before 5.3.x before 5.3.2 has unknown impact and attack vectors, a different vulnerability than CVE-2015-5168.
1.  [CVE-2014-3624](https://nvd.nist.gov/vuln/detail/CVE-2014-3624): Apache Traffic Server 5.1.x before 5.1.1 allows remote attackers to bypass access restrictions by leveraging failure to properly tunnel remap requests using CONNECT.
1.  [CVE-2017-5660](https://nvd.nist.gov/vuln/detail/CVE-2017-5660): There is a vulnerability in Apache Traffic Server (ATS) 6.2.0 and prior and 7.0.0 and prior with the Host header and line folding. This can have issues when interacting with upstream proxies and the wrong host being used.
1.  [CVE-2018-8022](https://nvd.nist.gov/vuln/detail/CVE-2018-8022): A carefully crafted invalid TLS handshake can cause Apache Traffic Server (ATS) to segfault. This affects version 6.2.2. To resolve this issue users running 6.2.2 should upgrade to 6.2.3 or later versions.
1.  [CVE-2018-11783](https://nvd.nist.gov/vuln/detail/CVE-2018-11783): sslheaders plugin extracts information from the client certificate and sets headers in the request based on the configuration of the plugin. The plugin doesn't strip the headers from the request in some scenarios. This problem was discovered in versions 6.0.0 to 6.0.3, 7.0.0 to 7.1.5, and 8.0.0 to 8.0.1.
1.  [CVE-2019-9516](https://nvd.nist.gov/vuln/detail/CVE-2019-9516): Some HTTP/2 implementations are vulnerable to a header leak, potentially leading to a denial of service. The attacker sends a stream of headers with a 0-length header name and 0-length header value, optionally Huffman encoded into 1-byte or greater headers. Some implementations allocate memory for these headers and keep the allocation alive until the session dies. This can consume excess memory.
1.  [CVE-2019-10079](https://nvd.nist.gov/vuln/detail/CVE-2019-10079): Apache Traffic Server is vulnerable to HTTP/2 setting flood attacks. Earlier versions of Apache Traffic Server didn't limit the number of setting frames sent from the client using the HTTP/2 protocol. Users should upgrade to Apache Traffic Server 7.1.7, 8.0.4, or later versions.
1.  [CVE-2019-17559](https://nvd.nist.gov/vuln/detail/CVE-2019-17559): There is a vulnerability in Apache Traffic Server 6.0.0 to 6.2.3, 7.0.0 to 7.1.8, and 8.0.0 to 8.0.5 with a smuggling attack and scheme parsing. Upgrade to versions 7.1.9 and 8.0.6 or later versions.
1.  [CVE-2020-9481](https://nvd.nist.gov/vuln/detail/CVE-2020-9481): Apache ATS 6.0.0 to 6.2.3, 7.0.0 to 7.1.9, and 8.0.0 to 8.0.6 is vulnerable to a HTTP/2 slow read attack.
1.  [CVE-2020-9494](https://nvd.nist.gov/vuln/detail/CVE-2020-9494): Apache Traffic Server 6.0.0 to 6.2.3, 7.0.0 to 7.1.10, and 8.0.0 to 8.0.7 is vulnerable to certain types of HTTP/2 HEADERS frames that can cause the server to allocate a large amount of memory and spin the thread.
1.  [CVE-2020-17509](https://nvd.nist.gov/vuln/detail/CVE-2020-17509): ATS negative cache option is vulnerable to a cache poisoning attack. If you have this option enabled, please upgrade or disable this feature. Apache Traffic Server versions 7.0.0 to 7.1.11 and 8.0.0 to 8.1.0 are affected.
1.  [CVE-2021-27737](https://nvd.nist.gov/vuln/detail/CVE-2021-27737): Apache Traffic Server 9.0.0 is vulnerable to a remote DOS attack on the experimental Slicer plugin.
1.  [CVE-2021-27577](https://nvd.nist.gov/vuln/detail/CVE-2021-27577): Incorrect handling of url fragment vulnerability of Apache Traffic Server allows an attacker to poison the cache. This issue affects Apache Traffic Server 7.0.0 to 7.1.12, 8.0.0 to 8.1.1, 9.0.0 to 9.0.1.
1.  [CVE-2021-35474](https://nvd.nist.gov/vuln/detail/CVE-2021-35474): Stack-based Buffer Overflow vulnerability in cachekey plugin of Apache Traffic Server. This issue affects Apache Traffic Server 7.0.0 to 7.1.12, 8.0.0 to 8.1.1, 9.0.0 to 9.0.1.
1.  [CVE-2021-41585](https://nvd.nist.gov/vuln/detail/CVE-2021-41585): Improper Input Validation vulnerability in accepting socket connections in Apache Traffic Server allows an attacker to make the server stop accepting new connections. This issue affects Apache Traffic Server 5.0.0 to 9.1.0.
1.  [CVE-2021-44040](https://nvd.nist.gov/vuln/detail/CVE-2021-44040): Improper Input Validation vulnerability in request line parsing of Apache Traffic Server allows an attacker to send invalid requests. This issue affects Apache Traffic Server 8.0.0 to 8.1.3 and 9.0.0 to 9.1.1.
1.  [CVE-2022-25763](https://nvd.nist.gov/vuln/detail/CVE-2022-25763): Improper Input Validation vulnerability in HTTP/2 request validation of Apache Traffic Server allows an attacker to create smuggle or cache poison attacks. This issue affects Apache Traffic Server 8.0.0 to 9.1.2.
1.  [CVE-2022-32749](https://nvd.nist.gov/vuln/detail/CVE-2022-32749): Improper Check for Unusual or Exceptional Conditions vulnerability handling requests in Apache Traffic Server allows an attacker to crash the server under certain conditions. This issue affects Apache Traffic Server: from 8.0.0 through 9.1.3.


