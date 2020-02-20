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

# Sign the Contributor License Agreement

You'll need to sign the Contributor License Agreement (CLA). When you create a pull request,
the CLA Assistant is going to ask you to sign the CLA.

If employees of a company contribute code, in addition to the individual agreement above,
there needs to be one company agreement submitted. This is mainly for the protection of the contributing employees.

A company representative authorized to do so needs to download, fill,
and print the [Corporate Contributor License Agreement](SAP_CCLA.pdf) form. Then either:

    * Scan it and e-mail it to opensource@sap.com
    * Fax it to: +49 6227 78-45813
    * Send it by traditional letter to: OSPO Core, Dietmar-Hopp-Allee 16, 69190 Walldorf, Germany

The form contains a list of employees who are authorized to contribute on behalf of your company.
When this list changes, please let us know.

# Add JUnit test cases

Please make sure you added unit tests for the changes you introduce. The more tests you add the better.
Please search the codebase to find related unit tests and add additional `@Test` methods within.

# Add Javadoc

Please make sure all new classes and methods have a simple Javadoc class comment
with at least a short note which describes what the class or the method is for.

# Commit the changes

Choose the granularity of your commits consciously and squash commits
that represent multiple edits or corrections of the same logical change.
See the [Commit Guidelines section of Pro Git](https://git-scm.com/book/en/Distributed-Git-Contributing-to-a-Project#Commit-Guidelines)
for best practices around commit messages, and use `git log` to see some examples.

*  Keep the subject line to 50 characters or less if possible.
*  Please refer to the GitHub issue number in the description of the commit.
*  Please do not end the subject line with a period.

# Make sure all tests pass

Please make sure that the `mvn clean package` command passes.
It's also going to run `checkstyle`, please fix all reported issues.

# Submit your pull request

*  Please explain the changes you introduce.
*  Please add any additional information.
*  Please expect discussion and rework.
