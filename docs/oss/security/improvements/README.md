# Improving security ratings for open-source projects

The page shows how security ratings may be improved for specific open-source projects.

## Open-source projects in scope

The table below contains a number of open-source projects
and shows how their security ratings have changed in 4 months.
For some projects, several security improvements were done.
The improvements were derived from the detailed rating reports.
Below you can find a list of implemented improvements for specific projects.

Column description:

*  `Project` contains a name open-source project.
*  `Rating on Sep 1st, 2020 (v1.0.0)` contains a baseline security rating that was calculated
   with [Fosstars Core 1.0.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.0.0) version.
*  `Improvements` contains a link to implemented security improvements for a specific project.
*  `Rating on Jan 4st, 2021 (v1.0.0)` contains an updated security rating that was calculated
   with the same [Fosstars Core 1.0.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.0.0) version.
   This column shows how the security rating has changed after implementing the security improvements.
*  `Rating on Jan 4st, 2021 (v1.1.0)` contains an updated security rating that was calculated
   with the new [Fosstars Core 1.1.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.1.0).
   The new version includes bug fixed and enhancements that were identified while improving
   the security ratings for the projects in the table.

| Project | Rating on Sep 1st, 2020 (v1.0.0) | Improvements | Rating on Jan 4st, 2021 (v1.0.0) | Rating on Jan 4st, 2021 (v1.1.0) |
|---|---|---|---|---|
| [Netty](https://github.com/netty/netty) | [3.52, BAD](report_Sep_1st_2020_v1_0_0/netty/netty.md) | TBD | TBD | [6.21, GOOD](report_Jan_4th_2021_v1_1_0/netty/netty.md) |
| [Bouncy Castle Java](https://github.com/bcgit/bc-java) | [3.06, BAD](report_Sep_1st_2020_v1_0_0/bcgit/bc-java.md) | TBD | TBD | [3.37, BAD](report_Jan_4th_2021_v1_1_0/bcgit/bc-java.md) |
| [EclipseLink](https://github.com/eclipse-ee4j/eclipselink) | [3.28, BAD](report_Sep_1st_2020_v1_0_0/eclipse-ee4j/eclipselink.md) | TBD | TBD | [4.53, MODERATE](report_Jan_4th_2021_v1_1_0/eclipse-ee4j/eclipselink.md) |
| [Apache HttpComponents Client](https://github.com/apache/httpcomponents-client) | [2.76, BAD](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-client.md) | TBD | TBD | [3.69, BAD](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-client.md) |
| [Apache HttpComponents Core](https://github.com/apache/httpcomponents-core) | [4.01, MODERATE](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-core.md) | TBD | TBD | [4.35, MODERATE](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-core.md) |
| [Apache Commons FileUpload](https://github.com/apache/commons-fileupload) | [2.59, BAD](report_Sep_1st_2020_v1_0_0/apache/commons-fileupload.md) | TBD | TBD | [3.73, BAD](report_Jan_4th_2021_v1_1_0/apache/commons-fileupload.md) |
| [Apache Commons Collections](https://github.com/apache/commons-collections) | [4.52, MODERATE](report_Sep_1st_2020_v1_0_0/apache/commons-collections.md) | TBD | TBD | [5.95, GOOD](report_Jan_4th_2021_v1_1_0/apache/commons-collections.md) |
| [Apache Commons I/O](https://github.com/apache/commons-io) | [5.17, GOOD](report_Sep_1st_2020_v1_0_0/apache/commons-io.md) | TBD | TBD | [5.99, GOOD](report_Jan_4th_2021_v1_1_0/apache/commons-io.md) |
| [Apache CXF](https://github.com/apache/cxf) | [4.88, GOOD](report_Sep_1st_2020_v1_0_0/apache/cxf.md) | TBD | TBD | [5.28, GOOD](report_Jan_4th_2021_v1_1_0/apache/cxf.md) |
| [SLF4J](https://github.com/qos-ch/slf4j) | [2.45, BAD](report_Sep_1st_2020_v1_0_0/qos-ch/slf4j.md) | TBD | TBD | [2.88, BAD](report_Jan_4th_2021_v1_1_0/qos-ch/slf4j.md) |
| [zlib](https://github.com/madler/zlib) | [3.07, BAD](report_Sep_1st_2020_v1_0_0/madler/zlib.md) | TBD | TBD | [3.12, BAD](report_Jan_4th_2021_v1_1_0/madler/zlib.md) |

## Identified improvements

### Netty

1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/netty/netty/ci/)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/netty/netty/?mode=list)
        (currently Netty has the grade A, but it may be A+)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the fuzzing score by including Netty to [the OSS-Fuzz project](https://github.com/google/oss-fuzz)
1.  Improve the memory-safety testing score by enabling [sanitizers](https://github.com/google/sanitizers):
    1.  AddressSanitizer
    1.  MemorySanitizer
    1.  UndefinedBehaviorSanitizer
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [sanitizers](https://github.com/google/sanitizers),
        [LGTM checks](https://lgtm.com/projects/g/netty/netty/ci/),
        [FindSecBugs](https://find-sec-bugs.github.io/)
    1.  Including Netty to [the OSS-Fuzz project](https://github.com/google/oss-fuzz)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### SLF4J

1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/qos-ch/slf4j/ci/?mode=list)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/qos-ch/slf4j/?mode=list)
        (currently the project has the grade C which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [LGTM checks](https://lgtm.com/projects/g/qos-ch/slf4j/ci/?mode=list),
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Bouncy Castle Java

1.  Improve the dependency testing score by enabling Dependabot or OWASP Dependency Check
1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/bcgit/bc-java/ci/)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/bcgit/bc-java/?mode=list)
        (currently the project has the grade C which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [LGTM checks](https://lgtm.com/projects/g/bcgit/bc-java/ci/),
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### EclipseLink

1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/eclipse-ee4j/eclipselink/ci/)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/eclipse-ee4j/eclipselink/?mode=list)
        (currently the project has the grade E which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [LGTM checks](https://lgtm.com/projects/g/eclipse-ee4j/eclipselink/ci/),
        [FindSecBugs](https://find-sec-bugs.github.io/),
        Dependabot
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### zlib

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade B which may be improved)
1.  Improve the memory-safety testing score by enabling [sanitizers](https://github.com/google/sanitizers):
    1.  AddressSanitizer
    1.  MemorySanitizer
    1.  UndefinedBehaviorSanitizer
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        LGTM checks,
        [sanitizers](https://github.com/google/sanitizers)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Apache Commons FileUpload

1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/apache/commons-fileupload/ci/)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/apache/commons-fileupload/)
        (currently the project has the grade C which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [LGTM checks](https://lgtm.com/projects/g/apache/commons-fileupload/ci/),
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.
1.  Fix two unpatched vulnerabilities

### Apache HttpComponents Client

1.  Improve the static analysis score by:
    1.  [Enabling LGTM checks for pull requests](https://lgtm.com/projects/g/apache/httpcomponents-client/ci/)
    1.  [Fixing issues reported by LGTM](https://lgtm.com/projects/g/apache/httpcomponents-client/?mode=list)
        (currently Netty has the grade A, but it may be A+)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        [LGTM checks](https://lgtm.com/projects/g/apache/httpcomponents-client/ci/),
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.
1.  Fix one unpatched vulnerabilities

### Apache HttpComponents Core

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        LGTM checks,
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Apache Commons Collections

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade B which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        LGTM checks,
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Apache Commons I/O

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade B which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by enabling [nohttp](https://github.com/spring-io/nohttp),
    LGTM checks,
    [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Apache CXF

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade D which may be improved)
    1.  Enabling [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the nohttp score by enabling [nohttp](https://github.com/spring-io/nohttp)
1.  Improve the security awareness score by:
    1.  [Adding a security policy](https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository)
    1.  Enabling [nohttp](https://github.com/spring-io/nohttp),
        LGTM checks,
        [FindSecBugs](https://find-sec-bugs.github.io/)
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.
