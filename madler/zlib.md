# madler/zlib

https://github.com/madler/zlib

Last updated on Aug 5, 2022

**Rating**: **BAD**

**Score**: **2.95**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **4.69** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **5.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **1.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **4.69** (weight is 1.0)
        1.  **[Static analysis](#static-analysis)**: **5.0** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
                
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
                
            1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
                
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **0.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **0.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **4.27** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **5.69** (weight is 0.2)
    


# ## How to improve the rating

You can ask the project maintainers to enable LGTM checks for pull requests in the project.
More info:
1.  [How to enable LGTM checks for pull requests](https://lgtm.com/help/lgtm/about-automated-code-review)


You can open a pull request to enable CodeQL scans in the project. Make sure that the scans are run on pull requests.
More info:
1.  [How to enable CodeQL checks for pull requests](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


You can open a pull request to enable CodeQL scans in the project.
More info:
1.  [How to enable CodeQL checks](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


You can open a pull request to add a security policy for the project.
More info:
1.  [About adding a security policy to a repository on GitHub](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
1.  [An example of a security policy](https://github.com/apache/nifi/blob/main/SECURITY.md)
1.  [Suggest a security policy for the project](https://github.com/madler/zlib/security/policy)


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


You can open a pull request to trigger Bandit scans job in the project using GitHub action workflow for every pull-request.
More info:
1.  [GitHub workflow action config to run Bandit code scanning job on every PR of a project.](https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#example-using-a-list-of-events)
1.  [An eample to trigger Bandit scan check on every pull-request.](https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L3)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **4.69**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Static analysis](#static-analysis)**: **5.0** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
        
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        
    1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
        
1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **1.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** No
1.  **Does it have a security team?** No
1.  **Does it have executable binaries?** No
1.  **Does it sign artifacts?** No
1.  **Does it use AddressSanitizer?** No
1.  **Does it use Dependabot?** No
1.  **Does it use FindSecBugs?** No
1.  **Does it use LGTM checks?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use OWASP ESAPI?** No
1.  **Does it use OWASP Java Encoder?** No
1.  **Does it use OWASP Java HTML Sanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Does it use nohttp?** No
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** Yes


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **4.69** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **5.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 10 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 10 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

0 commits in the last 3 months results to 0.00 points

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 0
1.  **Number of contributors in the last three months:** 0


### Project popularity

Score: **4.27**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 3700
1.  **Number of watchers for a GitHub repository:** 170


### Security reviews

Score: **5.69**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 1 feature:



1.  **Info about security reviews:** 1 security review


### Static analysis

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
    
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    
1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    


### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Fuzzing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** Yes
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** None


### LGTM score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Programming languages:** C, CPP, C_SHARP, OTHER
1.  **The worst LGTM grade of the project:** A+


### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it run CodeQL scans?** No
1.  **Does it use CodeQL checks for pull requests?** No
1.  **Does it use LGTM checks?** No
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### How a project uses Bandit

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)



The score is N/A because the project uses languages that are not supported by Bandit.

This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### Dependabot score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** No
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** None
1.  **Programming languages:** C, CPP, C_SHARP, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** None
1.  **What is the threshold for OWASP Dependency Check?** Not specified


## Known vulnerabilities

1.  [CVE-2002-0059](https://nvd.nist.gov/vuln/detail/CVE-2002-0059): The decompression algorithm in zlib 1.1.3 and earlier, as used in many different utilities and packages, causes inflateEnd to release certain memory more than once (a "double free"), which may allow local and remote attackers to execute arbitrary code via a block of malformed compression data.
1.  [CVE-2003-0107](https://nvd.nist.gov/vuln/detail/CVE-2003-0107): Buffer overflow in the gzprintf function in zlib 1.1.4, when zlib is compiled without vsnprintf or when long inputs are truncated using vsnprintf, allows attackers to cause a denial of service or possibly execute arbitrary code.
1.  [CVE-2004-0797](https://nvd.nist.gov/vuln/detail/CVE-2004-0797): The error handling in the (1) inflate and (2) inflateBack functions in ZLib compression library 1.2.x allows local users to cause a denial of service (application crash).
1.  [CVE-2005-2096](https://nvd.nist.gov/vuln/detail/CVE-2005-2096): zlib 1.2 and later versions allows remote attackers to cause a denial of service (crash) via a crafted compressed stream with an incomplete code description of a length greater than 1, which leads to a buffer overflow, as demonstrated using a crafted PNG file.
1.  [CVE-2005-1849](https://nvd.nist.gov/vuln/detail/CVE-2005-1849): inftrees.h in zlib 1.2.2 allows remote attackers to cause a denial of service (application crash) via an invalid file that causes a large dynamic tree to be produced.
1.  [CVE-2016-9843](https://nvd.nist.gov/vuln/detail/CVE-2016-9843): The crc32_big function in crc32.c in zlib 1.2.8 might allow context-dependent attackers to have unspecified impact via vectors involving big-endian CRC calculation.
1.  [CVE-2018-25032](https://nvd.nist.gov/vuln/detail/CVE-2018-25032): zlib before 1.2.12 allows memory corruption when deflating (i.e., when compressing) if the input has many distant matches.


