# apache/dubbo-go

https://github.com/apache/dubbo-go

Last updated on Feb 17, 2023

**Rating**: **GOOD**

**Score**: **6.71**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **8.04** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **7.69** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **6.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **8.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **8.04** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **7.69** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **7.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **4.68** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **4.95** (weight is 0.5)
    
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

Score: **8.04**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **7.69** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **6.0**, confidence is 10.0 (max), weight is 0.9 (high)

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
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **8.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **8.04** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **7.69** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **N/A** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **N/A** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **N/A** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 16 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 16 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **7.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** Yes
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **4.68**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

29 commits in the last 3 months results to 4.46 points
2 contributors increase the score value from 4.46 to 4.68

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 29
1.  **Number of contributors in the last three months:** 2


### Project popularity

Score: **4.95**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 129
1.  **Number of stars for a GitHub repository:** 4347
1.  **Number of watchers for a GitHub repository:** 155


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
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **7.69**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
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

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** Yes
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

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
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

1.  [CVE-2019-17564](https://nvd.nist.gov/vuln/detail/CVE-2019-17564): Unsafe deserialization occurs within a Dubbo application which has HTTP remoting enabled. An attacker may submit a POST request with a Java object in it to completely compromise a Provider instance of Apache Dubbo, if this instance enables HTTP. This issue affected Apache Dubbo 2.7.0 to 2.7.4, 2.6.0 to 2.6.7, and all 2.5.x versions.
1.  [CVE-2020-1948](https://nvd.nist.gov/vuln/detail/CVE-2020-1948): This vulnerability can affect all Dubbo users stay on version 2.7.6 or lower. An attacker can send RPC requests with unrecognized service name or method name along with some malicious parameter payloads. When the malicious parameter is deserialized, it will execute some malicious code. More details can be found below.
1.  [CVE-2020-11995](https://nvd.nist.gov/vuln/detail/CVE-2020-11995): A deserialization vulnerability existed in dubbo 2.7.5 and its earlier versions, which could lead to malicious code execution. Most Dubbo users use Hessian2 as the default serialization/deserialization protool, during Hessian2 deserializing the HashMap object, some functions in the classes stored in HasMap will be executed after a series of program calls, however, those special functions may cause remote command execution. For example, the hashCode() function of the EqualsBean class in rome-1.7.0.jar will cause the remotely load malicious classes and execute malicious code by constructing a malicious request. This issue was fixed in Apache Dubbo 2.6.9 and 2.7.8.
1.  [CVE-2021-30179](https://nvd.nist.gov/vuln/detail/CVE-2021-30179): Apache Dubbo prior to 2.6.9 and 2.7.9 by default supports generic calls to arbitrary methods exposed by provider interfaces. These invocations are handled by the GenericFilter which will find the service and method specified in the first arguments of the invocation and use the Java Reflection API to make the final call. The signature for the $invoke or $invokeAsync methods is Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/Object; where the first argument is the name of the method to invoke, the second one is an array with the parameter types for the method being invoked and the third one is an array with the actual call arguments. In addition, the caller also needs to set an RPC attachment specifying that the call is a generic call and how to decode the arguments. The possible values are: - true - raw.return - nativejava - bean - protobuf-json An attacker can control this RPC attachment and set it to nativejava to force the java deserialization of the byte array located in the third argument.
1.  [CVE-2021-36162](https://nvd.nist.gov/vuln/detail/CVE-2021-36162): Apache Dubbo supports various rules to support configuration override or traffic routing (called routing in Dubbo). These rules are loaded into the configuration center (eg: Zookeeper, Nacos, ...) and retrieved by the customers when making a request in order to find the right endpoint. When parsing these YAML rules, Dubbo customers will use SnakeYAML library to load the rules which by default will enable calling arbitrary constructors. An attacker with access to the configuration center he will be able to poison the rule so when retrieved by the consumers, it will get RCE on all of them. This was fixed in Dubbo 2.7.13, 3.0.2
1.  [CVE-2021-36161](https://nvd.nist.gov/vuln/detail/CVE-2021-36161): Some component in Dubbo will try to print the formated string of the input arguments, which will possibly cause RCE for a maliciously customized bean with special toString method. In the latest version, we fix the toString call in timeout, cache and some other places. Fixed in Apache Dubbo 2.7.13
1.  [CVE-2021-43297](https://nvd.nist.gov/vuln/detail/CVE-2021-43297): A deserialization vulnerability existed in dubbo hessian-lite 3.2.11 and its earlier versions, which could lead to malicious code execution. Most Dubbo users use Hessian2 as the default serialization/deserialization protocol, during Hessian catch unexpected exceptions, Hessian will log out some imformation for users, which may cause remote command execution. This issue affects Apache Dubbo Apache Dubbo 2.6.x versions prior to 2.6.12; Apache Dubbo 2.7.x versions prior to 2.7.15; Apache Dubbo 3.0.x versions prior to 3.0.5.
1.  [CVE-2022-24969](https://nvd.nist.gov/vuln/detail/CVE-2022-24969): bypass CVE-2021-25640 > In Apache Dubbo prior to 2.6.12 and 2.7.15, the usage of parseURL method will lead to the bypass of the white host check which can cause open redirect or SSRF vulnerability.
1.  [CVE-2022-39198](https://nvd.nist.gov/vuln/detail/CVE-2022-39198): A deserialization vulnerability existed in dubbo hessian-lite 3.2.12 and its earlier versions, which could lead to malicious code execution. This issue affects Apache Dubbo 2.7.x version 2.7.17 and prior versions; Apache Dubbo 3.0.x version 3.0.11 and prior versions; Apache Dubbo 3.1.x version 3.1.0 and prior versions.
1.  [CVE-2021-32824](https://nvd.nist.gov/vuln/detail/CVE-2021-32824): Apache Dubbo is a java based, open source RPC framework. Versions prior to 2.6.10 and 2.7.10 are vulnerable to pre-auth remote code execution via arbitrary bean manipulation in the Telnet handler. The Dubbo main service port can be used to access a Telnet Handler which offers some basic methods to collect information about the providers and methods exposed by the service and it can even allow to shutdown the service. This endpoint is unprotected. Additionally, a provider method can be invoked using the `invoke` handler. This handler uses a safe version of FastJson to process the call arguments. However, the resulting list is later processed with `PojoUtils.realize` which can be used to instantiate arbitrary classes and invoke its setters. Even though FastJson is properly protected with a default blocklist, `PojoUtils.realize` is not, and an attacker can leverage that to achieve remote code execution. Versions 2.6.10 and 2.7.10 contain fixes for this issue.


