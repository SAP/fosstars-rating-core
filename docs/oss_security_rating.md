# Security ratings for open-source projects

This section describes a security rating for open-source projects.
The rating is implemented in the [OssSecurityRating](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating.java) class.

The rating may be used to assess how well open-source projects and their communities care about security. 
This may then be used to estimate security risks that may affect an application when it uses open-source components.

[By definition](ratings.md), the security rating produces a score, a label and a confidence score.
Here is a list of labels:

1.  `GOOD`: the project implements relatively good security measures and in general cares about security.
1.  `MODERATE`: the security level in the project is not good but definitely not too bad.
1.  `BAD`: looks like the project doesn't care well about security.
1.  `UNCLEAR`: there is no enough data to reliably calculate a score and a label for the project.

The security rating uses
[thresholds](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating.java#L84)
for score and confidence to assign one of the labels to an open-source project.
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

The main goal of the method is to reduce a possible bias that may be introduced if experts set the thresholds directly.
Instead of setting the thresholds directly, the experts gives a list of well-known projects in the industry and specify
a desired fraction for the labels. At the moment, it is 20-50-30 that looks like a normal distribution.
As a result, the thresholds don't set a bar totally based on someone's opinion.
Instead, other open-source projects are compared with the real, well-known, established and trusted ones.

The procedure for calculating the thresholds is implemented in 
[SecurityRatingAnalysis notebook](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/jupyter/oss/security/SecurityRatingAnalysis.ipynb).

## What the security rating takes into account

The security rating tries to evaluate the following for an open-source project:

1.  How well the project implements security testing.
1.  If the project has vulnerabilities that have not been fixed.
1.  How well the community is aware about security.
1.  How the project is active.
1.  How the project is popular.
1.  How the community commits to support the project.

For example, here is a [detailed report](oss/security/curl/curl.md)
that shows all sub-scores, structure and data that were used to calculate a rating for curl.

## Security ratings for well-known open-source projects

Pre-calculated security ratings for well-known open-source projects can be found
in this [report](oss/security).

---

Next: [Getting the security ratings](getting_oss_security_rating.md)
