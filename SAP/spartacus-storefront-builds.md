**Status**: **Failed**

**Confidence**: Low (8.95, max confidence value is 10.0)

## Violated rules

1.  **[rl-assigned_teams-1]** Does it have enough teams on GitHub? **No**
1.  **[rl-license_file-1]** Does it have a license file? **No**
1.  **[rl-reuse_tool-1]** Does README mention REUSE? **No**
1.  **[rl-reuse_tool-2]** Does it have LICENSES directory with licenses? **No**
1.  **[rl-reuse_tool-3]** Is it registered in REUSE? **No**
1.  **[rl-reuse_tool-4]** Is it compliant with REUSE rules? **No**
1.  **[rl-vulnerability_alerts-1]** Are vulnerability alerts enabled? **No**


## Warnings

1.  **[rl-contributor_file-1]** Does it have a contributing guideline? **No**
1.  **[rl-contributor_file-2]** Does the contributing guideline have required text? **No**


## Unclear rules

1.  **[rl-license_file-2]** Does it use an allowed license? **unknown**
1.  **[rl-license_file-3]** Does the license have disallowed content? **unknown**


## Passed rules

1.  **[rl-assigned_teams-2]** Does it have an admin team on GitHub? **Yes**
1.  **[rl-assigned_teams-3]** Does it have enough admins on GitHub? **Yes**
1.  **[rl-assigned_teams-4]** Does it have a team with push privileges on GitHub? **Yes**
1.  **[rl-assigned_teams-5]** Does teams have enough members on GitHub? **Yes**
1.  **[rl-contributor_file-1]** Does it have a contributing guideline? **No**
1.  **[rl-contributor_file-2]** Does the contributing guideline have required text? **No**
1.  **[rl-readme_file-1]** Does it have a README file? **Yes**
1.  **[rl-readme_file-2]** If a project's README doesn't contain required info **No**
1.  **[rl-security_policy-1]** Does it have a security policy? **Yes**
1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **No**


## How to fix it

1.  Vulnerability alerts are disabled for your repository. An administrator should enable these alerts via Settings. Please see the GitHub documentation for details or contact the OSPO for more information.
    More info:
    1.  [Managing vulnerabilities in your project's dependencies](https://docs.github.com/en/code-security/supply-chain-security/managing-vulnerabilities-in-your-projects-dependencies)
2.  You should add information about the Developer Certificate of Origin (DCO) to the contributions file. Moreover, be sure that there are no longer any references to the outdated contributor license agreement (CLA) in the file.3.  No license file could be found in your repository. Be sure to add a license that can be detected by GitHub.
    More info:
    1.  [Licensing a repository](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/licensing-a-repository#determining-the-location-of-your-license)
4.  REUSE tool compliance requires that all used licenses are provided in a LICENSES folder on root level.5.  Only one or no team could be found which is assigned. Please make sure to have at least one member and one admin team assigned to the repository. If you need assistance, please contact the OSPO.6.  No contributing guideline file could be found in your repository. Though it's only a recommendation, we'd like to encourage you to add some information that can be detected by GitHub.
    More info:
    1.  [Setting guidelines for repository contributors](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/setting-guidelines-for-repository-contributors#adding-a-contributing-file)
7.  The README of your projects needs to include a link to the results of the REUSE tool scan as well as a licensing section.8.  A registration for the repository could not be found. Please verify that the repository is registered properly.
    More info:
    1.  [Register a project in REUSE](https://api.reuse.software/register)
9.  The latest REUSE tool compliance check resulted in errors. Please perform the check again on your repository and fix the issues.
