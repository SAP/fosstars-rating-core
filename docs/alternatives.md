# Similar projects

## BlackDuck OpenHub

[OpenHub](https://www.openhub.net) collects data about open source projects amd calculates a couple of security indexes:

1.  Security Confidence Index
1.  Vulnerability Exposure Index

The details can be found [here](https://blog.openhub.net/about-pvr/).

The indexes are based on the following:

1.  Known vulnerabilities, their CVSS scores and ages.
1.  Software versions where the vulnerabilities were found.

Here is what they say about the indexes (see the [Security Landscape section](https://blog.openhub.net/about-pvr/)):

> A project’s security landscape is largely built from what is known about vulnerabilities within the project.
> The National Vulnerability Database, the Mitre Common Vulnerabilities and Exposures database, and the VulnDB are examples of sources of project vulnerabilities.
> By reviewing the data in these databases, one can get a feel for how secure a project is by the number and severity of the vulnerabilities reported against it.
> However, it is a non-trivial amount of work to constantly fetch that information and lay it out in a manner so that the data are meaningful and informative.
> So, naturally, we want to do it for you!

## OSSPal

The [OSSPal project](http://osspal.org/content/welcome-osspal) allows users to provide their ratings
for open source projects in the following categories:

1.  Installability
1.  Usability
1.  Robustness
1.  Security
1.  Scalability
1.  Overall Quality

Unfortunately, it doesn't look like that the project has a lot of reviews. Here are several interesting quotes:

> First, the quantitative evaluation offered by the BRR was valuable, but people also wanted to see reviews based on actual experience with a project,
> in much the same way that people look for both quantitative data and subjective reviews for automobiles, hotels, consumer electronics, and more.

> Second, the number of FOSS projects has increased dramatically, making it impractical for people to review all of the projects in most areas.
> So we have used quantitative data measuring popularity to identify those FOSS projects with the greatest use,
> and have selected them as a starting point for the individual selection process.

> The OSSpal site is a work in progress, but has some important goals:
> - Making it possible for individuals to register on the site and enter reviews of listed FOSS projects;
> - Providing user-friendly mechanisms for entering and searching project reviews;
> - Identifying functional evaluation criteria for each category in the software taxonomy;
> - Connecting project listings to the quantitative project data collected in Open Hub.

## Core Infrastructure Initiative Best Practices Badge

This project identifies best practices for open source software and implements a badging system for those best practices.
It offers `passing`, `silver` or `gold` badges for a project.

Here is what [the project page](https://github.com/coreinfrastructure/best-practices-badge) says:

> This project identifies best practices for Free/Libre and Open Source Software (FLOSS)
> and implements a badging system for those best practices.
> The "BadgeApp" badging system is a simple web application that lets projects self-certify that they meet the criteria
> and show a badge.
> The real goal of this project is to encourage projects to apply best practices,
> and to help users determine which FLOSS projects do so.
> We believe that FLOSS projects that implement best practices are more likely to produce better software,
> including more secure software.

This is similar to what Fosstars offers.
But Core Infrastructure Initiative Best Practices Badge is based on manual assessments.
Only project committers are allowed to update assessments for their projects.
Also, only project committers may add their projects.

Here is a directory of [projects which have been assessed](https://bestpractices.coreinfrastructure.org/en/projects).

## Core Infrastructure Initiative Census

Here is what [the project page](https://github.com/coreinfrastructure/census) says:

> Automated quantitative review of open source software projects.
>
> This project contains programs and documentation to help identify open source software (OSS) projects
> that may need additional investment to improve security, by combining a variety of metrics.

> We have focused on automatically gathering metrics, especially those that suggest less active projects.
> We also provided a human estimate of the program's exposure to attack,
> and developed a scoring system to heuristically combine these metrics.
> These heuristics identified especially plausible candidates for further consideration.
> For our initial set of projects to examine, we took the set of packages installed by Debian base
> and added a set of packages that were identified as potentially concerning.

This is similar to what Fosstars offers.
It tries to collect some data automatically but also can take data entered by a user.
Unfortunately, the project looks inactive.

## Scorecard

Here is what [the project's page](https://github.com/ossf/scorecard) says about the goals:

> Goals
> 1. Automate analysis and trust decisions on the security posture of open source projects.
>
> 1. Use this data to proactively improve the security posture of the critical projects the world depends on.
>

For details check the [comparison between Fosstars and OSSF Scorecard](compare_with_score-card.md)

## Others

There are several other projects which collect data about project activity,
known vulnerabilities from public and private databases, etc, and then build some ratings.
Here are some of them:

1.  [Sonatype](https://www.sonatype.com/appscan)
1.  [Veracode](https://www.veracode.com/blog/security-news/towards-better-risk-score-open-source-security)
1.  [OSSGadget](https://github.com/microsoft/OSSGadget) is a collection of tools that can help analyze open source projects. These are intended to make it simple to perform low-level tasks, like locating the source code of a given package, downloading it, performing basic analyses on it, or estimating its health.

---

Next: [Notes](notes.md)
