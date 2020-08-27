# Security ratings for open-source projects

This section describes a security rating for open-source projects.
The rating is implemented in the [OssSecurityRating](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating.java) class.

The rating may be used to assess how well open-source projects and their communities care about security. 
This may then be uesd to estimate security risks that may affect an application when it uses open-source components.

[By definition](ratings.md), the security rating produces a score, a label and a confidence score.
Here is a list of labels:

1.  `GOOD`: the project implements relatively good security measures and in general cares about security.
1.  `MODERATE`: the security level in the project is not good but definetly not too bad.
1.  `BAD`: looks like the project doesn't care well about security.
1.  `UNCLEAR`: there is no enough data to reliably calculate a score and a label for the project.

The security rating uses [thresholds](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating.java#L84)
for score and confidence to assing one of the labels to an open-source project.
If a confidence score is lower than a certain value, then the project gets the `UNCLEAR` label.
If a calculated score is higher than a certain threshold, then then the projct gets `GOOD`, `MODERATE` or `BAD` label.

The thresholds depend on scores for the following [projects](oss/security):

*  Relatively popular projects in Apache and Eclipse software foundations
*  Relatively popular Spring projects
*  Relatively popular FasterXML projects
*  And a number of other well-known projects such as curl, Netty, OpenSSL and so on.

Here is how the thresholds are calculated:

*  First, calculate scores for the projects mentioned above.
*  Second, sort the projects by scores (from low to high).
*  Then, assign the `BAD` label to first 30% of the projects with the lowest scores.
   The highest score in this set becomes the threshold for the `BAD` label.
   In other words, a project gets the `BAD` label if its score is below this threshold.
*  Next, assing the `MODERATE` label to the next 50% of the projects.
   The highest score in this set becomes the threshold for the `MODERATE` label accordingly.
   In other words, a project gets the `MODERATE` label if its score is below this threshold.
*  Finally, the rest 20% of the projects get the `GOOD` label.

The main goal of the method is to reduce a possible bias that may be introduced if the thresholds are assigned by experts.
Instead of setting the thresholds directly, the experts gives a list of well-known projects in the industry and specify
a desired fraction for the labels. At the moment, it is 20-50-30 that looks like a normal distribution.
As a result, the thresholds don't set a bar that is based on someone's opinion.
Instead, other open-source projects are compared with the real, well-known, established and trusted ones.

The procedure for calculating the thresholds is implemented in [SecurityRatingAnalysis notebook](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/jupyter/oss/security/SecurityRatingAnalysis.ipynb).

## Features

Here is a list of features that are currently used in the open-source security rating.

1.  Info about vulnerabilities in open-source project.
1.  If an open-source project has a security team.
1.  If an open-source project has a security policy.
1.  If an open-source project has a bug bounty program.
1.  If an open-source project signs its artifacts.
1.  If a project uses Dependabot.
1.  If an open-source project uses FindSecBugs.
1.  If an open-source project uses AddressSanitizer.
1.  If an open-source project uses MemorySanitizer.
1.  If an open-source project uses UndefinedBehaviorSanitizer.
1.  If a project uses LGTM.
1.  The worst LGTM grade of a project.
1.  If an open-source project is included to OSS-Fuzz project.
1.  If a project uses nohttp tool.
1.  If a project uses OWASP Enterprise Security API (ESAPI).
1.  If a project uses OWASP Java HTML Sanitizer.
1.  If a project uses OWASP Java Encoder.
1.  If an open-source project is supported by a company.
1.  If an open-source project belongs to Eclipse Foundation.
1.  If an open-source project belongs to Apache Foundation.
1.  If a project uses GitHub as the main development platform.
1.  If a project uses signed commits.
1.  Number of stars for a GitHub repository.
1.  Number of contributors in the last three months.
1.  Number of commits in the last three months.
1.  Number of watchers for a GitHub repository.
1.  Programming languages that are used in a project.
1.  Package managers that are used in a project.
1.  If and how a project uses OWASP Dependency Check.
1.  If and how a project sets a CVSS threshold for OWASP Dependency Check to fail the build.

## Scores

The open-source security rating is based on the open-source security score
that is implemented in the [OssSecurityScore](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/score/oss/OssSecurityScore.java) class.

Here is a list of sub-scores that are currently used in the open-source security score.
Implementations for all the scores can be found in the [com.sap.sgs.phosphor.fosstars.model.score.oss](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/score/oss) package.

1.  **Security testing score** based on the following sub-scores:
    1.  How a project uses static analysis for security testing. It's based on the following sub-scores:
        1.  How a project addresses issues reported by LGTM. It's based on the following features:
            1.  If a project uses LGTM.
            1.  The worst LGTM grade of a project.
        1.  How a project uses FindSecBugs. It's based on the following features:
            1.  Programming languages that are used in a project.
            1.  If an open-source project uses FindSecBugs.
    1.  If a project uses nohttp tool. It's based on the following features:
        1.  Package managers that are used in a project.
        1.  If a project uses nohttp tool.
    1.  How a project uses fuzzing. It's based on the following features:
        1.  Programming languages that are used in a project.
        1.  If an open-source project is included to OSS-Fuzz project.
    1.  Dependency testing. It's based on the following sub-scores:
        1.  Dependabot score based on the following features:
            1.  Programming languages that are used in a project.
            1.  Package managers that are used in a project.
            1.  If a project uses Dependabot.
            1.  If a project uses GitHub as the main development platform.
        1.  OWASP Dependency Check score based on the following features:
            1.  If and how a project uses OWASP Dependency Check.
            1.  If and how a project sets a CVSS threshold for OWASP Dependency Check to fail the build.
    1.  Memory-safety testing. It's based on the following features:
        1.  Programming languages that are used in a project.
        1.  If an open-source project uses AddressSanitizer.
        1.  If an open-source project uses MemorySanitizer.
        1.  If an open-source project uses UndefinedBehaviorSanitizer.
1.  **Unpatched vulnerabilities score** based on the following features:
    1.  Info about vulnerabilities in open-source project.
1.  **Security awareness score** based on the following features:
    1.  If an open-source project has a security team.
    1.  If an open-source project has a security policy.
    1.  If a project uses signed commits.
    1.  If an open-source project has a bug bounty program.
    1.  If an open-source project signs its artifacts.
    1.  If a project uses Dependabot.
    1.  If an open-source project uses FindSecBugs.
    1.  If an open-source project uses AddressSanitizer.
    1.  If an open-source project uses MemorySanitizer.
    1.  If an open-source project uses UndefinedBehaviorSanitizer.
    1.  If a project uses LGTM.
    1.  If an open-source project is included to OSS-Fuzz project.
    1.  If a project uses nohttp tool.
    1.  If a project uses OWASP Enterprise Security API (ESAPI).
    1.  If a project uses OWASP Java HTML Sanitizer.
    1.  If a project uses OWASP Java Encoder.
    1.  If and how a project uses OWASP Dependency Check.
1.  **Project activity score** based on the following features:
    1.  Number of commits last three months.
    1.  Number of contributors last three months.
1.  **Project popularity score** based on the following features:
    1.  Number of watchers for a GitHub repository.
    1.  Number of stars for a GitHub repository.
1.  **Community commitment score** based on the following features:
    1.  If an open-source project belongs to Apache Foundation.
    1.  If an open-source project belongs to Eclipse Foundation.
    1.  If an open-source project is supported by a company.

## Security ratings for well-known open-source projects

Pre-calculated security ratings for well-known open-source projects can be found
in this [report](oss/security).

---

Next: [Alternatives](alternatives.md)
