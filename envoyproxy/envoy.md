# envoyproxy/envoy

https://github.com/envoyproxy/envoy

Last updated on Feb 17, 2023

**Rating**: **MODERATE**

**Score**: **5.17**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **5.7** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **3.92** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **4.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **5.7** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **3.92** (weight is 1.0)
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
                
            1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
                
            1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
                
            1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
                
        1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **0.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.01** (weight is 0.2)
    


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


You can create Snyk account and configure your project.
More info:
1.  [Getting started with snyk for open source](https://docs.snyk.io/products/snyk-open-source/getting-started-snyk-open-source)


You can open a pull request to trigger GoSec scans job in the project using GitHub action workflow for every pull-request.
More info:
1.  [GitHub workflow action config to run GoSec code scanning job on every PR of a project.](https://github.com/securego/gosec#github-action)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **5.7**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
        
    1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **3.92** (weight is 1.0)
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
        
    1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
        
    1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
        
    1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
        
1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **4.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** No
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
1.  **Is it included to OSS-Fuzz?** Yes


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **5.7** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **3.92** (weight is 1.0)
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
            
        1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
            
        1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
            
        1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
            
    1.  **[Fuzzing](#fuzzing)**: **10.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 55 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 55 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

4415 commits in the last 3 months results to 10.00 points
4 contributors increase the score value from 10.00 to 11.00

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 4415
1.  **Number of contributors in the last three months:** 4


### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 6
1.  **Number of stars for a GitHub repository:** 21449
1.  **Number of watchers for a GitHub repository:** 608


### Security reviews

Score: **0.01**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 1 feature:



1.  **Info about security reviews:** 1 security review


### Dependency testing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
    
1.  **[Snyk score](#snyk-score)**: **5.0** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **3.92**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
1.  **[Bandit score](#bandit-score)**: **0.0** (weight is 0.35)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.35)
    
1.  **[How a project uses Pylint](#how-a-project-uses-pylint)**: **0.0** (weight is 0.35)
    
1.  **[GoSec score](#gosec-score)**: **0.0** (weight is 0.3)
    
1.  **[How a project uses MyPy](#how-a-project-uses-mypy)**: **0.0** (weight is 0.2)
    


### Fuzzing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** Yes
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** RUBYGEMS, GOMODULES


### Dependabot score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** Yes
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** RUBYGEMS, GOMODULES
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### Snyk score

Score: **5.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use GitHub as the main development platform?** Yes
1.  **Does it use Snyk?** No
1.  **Package managers:** RUBYGEMS, GOMODULES
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** RUBYGEMS, GOMODULES
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### Bandit score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### How a project uses Pylint

Score: **0.0**, confidence is 10.0 (max), weight is 0.35 (medium)





This sub-score is based on 3 features:



1.  **Does it run Pylint scans on all commits?** No
1.  **Does it run Pylint scans?** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### GoSec score

Score: **0.0**, confidence is 10.0 (max), weight is 0.3 (medium)





This sub-score is based on 4 features:



1.  **Does it run GoSec scans on all pull requests?** No
1.  **Does it run GoSec scans with rules?** No
1.  **Does it run GoSec scans?** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


### How a project uses MyPy

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 3 features:



1.  **Does it run MyPy scans on all commits?** No
1.  **Does it run MyPy scans?** No
1.  **Programming languages:** C, CPP, JAVA, RUBY, PYTHON, JAVASCRIPT, GO, OTHER


## Known vulnerabilities

1.  [CVE-2019-7678](https://nvd.nist.gov/vuln/detail/CVE-2019-7678): A directory traversal vulnerability was discovered in Enphase Envoy R3.*.* via images/, include/, include/js, or include/css on TCP port 8888.
1.  [CVE-2019-9901](https://nvd.nist.gov/vuln/detail/CVE-2019-9901): Envoy 1.9.0 and before does not normalize HTTP URL paths. A remote attacker may craft a relative path, e.g., something/../admin, to bypass access control, e.g., a block on /admin. A backend server could then interpret the non-normalized path and provide an attacker access beyond the scope provided for by the access control policy.
1.  [CVE-2019-15225](https://nvd.nist.gov/vuln/detail/CVE-2019-15225): In Envoy through 1.11.1, users may configure a route to match incoming path headers via the libstdc++ regular expression implementation. A remote attacker may send a request with a very long URI to result in a denial of service (memory consumption). This is a related issue to CVE-2019-14993.
1.  [CVE-2019-15226](https://nvd.nist.gov/vuln/detail/CVE-2019-15226): Upon receiving each incoming request header data, Envoy will iterate over existing request headers to verify that the total size of the headers stays below a maximum limit. The implementation in versions 1.10.0 through 1.11.1 for HTTP/1.x traffic and all versions of Envoy for HTTP/2 traffic had O(n^2) performance characteristics. A remote attacker may craft a request that stays below the maximum request header size but consists of many thousands of small headers to consume CPU and result in a denial-of-service attack.
1.  [CVE-2019-18836](https://nvd.nist.gov/vuln/detail/CVE-2019-18836): Envoy 1.12.0 allows a remote denial of service because of resource loops, as demonstrated by a single idle TCP connection being able to keep a worker thread in an infinite busy loop when continue_on_listener_filters_timeout is used."
1.  [CVE-2019-18838](https://nvd.nist.gov/vuln/detail/CVE-2019-18838): An issue was discovered in Envoy 1.12.0. Upon receipt of a malformed HTTP request without a Host header, it sends an internally generated "Invalid request" response. This internally generated response is dispatched through the configured encoder filter chain before being sent to the client. An encoder filter that invokes route manager APIs that access a request's Host header causes a NULL pointer dereference, resulting in abnormal termination of the Envoy process.
1.  [CVE-2020-8664](https://nvd.nist.gov/vuln/detail/CVE-2020-8664): CNCF Envoy through 1.13.0 has incorrect Access Control when using SDS with Combined Validation Context. Using the same secret (e.g. trusted CA) across many resources together with the combined validation context could lead to the “static” part of the validation context to be not applied, even though it was visible in the active config dump.
1.  [CVE-2020-11767](https://nvd.nist.gov/vuln/detail/CVE-2020-11767): Istio through 1.5.1 and Envoy through 1.14.1 have a data-leak issue. If there is a TCP connection (negotiated with SNI over HTTPS) to *.example.com, a request for a domain concurrently configured explicitly (e.g., abc.example.com) is sent to the server(s) listening behind *.example.com. The outcome should instead be 421 Misdirected Request. Imagine a shared caching forward proxy re-using an HTTP/2 connection for a large subnet with many users. If a victim is interacting with abc.example.com, and a server (for abc.example.com) recycles the TCP connection to the forward proxy, the victim's browser may suddenly start sending sensitive data to a *.example.com server. This occurs because the forward proxy between the victim and the origin server reuses connections (which obeys the specification), but neither Istio nor Envoy corrects this by sending a 421 error. Similarly, this behavior voids the security model browsers have put in place between domains.
1.  [CVE-2020-8663](https://nvd.nist.gov/vuln/detail/CVE-2020-8663): Envoy version 1.14.2, 1.13.2, 1.12.4 or earlier may exhaust file descriptors and/or memory when accepting too many connections.
1.  [CVE-2020-15104](https://nvd.nist.gov/vuln/detail/CVE-2020-15104): In Envoy before versions 1.12.6, 1.13.4, 1.14.4, and 1.15.0 when validating TLS certificates, Envoy would incorrectly allow a wildcard DNS Subject Alternative Name apply to multiple subdomains. For example, with a SAN of *.example.com, Envoy would incorrectly allow nested.subdomain.example.com, when it should only allow subdomain.example.com. This defect applies to both validating a client TLS certificate in mTLS, and validating a server TLS certificate for upstream connections. This vulnerability is only applicable to situations where an untrusted entity can obtain a signed wildcard TLS certificate for a domain of which you only intend to trust a subdomain of. For example, if you intend to trust api.mysubdomain.example.com, and an untrusted actor can obtain a signed TLS certificate for *.example.com or *.com. Configurations are vulnerable if they use verify_subject_alt_name in any Envoy version, or if they use match_subject_alt_names in version 1.14 or later. This issue has been fixed in Envoy versions 1.12.6, 1.13.4, 1.14.4, 1.15.0.
1.  [CVE-2020-25018](https://nvd.nist.gov/vuln/detail/CVE-2020-25018): Envoy master between 2d69e30 and 3b5acb2 may fail to parse request URL that requires host canonicalization.
1.  [CVE-2020-35470](https://nvd.nist.gov/vuln/detail/CVE-2020-35470): Envoy before 1.16.1 logs an incorrect downstream address because it considers only the directly connected peer, not the information in the proxy protocol header. This affects situations with tcp-proxy as the network filter (not HTTP filters).
1.  [CVE-2021-21378](https://nvd.nist.gov/vuln/detail/CVE-2021-21378): Envoy is a cloud-native high-performance edge/middle/service proxy. In Envoy version 1.17.0 an attacker can bypass authentication by presenting a JWT token with an issuer that is not in the provider list when Envoy's JWT Authentication filter is configured with the `allow_missing` requirement under `requires_any` due to a mistake in implementation. Envoy's JWT Authentication filter can be configured with the `allow_missing` requirement that will be satisfied if JWT is missing (JwtMissed error) and fail if JWT is presented or invalid. Due to a mistake in implementation, a JwtUnknownIssuer error was mistakenly converted to JwtMissed when `requires_any` was configured. So if `allow_missing` was configured under `requires_any`, an attacker can bypass authentication by presenting a JWT token with an issuer that is not in the provider list. Integrity may be impacted depending on configuration if the JWT token is used to protect against writes or modifications. This regression was introduced on 2020/11/12 in PR 13839 which fixed handling `allow_missing` under RequiresAny in a JwtRequirement (see issue 13458). The AnyVerifier aggregates the children verifiers' results into a final status where JwtMissing is the default error. However, a JwtUnknownIssuer was mistakenly treated the same as a JwtMissing error and the resulting final aggregation was the default JwtMissing. As a result, `allow_missing` would allow a JWT token with an unknown issuer status. This is fixed in version 1.17.1 by PR 15194. The fix works by preferring JwtUnknownIssuer over a JwtMissing error, fixing the accidental conversion and bypass with `allow_missing`. A user could detect whether a bypass occurred if they have Envoy logs enabled with debug verbosity. Users can enable component level debug logs for JWT. The JWT filter logs will indicate that there is a request with a JWT token and a failure that the JWT token is missing.
1.  [CVE-2021-28683](https://nvd.nist.gov/vuln/detail/CVE-2021-28683): An issue was discovered in Envoy through 1.71.1. There is a remotely exploitable NULL pointer dereference and crash in TLS when an unknown TLS alert code is received.
1.  [CVE-2021-29492](https://nvd.nist.gov/vuln/detail/CVE-2021-29492): Envoy is a cloud-native edge/middle/service proxy. Envoy does not decode escaped slash sequences `%2F` and `%5C` in HTTP URL paths in versions 1.18.2 and before. A remote attacker may craft a path with escaped slashes, e.g. `/something%2F..%2Fadmin`, to bypass access control, e.g. a block on `/admin`. A backend server could then decode slash sequences and normalize path and provide an attacker access beyond the scope provided for by the access control policy. ### Impact Escalation of Privileges when using RBAC or JWT filters with enforcement based on URL path. Users with back end servers that interpret `%2F` and `/` and `%5C` and `\` interchangeably are impacted. ### Attack Vector URL paths containing escaped slash characters delivered by untrusted client. Patches in versions 1.18.3, 1.17.3, 1.16.4, 1.15.5 contain new path normalization option to decode escaped slash characters. As a workaround, if back end servers treat `%2F` and `/` and `%5C` and `\` interchangeably and a URL path based access control is configured, one may reconfigure the back end server to not treat `%2F` and `/` and `%5C` and `\` interchangeably.
1.  [CVE-2020-25752](https://nvd.nist.gov/vuln/detail/CVE-2020-25752): An issue was discovered on Enphase Envoy R3.x and D4.x devices. There are hardcoded web-panel login passwords for the installer and Enphase accounts. The passwords for these accounts are hardcoded values derived from the MD5 hash of the username and serial number mixed with some static strings. The serial number can be retrieved by an unauthenticated user at /info.xml. These passwords can be easily calculated by an attacker; users are unable to change these passwords.
1.  [CVE-2021-32778](https://nvd.nist.gov/vuln/detail/CVE-2021-32778): Envoy is an open source L7 proxy and communication bus designed for large modern service oriented architectures. In affected versions envoy’s procedure for resetting a HTTP/2 stream has O(N^2) complexity, leading to high CPU utilization when a large number of streams are reset. Deployments are susceptible to Denial of Service when Envoy is configured with high limit on H/2 concurrent streams. An attacker wishing to exploit this vulnerability would require a client opening and closing a large number of H/2 streams. Envoy versions 1.19.1, 1.18.4, 1.17.4, 1.16.5 contain fixes to reduce time complexity of resetting HTTP/2 streams. As a workaround users may limit the number of simultaneous HTTP/2 dreams for upstream and downstream peers to a low number, i.e. 100.
1.  [CVE-2021-39162](https://nvd.nist.gov/vuln/detail/CVE-2021-39162): Pomerium is an open source identity-aware access proxy. Envoy, which Pomerium is based on, can abnormally terminate if an H/2 GOAWAY and SETTINGS frame are received in the same IO event. This can lead to a DoS in the presence of untrusted *upstream* servers. 0.15.1 contains an upgraded envoy binary with this vulnerability patched. If only trusted upstreams are configured, there is not substantial risk of this condition being triggered.
1.  [CVE-2022-23606](https://nvd.nist.gov/vuln/detail/CVE-2022-23606): Envoy is an open source edge and service proxy, designed for cloud-native applications. When a cluster is deleted via Cluster Discovery Service (CDS) all idle connections established to endpoints in that cluster are disconnected. A recursion was introduced in the procedure of disconnecting idle connections that can lead to stack exhaustion and abnormal process termination when a cluster has a large number of idle connections. This infinite recursion causes Envoy to crash. Users are advised to upgrade.
1.  [CVE-2022-29224](https://nvd.nist.gov/vuln/detail/CVE-2022-29224): Envoy is a cloud-native high-performance proxy. Versions of envoy prior to 1.22.1 are subject to a segmentation fault in the GrpcHealthCheckerImpl. Envoy can perform various types of upstream health checking. One of them uses gRPC. Envoy also has a feature which can â€œholdâ€? (prevent removal) upstream hosts obtained via service discovery until configured active health checking fails. If an attacker controls an upstream host and also controls service discovery of that host (via DNS, the EDS API, etc.), an attacker can crash Envoy by forcing removal of the host from service discovery, and then failing the gRPC health check request. This will crash Envoy via a null pointer dereference. Users are advised to upgrade to resolve this vulnerability. Users unable to upgrade may disable gRPC health checking and/or replace it with a different health checking type as a mitigation.


