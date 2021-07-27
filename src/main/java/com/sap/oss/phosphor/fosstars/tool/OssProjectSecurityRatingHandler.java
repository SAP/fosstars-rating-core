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

  @Override
  void processConfig(String filename) throws IOException {
    nvd.preload();
    super.processConfig(filename);
  }

  /**
   * Calculate a rating for a single project identified by GAV coordinates.
   *
   * @param coordinates The GAV coordinates.
   * @throws IOException If something went wrong.
   */
  private void process(GAV coordinates) throws IOException {
    process(coordinates.toString(), gav -> {
      Optional<GitHubProject> project = new MavenScmFinder().findGithubProjectFor(coordinates);
      if (!project.isPresent()) {
        throw new IOException("Oh no! Could not find SCM on GitHub for the artifact!");
      }

      logger.info("SCM is {}", project.get().scm());

      return project;
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

  @Override
  Optional<Reporter<GitHubProject>> reporterFrom(ReportConfig reportConfig) throws IOException {
    requireNonNull(reportConfig.type, "Hey! Reporter type can't be null!");
    switch (reportConfig.type) {
      case JSON:
        return Optional.of(new MergedJsonReporter(reportConfig.where));
      case MARKDOWN:
        return Optional.of(
            new OssSecurityRatingMarkdownReporter(reportConfig.where, reportConfig.source,
                (OssSecurityRating) rating, OSS_SECURITY_GITHUB_ADVISOR));
      default:
        logger.warn("Oops! That's an unknown type of report: {}", reportConfig.type);
        return Optional.empty();
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
