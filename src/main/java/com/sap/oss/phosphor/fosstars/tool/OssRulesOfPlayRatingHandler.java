package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.advice.oss.OssRulesOfPlayAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.github.AdviceForGitHubContextFactory;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import com.sap.oss.phosphor.fosstars.tool.report.OssRulesOfPlayGitHubIssuesReporter;
import com.sap.oss.phosphor.fosstars.tool.report.OssRulesOfPlayMarkdownReporter;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This handler calculates {@link OssRulesOfPlayRating}.
 */
public class OssRulesOfPlayRatingHandler extends AbstractHandler<GitHubProject> {

  /**
   * An advisor for calculated security ratings.
   */
  private final OssRulesOfPlayAdvisor advisor;

  /**
   * A markdown formatter.
   */
  private final OssRulesOfPlayRatingMarkdownFormatter markdownFormatter;

  /**
   * Initializes a handler.
   *
   * @throws IOException If initialization failed.
   */
  public OssRulesOfPlayRatingHandler() throws IOException {
    super(RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class));
    advisor = new OssRulesOfPlayAdvisor(AdviceForGitHubContextFactory.INSTANCE);
    markdownFormatter = new OssRulesOfPlayRatingMarkdownFormatter(advisor);
  }

  @Override
  public String supportedRatingName() {
    return "oss-rules-of-play";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--url", "--purl", "--config");
  }

  @Override
  void processMaven(String coordinates) {
    throw new UnsupportedOperationException("Oops! I don't support GAV coordinates!");
  }

  /**
   * Calculate a rating for a single project identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @throws IOException If something went wrong.
   */
  @Override
  void processUrl(String url) throws IOException {
    GitHubProject project = GitHubProject.parse(url);
    process(project);
    if (commandLine.hasOption("create-issues")) {
      new OssRulesOfPlayGitHubIssuesReporter(fetcher, markdownFormatter).createIssuesFor(project);
    }
  }

  @Override
  void process(List<GitHubProject> projects, List<Reporter<GitHubProject>> reporters,
      String projectCacheFile) throws IOException {

    logger.info("Starting calculating ratings ...");
    MultipleRatingsCalculator multipleRatingsCalculator =
        new MultipleRatingsCalculator(calculator())
            .set(loadSubjectCache(projectCacheFile))
            .storeCacheTo(projectCacheFile)
            .calculateFor(projects);

    logger.info("Okay, we've done calculating the ratings");

    List<Subject> failedSubjects = multipleRatingsCalculator.failedSubjects();
    if (!failedSubjects.isEmpty()) {
      logger.warn("Ratings couldn't be calculated for {} project{}",
          failedSubjects.size(), failedSubjects.size() == 1 ? "" : "s");
      for (GitHubProject project : projects) {
        logger.info("    {}", project.scm());
      }
    }

    if (!reporters.isEmpty()) {
      logger.info("Now let's generate reports");
      for (Reporter<GitHubProject> reporter : reporters) {
        reporter.runFor(projects);
      }
    }
  }

  @Override
  void process(GitHubProject project) throws IOException {
    calculator().calculateFor(project);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(project).split("\n")).forEach(logger::info);
    logger.info("");
    storeReportIfRequested(project, commandLine);
  }

  @Override
  void processConfig(String filename) throws IOException {
    nvd.preload();
    logger.info("Loading config from {}", filename);
    Config config = Config.from(filename);

    // try to create reporters earlier to catch a possible misconfiguration
    // before calculating ratings
    final List<Reporter<GitHubProject>> reporters = makeReporters(config);

    logger.info("Look for projects ...");
    List<GitHubProject> projects = new GitHubProjectFinder(fetcher.github())
        .set(config.finderConfig)
        .run();
    logger.info("Found {} project{}", projects.size(), projects.size() > 1 ? "s" : "");
    for (GitHubProject project : projects) {
      logger.info("  {}", project.scm());
    }

    String projectCacheFile = projectCacheFile(config);
    process(projects, reporters, projectCacheFile);
  }

  @Override
  Formatter createFormatter(String type) {
    switch (type) {
      case "text":
        return commandLine.hasOption("v")
            ? PrettyPrinter.withVerboseOutput(advisor)
            : PrettyPrinter.withoutVerboseOutput();
      case "markdown":
        return markdownFormatter;
      default:
        throw new IllegalArgumentException(format("Unsupported report type: %s", type));
    }
  }

  @Override
  Optional<Reporter<GitHubProject>> reporterFrom(ReportConfig reportConfig) throws IOException {
    requireNonNull(reportConfig.type, "Hey! Reporter type can't be null!");
    switch (reportConfig.type) {
      case ISSUES:
        return Optional.of(new OssRulesOfPlayGitHubIssuesReporter(fetcher, markdownFormatter));
      case MARKDOWN:
        return Optional.of(new OssRulesOfPlayMarkdownReporter(reportConfig.where, advisor));
      default:
        logger.warn("Oops! That's an unknown type of report: {}", reportConfig.type);
        return Optional.empty();
    }
  }
}
