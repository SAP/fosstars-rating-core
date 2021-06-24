package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.advice.oss.OssRulesOfPlayAdvisor;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import com.sap.oss.phosphor.fosstars.tool.report.OssRulesOfPlayGitHubIssuesReporter;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This handler calculates {@link OssRulesOfPlayRating}.
 */
public class OssRulesOfPlayRatingHandler extends AbstractHandler {

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
    advisor = new OssRulesOfPlayAdvisor();
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
    Optional<OssRulesOfPlayGitHubIssuesReporter> reporter = issuesReporter();
    if (reporter.isPresent()) {
      reporter.get().createIssuesFor(project);
    }
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
  List<Reporter<GitHubProject>> makeReporters(Config config) throws IOException {
    List<Reporter<GitHubProject>> reporters = new ArrayList<>(super.makeReporters(config));
    issuesReporter().ifPresent(reporters::add);
    return reporters;
  }

  /**
   * Creates a {@link OssRulesOfPlayGitHubIssuesReporter} if possible.
   *
   * @return A new {@link OssRulesOfPlayGitHubIssuesReporter} if available.
   */
  private Optional<OssRulesOfPlayGitHubIssuesReporter> issuesReporter() {
    if (!commandLine.hasOption("create-issues") || fetcher == null) {
      return Optional.empty();
    }

    return Optional.of(new OssRulesOfPlayGitHubIssuesReporter(fetcher, markdownFormatter));
  }
}
