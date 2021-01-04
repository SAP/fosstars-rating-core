# apache/commons-io

```
Here is how the rating was calculated:
  Score:........Security of project
  Value:........5.17 out of 10.0
  Confidence:...High (10.0 out of 10.0)
  Based on:.....7 sub-scores:
      Sub-score:....Security testing
      Importance:...High (weight 1.0  out of  1.0)
      Value:........4.0  out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:.....5 sub-scores:
          Sub-score:....Dependency testing
          Importance:...High (weight 1.0  out of  1.0)
          Value:........6.0  out of 10.0
          Confidence:...High (10.0 out of 10.0)
          Based on:.....2 sub-scores:
              Sub-score:....Dependabot score
              Importance:...High (weight 1.0  out of  1.0)
              Value:........6.0  out of 10.0
              Confidence:...High (10.0 out of 10.0)
              Based on:...4 features:
                  A set of package managers:..............................MAVEN
                  A set of programming languages:.........................JAVA, OTHER
                  Does it use Dependabot?.................................No
                  Does it use GitHub as the main development platform?....Yes

              Sub-score:....OWASP Dependency Check score
              Importance:...High (weight 1.0  out of  1.0)
              Value:........0.0  out of 10.0
              Confidence:...High (10.0 out of 10.0)
              Based on:...2 features:
                  A CVSS threshold for OWASP Dependency Check to fail the build:...Not specified
                  How OWASP Dependency Check is used:..............................NOT_USED


          Sub-score:....Static analysis
          Importance:...High (weight 1.0  out of  1.0)
          Value:........4.0  out of 10.0
          Confidence:...High (10.0 out of 10.0)
          Based on:.....2 sub-scores:
              Sub-score:....LGTM score
              Importance:...High (weight 1.0  out of  1.0)
              Value:........4.0  out of 10.0
              Confidence:...High (10.0 out of 10.0)
              Based on:...2 features:
                  Does it use LGTM checks?...............No
                  The worst LGTM grade of the project:...B

              Sub-score:....FindSecBugs score
              Importance:...High (weight 1.0  out of  1.0)
              Value:........0.0  out of 10.0
              Confidence:...High (10.0 out of 10.0)
              Based on:...2 features:
                  A set of programming languages:...JAVA, OTHER
                  Does it use FindSecBugs?..........No


          Sub-score:....Fuzzing
          Importance:...High (weight 1.0  out of  1.0)
          Value:........N/A  
          Confidence:...High (10.0 out of 10.0)
          Based on:...2 features:
              A set of programming languages:...JAVA, OTHER
              Is it included to OSS-Fuzz?.......No

          Sub-score:....Memory-safety testing
          Importance:...High (weight 1.0  out of  1.0)
          Value:........N/A  
          Confidence:...High (10.0 out of 10.0)
          Based on:...4 features:
              A set of programming languages:............JAVA, OTHER
              Does it use AddressSanitizer?..............No
              Does it use MemorySanitizer?...............No
              Does it use UndefinedBehaviorSanitizer?....No

          Sub-score:....nohttp tool
          Importance:...Medium (weight 0.5  out of  1.0)
          Value:........0.0  out of 10.0
          Confidence:...High (10.0 out of 10.0)
          Based on:...2 features:
              A set of package managers:...MAVEN
              Does it use nohttp?..........No


      Sub-score:....Security awareness
      Description:..The score shows how a project is aware of
                    security. If the project has a security policy,
                    then the score adds 2.00. If the project has a
                    security team, then the score adds 3.00. If the
                    project uses verified signed commits, then the
                    score adds 0.50. If the project has a bug bounty
                    program, then the score adds 4.00. If the project
                    signs its artifacts, then the score adds 0.50. If
                    the project uses a security tool or library, then
                    the score adds 1.00.
      Importance:...High (weight 0.9  out of  1.0)
      Value:........5.0  out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:...17 features:
          Does it have a bug bounty program?.........No
          Does it have a security policy?............Yes
          Does it have a security team?..............Yes
          Does it sign artifacts?....................No
          Does it use AddressSanitizer?..............No
          Does it use Dependabot?....................No
          Does it use FindSecBugs?...................No
          Does it use LGTM checks?...................No
          Does it use MemorySanitizer?...............No
          Does it use OWASP ESAPI?...................No
          Does it use OWASP Java Encoder?............No
          Does it use OWASP Java HTML Sanitizer?.....No
          Does it use UndefinedBehaviorSanitizer?....No
          Does it use nohttp?........................No
          Does it use verified signed commits?.......No
          How OWASP Dependency Check is used:........NOT_USED
          Is it included to OSS-Fuzz?................No

      Sub-score:....Unpatched vulnerabilities
      Importance:...High (weight 0.8  out of  1.0)
      Value:........10.0 out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:...1 features:
          Info about vulnerabilities:...5 vulnerabilities
      Explanation:..No unpatched vulnerabilities found which is good

      Sub-score:....Vulnerability discovery and security testing
      Description:..The scores checks how security testing is done and
                    how many vulnerabilities were recently discovered.
                    If testing is good, and there are no recent
                    vulnerabilities, then the score value is max. If
                    there are vulnerabilities, then the score value is
                    high. If testing is bad, and there are no recent
                    vulnerabilities, then the score value is low. If
                    there are vulnerabilities, then the score is min.
      Importance:...Medium (weight 0.6  out of  1.0)
      Value:........2.0  out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:.....1 sub-scores:
          Sub-score:....Security testing
          Importance:...High (weight 1.0  out of  1.0)
          Value:........4.0  out of 10.0
          Confidence:...High (10.0 out of 10.0)

      Based on:...1 features:
          Info about vulnerabilities:...5 vulnerabilities

      Sub-score:....Project activity
      Description:..The score is based on number of commits and
                    contributors.
                    Here is how the number of commits
                    contributes to the score (up to 5.10):
                    0 -> 0.10,
                    200 -> 2.55, 310 -> 4.59
                    Here is how the number of
                    contributors contributes to the score (up to
                    5.10):
                    0 -> 0.10, 5 -> 2.55, 10 -> 4.59
      Importance:...Medium (weight 0.5  out of  1.0)
      Value:........6.22 out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:...2 features:
          Number of commits in the last three months:........225
          Number of contributors in the last three months:...4

      Sub-score:....Community commitment
      Importance:...Medium (weight 0.5  out of  1.0)
      Value:........7.0  out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:...3 features:
          Does it belong to Apache?........Yes
          Does it belong to Eclipse?.......No
          Is it supported by a company?....No

      Sub-score:....Project popularity
      Description:..The score is based on number of stars and
                    watchers.
                    Here is how a number of stars
                    contributes to the score:
                    0 -> 0.00 (min), 2500 ->
                    2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
                    Here is
                    how a number of watchers contributes to the
                    score:
                    0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50,
                    3000 -> 10.00 (max)
      Importance:...Medium (weight 0.5  out of  1.0)
      Value:........1.02 out of 10.0
      Confidence:...High (10.0 out of 10.0)
      Based on:...2 features:
          Number of stars for a GitHub repository:......721
          Number of watchers for a GitHub repository:...91

Rating:     5.17 out of 10.0 -> GOOD
Confidence: High (10.0 out of 10.0)

```
