# ansible/ansible

https://github.com/ansible/ansible

Last updated on Oct 19, 2021

**Rating**: **BAD**

**Score**: **4.37**, max score value is 10.0

**Confidence**: Max (10.0, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **3.75** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **4.5** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **9.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **2.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **0.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **3.75** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **4.5** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **9.0** (weight is 1.0)
                
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **0.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **10.0** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **10.0** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


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


You can fix the issues reported by LGTM for the project.
More info:
1.  [List of issues on LGTM](https://lgtm.com/projects/g/ansible/ansible)


You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can enable artifact signing in the project's build pipeline.
More info:
1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)


You can enable NoHttp tool in the project's build pipeline.
More info:
1.  [NoHttp tool home page](https://github.com/spring-io/nohttp)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **3.75**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **4.5** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **9.0** (weight is 1.0)
        
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **2.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00.



This sub-score is based on 17 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** No
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
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **0.0**, confidence is 10.0 (max), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **3.75** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **4.5** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **9.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 44 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 44 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **0.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** No


### Project activity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

274 commits in the last 3 months results to 10.00 points
2 contributors increase the score value from 10.00 to 10.50

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 274
1.  **Number of contributors in the last three months:** 2


### Project popularity

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 18315
1.  **Number of stars for a GitHub repository:** 50370
1.  **Number of watchers for a GitHub repository:** 1997


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **4.5**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[LGTM score](#lgtm-score)**: **9.0** (weight is 1.0)
    
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER


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
1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** None
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### LGTM score

Score: **9.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER
1.  **The worst LGTM grade of the project:** A


### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it run CodeQL scans?** No
1.  **Does it use CodeQL checks for pull requests?** No
1.  **Does it use LGTM checks?** No
1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER


### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** C_SHARP, PYTHON, GO, OTHER


## Known vulnerabilities

1.  [CVE-2013-4259](https://nvd.nist.gov/vuln/detail/CVE-2013-4259): runner/connection_plugins/ssh.py in Ansible before 1.2.3, when using ControlPersist, allows local users to redirect a ssh session via a symlink attack on a socket file with a predictable name in /tmp/.
1.  [CVE-2015-3908](https://nvd.nist.gov/vuln/detail/CVE-2015-3908): Ansible before 1.9.2 does not verify that the server hostname matches a domain name in the subject's Common Name (CN) or subjectAltName field of the X.509 certificate, which allows man-in-the-middle attackers to spoof SSL servers via an arbitrary valid certificate.
1.  [CVE-2016-3096](https://nvd.nist.gov/vuln/detail/CVE-2016-3096): The create_script function in the lxc_container module in Ansible before 1.9.6-1 and 2.x before 2.0.2.0 allows local users to write to arbitrary files or gain privileges via a symlink attack on (1) /opt/.lxc-attach-script, (2) the archived container in the archive_path directory, or the (3) lxc-attach-script.log or (4) lxc-attach-script.err files in the temporary directory.
1.  [CVE-2015-6240](https://nvd.nist.gov/vuln/detail/CVE-2015-6240): The chroot, jail, and zone connection plugins in ansible before 1.9.2 allow local users to escape a restricted environment via a symlink attack.
1.  [CVE-2014-3498](https://nvd.nist.gov/vuln/detail/CVE-2014-3498): The user module in ansible before 1.6.6 allows remote authenticated users to execute arbitrary commands.
1.  [CVE-2017-7550](https://nvd.nist.gov/vuln/detail/CVE-2017-7550): A flaw was found in the way Ansible (2.3.x before 2.3.3, and 2.4.x before 2.4.1) passed certain parameters to the jenkins_plugin module. Remote attackers could use this flaw to expose sensitive information from a remote host's logs. This flaw was fixed by not allowing passwords to be specified in the "params" argument, and noting this in the module documentation.
1.  [CVE-2018-1000149](https://nvd.nist.gov/vuln/detail/CVE-2018-1000149): A man in the middle vulnerability exists in Jenkins Ansible Plugin 0.8 and older in AbstractAnsibleInvocation.java, AnsibleAdHocCommandBuilder.java, AnsibleAdHocCommandInvocationTest.java, AnsibleContext.java, AnsibleJobDslExtension.java, AnsiblePlaybookBuilder.java, AnsiblePlaybookStep.java that disables host key verification by default.
1.  [CVE-2016-9587](https://nvd.nist.gov/vuln/detail/CVE-2016-9587): Ansible before versions 2.1.4, 2.2.1 is vulnerable to an improper input validation in Ansible's handling of data sent from client systems. An attacker with control over a client system being managed by Ansible and the ability to send facts back to the Ansible server could use this flaw to execute arbitrary code on the Ansible server using the Ansible server privileges.
1.  [CVE-2013-2233](https://nvd.nist.gov/vuln/detail/CVE-2013-2233): Ansible before 1.2.1 makes it easier for remote attackers to conduct man-in-the-middle attacks by leveraging failure to cache SSH host keys.
1.  [CVE-2017-7466](https://nvd.nist.gov/vuln/detail/CVE-2017-7466): Ansible before version 2.3 has an input validation vulnerability in the handling of data sent from client systems. An attacker with control over a client system being managed by Ansible, and the ability to send facts back to the Ansible server, could use this flaw to execute arbitrary code on the Ansible server using the Ansible server privileges.
1.  [CVE-2016-8614](https://nvd.nist.gov/vuln/detail/CVE-2016-8614): A flaw was found in Ansible before version 2.2.0. The apt_key module does not properly verify key fingerprints, allowing remote adversary to create an OpenPGP key which matches the short key ID and inject this key instead of the correct key.
1.  [CVE-2018-16876](https://nvd.nist.gov/vuln/detail/CVE-2018-16876): ansible before versions 2.5.14, 2.6.11, 2.7.5 is vulnerable to a information disclosure flaw in vvv+ mode with no_log on that can lead to leakage of sensible data.
1.  [CVE-2019-3828](https://nvd.nist.gov/vuln/detail/CVE-2019-3828): Ansible fetch module before versions 2.5.15, 2.6.14, 2.7.8 has a path traversal vulnerability which allows copying and overwriting files outside of the specified destination in the local ansible controller host, by not restricting an absolute path.
1.  [CVE-2019-10156](https://nvd.nist.gov/vuln/detail/CVE-2019-10156): A flaw was discovered in the way Ansible templating was implemented in versions before 2.6.18, 2.7.12 and 2.8.2, causing the possibility of information disclosure through unexpected variable substitution. By taking advantage of unintended variable substitution the content of any variable may be disclosed.
1.  [CVE-2019-10206](https://nvd.nist.gov/vuln/detail/CVE-2019-10206): ansible-playbook -k and ansible cli tools, all versions 2.8.x before 2.8.4, all 2.7.x before 2.7.13 and all 2.6.x before 2.6.19, prompt passwords by expanding them from templates as they could contain special characters. Passwords should be wrapped to prevent templates trigger and exposing them.
1.  [CVE-2019-10217](https://nvd.nist.gov/vuln/detail/CVE-2019-10217): A flaw was found in ansible 2.8.0 before 2.8.4. Fields managing sensitive data should be set as such by no_log feature. Some of these fields in GCP modules are not set properly. service_account_contents() which is common class for all gcp modules is not setting no_log to True. Any sensitive data managed by that function would be leak as an output when running ansible playbooks.
1.  [CVE-2019-14856](https://nvd.nist.gov/vuln/detail/CVE-2019-14856): ansible before versions 2.8.6, 2.7.14, 2.6.20 is vulnerable to a None
1.  [CVE-2019-14864](https://nvd.nist.gov/vuln/detail/CVE-2019-14864): Ansible, versions 2.9.x before 2.9.1, 2.8.x before 2.8.7 and Ansible versions 2.7.x before 2.7.15, is not respecting the flag no_log set it to True when Sumologic and Splunk callback plugins are used send tasks results events to collectors. This would discloses and collects any sensitive data.
1.  [CVE-2014-2686](https://nvd.nist.gov/vuln/detail/CVE-2014-2686): Ansible prior to 1.5.4 mishandles the evaluation of some strings.
1.  [CVE-2014-4967](https://nvd.nist.gov/vuln/detail/CVE-2014-4967): Multiple argument injection vulnerabilities in Ansible before 1.6.7 allow remote attackers to execute arbitrary code by leveraging access to an Ansible managed host and providing a crafted fact, as demonstrated by a fact with (1) a trailing " src=" clause, (2) a trailing " temp=" clause, or (3) a trailing " validate=" clause accompanied by a shell command.
1.  [CVE-2014-4659](https://nvd.nist.gov/vuln/detail/CVE-2014-4659): Ansible before 1.5.5 sets 0644 permissions for sources.list, which might allow local users to obtain sensitive credential information in opportunistic circumstances by reading a file that uses the "deb http://user:pass@server:port/" format.
1.  [CVE-2020-1733](https://nvd.nist.gov/vuln/detail/CVE-2020-1733): A race condition flaw was found in Ansible Engine 2.7.17 and prior, 2.8.9 and prior, 2.9.6 and prior when running a playbook with an unprivileged become user. When Ansible needs to run a module with become user, the temporary directory is created in /var/tmp. This directory is created with "umask 77 && mkdir -p <dir>"; this operation does not fail if the directory already exists and is owned by another user. An attacker could take advantage to gain control of the become user as the target directory can be retrieved by iterating '/proc/<pid>/cmdline'.
1.  [CVE-2020-1739](https://nvd.nist.gov/vuln/detail/CVE-2020-1739): A flaw was found in Ansible 2.7.16 and prior, 2.8.8 and prior, and 2.9.5 and prior when a password is set with the argument "password" of svn module, it is used on svn command line, disclosing to other users within the same node. An attacker could take advantage by reading the cmdline file from that particular PID on the procfs.
1.  [CVE-2020-1735](https://nvd.nist.gov/vuln/detail/CVE-2020-1735): A flaw was found in the Ansible Engine when the fetch module is used. An attacker could intercept the module, inject a new path, and then choose a new destination path on the controller node. All versions in 2.7.x, 2.8.x and 2.9.x branches are believed to be vulnerable.
1.  [CVE-2020-10684](https://nvd.nist.gov/vuln/detail/CVE-2020-10684): A flaw was found in Ansible Engine, all versions 2.7.x, 2.8.x and 2.9.x prior to 2.7.17, 2.8.9 and 2.9.6 respectively, when using ansible_facts as a subkey of itself and promoting it to a variable when inject is enabled, overwriting the ansible_facts after the clean. An attacker could take advantage of this by altering the ansible_facts, such as ansible_hosts, users and any other key data which would lead into privilege escalation or code injection.
1.  [CVE-2020-10744](https://nvd.nist.gov/vuln/detail/CVE-2020-10744): An incomplete fix was found for the fix of the flaw CVE-2020-1733 ansible: insecure temporary directory when running become_user from become directive. The provided fix is insufficient to prevent the race condition on systems using ACLs and FUSE filesystems. Ansible Engine 2.7.18, 2.8.12, and 2.9.9 as well as previous versions are affected and Ansible Tower 3.4.5, 3.5.6 and 3.6.4 as well as previous versions are affected.
1.  [CVE-2019-14904](https://nvd.nist.gov/vuln/detail/CVE-2019-14904): A flaw was found in the solaris_zone module from the Ansible Community modules. When setting the name for the zone on the Solaris host, the zone name is checked by listing the process with the 'ps' bare command on the remote machine. An attacker could take advantage of this flaw by crafting the name of the zone and executing arbitrary commands in the remote host. Ansible Engine 2.7.15, 2.8.7, and 2.9.2 as well as previous versions are affected.
1.  [CVE-2020-25635](https://nvd.nist.gov/vuln/detail/CVE-2020-25635): A flaw was found in Ansible Base when using the aws_ssm connection plugin as garbage collector is not happening after playbook run is completed. Files would remain in the bucket exposing the data. This issue affects directly data confidentiality.
1.  [CVE-2020-2310](https://nvd.nist.gov/vuln/detail/CVE-2020-2310): Missing permission checks in Jenkins Ansible Plugin 1.0 and earlier allow attackers with Overall/Read permission to enumerate credentials IDs of credentials stored in Jenkins.
1.  [CVE-2021-3447](https://nvd.nist.gov/vuln/detail/CVE-2021-3447): A flaw was found in several ansible modules, where parameters containing credentials, such as secrets, were being logged in plain-text on managed nodes, as well as being made visible on the controller node when run in verbose mode. These parameters were not protected by the no_log feature. An attacker can take advantage of this information to steal those credentials, provided when they have access to the log files containing them. The highest threat from this vulnerability is to data confidentiality. This flaw affects Red Hat Ansible Automation Platform in versions before 1.2.2 and Ansible Tower in versions before 3.8.2.
1.  [CVE-2021-20178](https://nvd.nist.gov/vuln/detail/CVE-2021-20178): A flaw was found in ansible module where credentials are disclosed in the console log by default and not protected by the security feature when using the bitbucket_pipeline_variable module. This flaw allows an attacker to steal bitbucket_pipeline credentials. The highest threat from this vulnerability is to confidentiality.
1.  [CVE-2021-33924](https://nvd.nist.gov/vuln/detail/CVE-2021-33924): Confluent Ansible (cp-ansible) version 5.5.0, 5.5.1, 5.5.2 and 6.0.0 is vulnerable to Incorrect Access Control via its auxiliary component that allows remote attackers to access sensitive information.


