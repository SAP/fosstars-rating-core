package com.sap.oss.phosphor.fosstars.tool.github;

import static java.lang.String.format;

import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURL.StandardTypes;
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
import java.util.Arrays;
import java.util.List;
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
  public OssRulesOfPlayRatingHandler run() throws Exception {
    requireOneOfIn(commandLine, "--url" , "--purl", "--config");

    if (commandLine.hasOption("url")) {
      processUrl(commandLine.getOptionValue("url"));
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

    if (StandardTypes.GITHUB.equalsIgnoreCase(purl.getType())) {
      String url = format("https://github.com/%s/%s", purl.getNamespace(), purl.getName());
      processUrl(url);
    } else {
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
    createIssuesIfRequested(project, commandLine);
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
