**Status**: **Failed**

**Confidence**: Low (8.95, max confidence value is 10.0)

## Violated rules

1.  **[rl-license_file-1]** Does it have a license file? **No**
1.  **[rl-reuse_tool-1]** Does README mention REUSE? **No**
1.  **[rl-reuse_tool-3]** Is it registered in REUSE? **No**
1.  **[rl-reuse_tool-4]** Is it compliant with REUSE rules? **No**


## Warnings

1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **Yes**


## Unclear rules

1.  **[rl-license_file-2]** Does it use an allowed license? **unknown**
1.  **[rl-license_file-3]** Does the license have disallowed content? **unknown**


## Passed rules

1.  **[rl-assigned_teams-1]** Does it have enough teams on GitHub? **Yes**
1.  **[rl-assigned_teams-2]** Does it have an admin team on GitHub? **Yes**
1.  **[rl-assigned_teams-3]** Does it have enough admins on GitHub? **Yes**
1.  **[rl-assigned_teams-4]** Does it have a team with push privileges on GitHub? **Yes**
1.  **[rl-assigned_teams-5]** Does teams have enough members on GitHub? **Yes**
1.  **[rl-contributor_file-1]** Does it have a contributing guideline? **Yes**
1.  **[rl-contributor_file-2]** Does the contributing guideline have required text? **Yes**
1.  **[rl-readme_file-1]** Does it have a README file? **Yes**
1.  **[rl-readme_file-2]** If a project's README doesn't contain required info **No**
1.  **[rl-reuse_tool-2]** Does it have LICENSES directory with licenses? **Yes**
1.  **[rl-security_policy-1]** Does it have a security policy? **Yes**
1.  **[rl-vulnerability_alerts-1]** Are vulnerability alerts enabled? **Yes**
1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **Yes**


## How to fix it

1.  No license file could be found in your repository. Be sure to add a license that can be detected by GitHub.
    More info:
    1.  [Licensing a repository](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/licensing-a-repository#determining-the-location-of-your-license)
2.  The README of your projects needs to include a link to the results of the REUSE tool scan as well as a licensing section.3.  A registration for the repository could not be found. Please verify that the repository is registered properly.
    More info:
    1.  [Register a project in REUSE](https://api.reuse.software/register)
4.  The latest REUSE tool compliance check resulted in errors. Please perform the check again on your repository and fix the issues.