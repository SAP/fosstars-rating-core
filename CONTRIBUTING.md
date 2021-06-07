We welcome ideas for improvements and pull requests.
This document describes how to submit them.

# Understand the basics

We encourage using forks and pull requests.
Please take a look at GitHub's excellent
[help documentation first](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests).

# Using GitHub issues

We use [GitHub issues](https://github.com/SAP/fosstars-rating-core/issues)
to track bugs and enhancement.

Before you open a new issue, please do a bit of searching in our GitHub issues to see
if you can find something similar. If not, please create a new issue before submitting a pull request.

If you are reporting a bug, please provide as much information as possible.

# Discuss non-trivial contribution ideas with committers

If you're considering anything more than correcting a typo or fixing a minor bug,
please open an issue and discuss the proposal with us
before submitting a pull request.

## Developer Certificate of Origin (DCO)

Due to legal reasons, contributors will be asked to accept a DCO before they
submit the first pull request to this projects, this happens in an automated
fashion during the submission process. SAP uses
[the standard DCO text of the Linux Foundation](https://developercertificate.org/).

# Add JUnit test cases

Please make sure you added unit tests for the changes you introduce. The more tests you add, the better.
Please search the codebase to find related unit tests and add additional `@Test` methods within.

# Add Javadoc

Please make sure all new classes and methods have Javadoc comments
with at least a short note which describes what the class or the method does.
Please make sure there is no Javadoc warnings by running `mvn javadoc:javadoc`.

# Code formatting

Please follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
Many of popular IDEs allow importing the ruleset.
To make sure that the code is formatted correctly, we run Maven Checkstyle plugin in the build pipeline.
You can run it with `mvn checkstyle:check`.

# Commit the changes

Choose the granularity of your commits consciously and squash commits
that represent multiple edits or corrections of the same logical change.
See the [Commit Guidelines section of Pro Git](https://git-scm.com/book/en/Distributed-Git-Contributing-to-a-Project#Commit-Guidelines)
for best practices around commit messages, and use `git log` to see some examples.

*  Keep the subject line short if possible.
*  Please refer to the GitHub issue number in the description of the commit.
*  Please do not end the subject line with a period.

# Make sure all tests pass

Please make sure that the `mvn clean package` command passes.
It's also going to run `checkstyle`, please fix all reported issues.

Please also run the test suite for the demo tool.
The test suite is available in `src/test/shell/tool/github`.
It's not yet included in the build process.
The instructions are available in [the README file](src/test/shell/tool/github/README.md).

# Submit your pull request

*  Please explain the changes.
*  Please add any additional information that you think is useful for reviewers.
*  Please expect discussion and rework.
