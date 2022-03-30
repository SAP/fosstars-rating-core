# Example output of the command-line tool

Here is how a security rating was calculated for curl:

```
[+] Okay, we have a GitHub token, let's try to use it
[+] Let's gather info and calculate a rating for:
[+]   https://github.com/curl/curl
[+] Figuring out how many projects on GitHub uses this project ...
[+] Here is what we know about the project:
[+]    What is the threshold for OWASP Dependency Check? Not specified
[+]    Package managers: None
[+]    Programming languages: C, CPP, PYTHON, OTHER
[+]    How is OWASP Dependency Check used? Not used
[+]    Does it have a bug bounty program? Yes
[+]    Does the project have open pull requests from Dependabot? No
[+]    If a project runs Bandit scan checks for commits: No
[+]    If a project runs Bandit scans: No
[+]    Does it use CodeQL checks for pull requests? No
[+]    Does it run CodeQL scans? No
[+]    Does it sign artifacts? No
[+]    Does it use Dependabot? No
[+]    Does it use GitHub as the main development platform? Yes
[+]    Does it use LGTM checks? No
[+]    Does it use OWASP ESAPI? No
[+]    Does it use OWASP Java Encoder? No
[+]    Does it use OWASP Java HTML Sanitizer? No
[+]    Does it use nohttp? No
[+]    Does it use verified signed commits? No
[+]    Does it belong to Apache? No
[+]    Does it belong to Eclipse? No
[+]    Does it have a security policy? Yes
[+]    Does it have a security team? No
[+]    Is it included to OSS-Fuzz? Yes
[+]    Is it supported by a company? No
[+]    Does it use AddressSanitizer? No
[+]    Does it use FindSecBugs? No
[+]    Does it use MemorySanitizer? No
[+]    Does it use UndefinedBehaviorSanitizer? No
[+]    Info about vulnerabilities in the project: 80 vulnerabilities
[+]    Number of commits in the last three months: 315
[+]    Number of contributors in the last three months: 7
[+]    Number of projects on GitHub that use an open source project: unknown
[+]    Number of stars for a GitHub repository: 23069
[+]    Number of watchers for a GitHub repository: 714
[+]    Info about security reviews: 0 security reviews
[+]    The worst LGTM grade of the project: A
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........5.44 out of 10.0
[+]   Confidence:...High (9.65 out of 10.0)
[+]   Based on:.....8 sub-scores
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........4.25 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....5 sub-scores
[+]           Sub-score:....Dependency testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........N/A  
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores
[+]               Sub-score:....Dependabot score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........N/A  
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+]                   Does it use Dependabot?.................................No
[+]                   Does it use GitHub as the main development platform?....Yes
[+]                   Package managers:.......................................None
[+]                   Programming languages:..................................C, CPP, PYTHON, OTHER
[+] 
[+]               Sub-score:....OWASP Dependency Check score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........N/A  
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...3 features
[+]                   How is OWASP Dependency Check used?..................Not used
[+]                   Package managers:....................................None
[+]                   What is the threshold for OWASP Dependency Check?....Not specified
[+] 
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........3.6  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....4 sub-scores
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........9.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+]                   Programming languages:.................C, CPP, PYTHON, OTHER
[+]                   The worst LGTM grade of the project:...A
[+] 
[+]               Sub-score:....How a project uses CodeQL
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+]                   Does it run CodeQL scans?.......................No
[+]                   Does it use CodeQL checks for pull requests?....No
[+]                   Does it use LGTM checks?........................No
[+]                   Programming languages:..........................C, CPP, PYTHON, OTHER
[+] 
[+]               Sub-score:....How a project uses Bandit
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...3 features
[+]                   If a project runs Bandit scan checks for commits:...No
[+]                   If a project runs Bandit scans:.....................No
[+]                   Programming languages:..............................C, CPP, PYTHON, OTHER
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........N/A  
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+]                   Does it use FindSecBugs?....No
[+]                   Programming languages:......C, CPP, PYTHON, OTHER
[+] 
[+]           Sub-score:....Fuzzing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........10.0 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+]               Is it included to OSS-Fuzz?....Yes
[+]               Programming languages:.........C, CPP, PYTHON, OTHER
[+] 
[+]           Sub-score:....Memory-safety testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...4 features
[+]               Does it use AddressSanitizer?..............No
[+]               Does it use MemorySanitizer?...............No
[+]               Does it use UndefinedBehaviorSanitizer?....No
[+]               Programming languages:.....................C, CPP, PYTHON, OTHER
[+] 
[+]           Sub-score:....nohttp tool
[+]           Importance:...Low (weight 0.2  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+]               Does it use nohttp?....No
[+]               Package managers:......None
[+] 
[+]       Sub-score:....Security awareness
[+]       Description:..The score shows how a project is aware of
[+]                     security. If the project has a security policy,
[+]                     then the score adds 2.00. If the project has a
[+]                     security team, then the score adds 3.00. If the
[+]                     project uses verified signed commits, then the
[+]                     score adds 0.50. If the project has a bug bounty
[+]                     program, then the score adds 4.00. If the project
[+]                     signs its artifacts, then the score adds 0.50. If
[+]                     the project uses a security tool or library, then
[+]                     the score adds 1.00.
[+]       Importance:...High (weight 0.9  out of  1.0)
[+]       Value:........7.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...17 features
[+]           Does it have a bug bounty program?.........Yes
[+]           Does it have a security policy?............Yes
[+]           Does it have a security team?..............No
[+]           Does it sign artifacts?....................No
[+]           Does it use AddressSanitizer?..............No
[+]           Does it use Dependabot?....................No
[+]           Does it use FindSecBugs?...................No
[+]           Does it use LGTM checks?...................No
[+]           Does it use MemorySanitizer?...............No
[+]           Does it use OWASP ESAPI?...................No
[+]           Does it use OWASP Java Encoder?............No
[+]           Does it use OWASP Java HTML Sanitizer?.....No
[+]           Does it use UndefinedBehaviorSanitizer?....No
[+]           Does it use nohttp?........................No
[+]           Does it use verified signed commits?.......No
[+]           How is OWASP Dependency Check used?........Not used
[+]           Is it included to OSS-Fuzz?................Yes
[+] 
[+]       Sub-score:....Vulnerability discovery and security testing
[+]       Description:..The scores checks how security testing is done and
[+]                     how many vulnerabilities were recently discovered.
[+]                     If testing is good, and there are no recent
[+]                     vulnerabilities, then the score value is max. If
[+]                     there are vulnerabilities, then the score value is
[+]                     high. If testing is bad, and there are no recent
[+]                     vulnerabilities, then the score value is low. If
[+]                     there are vulnerabilities, then the score is min.
[+]       Importance:...Medium (weight 0.6  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....1 sub-scores
[+]           Sub-score:....Security testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........4.25 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about vulnerabilities in the project:...80 vulnerabilities
[+] 
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about vulnerabilities in the project:...80 vulnerabilities
[+]       Explanation:..No unpatched vulnerabilities found which is good
[+] 
[+]       Sub-score:....Community commitment
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...3 features
[+]           Does it belong to Apache?........No
[+]           Does it belong to Eclipse?.......No
[+]           Is it supported by a company?....No
[+] 
[+]       Sub-score:....Project activity
[+]       Description:..The score evaluates how active a project is. It's
[+]                     based on number of commits and contributors in the
[+]                     last 3 months.
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features
[+]           Number of commits in the last three months:........315
[+]           Number of contributors in the last three months:...7
[+]       Explanation:..315 commits in the last 3 months results to 10.00 points
[+]                     7 contributors increase the score value from 10.00 to 12.00
[+] 
[+]       Sub-score:....Project popularity
[+]       Description:..This scoring function is based on number of stars,
[+]                     watchers and dependent projects.
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Low (6.67 out of 10.0)
[+]       Based on:...3 features
[+]           Number of projects on GitHub that use an open source project:...unknown
[+]           Number of stars for a GitHub repository:........................23069
[+]           Number of watchers for a GitHub repository:.....................714
[+] 
[+]       Sub-score:....Security reviews
[+]       Importance:...Low (weight 0.2  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+]           Info about security reviews:...0 security reviews
[+]       Explanation:..No security reviews have been done
[+] 
[+] Rating:     5.44 out of 10.0 -> MODERATE
[+] Confidence: High (9.65 out of 10.0)
[+] 
[+] Here is how the rating may be improved:
[+] 1. You can ask the project maintainers to enable LGTM
[+]    checks for pull requests in the project.
[+]    More info:
[+]    1. How to enable LGTM checks for pull requests:
[+]       https://lgtm.com/help/lgtm/about-automated-code-review
[+] 2. You can open a pull request to enable CodeQL scans
[+]    in the project. Make sure that the scans are run
[+]    on pull requests.
[+]    More info:
[+]    1. How to enable CodeQL checks for pull requests:
[+]       https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions
[+] 3. You can open a pull request to enable CodeQL scans
[+]    in the project.
[+]    More info:
[+]    1. How to enable CodeQL checks:
[+]       https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions
[+] 4. You can fix the issues reported by LGTM for the
[+]    project.
[+]    More info:
[+]    1. List of issues on LGTM:
[+]       https://lgtm.com/projects/g/curl/curl
[+] 5. You can open a pull request to enable FindSecBugs
[+]    for the project.
[+]    More info:
[+]    1. FindSecBugs home page:
[+]       https://find-sec-bugs.github.io/
[+] 6. You can open a pull request to enable
[+]    AddressSanitizer for the project.
[+]    More info:
[+]    1. About AddressSanitizer:
[+]       https://github.com/google/sanitizers/wiki/AddressSanitizer
[+] 7. You can open a pull request to enable
[+]    MemorySanitizer for the project.
[+]    More info:
[+]    1. About MemorySanitizer:
[+]       https://github.com/google/sanitizers/wiki/MemorySanitizer
[+] 8. You can open a pull request to enable
[+]    UndefinedBehaviorSanitizer for the project.
[+]    More info:
[+]    1. About UndefinedBehaviorSanitizer:
[+]       https://clang.llvm.org/docs/UndefinedBehaviorSanitizer.html
[+] 9. You can enable artifact signing in the project's
[+]    build pipeline.
[+]    More info:
[+]    1. Apache Maven Jarsigner Plugin:
[+]       https://maven.apache.org/plugins/maven-jarsigner-plugin/
[+] 10. You can enable NoHttp tool in the project's build
[+]    pipeline.
[+]    More info:
[+]    1. NoHttp tool home page:
[+]       https://github.com/spring-io/nohttp
[+] 11. You can open a pull request to run Bandit scans in
[+]    the project using GitHub action workflow.
[+]    More info:
[+]    1. GitHub workflow action job config to run Bandit code scanning for a repository.:
[+]       https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idstepsrun
[+]    2. An example to run Bandit scan check as part of GitHub action workflow.:
[+]       https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L28
[+] 12. You can open a pull request to trigger Bandit
[+]    scans job in the project using GitHub action
[+]    workflow for every pull-request.
[+]    More info:
[+]    1. GitHub workflow action config to run Bandit code scanning job on every PR of a project.:
[+]       https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#example-using-a-list-of-events
[+]    2. An eample to trigger Bandit scan check on every pull-request.:
[+]       https://github.com/TNLinc/CV/blob/main/.github/workflows/bandit.yml#L3
[+] 
[+] Bye!
```