package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.advice.oss.OssRulesOfPlayAdvisor;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.github.GHIssue;

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
    createIssuesIfRequested(project, commandLine);
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

  /**
   * Creates issues for findings in the respective repositories if a user asked about it.
   *
   * @param project The project.
   * @param commandLine Command-line options.
   * @throws IOException If something went wrong.
   */
  private void createIssuesIfRequested(GitHubProject project, CommandLine commandLine)
      throws IOException {

    if (commandLine.hasOption("create-issues")) {
      if (!project.ratingValue().isPresent()) {
        throw new IOException("Could not calculate a rating!");
      }

      logger.info("Creating issues for findings on {}", project.toString());

      List<Value<Boolean>> violations
          = OssRulesOfPlayScore.findViolatedRulesIn(
              project.ratingValue().get().scoreValue().usedValues());

      for (Value<Boolean> violation : violations) {
        String issueHeader = printTitle(violation);
        List<GHIssue> existingGitHubIssues = this.fetcher.gitHubIssuesFor(project, issueHeader);
        if (existingGitHubIssues.isEmpty()) {
          fetcher.createGitHubIssue(project, printTitle(violation), printBody(violation));
          logger.info("New issue: " + issueHeader);
        } else {
          logger.info("Issue already existing: " + issueHeader);
        }
      }
    }
  }

  /**
   * Print a title for a value.
   *
   * @param value The value.
   * @return The title.
   */
  public String printTitle(Value<?> value) {
    return String.format("%s Violation against OSS Rules of Play",
        markdownFormatter.identifierOf(value.feature()));
  }

  /**
   * Print a body for the value.
   *
   * @param value The value.
   * @return The body.
   */
  public String printBody(Value<?> value) {
    StringBuilder sb = new StringBuilder(
        "A violation against the OSS Rules of Play has been detected.\n\n");

    sb.append(String.format("Rule ID: %s\nExplanation: %s **%s**\n\n",
        markdownFormatter.identifierOf(value.feature()).replaceAll("[\\[\\]]", ""),
        markdownFormatter.nameOf(value.feature()),
        markdownFormatter.printValueAnswer(value)));

    if (markdownFormatter.ruleDocumentationUrl().isPresent()) {
      sb.append(String.format("Find more information at: %s",
          markdownFormatter.ruleDocumentationUrl().get()));
    }

    return sb.toString();
  }
}
