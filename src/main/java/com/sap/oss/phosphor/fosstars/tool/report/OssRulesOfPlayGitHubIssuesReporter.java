package com.sap.oss.phosphor.fosstars.tool.report;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHIssue;

/**
 * This reporter creates GitHub issues for RoP violations.
 */
public class OssRulesOfPlayGitHubIssuesReporter implements Reporter<GitHubProject> {

  /**
   * A logger.
   */
  private static final Logger LOGGER
      = LogManager.getLogger(OssRulesOfPlayGitHubIssuesReporter.class);

  /**
   * An interface to GitHub.
   */
  private final GitHubDataFetcher fetcher;

  /**
   * A Markdown formatter.
   */
  private final OssRulesOfPlayRatingMarkdownFormatter formatter;

  /**
   * Create a new reporter.
   *
   * @param fetcher An interface to GitHub.
   * @param formatter A Markdown formatter.
   */
  public OssRulesOfPlayGitHubIssuesReporter(
      GitHubDataFetcher fetcher, OssRulesOfPlayRatingMarkdownFormatter formatter) {

    requireNonNull(fetcher, "Oh no! Fetcher is null!");
    requireNonNull(formatter, "Oh no! Formatter is null!");

    this.fetcher = fetcher;
    this.formatter = formatter;
  }

  @Override
  public void runFor(List<GitHubProject> projects) {
    LOGGER.info("Creating issues for OSS RoP violations ...");
    for (GitHubProject project : projects) {
      try {
        createIssuesFor(project);
      } catch (Exception e) {
        LOGGER.warn(() -> format("Oops! Could not create issues for %s", project.scm()), e);
      }
    }
  }

  /**
   * Create issues for RoP violations in a project.
   *
   * @param project The project.
   * @throws IOException If something went wrong.
   */
  public void createIssuesFor(GitHubProject project) throws IOException {
    if (!project.ratingValue().isPresent()) {
      LOGGER.warn("Oops! No rating calculated for {}", project.scm());
      return;
    }

    if (!fetcher.repositoryFor(project).hasIssues()) {
      LOGGER.warn("Creating issues is disabled for project {}", project.toString());
      return;
    }

    LOGGER.info("Creating issues for violations on {}", project.toString());

    List<Value<Boolean>> violations
        = OssRulesOfPlayScore.findViolatedRulesIn(
            project.ratingValue().get().scoreValue().usedValues());

    for (Value<Boolean> violation : violations) {
      String issueHeader = printTitle(violation);
      String withIssueTitle = formatter.identifierOf(violation.feature()).orElse(issueHeader);
      List<GHIssue> existingGitHubIssues = fetcher.gitHubIssuesFor(project, withIssueTitle);
      if (existingGitHubIssues.isEmpty()) {
        fetcher.createGitHubIssue(project, issueHeader, printBody(violation));
        LOGGER.info("New issue: " + issueHeader);
      } else {
        LOGGER.info("Issue already exists: " + issueHeader);
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
    String title = "Violation against OSS Rules of Play";
    return formatter.identifierOf(value.feature())
        .map(id -> format("%s %s", id, title)).orElse(title);
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

    sb.append(format("Rule ID: %s\nExplanation: %s **%s**\n\n",
        formatter.identifierOf(value.feature()).orElse(EMPTY).replaceAll("[\\[\\]]", EMPTY),
        formatter.nameOf(value.feature()),
        formatter.printValueAnswer(value)));

    if (formatter.ruleDocumentationUrl().isPresent()) {
      sb.append(format("Find more information at: %s", formatter.ruleDocumentationUrl().get()));
    }

    return sb.toString();
  }
}
