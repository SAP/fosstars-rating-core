**Status**: **Failed**

**Confidence**: Max (10.0, max confidence value is 10.0)

## Violated rules
1.  [**[rl-reuse_tool-1]**](#rl-reuse_tool-1) Does README mention REUSE? **No**
1.  [**[rl-reuse_tool-3]**](#rl-reuse_tool-3) Is it registered in REUSE? **No**
1.  [**[rl-reuse_tool-4]**](#rl-reuse_tool-4) Is it compliant with REUSE rules? **No**


## Warnings
1.  [**[rl-contributor_file-2]**](#rl-contributor_file-2) Does the contributing guideline have required text? **No**
1.  [**[rl-contributor_file-1]**](#rl-contributor_file-1) Does it have a contributing guideline? **No**




## Passed rules
1.  **[rl-readme_file-1]** Does it have a README file? **Yes**
1.  **[rl-assigned_teams-4]** Does it have a team with push privileges on GitHub? **Yes**
1.  **[rl-assigned_teams-5]** Does teams have enough members on GitHub? **Yes**
1.  **[rl-vulnerability_alerts-1]** Are vulnerability alerts enabled? **Yes**
1.  **[rl-readme_file-2]** Is README incomplete? **No**
1.  **[rl-license_file-1]** Does it have a license file? **Yes**
1.  **[rl-reuse_tool-2]** Does it have LICENSES directory with licenses? **Yes**
1.  **[rl-assigned_teams-2]** Does it have an admin team on GitHub? **Yes**
1.  **[rl-assigned_teams-3]** Does it have enough admins on GitHub? **Yes**
1.  **[rl-security_policy-1]** Does it have a security policy? **Yes**
1.  **[rl-assigned_teams-1]** Does it have enough teams on GitHub? **Yes**
1.  **[rl-license_file-3]** Does the license have disallowed content? **No**
1.  **[rl-vulnerability_alerts-2]** Does it have unresolved vulnerability alerts? **No**
1.  **[rl-license_file-2]** Does it use an allowed license? **Yes**


## What is wrong, and how to fix it

### rl-reuse_tool-1

The README does not seem to have a badge that points to REUSE status (https://api.reuse.software/info/github.com/SAP/stewardci-log-forwarding)

The README of your projects needs to include a link to the results of the REUSE tool scan as well as a licensing section.


### rl-reuse_tool-3

The project is not registered in REUSE

A registration for the repository could not be found. Please verify that the repository is registered properly.
*  [Register a project in REUSE](https://api.reuse.software/register)


### rl-reuse_tool-4

The project is not registered in REUSE

The latest REUSE tool compliance check resulted in errors. Please perform the check again on your repository and fix the issues.


### rl-contributor_file-2

You should add information about the Developer Certificate of Origin (DCO) to the contributions file. Moreover, be sure that there are no longer any references to the outdated contributor license agreement (CLA) in the file.


### rl-contributor_file-1

No contributing guideline file could be found in your repository. Though it's only a recommendation, we'd like to encourage you to add some information that can be detected by GitHub.
*  [Setting guidelines for repository contributors](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/setting-guidelines-for-repository-contributors#adding-a-contributing-file)



