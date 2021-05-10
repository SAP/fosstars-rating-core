**Status**: **Failed**

**Confidence**: Max (10.0, max confidence value is 10.0)

## Violated rules

1.  **[rl-contributor_file-2]** Does the contributing guideline have required text? **No**
1.  **[rl-reuse_tool-1]** Does README mention REUSE? **No**
1.  **[rl-vulnerability_alerts-1]** Are vulnerability alerts enabled? **No**


## Warnings

1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **Yes**




## Passed rules

1.  **[rl-assigned_teams-1]** Does it have enough teams on GitHub? **Yes**
1.  **[rl-assigned_teams-2]** Does it have an admin team on GitHub? **Yes**
1.  **[rl-assigned_teams-3]** Does it have enough admins on GitHub? **Yes**
1.  **[rl-assigned_teams-4]** Does it have a team with push privileges on GitHub? **Yes**
1.  **[rl-assigned_teams-5]** Does teams have enough members on GitHub? **Yes**
1.  **[rl-contributor_file-1]** Does it have a contributing guideline? **Yes**
1.  **[rl-license_file-1]** Does it have a license file? **Yes**
1.  **[rl-license_file-2]** Does it use an allowed license? **Yes**
1.  **[rl-license_file-3]** Does the license have disallowed content? **No**
1.  **[rl-readme_file-1]** Does it have a README file? **Yes**
1.  **[rl-reuse_tool-2]** Does it have LICENSES directory with licenses? **Yes**
1.  **[rl-reuse_tool-3]** Is it registered in REUSE? **Yes**
1.  **[rl-reuse_tool-4]** Is it compliant with REUSE rules? **Yes**
1.  **[rl-security_policy-1]** Does it have a security policy? **Yes**
1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **Yes**


## How to fix it

1.  Vulnerability alerts are disabled for your repository. An administrator should enable these alerts via Settings. Please see the GitHub documentation for details or contact the OSPO for more information.
    More info:
    1.  [Managing vulnerabilities in your project's dependencies](https://docs.github.com/en/code-security/supply-chain-security/managing-vulnerabilities-in-your-projects-dependencies)
2.  You should add information about the Developer Certificate of Origin (DCO) to the contributions file. Moreover, be sure that there are no longer any references to the outdated contributor license agreement (CLA) in the file.3.  In earlier times, SAP requested projects to add an API usage section to the LICENSE file. As the LICENSE file should only contain the native license text and the API section has moved to the dep5 file of the REUSE project information, we ask all projects to remove the API section from the LICENSE file.4.  The README of your projects needs to include a link to the results of the REUSE tool scan as well as a licensing section.
