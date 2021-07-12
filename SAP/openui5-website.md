**Status**: **Failed**

**Confidence**: Max (10.0, max confidence value is 10.0)

## Violated rules
1.  [**[rl-license_file-2]**](#rl-license_file-2) Does it use an allowed license? **No**


## Warnings
1.  [**[rl-readme_file-2]**](#rl-readme_file-2) Is README incomplete? **Yes**
1.  [**[rl-vulnerability_alerts-2]**](#rl-vulnerability_alerts-2) Does it have unresolved vulnerability alerts? **Yes**
1.  [**[rl-contributor_file-2]**](#rl-contributor_file-2) Does the contributing guideline have required text? **No**




## Passed rules
1.  **[rl-readme_file-1]** Does it have a README file? **Yes**
1.  **[rl-assigned_teams-4]** Does it have a team with push privileges on GitHub? **Yes**
1.  **[rl-assigned_teams-5]** Does teams have enough members on GitHub? **Yes**
1.  **[rl-vulnerability_alerts-1]** Are vulnerability alerts enabled? **Yes**
1.  **[rl-license_file-1]** Does it have a license file? **Yes**
1.  **[rl-reuse_tool-2]** Does it have LICENSES directory with licenses? **Yes**
1.  **[rl-assigned_teams-2]** Does it have an admin team on GitHub? **Yes**
1.  **[rl-assigned_teams-3]** Does it have enough admins on GitHub? **Yes**
1.  **[rl-security_policy-1]** Does it have a security policy? **Yes**
1.  **[rl-assigned_teams-1]** Does it have enough teams on GitHub? **Yes**
1.  **[rl-contributor_file-1]** Does it have a contributing guideline? **Yes**
1.  **[rl-license_file-3]** Does the license have disallowed content? **No**
1.  **[rl-reuse_tool-1]** Does README mention REUSE? **Yes**
1.  **[rl-reuse_tool-3]** Is it registered in REUSE? **Yes**
1.  **[rl-reuse_tool-4]** Is it compliant with REUSE rules? **Yes**


## What is wrong, and how to fix it

### rl-license_file-2

NOASSERTION is not allowed

A license file was detected, but it doesn't seem to be approved by SAP. Under normal circumstances, we use Apache 2.0 exclusively for our open source projects. Of course, exceptions apply and should be covered by this check.


### rl-readme_file-2

The README does not contain required text that should match '# Requirements', '# Download and [Ii]nstallation', '# How to obtain support'

### rl-vulnerability_alerts-2

The project has unresolved vulnerability alerts

### rl-contributor_file-2

You should add information about the Developer Certificate of Origin (DCO) to the contributions file. Moreover, be sure that there are no longer any references to the outdated contributor license agreement (CLA) in the file.



