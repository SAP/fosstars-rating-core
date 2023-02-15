![Java CI](https://github.com/SAP/fosstars-rating-core/workflows/Java%20CI/badge.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/SAP/fosstars-rating-core.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/SAP/fosstars-rating-core/context:java)
[![REUSE status](https://api.reuse.software/badge/github.com/SAP/fosstars-rating-core)](https://api.reuse.software/info/github.com/SAP/fosstars-rating-core)
[![Fosstars security rating](https://raw.githubusercontent.com/SAP/fosstars-rating-core/fosstars-report/fosstars_badge.svg)](https://github.com/SAP/fosstars-rating-core/blob/fosstars-report/fosstars_report.md)
[![Fosstars RoP status](https://raw.githubusercontent.com/SAP/fosstars-rating-core/fosstars-rop-report/fosstars_rop_rating.svg)](https://github.com/SAP/fosstars-rating-core/blob/fosstars-rop-report/README.md)

# Ratings for open source projects

This is a framework for defining and calculating ratings for open source projects.
See [docs](https://sap.github.io/fosstars-rating-core/) for more details.

## Security rating for open source projects

open source software helps a lot, but it also may bring new security issues
and therefore increase security risks.
Is it safe to use a particular open source component?
Sometimes answering this question is not easy.
The security rating for open source projects helps to answer this question.
First, it gathers various data about an open source project.
Then, it calculates a security rating for it.
The rating helps to assess the security risk that comes with this open source project.

More details about the security rating
can be found in the [docs](https://sap.github.io/fosstars-rating-core/oss_security_rating.html).

## Requirements

*  Java 8+
*  Maven 3.6.0+
*  Python 3.6.8+
*  Jupyter Notebook 4.4.0+

## Download and installation

The [jars](https://mvnrepository.com/artifact/com.sap.oss.phosphor/fosstars-rating-core) are available on the Maven Central repository:

```
<dependency>
    <groupId>com.sap.oss.phosphor</groupId>
    <artifactId>fosstars-rating-core</artifactId>
    <version>1.12.0</version>
</dependency>
```

Or, the project can be built and installed with the following command:

```
mvn clean install
```

## Fosstars GitHub action

For projects on GitHub, there is a [GitHub action](https://github.com/SAP/fosstars-rating-core-action)
that calculates a security rating and generates a badge.

## CLI for calculating ratings

There is a CLI for calculating ratings

The tool can be run with commands like the following:

```
git clone https://github.com/SAP/fosstars-rating-core.git
cd fosstars-rating-core
mvn package -DskipTests
TOKEN=xyz # use your personal token, see below
java -jar target/fosstars-github-rating-calc.jar --rating security --url https://github.com/curl/curl --verbose --token ${TOKEN}
```

The `TOKEN` variable contains a token for accessing the GitHub API.
You can create a personal token in the
[settings/tokens](https://github.com/settings/tokens) tab in your profile on GitHub.

In the verbose mode, the tool is going to print out the following:

*  Data that was used for calculating a security rating
*  Sub-scores that describes particular security aspects
*  Overall score and label
*  Advice on how the rating may be improved.

Here is what the output looks like:

![CLI demo](command_line_tool_demo.gif)

[Here](EXAMPLE.md) you can find full output.

If `--interactive` option is specified, the tool becomes a bit interactive,
and may ask the user a couple of questions.
You can also find more details in the [docs](https://sap.github.io/fosstars-rating-core/getting_oss_security_rating.html).

## Running CLI in Docker

You can also run the CLI in a Docker container:

```
docker build --tag fosstars --file src/main/docker/cli/Dockerfile .
docker run -v $(pwd):/work fosstars --rating security --token $TOKEN --url https://github.com/apache/poi
```

## Known issues

Please see [GitHub issues](https://github.com/SAP/fosstars-rating-core/issues).

## How to obtain support

Please create a new [GitHub issue](https://github.com/SAP/fosstars-rating-core/issues)
if you found a bug, or you'd like to propose an enhancement.
If you think you found a security issue, please follow [this guideline](SECURITY.md).

If you have a question, please [open a discussion](https://github.com/SAP/fosstars-rating-core/discussions).

# Contributing

We appreciate feedback, ideas for improvements and, of course, pull requests.

Please follow [this guideline](CONTRIBUTING.md) if you'd like to contribute to the project.
