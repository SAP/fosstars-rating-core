# Rules of play ratings for open source projects

This section describes a rules of play rating for OSS projects. The rating assesses how well projects care about open source best practices and community health aspects. 
The rating may be used to estimate how well-administered an open source project is and how it adheres to recommendations how an open source project should be set up.

The rules follow the guidelines that the Open Source Program Office (OSPO) at SAP is applying to the company's own open source projects. Those rules and guidelines are mainly based on well established best practices in the open source community to [setup projects for healthy contributions](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions) and to ensure their maintainability in the long run. As there are usually many similarities between open source projects in one organizations, e.g. with respect to the security policy or the code of conduct, many of those community health files [can be stored centrally](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/creating-a-default-community-health-file) so they apply automatically to all projects if they are not overridden by the respective project maintainers. The OSS rules of play ratings take all these aspects into account and are of course also open to changes and extensions by external contributors.

The rating is implemented in the [OssRulesOfPlayRating](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssRulesOfPlayRating.java) class.

[By definition](ratings.md), the rules of play rating produces a score, a label and a confidence score - the same way as the security score.
Here is a list of labels:

1.  `PASSED`: the project implements good measures and in general cares about open source rules of play.
1.  `PASSED_WITH_WARNING`: the project fulfills the minimum expectations of the rules of play compliance, but shows some issues that should be resolved.
1.  `FAILED`: the project doesn't fulfill the minimum expectation of the rules of play compliance.
1.  `UNCLEAR`: there is not enough information to reliably calculate a score and a label for the project.

The [rating procedure](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/rating/oss/OssRulesOfPlayRating.java#L31) checks if any rules that are expected to be [true](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L51) or [false](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L71) match the expectations. If one of the expectations are not met, the project gets the `FAILED` label. 

Furthermore, it checks if any rules that are only recommended to be [true](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L77) or [false](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/score/oss/OssRulesOfPlayScore.java#L83) match the recommendations. In case that the project doesn't implement one or more of them, but otherwise implements all expected ones, it gets the `PASSED_WITH_WARNING` label.

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

To assess these factors, the OSS rules of play rating uses the following info about the open source project:

1.  Whether the project includes a README file or not.
1.  Whether the project's README file contains informative sections that explain the project and its use or not.
1.  Whether the project correctly [specifies a license](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/creating-a-repository-on-github/licensing-a-repository) or not.
1.  Whether the project's license is an expected open source license or not.
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
1.  Whether the project contains a LICENSES folder that includes the licenses referenced by the REUSE Tool or not.
1.  Whether the project is correctly registered with the REUSE Tool or not.
1.  Whether the project repository is compliant with the REUSE Tool specifications or not.
1.  Whether the project specifies a security policy or not.

## Frequent issues with the OSS rules of play

### The license is not detected properly, check failed even with an expected license
We use GitHub's built-in license detection functionality for this check. In case you have a valid license, please check if it was detected properly by GitHub. If you open the LICENSE file via GitHub, you should see an explanatory box showing some license details. If you can't see this box, it probably helps if you include a license from GitHub's templates. Open the LICENSE file via GitHub, switch to the edit mode, and press the "Choose a license template" button to choose the one that applies to your repository.

If you are already using a standard license text and the license is still not detected properly, there may be other files especially in the root folder of your project that interfere with the license detection functionality. GitHub is using [licensee](https://github.com/licensee/licensee) for this. Try installing it locally and run it for your project to see the detailed results. Usually, files that contain "license" or "copyright" are used by licensee to determine the project's license, but may also lead to detection errors if they contain unrelated content. For a detailed overview of what the tool is checking, please [see their documentation](https://github.com/licensee/licensee/blob/master/docs/what-we-look-at.md). In order to resolve such issues, rename the files that are unrelated to the license of your project, so that [licensee's regular expressions](https://github.com/licensee/licensee/blob/master/lib/licensee/project_files/license_file.rb) don't match anymore. Be sure to include the actual LICENSE file in the commit (by changing it slightly, e.g. adding a newline at the end), so that GitHub triggers a new license detection run.

If you've checked everything and the license detection still fails, please create [create an issue](https://github.com/SAP/fosstars-rating-core/issues/new).

### Why do I need to specify a license in the root folder of my project and as well in the LICENSES folder?
GitHub uses [licensee](https://github.com/licensee/licensee) to determine the copyright and licensing information of an open source repository. The results are prominently displayed on the repository landing page and are also made available via the [License API](https://docs.github.com/en/rest/reference/licenses#get-the-license-for-a-repository). Having the licensing information easily available via the landing page facilitates both consumption and contribution use cases for this particular project. The same applies to the GitHub API, especially for integration with third-party tools. Unfortunately, Licensee/GitHub currently doesn't support the license and copyright metadata that is being made available via the [REUSE Tool](https://reuse.software/). The REUSE tool goes beyond the generic license/copyright statement for a complete repository and supports the copyright/license annotation of individual files in a repository. This is especially important if the repository contains code that was copied from other sources and afterwards integrated into the code base. Please see the [REUSE tool introduction](https://reuse.software/) for the details why this is important.

As long as GitHub doesn't integrate the REUSE tool specification, repository maintainers should be asked to support all of the aforementioned scenarios. That makes it necessary to include both a license file in the repository's root folder and the individual licenses in the LICENSES folder that correspond to the REUSE tool metadata. If you want to avoid duplicate files, you can use a symbolic link that points from the main license in the LICENSES folder to the LICENSE file in the root folder. Please note that this mechanism only works in this direction as only the REUSE tool supports symbolic links! As a consequence, the LICENSE file in the root folder always needs to be a physical file and the symbolic link must be placed in the LICENSES folder.

---

Next: [Getting rules of play ratings for open source projects](getting_oss_rules_of_play_rating.md)
