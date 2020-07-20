# apache/cayenne

```
Here is how the rating was calculated:
  Score:........Security of project
  Value:........4.66 out of 10.00
  Confidence:...10.00 out of 10.00
  Based on:.....7 sub-scores:
      Sub-score:....Security testing
      Importance:...High (weight 1.00 out of 1.00)
      Value:........1.60 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:.....5 sub-scores:
          Sub-score:....Static analysis
          Importance:...High (weight 1.00 out of 1.00)
          Value:........4.00 out of 10.00
          Confidence:...10.00 out of 10.00
          Based on:.....2 sub-scores:
              Sub-score:....How a project addresses issues reported by LGTM
              Importance:...High (weight 1.00 out of 1.00)
              Value:........4.00 out of 10.00
              Confidence:...10.00 out of 10.00
              Based on:...2 features:
                  Does it use LGTM?....................No
                  The worst LGTM grade of a project:...B

              Sub-score:....How a project uses FindSecBugs
              Importance:...High (weight 1.00 out of 1.00)
              Value:........0.00 out of 10.00
              Confidence:...10.00 out of 10.00
              Based on:...2 features:
                  A set of programming languages:...JAVA, OTHER
                  Does it use FindSecBugs?..........No


          Sub-score:....Fuzzing
          Importance:...High (weight 1.00 out of 1.00)
          Value:........N/A  
          Confidence:...10.00 out of 10.00
          Based on:...2 features:
              A set of programming languages:..............................JAVA, OTHER
              If an open-source project is included to OSS-Fuzz project:...No

          Sub-score:....Dependency testing
          Importance:...High (weight 1.00 out of 1.00)
          Value:........0.00 out of 10.00
          Confidence:...10.00 out of 10.00
          Based on:...5 features:
              A set of package managers:..............................MAVEN
              A set of programming languages:.........................JAVA, OTHER
              Does it scan for vulnerable dependencies?...............No
              Does it use Dependabot?.................................No
              Does it use GitHub as the main development platform?....No

          Sub-score:....Memory-safety testing
          Importance:...High (weight 1.00 out of 1.00)
          Value:........N/A  
          Confidence:...10.00 out of 10.00
          Based on:...4 features:
              A set of programming languages:............JAVA, OTHER
              Does it use AddressSanitizer?..............No
              Does it use MemorySanitizer?...............No
              Does it use UndefinedBehaviorSanitizer?....No

          Sub-score:....nohttp tool
          Importance:...Medium (weight 0.50 out of 1.00)
          Value:........0.00 out of 10.00
          Confidence:...10.00 out of 10.00
          Based on:...2 features:
              A set of package managers:...MAVEN
              Does it use nohttp?..........No


      Sub-score:....Security awareness
      Description:..The score shows how a project is aware of security.
                    If the project has a security policy, then the score adds 3.00.
                    If the project has a security team, then the score adds 5.00.
                    If the project uses verified signed commits, then the score adds 2.00.
                    If the project has a bug bounty program, then the score adds 4.00
                    If the project signs its artifacts, then the score adds 2.00.
      Importance:...High (weight 0.90 out of 1.00)
      Value:........7.00 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:...5 features:
          Does it have a bug bounty program?......No
          Does it have a security policy?.........No
          Does it have a security team?...........Yes
          Does it sign artifacts?.................Yes
          Does it use verified signed commits?....No

      Sub-score:....Unpatched vulnerabilities
      Importance:...High (weight 0.80 out of 1.00)
      Value:........10.00 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:...1 features:
          Info about vulnerabilities:...1 vulnerability
      Explanation:..No unpatched vulnerabilities found which is good

      Sub-score:....Vulnerability discovery and security testing
      Description:..The scores checks how security testing is done
                    and how many vulnerabilities were recently discovered.
                    If testing is good, and there are no recent vulnerabilities,
                    then the score value is max.
                    If there are vulnerabilities, then the score value is high.
                    If testing is bad, and there are no recent vulnerabilities,
                    then the score value is low.
                    If there are vulnerabilities, then the score is min.
      Importance:...Medium (weight 0.60 out of 1.00)
      Value:........2.00 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:.....1 sub-scores:
          Sub-score:....Security testing
          Importance:...High (weight 1.00 out of 1.00)
          Value:........1.60 out of 10.00
          Confidence:...10.00 out of 10.00

      Based on:...1 features:
          Info about vulnerabilities:...1 vulnerability

      Sub-score:....Project activity
      Description:..The score is based on number of commits and contributors.
                    Here is how the number of commits contributes to the score (up to 5.10):
                    0 -> 0.10, 200 -> 2.55, 310 -> 4.59
                    Here is how the number of contributors contributes to the score (up to 5.10):
                    0 -> 0.10, 5 -> 2.55, 10 -> 4.59
      Importance:...Medium (weight 0.50 out of 1.00)
      Value:........3.26 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:...2 features:
          Number of commits in the last three months:........43
          Number of contributors in the last three months:...4

      Sub-score:....Community commitment
      Importance:...Medium (weight 0.50 out of 1.00)
      Value:........7.00 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:...3 features:
          Does it belong to Apache?........Yes
          Does it belong to Eclipse?.......No
          Is it supported by a company?....No

      Sub-score:....Project popularity
      Description:..The score is based on number of stars and watchers.
                    Here is how a number of stars contributes to the score:
                    0 -> 0.00 (min), 2500 -> 2.50, 5000 -> 5.00, 10000 -> 10.00 (max)
                    Here is how a number of watchers contributes to the score:
                    0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50, 3000 -> 10.00 (max)
      Importance:...Medium (weight 0.50 out of 1.00)
      Value:........0.31 out of 10.00
      Confidence:...10.00 out of 10.00
      Based on:...2 features:
          Number of stars for a GitHub repository:......221
          Number of watchers for a GitHub repository:...27

Rating: 4.66 out of 10.00 -> MODERATE
Confidence: 10.00 out of 10.00

```
