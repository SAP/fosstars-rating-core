# netty/netty

https://github.com/netty/netty

Last updated on Aug 5, 2022

**Rating**: **MODERATE**

**Score**: **5.44**, max score value is 10.0

**Confidence**: High (9.65, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **4.76** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **8.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.5)
            
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
            
    1.  **[Fuzzing](#fuzzing)**: **0.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **10.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **2.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **4.76** (weight is 1.0)
        1.  **[Static analysis](#static-analysis)**: **8.0** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
                
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
                
            1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.5)
                
        1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
                
        1.  **[Fuzzing](#fuzzing)**: **0.0** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **10.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **8.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can ask the project maintainers to enable LGTM checks for pull requests in the project.
More info:
1.  [How to enable LGTM checks for pull requests](https://lgtm.com/help/lgtm/about-automated-code-review)


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


You can include the project to OSS-Fuzz. Then, the project is going to be regularly fuzzed.
More info:
1.  [The OSS-Fuzz project](https://github.com/google/oss-fuzz)


You can add OWASP Dependency Check to the project's build pipeline.
More info:
1.  [OWASP Dependnecy Check](https://jeremylong.github.io/DependencyCheck/)
1.  [How to use OWASP Dependency Check with Maven](https://jeremylong.github.io/DependencyCheck/dependency-check-maven)
1.  [How to use OWASP Dependnecy Check with Gradle](https://github.com/dependency-check/dependency-check-gradle)


You can set a CVSS threshold for vulnerabilities reported by OWASP Dependency Check.
More info:
1.  [OWASP Dependnecy Check](https://jeremylong.github.io/DependencyCheck/)
1.  [Configuring OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html)


You can enable artifact signing in the project's build pipeline.
More info:
1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)


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

Score: **4.76**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Static analysis](#static-analysis)**: **8.0** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
        
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
        
    1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.5)
        
1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
        
1.  **[Fuzzing](#fuzzing)**: **0.0** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **10.0** (weight is 0.2)
    


### Security awareness

Score: **2.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00. If the project has executable binaries, then the score subtracts 2.00.



This sub-score is based on 18 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** No
1.  **Does it have executable binaries?** Yes
1.  **Does it sign artifacts?** No
1.  **Does it use AddressSanitizer?** No
1.  **Does it use Dependabot?** Yes
1.  **Does it use FindSecBugs?** No
1.  **Does it use LGTM checks?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use OWASP ESAPI?** No
1.  **Does it use OWASP Java Encoder?** No
1.  **Does it use OWASP Java HTML Sanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Does it use nohttp?** Yes
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **4.76** (weight is 1.0)
    1.  **[Static analysis](#static-analysis)**: **8.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
            
        1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.5)
            
    1.  **[Dependency testing](#dependency-testing)**: **10.0** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
            
    1.  **[Fuzzing](#fuzzing)**: **0.0** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **0.0** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **10.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 16 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 16 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **8.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** Yes


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

67 commits in the last 3 months results to 10.00 points
4 contributors increase the score value from 10.00 to 11.00

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 67
1.  **Number of contributors in the last three months:** 4


### Project popularity

Score: **10.0**, confidence is 6.67 (low), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** unknown
1.  **Number of stars for a GitHub repository:** 29698
1.  **Number of watchers for a GitHub repository:** 1779


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Static analysis

Score: **8.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[LGTM score](#lgtm-score)**: **10.0** (weight is 1.0)
    
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **10.0** (weight is 1.0)
    
1.  **[How a project uses Bandit](#how-a-project-uses-bandit)**: **N/A** (weight is 0.5)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **0.0** (weight is 0.5)
    


### Dependency testing

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **10.0** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **0.0** (weight is 1.0)
    


### Fuzzing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### Memory-safety testing

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### nohttp tool

Score: **10.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** Yes
1.  **Package managers:** MAVEN


### LGTM score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER
1.  **The worst LGTM grade of the project:** A+


### How a project uses CodeQL

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it run CodeQL scans?** Yes
1.  **Does it use CodeQL checks for pull requests?** Yes
1.  **Does it use LGTM checks?** No
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### How a project uses Bandit

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)



The score is N/A because the project uses languages that are not supported by Bandit.

This sub-score is based on 3 features:



1.  **If a project runs Bandit scan checks for commits:** No
1.  **If a project runs Bandit scans:** No
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### FindSecBugs score

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### Dependabot score

Score: **10.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** Yes
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** MAVEN
1.  **Programming languages:** C, JAVA, JAVASCRIPT, OTHER


### OWASP Dependency Check score

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** MAVEN
1.  **What is the threshold for OWASP Dependency Check?** Not specified


## Known vulnerabilities

1.  [CVE-2014-0193](https://nvd.nist.gov/vuln/detail/CVE-2014-0193): WebSocket08FrameDecoder in Netty 3.6.x before 3.6.9, 3.7.x before 3.7.1, 3.8.x before 3.8.2, 3.9.x before 3.9.1, and 4.0.x before 4.0.19 allows remote attackers to cause a denial of service (memory consumption) via a TextWebSocketFrame followed by a long stream of ContinuationWebSocketFrames.
1.  [CVE-2014-3488](https://nvd.nist.gov/vuln/detail/CVE-2014-3488): The SslHandler in Netty before 3.9.2 allows remote attackers to cause a denial of service (infinite loop and CPU consumption) via a crafted SSLv2Hello message.
1.  [CVE-2016-4970](https://nvd.nist.gov/vuln/detail/CVE-2016-4970): handler/ssl/OpenSslEngine.java in Netty 4.0.x before 4.0.37.Final and 4.1.x before 4.1.1.Final allows remote attackers to cause a denial of service (infinite loop).
1.  [CVE-2015-2156](https://nvd.nist.gov/vuln/detail/CVE-2015-2156): Netty before 3.9.8.Final, 3.10.x before 3.10.3.Final, 4.0.x before 4.0.28.Final, and 4.1.x before 4.1.0.Beta5 and Play Framework 2.x before 2.3.9 might allow remote attackers to bypass the httpOnly flag on cookies and obtain sensitive information by leveraging improper validation of cookie name and value characters.
1.  [CVE-2019-16869](https://nvd.nist.gov/vuln/detail/CVE-2019-16869): Netty before 4.1.42.Final mishandles whitespace before the colon in HTTP headers (such as a "Transfer-Encoding : chunked" line), which leads to HTTP request smuggling.
1.  [CVE-2020-7238](https://nvd.nist.gov/vuln/detail/CVE-2020-7238): Netty 4.1.43.Final allows HTTP Request Smuggling because it mishandles Transfer-Encoding whitespace (such as a [space]Transfer-Encoding:chunked line) and a later Content-Length header. This issue exists because of an incomplete fix for CVE-2019-16869.
1.  [CVE-2019-20445](https://nvd.nist.gov/vuln/detail/CVE-2019-20445): HttpObjectDecoder.java in Netty before 4.1.44 allows a Content-Length header to be accompanied by a second Content-Length header, or by a Transfer-Encoding header.
1.  [CVE-2020-11612](https://nvd.nist.gov/vuln/detail/CVE-2020-11612): The ZlibDecoders in Netty 4.1.x before 4.1.46 allow for unbounded memory allocation while decoding a ZlibEncoded byte stream. An attacker could send a large ZlibEncoded byte stream to the Netty server, forcing the server to allocate all of its free memory to a single decoder.
1.  [CVE-2021-21290](https://nvd.nist.gov/vuln/detail/CVE-2021-21290): Netty is an open-source, asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. In Netty before version 4.1.59.Final there is a vulnerability on Unix-like systems involving an insecure temp file. When netty's multipart decoders are used local information disclosure can occur via the local system temporary directory if temporary storing uploads on the disk is enabled. On unix-like systems, the temporary directory is shared between all user. As such, writing to this directory using APIs that do not explicitly set the file/directory permissions can lead to information disclosure. Of note, this does not impact modern MacOS Operating Systems. The method "File.createTempFile" on unix-like systems creates a random file, but, by default will create this file with the permissions "-rw-r--r--". Thus, if sensitive information is written to this file, other local users can read this information. This is the case in netty's "AbstractDiskHttpData" is vulnerable. This has been fixed in version 4.1.59.Final. As a workaround, one may specify your own "java.io.tmpdir" when you start the JVM or use "DefaultHttpDataFactory.setBaseDir(...)" to set the directory to something that is only readable by the current user.
1.  [CVE-2021-21295](https://nvd.nist.gov/vuln/detail/CVE-2021-21295): Netty is an open-source, asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. In Netty (io.netty:netty-codec-http2) before version 4.1.60.Final there is a vulnerability that enables request smuggling. If a Content-Length header is present in the original HTTP/2 request, the field is not validated by `Http2MultiplexHandler` as it is propagated up. This is fine as long as the request is not proxied through as HTTP/1.1. If the request comes in as an HTTP/2 stream, gets converted into the HTTP/1.1 domain objects (`HttpRequest`, `HttpContent`, etc.) via `Http2StreamFrameToHttpObjectCodec `and then sent up to the child channel's pipeline and proxied through a remote peer as HTTP/1.1 this may result in request smuggling. In a proxy case, users may assume the content-length is validated somehow, which is not the case. If the request is forwarded to a backend channel that is a HTTP/1.1 connection, the Content-Length now has meaning and needs to be checked. An attacker can smuggle requests inside the body as it gets downgraded from HTTP/2 to HTTP/1.1. For an example attack refer to the linked GitHub Advisory. Users are only affected if all of this is true: `HTTP2MultiplexCodec` or `Http2FrameCodec` is used, `Http2StreamFrameToHttpObjectCodec` is used to convert to HTTP/1.1 objects, and these HTTP/1.1 objects are forwarded to another remote peer. This has been patched in 4.1.60.Final As a workaround, the user can do the validation by themselves by implementing a custom `ChannelInboundHandler` that is put in the `ChannelPipeline` behind `Http2StreamFrameToHttpObjectCodec`.
1.  [CVE-2021-21409](https://nvd.nist.gov/vuln/detail/CVE-2021-21409): Netty is an open-source, asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. In Netty (io.netty:netty-codec-http2) before version 4.1.61.Final there is a vulnerability that enables request smuggling. The content-length header is not correctly validated if the request only uses a single Http2HeaderFrame with the endStream set to to true. This could lead to request smuggling if the request is proxied to a remote peer and translated to HTTP/1.1. This is a followup of GHSA-wm47-8v5p-wjpj/CVE-2021-21295 which did miss to fix this one case. This was fixed as part of 4.1.61.Final.
1.  [CVE-2021-37136](https://nvd.nist.gov/vuln/detail/CVE-2021-37136): The Bzip2 decompression decoder function doesn't allow setting size restrictions on the decompressed output data (which affects the allocation size used during decompression). All users of Bzip2Decoder are affected. The malicious input can trigger an OOME and so a DoS attack
1.  [CVE-2021-43797](https://nvd.nist.gov/vuln/detail/CVE-2021-43797): Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. Netty prior to version 4.1.71.Final skips control chars when they are present at the beginning / end of the header name. It should instead fail fast as these are not allowed by the spec and could lead to HTTP request smuggling. Failing to do the validation might cause netty to "sanitize" header names before it forward these to another remote system when used as proxy. This remote system can't see the invalid usage anymore, and therefore does not do the validation itself. Users should upgrade to version 4.1.71.Final.
1.  [CVE-2022-24823](https://nvd.nist.gov/vuln/detail/CVE-2022-24823): Netty is an open-source, asynchronous event-driven network application framework. The package `io.netty:netty-codec-http` prior to version 4.1.77.Final contains an insufficient fix for CVE-2021-21290. When Netty's multipart decoders are used local information disclosure can occur via the local system temporary directory if temporary storing uploads on the disk is enabled. This only impacts applications running on Java version 6 and lower. Additionally, this vulnerability impacts code running on Unix-like systems, and very old versions of Mac OSX and Windows as they all share the system temporary directory between all users. Version 4.1.77.Final contains a patch for this vulnerability. As a workaround, specify one's own `java.io.tmpdir` when starting the JVM or use DefaultHttpDataFactory.setBaseDir(...) to set the directory to something that is only readable by the current user.


