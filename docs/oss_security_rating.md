# Security ratings for open source projects

This section describes a security rating for open source projects.
The rating is implemented in the [OssSecurityRating](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssSecurityRating.java) class.

The rating assesses how well open source projects and their communities care about security. 
The rating may be used to estimate security risks for an application that uses open source components.

[By definition](ratings.md), the security rating produces a score, a label and a confidence score.
Here is a list of labels:

1.  `GOOD`: the project implements relatively good security measures and in general cares about security.
1.  `MODERATE`: the security level in the project is not good but definitely not too bad.
1.  `BAD`: looks like the project doesn't care well about security.
1.  `UNCLEAR`: there is no enough data to reliably calculate a score and a label for the project.

The rating procedure uses
[thresholds](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssSecurityRating.java#L84)
for score and confidence to select one of the labels for an open source project.
If a confidence score is lower than a certain value, then the project gets the `UNCLEAR` label.
If a calculated score is higher than a certain threshold, then the project gets `GOOD`, `MODERATE` or `BAD` label.

The thresholds depend on scores for the following [projects](oss/security):

*  Popular projects in Apache and Eclipse software foundations
*  Popular Spring projects
*  Popular FasterXML projects
*  A number of other well-known projects such as curl, Netty, OpenSSL and so on.

Here is how the thresholds are calculated:

*  First, calculate scores for the projects mentioned above.
*  Second, sort the projects by scores (from low to high).
*  Then, assign the `BAD` label to first 30% of the projects with the lowest scores.
   The highest score in this set becomes the threshold for the `BAD` label.
   In other words, a project gets the `BAD` label if its score is below this threshold.
*  Next, assign the `MODERATE` label to the next 50% of the projects.
   The highest score in this set becomes the threshold for the `MODERATE` label accordingly.
   In other words, a project gets the `MODERATE` label if its score is below this threshold.
*  Finally, the rest 20% of the projects get the `GOOD` label.

The main goal of this method is to reduce possible bias that may be introduced if experts set the thresholds directly.
Instead of setting the thresholds directly, the experts gives a list of well-known in the industry projects and specify
a desired fraction for the labels. At the moment, it is 20-50-30 that looks like a normal distribution.
As a result, the thresholds don't set a bar totally based on someone's opinion.
Instead, other open source projects are compared with the real, well-known and trusted ones.
The procedure for re-calculating the thresholds is described [here](oss_security_rating_tuning.md).

## What the security rating takes into account

The security rating assesses the following factors of an open source project:

1.  How well the project implements security testing.
1.  Unpatched vulnerabilities.
1.  Security reviews.
1.  How well the community is aware of security.
1.  How the project is active.
1.  How the project is popular.
1.  How the community commits to support the project.

To assess these factors, the security rating uses the following info about the open source project:

1.  Whether the project runs CodeQL checks or not.
1.  Whether the project runs CodeQL scans or not.
1.  Whether the project uses LGTM checks for commits or not.
1.  The worst LGTM grade for the project.
1.  Whether the project uses FindSecBugs or not.
1.  Whether the project uses Dependabot or not.
1.  Whether the project runs Bandit checks or not.
1.  Whether the project runs Bandit scans or not.
1.  Whether the project uses OWASP Dependency Check or not.
1.  Whether the project has a CVSS threshold for OWASP Dependency Check to fail the build or not.
1.  Whether the project uses AddressSanitizer or not.
1.  Whether the project uses MemorySanitizer or not.
1.  Whether the project uses UndefinedBehaviorSanitizer or not.
1.  Whether the project uses OWASP Enterprise Security API (ESAPI) or not.
1.  Whether the project uses OWASP Java Encoder or not.
1.  Whether the project uses OWASP Java HTML Sanitizer or not.
1.  Whether the project uses NoHTTP tool or not.
1.  Info about security reviews that have been done for the project.
1.  Info about vulnerabilities in the project.
1.  Whether the project is included to OSS-Fuzz project or not.
1.  Whether the project signs artifacts or not.
1.  Whether the project uses signed commits or not.
1.  Whether the project has a security policy or not.
1.  Whether the project has a security team or not.
1.  Whether the project has a bug bounty program or not.
1.  Whether the project belongs to Apache Foundation or not.
1.  Whether the project belongs to Eclipse Foundation or not.
1.  Whether the project is supported by a company or not.
1.  Whether the project uses GitHub for development or not.
1.  Programming languages used in the project.
1.  Package managers used in the project.
1.  Number of commits in the last three months.
1.  Number of contributors in the last three months.
1.  Number of stars in the GitHub repository.
1.  Number of watchers in the GitHub repository.

For example, here is a [detailed report](oss/security/curl/curl.md)
that shows all sub-scores, structure and data that were used to calculate the security rating for curl.

## Security ratings for well-known open source projects

Pre-calculated security ratings for well-known open source projects can be found
in this [report](oss/security/README.md).

## How to improve security ratings

Security ratings may be improved by contributing security enhancements to open source projects.
For example:

*  [Contribute a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
*  Improve a project's LGTM grade by fixing issues reported by [LGTM](https://lgtm.com)
*  [Enable CodeQL scans](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository)

In the bottom of a rating report for a project, you can find a list of advice for improving its rating.

Check out [this page](oss/security/improvements/README.md)
that shows how security ratings have been improved for a number of open source projects.

---

Next: [Getting the security ratings](getting_oss_security_rating.md)
