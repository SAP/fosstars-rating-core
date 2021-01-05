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
*  `Rating on Sep 1st, 2020 (v1.0.0) baseline` contains a baseline security rating that was calculated
   with [Fosstars Core 1.0.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.0.0) version.
*  `Improvements` contains a link to implemented security improvements for a specific project.
*  `Rating on Jan 5th, 2021 (v1.0.0)` contains an updated security rating that was calculated
   with the same [Fosstars Core 1.0.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.0.0) version.
   This column shows how the security rating has changed after implementing the security improvements.
*  `Rating on Jan 5th, 2021 (v1.1.0)` contains an updated security rating that was calculated
   with the new [Fosstars Core 1.1.0](https://github.com/SAP/fosstars-rating-core/releases/tag/v1.1.0).
   The new version includes bug fixed and enhancements that were identified while improving
   the security ratings for the projects in the table.

| Project | Rating&nbsp;on&nbsp;Sep&nbsp;1st,&nbsp;2020 (v1.0.0)&nbsp;baseline | Improvements | Rating&nbsp;on&nbsp;Jan&nbsp;5th,&nbsp;2021 (v1.0.0) | Rating&nbsp;on&nbsp;Jan&nbsp;5th,&nbsp;2021 (v1.1.0) |
|---|---|---|---|---|
| [Netty](https://github.com/netty/netty) | [3.52, BAD](report_Sep_1st_2020_v1_0_0/netty/netty.md) | [9&nbsp;improvements](#Netty) | [4.09, MODERATE](report_Jan_4th_2021_v1_0_0/netty/netty.md) :arrow_up: | [6.02, GOOD](report_Jan_4th_2021_v1_1_0/netty/netty.md) :arrow_up: |
| [Bouncy Castle Java](https://github.com/bcgit/bc-java) | [3.06, BAD](report_Sep_1st_2020_v1_0_0/bcgit/bc-java.md) | [3&nbsp;improvements](#BouncyCastle) | [3.18, BAD](report_Jan_4th_2021_v1_0_0/bcgit/bc-java.md) :arrow_up: | [3.37, BAD](report_Jan_4th_2021_v1_1_0/bcgit/bc-java.md) :arrow_up: |
| [Apache HttpComponents Client](https://github.com/apache/httpcomponents-client) | [2.76, BAD](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-client.md) | [3&nbsp;improvements](#apache-httpcomponents-client) | [3.27, BAD](report_Jan_4th_2021_v1_0_0/apache/httpcomponents-client.md) :arrow_up: | [3.64, BAD](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-client.md) :arrow_up: |
| [Apache CXF](https://github.com/apache/cxf) | [4.88, GOOD](report_Sep_1st_2020_v1_0_0/apache/cxf.md) | [1&nbsp;improvement](#apache-cXF) | [5.05, GOOD](report_Jan_4th_2021_v1_0_0/apache/cxf.md) :arrow_up: | [5.62, GOOD](report_Jan_4th_2021_v1_1_0/apache/cxf.md) :arrow_up: |
| [Apache Commons Collections](https://github.com/apache/commons-collections) | [4.52, MODERATE](report_Sep_1st_2020_v1_0_0/apache/commons-collections.md) | [1&nbsp;improvement](#apache-commons-collections) | [4.40, MODERATE](report_Jan_4th_2021_v1_0_0/apache/commons-collections.md) :arrow_down: | [5.58, GOOD](report_Jan_4th_2021_v1_1_0/apache/commons-collections.md) :arrow_up: |
| [EclipseLink](https://github.com/eclipse-ee4j/eclipselink) | [3.28, BAD](report_Sep_1st_2020_v1_0_0/eclipse-ee4j/eclipselink.md) | Nothing | [3.42, BAD](report_Jan_4th_2021_v1_0_0/eclipse-ee4j/eclipselink.md) :arrow_up: | [4.53, MODERATE](report_Jan_4th_2021_v1_1_0/eclipse-ee4j/eclipselink.md) :arrow_up: |
| [Apache HttpComponents Core](https://github.com/apache/httpcomponents-core) | [4.01, MODERATE](report_Sep_1st_2020_v1_0_0/apache/httpcomponents-core.md) | Nothing | [3.94, MODERATE](report_Jan_4th_2021_v1_0_0/apache/httpcomponents-core.md) :arrow_down: | [4.35, MODERATE](report_Jan_4th_2021_v1_1_0/apache/httpcomponents-core.md) :arrow_up: |
| [Apache Commons FileUpload](https://github.com/apache/commons-fileupload) | [2.59, BAD](report_Sep_1st_2020_v1_0_0/apache/commons-fileupload.md) | Nothing | [2.59, BAD](report_Jan_4th_2021_v1_0_0/apache/commons-fileupload.md) | [3.36, BAD](report_Jan_4th_2021_v1_1_0/apache/commons-fileupload.md) :arrow_up: |
| [Apache Commons I/O](https://github.com/apache/commons-io) | [5.17, GOOD](report_Sep_1st_2020_v1_0_0/apache/commons-io.md) | Nothing | [4.53, MODERATE](report_Jan_4th_2021_v1_0_0/apache/commons-io.md) :arrow_down: | [5.61, GOOD](report_Jan_4th_2021_v1_1_0/apache/commons-io.md) :arrow_up: |
| [SLF4J](https://github.com/qos-ch/slf4j) | [2.45, BAD](report_Sep_1st_2020_v1_0_0/qos-ch/slf4j.md) | Nothing | [2.46, BAD](report_Jan_4th_2021_v1_0_0/qos-ch/slf4j.md) :arrow_up: | [2.88, BAD](report_Jan_4th_2021_v1_1_0/qos-ch/slf4j.md) :arrow_up: |
| [zlib](https://github.com/madler/zlib) | [3.07, BAD](report_Sep_1st_2020_v1_0_0/madler/zlib.md) | Nothing | [3.14, BAD](report_Jan_4th_2021_v1_0_0/madler/zlib.md) :arrow_up: | [3.12, BAD](report_Jan_4th_2021_v1_1_0/madler/zlib.md) :arrow_up: |

## Implemented security improvements

### Netty

1.  [Fix or suppress LGTM findings](https://github.com/netty/netty/pull/10689) by @artem-smotrakov
1.  [Better hash algorithm in FingerprintTrustManagerFactory](https://github.com/netty/netty/pull/10683) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Suppress warnings about weak hash algorithms](https://github.com/netty/netty/pull/10647) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Avoid casting numbers to narrower types](https://github.com/netty/netty/pull/10645) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Fix possible bugs in HTTP/2 Codec](https://github.com/netty/netty/pull/10640) by [@hyperxpro](https://github.com/hyperxpro)
1.  [Enable HTTP header validation in HttpServerUpgradeHandler](https://github.com/netty/netty/pull/10643) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Create codeql-analysis.yml](https://github.com/netty/netty/pull/10696) by [@normanmaurer](https://github.com/normanmaurer)
1.  [Enable nohttp check during the build](https://github.com/netty/netty/pull/10708) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Added a security policy](https://github.com/netty/netty/pull/10692) by [@artem-smotrakov](https://github.com/artem-smotrakov)

### BouncyCastle

1.  [Suppress index-out-of-bound false-positives from LGTM](https://github.com/bcgit/bc-java/pull/814) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Removed redundant fields and null-checks](https://github.com/bcgit/bc-java/pull/810) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Make BcKeyStoreSpi less predictable](https://github.com/bcgit/bc-java/pull/809) by [@artem-smotrakov](https://github.com/artem-smotrakov)
    
### Apache HttpComponents Client

1.  [Fixed and suppressed several findings from LGTM.com](https://github.com/apache/httpcomponents-client/pull/262) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Set up CodeQL scans](https://github.com/apache/httpcomponents-client/pull/264) by [@artem-smotrakov](https://github.com/artem-smotrakov)
1.  [Added a security policy](https://github.com/apache/httpcomponents-client/pull/263) by [@artem-smotrakov](https://github.com/artem-smotrakov)
    
### Apache Commons Collections

1.  [Add SECURITY.MD.](https://github.com/apache/commons-collections/commit/f51e6c61ed591b85525a2a10adef1fba834d5007) by [@garydgregory](https://github.com/garydgregory)
    
### Apache CXF

1.  [Create codeql-analysis.yml](https://github.com/apache/cxf/commit/a4b4085f1c019302b1f1124a60dc1d5af1ce7085) by [@coheigea](https://github.com/coheigea)

