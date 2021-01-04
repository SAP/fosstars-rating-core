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
| [Netty](https://github.com/netty/netty) | [3.52, BAD](report_Sep_1st_2020_v1_0_0/netty/netty.md) | [9 improvements](#Netty) | [4.47, MODERATE](report_Jan_4th_2021_v1_0_0/netty/netty.md) | [TBD](report_Jan_4th_2021_v1_1_0/netty/netty.md) |
| [Bouncy Castle Java](https://github.com/bcgit/bc-java) | [3.06, BAD](report_Sep_1st_2020_v1_0_0/bcgit/bc-java.md) | [3 improvements](#BouncyCastle) | [3.18, BAD](report_Jan_4th_2021_v1_0_0/bcgit/bc-java.md) | [TBD](report_Jan_4th_2021_v1_1_0/bcgit/bc-java.md) |
| [EclipseLink](https://github.com/eclipse-ee4j/eclipselink) | [3.28, BAD](report_Sep_1st_2020_v1_0_0/eclipse-ee4j/eclipselink.md) | Nothing | [3.42, BAD](report_Jan_4th_2021_v1_0_0/eclipse-ee4j/eclipselink.md) | [TBD](report_Jan_4th_2021_v1_1_0/eclipse-ee4j/eclipselink.md) |
| [Apache HttpComponents Client](https://github.com/apache/httpcomponents-client) | [2.76, BAD](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-client.md) | [3 improvements](#Apache HttpComponents Client) | [3.64, BAD](report_Jan_4th_2021_v1_0_0/apache/httpcomponents-client.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-client.md) |
| [Apache HttpComponents Core](https://github.com/apache/httpcomponents-core) | [4.01, MODERATE](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-core.md) | Nothing | [3.94, BAD](report_Jan_4th_2021_v1_0_0/apache/httpcomponents-core.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-core.md) |
| [Apache Commons FileUpload](https://github.com/apache/commons-fileupload) | [2.59, BAD](report_Sep_1st_2020_v1_0_0/apache/commons-fileupload.md) | Nothing | [2.97, BAD](report_Jan_4th_2021_v1_0_0/apache/commons-fileupload.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/commons-fileupload.md) |
| [Apache Commons Collections](https://github.com/apache/commons-collections) | [4.52, MODERATE](report_Sep_1st_2020_v1_0_0/apache/commons-collections.md) | [1 improvement](#Apache Commons Collections) | [4.78, MODERATE](report_Jan_4th_2021_v1_0_0/apache/commons-collections.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/commons-collections.md) |
| [Apache Commons I/O](https://github.com/apache/commons-io) | [5.17, GOOD](report_Sep_1st_2020_v1_0_0/apache/commons-io.md) | Nothing | [4.90, MODERATE](report_Jan_4th_2021_v1_0_0/apache/commons-io.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/commons-io.md) |
| [Apache CXF](https://github.com/apache/cxf) | [4.88, GOOD](report_Sep_1st_2020_v1_0_0/apache/cxf.md) | [1 improvement](#Apache CXF) | [5.07, MODERATE](report_Jan_4th_2021_v1_0_0/apache/cxf.md) | [TBD](report_Jan_4th_2021_v1_1_0/apache/cxf.md) |
| [SLF4J](https://github.com/qos-ch/slf4j) | [2.45, BAD](report_Sep_1st_2020_v1_0_0/qos-ch/slf4j.md) | Nothing | [2.46, BAD](report_Jan_4th_2021_v1_0_0/qos-ch/slf4j.md) | [TBD](report_Jan_4th_2021_v1_1_0/qos-ch/slf4j.md) |
| [zlib](https://github.com/madler/zlib) | [3.07, BAD](report_Sep_1st_2020_v1_0_0/madler/zlib.md) | Nothing | [3.14, BAD](report_Jan_4th_2021_v1_0_0/madler/zlib.md) | [TBD](report_Jan_4th_2021_v1_1_0/madler/zlib.md) |

## Implemented security improvements

### Netty

1.  [Fix or suppress LGTM findings](https://github.com/netty/netty/pull/10689) by @artem-smotrakov
1.  [Better hash algorithm in FingerprintTrustManagerFactory](https://github.com/netty/netty/pull/10683) by @artem-smotrakov
1.  [Suppress warnings about weak hash algorithms](https://github.com/netty/netty/pull/10647) by @artem-smotrakov
1.  [Avoid casting numbers to narrower types](https://github.com/netty/netty/pull/10645) by @artem-smotrakov
1.  [Fix possible bugs in HTTP/2 Codec](https://github.com/netty/netty/pull/10640) by @hyperxpro
1.  [Enable HTTP header validation in HttpServerUpgradeHandler](https://github.com/netty/netty/pull/10643) by @artem-smotrakov
1.  [Create codeql-analysis.yml](https://github.com/netty/netty/pull/10696) by @normanmaurer
1.  [Enable nohttp check during the build](https://github.com/netty/netty/pull/10708) by @artem-smotrakov
1.  [Added a security policy](https://github.com/netty/netty/pull/10692) by @artem-smotrakov

### BouncyCastle

1.  [Suppress index-out-of-bound false-positives from LGTM](https://github.com/bcgit/bc-java/pull/814) by @artem-smotrakov
1.  [Removed redundant fields and null-checks](https://github.com/bcgit/bc-java/pull/810) by @artem-smotrakov
1.  [Make BcKeyStoreSpi less predictable](https://github.com/bcgit/bc-java/pull/809) by @artem-smotrakov
    
### Apache HttpComponents Client

1.  [Fixed and suppressed several findings from LGTM.com](https://github.com/apache/httpcomponents-client/pull/262) by @artem-smotrakov
1.  [Set up CodeQL scans](https://github.com/apache/httpcomponents-client/pull/264) by @artem-smotrakov
1.  [Added a security policy](https://github.com/apache/httpcomponents-client/pull/263) by @artem-smotrakov
    
### Apache Commons Collections

1.  [Add SECURITY.MD.](https://github.com/apache/commons-collections/commit/f51e6c61ed591b85525a2a10adef1fba834d5007) by @garydgregory
    
### Apache CXF

1.  [Create codeql-analysis.yml](https://github.com/apache/cxf/commit/a4b4085f1c019302b1f1124a60dc1d5af1ce7085) by @coheigea

