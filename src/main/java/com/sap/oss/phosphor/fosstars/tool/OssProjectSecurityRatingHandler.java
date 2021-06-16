package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This handler calculates {@link OssSecurityRating}.
 */
public class OssProjectSecurityRatingHandler extends AbstractHandler {

  /**
   * An advisor for calculated security ratings.
   */
  private static final Advisor OSS_SECURITY_GITHUB_ADVISOR = new OssSecurityGithubAdvisor();

  /**
   * Initializes a handler.
   */
  public OssProjectSecurityRatingHandler() {
    super(RatingRepository.INSTANCE.rating(OssSecurityRating.class));
  }

  @Override
  public String supportedRatingName() {
    return "security";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--url", "--gav", "--npm", "--purl", "--config");
  }

  @Override
  void processMaven(String coordinates) throws IOException {
    process(GAV.parse(coordinates));
  }

  /**
   * Calculate a rating for a single project identified by NPM name.
   *
   * @param name The NPM name.
   * @throws IOException If something went wrong.
   */
  @Override
  void processNpm(String name) throws IOException {
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

      if (isOnGitHub(url)) {
        return Optional.of(GitHubProject.parse(url));
      }

      logger.info("But unfortunately, I can work only with projects that stay on GitHub ...");
      logger.info("Let me try to find a mirror on GitHub ...");

      Optional<GitHubProject> mirror = finder.findGithubProjectFor(coordinates);
      if (!mirror.isPresent()) {
        logger.warn("Oh no! I could not find a mirror on GitHub!");
        return Optional.empty();
      }

      logger.info("Yup, that seems to be a corresponding project on GitHub:");
      logger.info("  {}", mirror.get().scm().toString());

      return mirror;
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
