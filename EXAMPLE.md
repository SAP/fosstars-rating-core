# Example output of the command-line tool

Here is how a security rating was calculated for curl:

```
[+] Okay, we have a GitHub token, let's try to use it
[+] Let's gather info and calculate a security rating for:
[+]   https://github.com/curl/curl
[+] Counting how many commits have been done in the last three months ...
[+] Pulling updates from https://github.com/curl/curl ...
[+] Counting how many people contributed to the project in the last three months ...
[+] Counting how many stars the project has ...
[+] Counting how many watchers the project has ...
[+] Figuring out if the project has a security team ...
[+] Figuring out if the project is supported by a company ...
[+] Figuring out if the project has a security policy ...
[+] Figuring out if the project has a bug bounty program ...
[+] Looking for vulnerabilities in the project ...
[+] Figuring out if the project has any unpatched vulnerability ...
[+] Looking for vulnerabilities in NVD ...
[+] Figuring out if the project belongs to the Apache Software Foundation ...
[+] Figuring out if the project belongs to the Eclipse Software Foundation ...
[+] Figuring out how the project uses CodeQL ...
[+] Figuring out how the project uses LGTM ...
[+] Figuring out if the project uses signed commits ...
[+] Checking if the project uses Dependabot ...
[+] Looking for programming languages that are used in the project...
[+] Looking for package managers ...
[+] Looking for programming languages that are used in the project...
[+] Figuring out if the project uses nohttp ...
[+] Figuring out if the project uses GitHub for development ...
[+] Figuring out if the project uses OWASP Dependency Check ...
[+] Figuring out if the project uses sanitizers ...
[+] Figuring out if the project uses FindSecBugs ...
[+] Figuring out if the project is fuzzed in OSS-Fuzz ...
[+] Figuring out if the project signs jar files ...
[+] Figuring out if the project uses OWASP security libraries ...
[+] Here is what we know about the project:
[+]    Does it belong to Apache? No
[+]    Does it belong to Eclipse? No
[+]    Does it have a bug bounty program? Yes
[+]    Does it have a security policy? Yes
[+]    Does it have a security team? No
[+]    Does it run CodeQL scans? No
[+]    Does it sign artifacts? No
[+]    Does it use AddressSanitizer? Yes
[+]    Does it use CodeQL checks for pull requests? No
[+]    Does it use Dependabot? No
[+]    Does it use FindSecBugs? No
[+]    Does it use GitHub as the main development platform? Yes
[+]    Does it use LGTM checks? No
[+]    Does it use MemorySanitizer? No
[+]    Does it use OWASP ESAPI? No
[+]    Does it use OWASP Java Encoder? No
[+]    Does it use OWASP Java HTML Sanitizer? No
[+]    Does it use UndefinedBehaviorSanitizer? Yes
[+]    Does it use nohttp? No
[+]    Does it use verified signed commits? Yes
[+]    How is OWASP Dependency Check used? Not used
[+]    Info about vulnerabilities: 0 vulnerabilities
[+]    Is it included to OSS-Fuzz? Yes
[+]    Is it supported by a company? No
[+]    Number of commits in the last three months: 405
[+]    Number of contributors in the last three months: 9
[+]    Number of stars for a GitHub repository: 19479
[+]    Number of watchers for a GitHub repository: 707
[+]    Package managers: None
[+]    Programming languages: C, CPP, PYTHON, OTHER
[+]    The worst LGTM grade of the project: A+
[+]    What is the threshold for OWASP Dependency Check? Not specified
[+] Here is how the rating was calculated:
[+]   Score:........Security of project
[+]   Value:........6.87 out of 10.0
[+]   Confidence:...Max (10.0 out of 10.0)
[+]   Based on:.....7 sub-scores
[+]       Sub-score:....Security testing
[+]       Importance:...High (weight 1.0  out of  1.0)
[+]       Value:........5.22 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....5 sub-scores
[+]           Sub-score:....Dependency testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....2 sub-scores
[+]               Sub-score:....Dependabot score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+] 
[+]               Sub-score:....OWASP Dependency Check score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+] 
[+]           Sub-score:....Static analysis
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........5.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:.....3 sub-scores
[+]               Sub-score:....LGTM score
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........10.0 out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+] 
[+]               Sub-score:....How a project uses CodeQL
[+]               Importance:...High (weight 1.0  out of  1.0)
[+]               Value:........0.0  out of 10.0
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...4 features
[+] 
[+]               Sub-score:....FindSecBugs score
[+]               Importance:...Medium (weight 0.5  out of  1.0)
[+]               Value:........N/A  
[+]               Confidence:...Max (10.0 out of 10.0)
[+]               Based on:...2 features
[+] 
[+]           Sub-score:....Fuzzing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........10.0 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+] 
[+]           Sub-score:....Memory-safety testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........8.5  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...4 features
[+] 
[+]           Sub-score:....nohttp tool
[+]           Importance:...Medium (weight 0.5  out of  1.0)
[+]           Value:........0.0  out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]           Based on:...2 features
[+] 
[+]       Sub-score:....Security awareness
[+]       Importance:...High (weight 0.9  out of  1.0)
[+]       Value:........9.5  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...17 features
[+] 
[+]       Sub-score:....Unpatched vulnerabilities
[+]       Importance:...High (weight 0.8  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+] 
[+]       Sub-score:....Vulnerability discovery and security testing
[+]       Importance:...Medium (weight 0.6  out of  1.0)
[+]       Value:........2.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:.....1 sub-scores
[+]           Sub-score:....Security testing
[+]           Importance:...High (weight 1.0  out of  1.0)
[+]           Value:........5.22 out of 10.0
[+]           Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...1 features
[+] 
[+]       Sub-score:....Community commitment
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........0.0  out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...3 features
[+] 
[+]       Sub-score:....Project activity
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features
[+] 
[+]       Sub-score:....Project popularity
[+]       Importance:...Medium (weight 0.5  out of  1.0)
[+]       Value:........10.0 out of 10.0
[+]       Confidence:...Max (10.0 out of 10.0)
[+]       Based on:...2 features
[+] 
[+] Rating:     6.87 out of 10.0 -> GOOD
[+] Confidence: Max (10.0 out of 10.0)
[+] 
[+] Bye!
```