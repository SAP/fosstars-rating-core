# Getting the OSS rules of play ratings

The page describes how a Rules of Play (RoP) rating may be calculated for an open source project.

## Configuration

Several of the rules need to be configured as the detailed guidelines differ from project to project,
for example, which licenses may be allowed in your organization, or which sections you want to enforce in the README file.
Thus, in order to get proper OSS rules of play ratings, you need to provide some details.
These details are usually passed to Fosstars via YAML configuration files for the respective data providers. 
Below you find the configurable data providers, information how to name the configuration files and the possible options in the respective configuration file.
The section about the command-line tool further below explains how to pass these configuration files to Fosstars.
The configuration that SAP uses for their OSS rules of play check run can be found in the [fosstars-oss-rules-of-play-sap-report](https://github.com/SAP/fosstars-rating-core/tree/fosstars-oss-rules-of-play-sap-report) branch.

### Contributing Guidelines
The data provider `ContributingGuidelineInfo` allows you to specify 2 types of regular expression patterns 
- The content that you expect to be part of the contributing guidelines.
- The content that you expect must not be part of the contributing guidelines.
The option is called `requiredContentPatterns` and expects a list of one or multiple Regex. If you enter multiple entries, the contribution guidelines must comply with all the patterns for the rule to pass.

### Code of Conduct Guidelines
The data provider `CodeOfConductGuidelineInfo` allows you to specify 1 type of regular expression pattern
- The content that you expect to be part of the code of conduct guidelines.
The option is called `requiredContentPatterns` and expects a list of one Regex. If you enter multiple entries, the code of conduct guidelines must comply with all the patterns for the rule to pass.

### Code Scan Alerts
The data provider `CodeqlDataProvider` allows you to check for the following things -
1. If the code scans are enabled for the project or not.
2. If the code scans are availble on pull requests or not.
The information about the codeQL scans can be found [here](https://docs.github.com/en/code-security/code-scanning/automatically-scanning-your-code-for-vulnerabilities-and-errors/about-code-scanning-with-codeql).

### Licensing
The data provider `LicenseInfo` allows you to specify which license(s) should be allowed in the analyzed project.
This option is called `allowedLicenses` and expects a list of one or more SPDX license IDs.
In addition you can check for terms that must not in the license (e.g. to avoid modifications) using regular expression patterns.
The corresponding option is called `disallowedLicensePatterns`, and it accepts a list of patterns, but can also be left empty.
Finally, you are also able to specify exceptions, i.e. which repositories shall not be checked with this data provider, for example, because certain repositories are known to use other licenses, but have gone through an exception process.
The option is called `repositoryExceptions` and can accept a list of full repository URLs, e.g. `https://github.com/SAP/fosstars-rating-core`, but can also be left empty if there are no exceptions.

### Readme
The data provider `ReadmeInfo` allows you to specify which terms or patterns you expect to see in a README file.
The corresponding option `requiredContentPatterns` expects a list of one or more regular expression patterns.
If you enter multiple entries, the README file content must match all patterns for the rule to pass.

### REUSE tool
The data provider `UseReuseDataProvider` allows you to specify exceptions, i.e. which repositories shall not be checked with this data provider, for example, because certain repositories are known to use other means of annotating copyright statements, but have gone through an exception process.
The option is called `repositoryExceptions` and can accept a list of full repository URLs, e.g. `https://github.com/SAP/fosstars-rating-core`, but can also be left empty if there are no exceptions.

## Rule IDs and Documentation URL

In order to be able to identify a certain rule better in the final result report, e.g. for follow-up actions, Fosstars expects that all rules are configured with a customized rule ID. That configuration needs to be in a file called `OssRulesOfPlayRatingMarkdownFormatter.config.yaml` and should be placed in the current working directory. The relevant option is called `ruleIds` and expects a list of key/value pairs where the key is the customized rule ID and the value the descriptive name of the rule that is maintained in the [OssFeatures class](../src/main/java/com/sap/oss/phosphor/fosstars/model/feature/oss/OssFeatures.java). 
Moreover, you should provide a documentation URL that explains your OSS rules of play configuration and the expected content of the checked repositories. The corresponding option is called `documentationUrl` and expects a single string pointing to the documentation web page.
The configuration that SAP uses for their OSS rules of play check run can be accessed in the [fosstars-oss-rules-of-play-sap-report](https://github.com/SAP/fosstars-rating-core/blob/fosstars-oss-rules-of-play-sap-report/OssRulesOfPlayRatingMarkdownFormatter.config.yml) branch.

## Command-line tool for calculating OSS rules of play ratings

The command-line tool can be used to calculate OSS Rules of Play ratings.

The following commands download the source code and build the command-line tool with Maven:

```
git clone https://github.com/SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
```

To calculate an OSS rules of play rating for an open source project, the command-line tool needs a URL to its source code management system (SCM). Currently, the tool works best with projects that stay on GitHub.

### Calculating the rating by providing command-line parameters

A URL to the repository can be passed to the tool by using `--url` command-line parameter.
For example, here is how an OSS rules of play rating may be calculated for the repository `code-pal-for-abap` in the GitHub organization `SAP`:

```
java -jar target/fosstars-github-rating-calc.jar --url https://github.com/SAP/code-pal-for-abap --token ${TOKEN} --rating oss-rules-of-play --data-provider-configs=./LicenseInfo.yml,./ContributingGuidelineInfo.yml,ReadmeInfo.yml --verbose
```

The environment variable `TOKEN` contains a token for accessing the GitHub API. You can create a personal token in the [settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub. **Please note:** In order to work correctly, the token needs to have elevated permissions. To get the status of vulnerability alerts, the user who issued the token [must have admin access to the respective repository](https://docs.github.com/en/rest/reference/repos#check-if-vulnerability-alerts-are-enabled-for-a-repository). To get the correct team assignment status, the token [requires the `read:org` scope](https://docs.github.com/en/rest/reference/teams).

Be sure to use the option `--rating` with the value `oss-rules-of-play` as otherwise you won't get the OSS rules of play rating, but the security rating for the repository. 
The option `--data-provider-configs` expects a comma-separated list of YAML configuration files for the individual data providers as outlined above. Be sure to name configuration files according to the data provider IDs so that they can be found properly.
In addition, remember to keep the `OssRulesOfPlayRatingMarkdownFormatter.config.yaml` in the current working directory as explained above.

If everything is correctly set up, the output is going to look like the following:

```
[+] Okay, we have a GitHub token, let's try to use it
[+] Let's gather info and calculate a rating for:
[+]   https://github.com/SAP/ui5-webcomponents-react
[+] Gathering info about project's README file ...
[+] Cloning https://github.com/SAP/ui5-webcomponents-react ...
[+] Figuring out how the project uses CodeQL ...
[+] Fetching info about project's teams ...
[+] Gathering info about project's code of conduct guidelines ...
[+] Gathering info about vulnerability alerts ...
[+] Gathering info about project's contributing guidelines ...
[+] Gathering info about project's license ...
[+] Figuring out how the project uses REUSE ...
[+] Figuring out if the project has a security policy ...
[+] Here is what we know about the project:
[+]    Does the license have disallowed content? No
[+]    Does it have LICENSES directory with licenses? Yes
[+]    Does it have a README file? Yes
[+]    Does it have a code of conduct guideline? Yes
[+]    Does it have a contributing guideline? Yes
[+]    Does it have a license file? Yes
[+]    Does it have a team with push privileges on GitHub? Yes
[+]    Does it have an admin team on GitHub? Yes
[+]    Does it have enough admins on GitHub? Yes
[+]    Does teams have enough members on GitHub? Yes
[+]    Does it have enough teams on GitHub? Yes
[+]    Does it have unresolved vulnerability alerts? Yes
[+]    Is it compliant with REUSE rules? Yes
[+]    Is it registered in REUSE? Yes
[+]    Does it use CodeQL checks for pull requests? Yes
[+]    Does it run CodeQL scans? Yes
[+]    Does the project use REUSE? Yes
[+]    Does it use an allowed license? Yes
[+]    Is README incomplete? No
[+]    Does the code of conduct guideline have required text? Yes
[+]    Does the contributing guideline have required text? Yes
[+]    Does it have a security policy? Yes
[+]    Does README mention REUSE? Yes
[+]    Are vulnerability alerts enabled? Yes
[+] Here is how the rating was calculated:
[+]   Score:........Open source rules or play score
[+]   Description:..The score shows whether an open source project
[+]                 violates rules or not.
[+]   Value:........9,0  out of 10,0
[+]   Confidence:...Max (10,0 out of 10,0)
[+]   Based on:...23 features
[+]       Are vulnerability alerts enabled?.........................Yes
[+]       Does README mention REUSE?................................Yes
[+]       Does it have LICENSES directory with licenses?............Yes
[+]       Does it have a README file?...............................Yes
[+]       Does it have a code of conduct guideline?.................Yes
[+]       Does it have a contributing guideline?....................Yes
[+]       Does it have a license file?..............................Yes
[+]       Does it have a security policy?...........................Yes
[+]       Does it have a team with push privileges on GitHub?.......Yes
[+]       Does it have an admin team on GitHub?.....................Yes
[+]       Does it have enough admins on GitHub?.....................Yes
[+]       Does it have enough teams on GitHub?......................Yes
[+]       Does it have unresolved vulnerability alerts?.............Yes
[+]       Does it run CodeQL scans?.................................Yes
[+]       Does it use CodeQL checks for pull requests?..............Yes
[+]       Does it use an allowed license?...........................Yes
[+]       Does teams have enough members on GitHub?.................Yes
[+]       Does the code of conduct guideline have required text?....Yes
[+]       Does the contributing guideline have required text?.......Yes
[+]       Does the license have disallowed content?.................No
[+]       Is README incomplete?.....................................No
[+]       Is it compliant with REUSE rules?.........................Yes
[+]       Is it registered in REUSE?................................Yes
[+]   Explanation:..Found 1 recommendations
[+] 
[+] Rating:     9,0  out of 10,0 -> PASSED_WITH_WARNING
[+] Confidence: Max (10,0 out of 10,0)
[+] 
[+] Bye!
```

The tool prints out the gathered data and a status for the project.


### Creating GitHub issues for failed rules

Exclusively for the OSS rules of play rating, Fosstars can create GitHub issues for failed rules directly in the affected repositories so the issues can be followed up by the repository owners. If you want to do that, add the command-line option `--create-issues` or enable `ISSUES` reporter in a config file passes through `--config` options.
Fosstars will inform you on the command-line about the issue creation process as well:

```
[+] Creating issues for findings on https://github.com/<organization>/<repository>
[+] New issue: [rop-license_file-3] Violation against OSS Rules of Play
[+] New issue: [rop-vulnerability_alerts-1] Violation against OSS Rules of Play
[+] New issue: [rop-reuse_tool-2] Violation against OSS Rules of Play
[+] New issue: [rop-security_policy-1] Violation against OSS Rules of Play
[+] New issue: [rop-license_file-2] Violation against OSS Rules of Play
[+] New issue: [rop-reuse_tool-1] Violation against OSS Rules of Play
[+] New issue: [rop-reuse_tool-3] Violation against OSS Rules of Play
[+] New issue: [rop-reuse_tool-4] Violation against OSS Rules of Play
```

Here is an example of a configuration file the scans projects in an organization on GitHub, creates a Markdown report and opens issues for violated rules:

cache: .fosstars/project_rating_cache.json
reports:
  - type: markdown # create a Markdown report in the current directory
    where: ./
  - type: ISSUES # open issues for violated rules
finder:
  organizations: # scan project in the SAP organization
    - name: SAP

Of course, it won't create duplicates of already existing GitHub issues. Fosstars uses the previously defined rule IDs to check if the repository already contains an issue with the same ID that is not closed yet. In such cases, the command-line output will also report that accordingly:

```
[+] Creating issues for findings on https://github.com/<organization>/<repository>
[+] Issue already existing: [rop-license_file-3] Violation against OSS Rules of Play
[+] New issue: [rop-vulnerability_alerts-1] Violation against OSS Rules of Play
[+] Issue already existing: [rop-reuse_tool-2] Violation against OSS Rules of Play
[+] Issue already existing: [rop-security_policy-1] Violation against OSS Rules of Play
[+] Issue already existing: [rop-license_file-2] Violation against OSS Rules of Play
[+] Issue already existing: [rop-reuse_tool-1] Violation against OSS Rules of Play
[+] Issue already existing: [rop-reuse_tool-3] Violation against OSS Rules of Play
[+] Issue already existing: [rop-reuse_tool-4] Violation against OSS Rules of Play  
```

### Other options

Other command-line options are as well available for use with the OSS rules of play rating. Please refer to the [security rating documentation](getting_oss_security_rating.md) for more details.

## Fosstars OSS Rules of Play GitHub action

SAP already uses the OSS Rules of Play rating in a [GitHub action](https://github.com/SAP/fosstars-rating-core/blob/master/.github/workflows/fosstars-rules-of-play.yml) and posts the results to the branch [fosstars-oss-rules-of-play-sap-report](https://github.com/SAP/fosstars-rating-core/tree/fosstars-oss-rules-of-play-sap-report).

---

Next: [Notes](notes.md)
