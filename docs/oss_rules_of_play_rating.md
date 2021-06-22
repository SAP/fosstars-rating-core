# Rules of play ratings for open source projects

This section describes a rules of play rating for (enterprise) open source projects.
The rating is implemented in the [OssRulesOfPlayRating](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssRulesOfPlayRating.java) class.

The rating assesses how well projects care about open source best practices in an enterprise context and community health aspects. 
The rating may be used to estimate well-administered an open source project is and how it adheres to recommendations how an open source project in an enterprise context should be set up.

[By definition](ratings.md), the rules of play rating produces a score, a label and a confidence score - the same way as the security score.
Here is a list of labels:

1.  `PASSED`: the project implements relatively good measures and in general cares about open source rules of play.
1.  `PASSED_WITH_WARNING`: the compliance to open source rules of play in the project fulfills the minimum expectations, but shows some issues that should be resolved.
1.  `FAILED`: looks like the project doesn't care well about open source rules of play.
1.  `UNCLEAR`: there is no enough data to reliably calculate a score and a label for the project.

The [rating procedure](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssRulesOfPlayRating.java#L31) checks if any rules that are expected to be [true](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L51) or [false](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L71) match the expectations. If one of the expectations are not met, the project gets the `FAILED` label. 

Furthermore, it checks if any rules are only recommended to be [true](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L77) or [false](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L83). In case that the project doesn't implement one or more of these recommendations, but otherwise implements all expected ones, it gets the `PASSED_WITH_WARNING` label.

If the project implements all expected and recommended measures, it gets the `PASSED` label.

If a confidence score is lower than a certain value, then the project gets the `UNCLEAR` label.

## What the OSS rules of play rating takes into account

The OSS rules of play rating assesses the following factors of an open source project:

1.  The license.
1.  The README file.
1.  Its [guidelines for contributors](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/setting-guidelines-for-repository-contributors).
1.  Assigned teams and their permissions.
1.  The use of [Dependabot vulnerability alerts](https://docs.github.com/en/code-security/supply-chain-security/managing-vulnerabilities-in-your-projects-dependencies/about-alerts-for-vulnerable-dependencies).
1.  The use of the [REUSE Tool](https://reuse.software/).
1.  The existence of a [security policy](https://docs.github.com/en/code-security/getting-started/adding-a-security-policy-to-your-repository).

To assess these factors, the the OSS rules of play rating uses the following info about the open source project:

1.  Whether the project includes a README file or not.
1.  Whether the project's README file contains vital sections that explain the project and its use or not.
1.  Whether the project correctly [specifies a license](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/creating-a-repository-on-github/licensing-a-repository) or not.
1.  Whether the project's license is a permissive open source license or not.
1.  Whether the project's license contains unwanted extensions to the official text.
1.  Whether the project specifies [guidelines for contributors](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/setting-guidelines-for-repository-contributors) or not.
1.  Whether the project contribution guidelines use the Developer Certificate of Origin or not.
1.  Whether the project has at least two teams assigned or not.
1.  Whether the project has a team with admin privileges (project administrators) or not.
1.  Whether the project's administrators team has at least two members or not.
1.  Whether the project has a team with write privileges (project members) or not.
1.  Whether the project's members team has at least two members or not.
1.  Whether the project uses [Dependabot vulnerability alerts](https://docs.github.com/en/code-security/supply-chain-security/managing-vulnerabilities-in-your-projects-dependencies/about-alerts-for-vulnerable-dependencies) or not.
1.  Whether the project's README references licensing information using the [REUSE Tool](https://reuse.software/) or not.
1.  Whether the project contains a LICENSES folder that includes the licenses referenced using the REUSE Tool or not.
1.  Whether the project is correctly registered with the REUSE Tool or not.
1.  Whether the project repository is compliant with the REUSE Tool specifications or not.
1.  Whether the project specifies a security policy or not.

## OSS rules of play configuration

tbc.

## Frequent issues with the OSS rules of play

### The license is not detected properly, check failed even with a permissive license
We use GitHub's built-in license detection functionality for this check. In case you have a valid license, please check if it was detected properly by GitHub. If you open the LICENSE file via GitHub, you should see an explanatory box showing some license details. If you can't see this box, it probably helps if you include a license from GitHub's templates. Open the LICENSE file via GitHub, switch to the edit mode, and press the "Choose a license template" button to choose the one that applies to your repository. If everything is correct and this error comes up in error, be sure to [create an issue](https://github.com/SAP/fosstars-rating-core/issues/new).

---

Next: [???](???.md)