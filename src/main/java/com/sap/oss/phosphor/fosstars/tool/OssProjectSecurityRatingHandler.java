package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURL.StandardTypes;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.oss.github.OssSecurityGithubAdvisor;
import com.sap.oss.phosphor.fosstars.maven.GAV;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssSecurityRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import com.sap.oss.phosphor.fosstars.tool.report.MergedJsonReporter;
import com.sap.oss.phosphor.fosstars.tool.report.OssSecurityRatingMarkdownReporter;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class OssProjectSecurityRatingHandler extends AbstractHandler {

  /**
   * An advisor for calculated security ratings.
   */
  private static final Advisor OSS_SECURITY_GITHUB_ADVISOR = new OssSecurityGithubAdvisor();

  /**
   *
   */
  public OssProjectSecurityRatingHandler() {
    super(RatingRepository.INSTANCE.rating(OssSecurityRating.class));
  }

  @Override
  public String supportedRatingName() {
    return "security";
  }

  @Override
  public OssProjectSecurityRatingHandler run() throws Exception {
    requireOneOfIn(commandLine, "--url" , "--gav", "--npm", "--purl", "--config");

    if (commandLine.hasOption("url")) {
      processUrl(commandLine.getOptionValue("url"));
    }

    if (commandLine.hasOption("gav")) {
      process(GAV.parse(commandLine.getOptionValue("gav")));
    }

    if (commandLine.hasOption("npm")) {
      processNpm(commandLine.getOptionValue("npm"));
    }

    if (commandLine.hasOption("config")) {
      processConfig(commandLine.getOptionValue("config"));
    }

    if (commandLine.hasOption("purl")) {
      processPurl(commandLine.getOptionValue("purl"));
    }

    return this;
  }

  /**
   * Calculate a rating for a single project identified by a PURL.
   *
   * @param packageUrl The PURL.
   * @throws IOException If something went wrong.
   */
  private void processPurl(String packageUrl) throws Exception {
    PackageURL purl = new PackageURL(packageUrl);

    switch (purl.getType().toLowerCase()) {
      case StandardTypes.GITHUB:
        String url = format("https://github.com/%s/%s", purl.getNamespace(), purl.getName());
        processUrl(url);
        break;
      case StandardTypes.MAVEN:
        process(new GAV(purl.getNamespace(), purl.getName(), purl.getVersion()));
        break;
      case StandardTypes.NPM:
        processNpm(purl.getName());
        break;
      default:
        throw new IOException(format("Oh no! Unsupported PURL type: '%s'", purl.getType()));
    }
  }

  /**
   * Calculate a rating for a single project identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @throws IOException If something went wrong.
   */
  private void processUrl(String url) throws IOException {
    GitHubProject project = GitHubProject.parse(url);

    calculator().calculateFor(project);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(project).split("\n")).forEach(logger::info);
    logger.info("");
    storeReportIfRequested(project, commandLine);
  }

  /**
   * Calculate a rating for a single project identified by NPM name.
   *
   * @param name The NPM name.
   * @throws IOException If something went wrong.
   */
  private void processNpm(String name) throws IOException {
    process(name, identifier -> {
      Optional<String> scm = new NpmScmFinder().scmForNpm(identifier);
      if (!scm.isPresent()) {
        throw new IOException("Oh no! Could not find a URL to SCM!");
      }

      String url = scm.get();
      logger.info("SCM is {}", url);

      if (!isOnGitHub(url)) {
        logger.info("But unfortunately, I can work only with projects that stay on GitHub ...");
        return Optional.empty();
      }

      return Optional.of(GitHubProject.parse(url));
    });
  }

  /**
   * Calculate a rating for a single project identified by GAV coordinates.
   *
   * @param coordinates The GAV coordinates.
   * @throws IOException If something went wrong.
   */
  private void process(GAV coordinates) throws IOException {
    process(coordinates.toString(), gav -> {
      MavenScmFinder finder = new MavenScmFinder();

      Optional<String> scm = finder.findScmFor(gav);
      if (!scm.isPresent()) {
        throw new IOException("Oh no! Could not find a URL to SCM!");
      }

      String url = scm.get();
      logger.info("SCM is {}", url);

      if (!isOnGitHub(url)) {
        logger.info("But unfortunately, I can work only with projects that stay on GitHub ...");
        logger.info("Let me try to find a mirror on GitHub ...");

        Optional<GitHubProject> mirror = finder.findGithubProjectFor(coordinates);
        if (!mirror.isPresent()) {
          throw new IOException("Oh no! I could not find a mirror on GitHub!");
        }

        logger.info("Yup, that seems to be a corresponding project on GitHub:");
        logger.info("  {}", mirror.get().scm().toString());

        return mirror;
      }

      return Optional.empty();
    });
  }

  /**
   * Calculate a rating for a single project.
   *
   * @param identifier An identifier of the project.
   * @param gitHubProjectResolver A resolver that looks for a GitHub repository for the project.
   * @throws IOException If something went wrong.
   */
  private void process(String identifier, Resolver<GitHubProject> gitHubProjectResolver)
      throws IOException {

    Optional<GitHubProject> project = gitHubProjectResolver.runFor(identifier);
    if (!project.isPresent()) {
      throw new IOException("Oh no! Could not find project's repository on GitHub!");
    }

    processUrl(project.get().scm().toString());
  }

  @Override
  List<Reporter<GitHubProject>> makeReporters(Config config) throws IOException {
    requireNonNull(config, "Oh no! Config is null!");
    if (config.reportConfigs == null) {
      return Collections.singletonList(Reporter.dummy());
    }

    List<Reporter<GitHubProject>> reporters = new ArrayList<>();
    for (ReportConfig reportConfig : config.reportConfigs) {
      reporters.add(reporterFrom(reportConfig));
    }

    return reporters;
  }

  @Override
  Formatter createFormatter(String type) {
    switch (type) {
      case "text":
        return PrettyPrinter.withVerboseOutput(OSS_SECURITY_GITHUB_ADVISOR);
      case "markdown":
          return new OssSecurityRatingMarkdownFormatter(OSS_SECURITY_GITHUB_ADVISOR);
      default:
        throw new IllegalArgumentException(format("Unsupported report type: %s", type));
    }
  }

  /**
   * Create a reporter from a specified report config.
   *
   * @param reportConfig The config.
   * @return A reporter.
   * @throws IOException If something went wrong.
   */
  private Reporter<GitHubProject> reporterFrom(ReportConfig reportConfig) throws IOException {
    requireNonNull(reportConfig.type, "Hey! Reporter type can't be null!");
    switch (reportConfig.type) {
      case JSON:
        return new MergedJsonReporter(reportConfig.where);
      case MARKDOWN:
        return new OssSecurityRatingMarkdownReporter(reportConfig.where, reportConfig.source,
            (OssSecurityRating) rating, OSS_SECURITY_GITHUB_ADVISOR);
      default:
        throw new IllegalArgumentException(format(
            "Oh no! That's an unknown type of report: %s", reportConfig.type));
    }
  }

  /**
   * A functional interface of a resolves that resolves an identifier
   * to something of a specified result type.
   *
   * @param <R> A type of the result.
   */
  private interface Resolver<R> {

    /**
     * Run the resolver for an identifier.
     *
     * @param identifier The identifier.
     * @return The result.
     * @throws IOException If something went wrong.
     */
    Optional<R> runFor(String identifier) throws IOException;
  }
}
