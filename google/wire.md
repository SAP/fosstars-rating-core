# google/wire

https://github.com/google/wire

Last updated on Feb 17, 2023

**Rating**: **BAD**

**Score**: **4.2**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **2.27** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **5.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **2.5** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **2.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **2.27** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **5.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **8.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **0.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can open a pull request to enable CodeQL scans in the project. Make sure that the scans are run on pull requests.
More info:
1.  [How to enable CodeQL checks for pull requests](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


You can open a pull request to enable CodeQL scans in the project.
More info:
1.  [How to enable CodeQL checks](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


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

Score: **2.27**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **5.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **2.5**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
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
1.  **Does it use verified signed commits?** Yes
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **2.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **2.27** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **5.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 10 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 10 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **8.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** Yes


### Project activity

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

0 commits in the last 3 months results to 0.00 points

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 0
1.  **Number of contributors in the last three months:** 0


### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 7537
1.  **Number of stars for a GitHub repository:** 10021
1.  **Number of watchers for a GitHub repository:** 106


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    
1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
    


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** GO, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** GO, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** GOMODULES


### Dependabot score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** No
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** GOMODULES
1.  **Programming languages:** GO, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** GOMODULES
1.  **Programming languages:** GO, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** GOMODULES
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** No
1.  **Does it use CodeQL checks for pull requests?** No
1.  **Programming languages:** GO, OTHER


### Bandit score

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)



The score is N/A because the project uses languages that are not supported by Bandit.

This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** GO, OTHER


### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** GO, OTHER


### How a project uses Pylint

Score: **N/A**, confidence is 10.0 (max), weight is 0.35 (medium)



The score is N/A because the project uses languages that are not supported by Pylint.

This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** GO, OTHER


### GoSec score

Score: **0.0**, confidence is 10.0 (max), weight is 0.3 (medium)





This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** GO, OTHER


### How a project uses MyPy

Score: **N/A**, confidence is 10.0 (max), weight is 0.2 (low)



The score is N/A because the project uses languages that are not supported by MyPy.

This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** GO, OTHER


## Known vulnerabilities

1.  [CVE-2018-8909](https://nvd.nist.gov/vuln/detail/CVE-2018-8909): The Wire application before 2018-03-07 for Android allows attackers to write to pathnames outside of the downloads directory via a ../ in a filename of a received file, related to AssetService.scala.
1.  [CVE-2020-15258](https://nvd.nist.gov/vuln/detail/CVE-2020-15258): In Wire before 3.20.x, `shell.openExternal` was used without checking the URL. This vulnerability allows an attacker to execute code on the victims machine by sending messages containing links with arbitrary protocols. The victim has to interact with the link and sees the URL that is opened. The issue was patched by implementing a helper function which checks if the URL's protocol is common. If it is common, the URL will be opened externally. If not, the URL will not be opened and a warning appears for the user informing them that a probably insecure URL was blocked from being executed. The issue is patched in Wire 3.20.x. More technical details about exploitation are available in the linked advisory.
1.  [CVE-2020-27853](https://nvd.nist.gov/vuln/detail/CVE-2020-27853): Wire before 2020-10-16 allows remote attackers to cause a denial of service (application crash) or possibly execute arbitrary code via a format string. This affects Wire AVS (Audio, Video, and Signaling) 5.3 through 6.x before 6.4, the Wire Secure Messenger application before 3.49.918 for Android, and the Wire Secure Messenger application before 3.61 for iOS. This occurs via the value parameter to sdp_media_set_lattr in peerflow/sdp.c.
1.  [CVE-2021-21301](https://nvd.nist.gov/vuln/detail/CVE-2021-21301): Wire is an open-source collaboration platform. In Wire for iOS (iPhone and iPad) before version 3.75 there is a vulnerability where the video capture isn't stopped in a scenario where a user first has their camera enabled and then disables it. It's a privacy issue because video is streamed to the call when the user believes it is disabled. It impacts all users in video calls. This is fixed in version 3.75.
1.  [CVE-2021-29508](https://nvd.nist.gov/vuln/detail/CVE-2021-29508): Due to how Wire handles type information in its serialization format, malicious payloads can be passed to a deserializer. e.g. using a surrogate on the sender end, an attacker can pass information about a different type for the receiving end. And by doing so allowing the serializer to create any type on the deserializing end. This is the same issue that exists for .NET BinaryFormatter https://docs.microsoft.com/en-us/visualstudio/code-quality/ca2300?view=vs-2019. This also applies to the fork of Wire.
1.  [CVE-2021-32666](https://nvd.nist.gov/vuln/detail/CVE-2021-32666): wire-ios is the iOS version of Wire, an open-source secure messaging app. In wire-ios versions 3.8.0 and prior, a vulnerability exists that can cause a denial of service between users. If a user has an invalid assetID for their profile picture and it contains the " character, it will cause the iOS client to crash. The vulnerability is patched in wire-ios version 3.8.1.
1.  [CVE-2021-32755](https://nvd.nist.gov/vuln/detail/CVE-2021-32755): Wire is a collaboration platform. wire-ios-transport handles authentication of requests, network failures, and retries for the iOS implementation of Wire. In the 3.82 version of the iOS application, a new web socket implementation was introduced for users running iOS 13 or higher. This new websocket implementation is not configured to enforce certificate pinning when available. Certificate pinning for the new websocket is enforced in version 3.84 or above.
1.  [CVE-2021-41094](https://nvd.nist.gov/vuln/detail/CVE-2021-41094): Wire is an open source secure messenger. Users of Wire by Bund may bypass the mandatory encryption at rest feature by simply disabling their device passcode. Upon launching, the app will attempt to enable encryption at rest by generating encryption keys via the Secure Enclave, however it will fail silently if no device passcode is set. The user has no indication that encryption at rest is not active since the feature is hidden to them. This issue has been resolved in version 3.70


