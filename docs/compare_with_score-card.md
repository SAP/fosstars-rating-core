# Compare Fosstars rating core and OSSF Score card

This page contains a comparison between the features provided by Fosstars and OSSF ScoreCard. The comparison is based on the releases available in December 2021 ([Fosstars `1.5.0`](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.5.0) and [OSSF ScoreCard `3.2.1`](https://github.com/ossf/scorecard/releases/tag/v3.2.1)).
Fosstars scores and OSSF Scorecard checks are a bit different and hence can not be mapped one to one. Hence, what is in this comparison mentioned as Features can be (more or less) mapped to a Fosstars Score and Scorecard check.

## Feature matrix

| Feature  |  OSSF ScoreCard (Weight)  | Fosstars (Weight) | Comments |
|----------|:-------------:|:------:|:------:|
| Binary-Artifacts |  ✅ (high) | ⛔️ | - |
| Branch protection |  ✅ (high) | ⛔️ | - |
| Code Review |  ✅ (high) | ⛔️ | - |
| Dangerous-Workflow |  ✅ (high) | ⛔️ | - |
| Signed-Releases |  ✅ (high) | ⛔️ | - |
| Token-Permissions |  ✅ (high) | ⛔️ | - |
| LGTM score |  ⛔️ | ✅ (high) | - |
| CodeQL use |  ⛔️ | ✅ (high) | - |
| OWASP Dependency Check |  ⛔️ | ✅ (high) | - |
| Dependency-Update-Tool |  ✅ (high) | ✅ (high) | - |
| Maintained |  ✅ (high) | ✅ (high) | - |
| Vulnerabilities |  ✅ (high) | ✅ (high) | - |
| Project activity |  ⛔️ | ✅ (medium) | - |
| Project popularity |  ⛔️ | ✅ (medium) | - |
| Community commitment |  ⛔️ | ✅ (medium) | - |
| Packaging |  ✅ (medium) | ⛔️ | - |
| Pinned-Dependencies |  ✅ (medium) | ⛔️ | - |
| Fuzzing |  ✅ (medium) | ✅ (medium) | - |
| SAST |  ✅ (medium) | ✅ (medium) | - |
| Security-Policy |  ✅ (medium) | ✅ (medium) | - |
| License |  ✅ (low) | ✅ (low) | - |
| Run CI-Tests |  ✅ (low) | ⛔️ | - |
| CII-Best-Practices |  ✅ (low) | ⛔️ | - |
| Contributors |  ✅ (low) | ⛔️ | - |

## Scorecard features not available in Fosstars

### High Weighted scores

#### Binary-Artifacts
Scan the projects to detect any existence of executables (.class, .pyc....). In terms of security aspect it makes sense to scan through the project and identify if there are any security threats.
But there are projects which also stores binary executables as part of the project
  - Maybe for a third party lib usage
  - Or a way of providing an executable

These projects will get a BAD score.

#### Branch protection
Checks if the default and release branches are protected by [protection settings](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches). To check this an Admin privilege token needs to be used. Some of the main rules and regulations
- Require code review: requires at least one reviewer, which greatly reduces the risk that a compromised contributor can inject malicious code. Review also increases the likelihood that an unintentional vulnerability in a contribution will be detected and fixed before the change is accepted.
- Prevent force push: prevents use of the `--force` command on public branches, which overwrites code irrevocably. This protection prevents the rewriting of public history without external notice.
- Require [status checks](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/collaborating-on-repositories-with-code-quality-features/about-status-checks): ensures that all required CI tests are met before a change is accepted.

Also the evaluation is done in Tiers. i.e if all the conditions meet then high value, otherwise lower values.

#### Code Review
Check if the project enables mandatory code review before a PR is merged.

#### Dangerous-Workflow
This check determines whether the project's GitHub Action workflows has dangerous code patterns. Some examples of these patterns are untrusted code checkouts, logging github context and secrets, or use of potentially untrusted inputs in scripts.
More information [here](https://securitylab.github.com/research/github-actions-preventing-pwn-requests/).
If no dangerous work flows exist than high score otherwise low.

#### Signed-Releases
This check tries to determine if the project cryptographically signs release artifacts. It is currently limited to repositories hosted on GitHub, and does not support other source hosting repositories (i.e., Forges).

Signed releases attest to the provenance of the artifact.

This check looks for the following filenames in the project's last five releases: *.minisig , *.asc (pgp), *.sig, *.sign.

#### Token-Permissions
This check determines whether the project's automated workflows tokens are set to read-only by default. It is currently limited to repositories hosted on GitHub, and does not support other source hosting repositories (i.e., Forges).
If readonly tokens are used more secure. High score.

### Medium Weighted scores

#### Packaging
This check tries to determine if the project is published as a package. It is currently limited to repositories hosted on GitHub, and does not support other source hosting repositories (i.e., Forges).

You can create a package in several ways:

- Many program language ecosystems have a generally-used packaging format supported by a language-level package manager tool and public package repository.
- Many operating system platforms also have at least one package format, tool, and public repository (in some cases the source repository generates system-independent source packages, which are then used by others to generate system executable packages).
- Using container images.

#### Pinned-Dependencies

This check tries to determine if the project is an application that has declared and pinned its dependencies. A "pinned dependency" is a dependency that is explicitly set to a specific version instead of allowing a range of versions. It is currently limited to repositories hosted on GitHub, and does not support other source hosting repositories (i.e., Forges). If this project is a library (not an application), this check should automatically pass (but see limitations below).

The check works by looking for:
- the following files in the root directory: go.mod, go.sum (Golang), package-lock.json, npm-shrinkwrap.json (Javascript), requirements.txt, pipfile.lock (Python), gemfile.lock (Ruby), cargo.lock (Rust), yarn.lock (package manager), composer.lock (PHP), vendor/, third_party/, third-party/;
- unpinned dependencies in Dockerfiles, shell scripts and GitHub workflows.

This ensures that the dependency and the project management is more in control.
However, pinning dependencies can inhibit software updates, either because of a security vulnerability or because the pinned version is compromised. Mitigate this risk by:

- having applications and not libraries pin to specific versions;
- using automated tools to notify applications when their dependencies are outdated;
- quickly updating applications that do pin dependencies.

### Low Weighted scores

#### Run CI-Tests
Checks if the projects runs tests on PR before the merge. The check works by looking for a set of CI-system names in GitHub `CheckRuns` and `Statuses` among the recent commits (~30). A CI-system is considered well-known if its name contains any of the following: appveyor, buildkite, circleci, e2e, github-actions, jenkins, mergeable, test, travis-ci.

#### CII-Best-Practices
Checks whether the project has earned [CII Best Coding practices followed](https://bestpractices.coreinfrastructure.org/ru/login) badge. The [criterion](https://bestpractices.coreinfrastructure.org/en/criteria/0)
To earn the passing badge, the project MUST:
- publish the process for reporting vulnerabilities on the project site
- provide a working build system that can automatically rebuild the software from source code (where applicable)
- have a general policy that tests will be added to an automated test suite when major new functionality is added
- meet various cryptography criteria where applicable
- have at least one primary developer who knows how to design secure software
- have at least one primary developer who knows of common kinds of errors that lead to vulnerabilities in this kind of software (and at least one method to counter or mitigate each of them)
- apply at least one static code analysis tool (beyond compiler warnings and "safe" language modes) to any proposed major production release.

If the badge is present high score.

#### Contributors
This check tries to determine if the project has recent contributors from multiple organizations (e.g., companies). It is currently limited to repositories hosted on GitHub, and does not support other source hosting repositories (i.e., Forges).

The check looks at the Company field on the GitHub user profile for authors of recent commits. To receive the highest score, the project must have had contributors from at least 3 different companies in the last 30 commits; each of those contributors must have had at least 5 commits in the last 30 commits.

## Features available in Scorecard and Fosstars

### High Weighted scores

#### Dependency-Update-Tool
This check tries to determine if the project uses a dependency update tool, specifically [dependabot](https://github.com/dependabot) or [renovatebot](https://docs.renovatebot.com/configuration-options/). If present high score.

#### Maintained

This check determines whether the project is actively maintained. If the project is archived, it receives the lowest score. If there is at least one commit per week during the previous 90 days, the project receives the highest score.
In rating core we have something similar to determine the 
- Any commits in last 3 months
- Artifact release trend by analyzing the release spectrum.

#### Vulnerabilities
OSSF score card uses [OSV](https://osv.dev/), to detect the list of vulnerabilities if present for the project.
Rating core uses
- NVD scanning using CPE mapping 
- OWASP dependency check for maven artifacts

Rating core detection of vulnerabilities is not the most efficient way.

### Medium Weighted scores

#### Fuzzing
This check tries to determine if the project uses [fuzzing](https://owasp.org/www-community/Fuzzing) by checking if the repository name is included in the [OSS-Fuzz](https://github.com/google/oss-fuzz) project list.
Fuzz testing is a well-known technique for uncovering programming errors in software. Many of these detectable errors, like buffer overflow, can have serious security implications. Google has found thousands of security vulnerabilities and stability bugs by deploying guided in-process fuzzing of Chrome components, and we now want to share that service with the open source community.
If the project is enrolled in OSS-Fuzz then high score.

#### SAST
Check if a SAST plugin of some sort exists. Ensuring that the project is checked for unknown bugs.
Rating core has something similar 
- CodeQL scans badge
- FindSecBugs
- NoHttpTool
- Bandit (To be implemented)
- Memory Sanitizers

#### Security-Policy
Check if any security policy exist as part of the project. Usually stored in Security tab of the GitHub repo.

### Low Weighted scores

#### License
Checks if the project has a Published license. Fosstars rating core has something similar where it check 
- if the project belongs to a well known organization 
  - Is Apache
  - Is Eclipse
- if the executables released are signed artifacts (To be implemented in rating core)

## Fosstars features not available in OSSF Scorecard

### High Weighted scores

#### OWASP Dependency Check
Checks how is OWASP Dependency Check used and what is the threshold for OWASP Dependency Check.

#### LGTM score
Checks the programming languages used and the worst LGTM grade of the project.

### Medium Weighted scores

#### Project activity
Evaluates how active a project is. It’s based on number of commits and contributors in the last 3 months.

#### Project popularity
The project popularity is based on number of stars and watchers. Here is how a number of stars contributes to the score: 0 -> 0.00 (min), 2500 -> 2.50, 5000 -> 5.00, 10000 -> 10.00 (max) Here is how a number of watchers contributes to the score: 0 -> 0.00 (min), 450 -> 1.50, 750 -> 2.50, 3000 -> 10.00 (max)

#### Community commitment
Checks if the community is backed by a company or a foundation (e.g. Apache, Eclipse, ...)
