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
 *
 */
public class OssRulesOfPlayRatingHandler extends AbstractHandler {

  /**
   * An advisor for calculated security ratings.
   */
  private final OssRulesOfPlayAdvisor advisor;

  /**
   *
   * @throws IOException
   */
  public OssRulesOfPlayRatingHandler() throws IOException {
    super(RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class));
    this.advisor = new OssRulesOfPlayAdvisor();
  }

  @Override
  public String supportedRatingName() {
    return "oss-rules-of-play";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--url" , "--purl", "--config");
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
    super.processUrl(url);
    createIssuesIfRequested(GitHubProject.parse(url), commandLine);
  }

  @Override
  Formatter createFormatter(String type) throws IOException {
    switch (type) {
      case "text":
        return commandLine.hasOption("v")
            ? PrettyPrinter.withVerboseOutput(advisor)
            : PrettyPrinter.withoutVerboseOutput();
      case "markdown":
        return new OssRulesOfPlayRatingMarkdownFormatter(advisor);
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

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    if (commandLine.hasOption("create-issues") && rating instanceof OssRulesOfPlayRating) {
      logger.info("Creating issues for findings on {}", project.toString());
      Formatter formatter = createFormatter("markdown");
      List<Value<Boolean>> violations =
          OssRulesOfPlayScore.findViolatedRulesIn(
              project.ratingValue().get().scoreValue().usedValues());
      for (Value<Boolean> violation : violations) {
        String issueHeader = formatter.printTitle(violation);
        List<GHIssue> existingGitHubIssues = this.fetcher.gitHubIssuesFor(project, issueHeader);
        if (existingGitHubIssues.isEmpty()) {
          logger.info("New issue: " + issueHeader);
          this.fetcher.createGitHubIssue(
              project, formatter.printTitle(violation), formatter.printBody(violation));
        } else {
          logger.info("Issue already existing: " + issueHeader);
        }
      }
    }
  }
}
