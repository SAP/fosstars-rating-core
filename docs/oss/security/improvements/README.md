# Improving security ratings for open-source projects

The page shows how security ratings changed in a certain period,
and which changes were done in the projects to improve the ratings.

## Open-source projects in scope

The table below lists several open-source projects
and shows how their security ratings changed over time.

For each project, there is a list of improvements that would improve the security rating.
The improvements were derived from the detailed rating reports.
Then, the table provides a list of improvements that were implemented in the project.

[Fosstars Core 1.0.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.0.0)
was used to calculate the security ratings.

| Project | Rating on Sep 1st, 2020 | Rating on Nov 1st, 2020  |
|---|---|---|
| [Netty](https://github.com/netty/netty) | [3.52, BAD](report_Sep_1st_2020/netty/netty.md) | TBD |
| [SLF4J](https://github.com/qos-ch/slf4j) | [2.45, BAD](report_Sep_1st_2020/qos-ch/slf4j.md) | TBD |
| [Bouncy Castle Java](https://github.com/bcgit/bc-java) | [3.06, BAD](report_Sep_1st_2020/bcgit/bc-java.md) | TBD |
| [EclipseLink](https://github.com/eclipse-ee4j/eclipselink) | [3.28, BAD](report_Sep_1st_2020/eclipse-ee4j/eclipselink.md) | TBD |
| [zlib](https://github.com/madler/zlib) | [3.07, BAD](report_Sep_1st_2020/madler/zlib.md) | TBD |
| [Apache Commons FileUpload](https://github.com/apache/commons-fileupload) | [2.59, BAD](report_Sep_1st_2020/apache/commons-fileupload.md) | TBD |
| [Apache Http Client](https://github.com/apache/httpcomponents-client) | [2.76, BAD](report_Sep_1st_2020/apache/httpcomponents-client.md) | TBD |
| [Apache Http Core](https://github.com/apache/httpcomponents-core) | [4.01, MODERATE](report_Sep_1st_2020/apache/httpcomponents-core.md) | TBD |
| [Apache Commons Collections](https://github.com/apache/commons-collections) | [4.52, MODERATE](report_Sep_1st_2020/apache/commons-collections.md) | TBD |
| [Apache Commons I/O](https://github.com/apache/commons-io) | [5.17, GOOD](report_Sep_1st_2020/apache/commons-io.md) | TBD |
| [Apache CXF](https://github.com/apache/cxf) | [4.88, GOOD](report_Sep_1st_2020/apache/cxf.md) | TBD |

## Possible improvements

### Netty

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently Netty has the grade A, but it may be A+)
    1.  Enabling FindSecBugs
1.  Improve the fuzzing score by including Netty to the OSS-Fuzz project
1.  Improve the memory-safety testing score by:
    1.  Enabling AddressSanitizer
    1.  Enabling MemorySanitizer
    1.  Enabling UndefinedBehaviorSanitizer
1.  Improve the nohttp score by enabling nohttp
1.  Improve the security awareness score by:
    1.  Adding a security policy
    1.  Enabling nohttp, sanitizers, LGTM checks, FindSecBugs
    1.  Including Netty to the OSS-Fuzz project
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### SLF4J

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade C which may be improved)
    1.  Enabling FindSecBugs
1.  Improve the nohttp score by enabling nohttp
1.  Improve the security awareness score by:
    1.  Adding a security policy
    1.  Enabling nohttp, LGTM checks, FindSecBugs
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Bouncy Castle Java

1.  Improve the dependency testing score by enabling Dependabot or OWASP Dependency Check
1.  Improve the static analysis score by:
        1.  Enabling LGTM checks for pull requests
        1.  Fixing issues reported by LGTM (currently the project has the grade C which may be improved)
        1.  Enabling FindSecBugs
1.  Improve the nohttp score by enabling nohttp
1.  Improve the security awareness score by:
    1.  Adding a security policy
    1.  Enabling nohttp, LGTM checks, FindSecBugs
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### EclipseLink

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade E which may be improved)
    1.  Enabling FindSecBugs
1.  Improve the nohttp score by enabling nohttp
1.  Improve the security awareness score by:
    1.  Adding a security policy
    1.  Enabling nohttp, LGTM checks, FindSecBugs, Dependabot
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### zlib

1.  Improve the static analysis score by:
    1.  Enabling LGTM checks for pull requests
    1.  Fixing issues reported by LGTM (currently the project has the grade B which may be improved)
1.  Improve the memory-safety testing score by:
    1.  Enabling AddressSanitizer
    1.  Enabling MemorySanitizer
    1.  Enabling UndefinedBehaviorSanitizer
1.  Improve the nohttp score by enabling nohttp
1.  Improve the security awareness score by:
    1.  Adding a security policy
    1.  Enabling nohttp, LGTM checks, sanitizers
1.  Improve the vulnerability discovery and security testing score by improving the score for security testing.

### Apache Commons FileUpload

TBD

### Apache Http Client

TBD

### Apache Http Core

TBD

### Apache Commons Collections

TBD

### Apache Commons I/O

TBD

### Apache CXF

TBD
