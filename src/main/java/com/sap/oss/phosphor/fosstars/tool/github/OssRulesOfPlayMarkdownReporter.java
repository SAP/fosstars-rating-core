package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating.OssRulesOfPlayLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A Markdown reporter
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating}s.
 */
public class OssRulesOfPlayMarkdownReporter extends AbstractReporter<GitHubProject> {

  /**
   * A file where a report is going to be stored.
   */
  static final String REPORT_FILENAME = "README.md";

  /**
   * A resource with a template for the report.
   */
  private static final String RESOURCE = "OssRulesOfPlayMarkdownReporterTemplate.md";

  /**
   * A template for the report.
   */
  private static final String TEMPLATE
      = loadFrom(RESOURCE, OssSecurityRatingMarkdownReporter.class);

  /**
   * A template for a table row in the report.
   */
  private static final String PROJECT_LINE_TEMPLATE
      = "| %NAME% | %STATUS% | %NUMBER_OF_VIOLATED_RULES% |";

  /**
   * An output directory.
   */
  private final Path outputDirectory;

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory An output directory.
   * @throws IOException If something went wrong.
   */
  public OssRulesOfPlayMarkdownReporter(String outputDirectory) throws IOException {
    Objects.requireNonNull(outputDirectory, "Oh no! Output directory is null!");

    Path directory = Paths.get(outputDirectory);
    if (Files.isSymbolicLink(directory) || Files.isRegularFile(directory)) {
      throw new IOException(
          String.format("Oops! Output directory is not a directory: %s", outputDirectory));
    }
    if (!Files.isDirectory(directory)) {
      Files.createDirectories(directory);
    }
    if (!Files.isDirectory(directory)) {
      throw new IOException(
          String.format("Oops! Output directory doesn't exist: %s", directory));
    }

    this.outputDirectory = directory;
  }

  @Override
  public void runFor(List<GitHubProject> projects) throws IOException {
    int total = 0;
    long numberOfPass = 0;
    long numberOfFail = 0;
    long numberOfUnclear = 0;
    for (GitHubProject project : projects) {
      Optional<RatingValue> something = project.ratingValue();
      if (!something.isPresent()) {
        continue;
      }
      RatingValue ratingValue = something.get();
      if (ratingValue.label() == OssRulesOfPlayLabel.PASS) {
        numberOfPass++;
      } else if (ratingValue.label() == OssRulesOfPlayLabel.FAIL) {
        numberOfFail++;
      } else if (ratingValue.label() == OssRulesOfPlayLabel.UNCLEAR) {
        numberOfUnclear++;
      } else {
        throw new IllegalStateException(
            String.format("Oh no! Unexpected label: %s", ratingValue.label()));
      }
      total++;
    }

    double percentOfPass = (double) numberOfPass / total * 100;
    double percentOfFail = (double) numberOfFail / total * 100;
    double percentOfUnclear = (double) numberOfUnclear / total * 100;

    String content = TEMPLATE
        .replace("%NUMBER_OF_PROJECTS%", String.valueOf(total))
        .replace("%NUMBER_FAILED_PROJECTS%", String.valueOf(numberOfFail))
        .replace("%NUMBER_PASSED_PROJECTS%", String.valueOf(numberOfPass))
        .replace("%NUMBER_UNCLEAR_PROJECTS%", String.valueOf(numberOfUnclear))
        .replace("%PERCENT_FAILED_PROJECTS%", String.format("%2.1f", percentOfFail))
        .replace("%PERCENT_PASSED_PROJECTS%", String.format("%2.1f", percentOfPass))
        .replace("%PERCENT_UNCLEAR_PROJECTS%", String.format("%2.1f", percentOfUnclear))
        .replace("%PROJECT_TABLE%", tableOf(projects));

    Path path = outputDirectory.resolve(REPORT_FILENAME);
    logger.info("Storing a report to {}", path);
    Files.write(path, content.getBytes());
  }

  private static String tableOf(List<GitHubProject> projects) {
    return projects.stream()
        .map(OssRulesOfPlayMarkdownReporter::rowFor)
        .collect(Collectors.joining("\n"));
  }

  private static String rowFor(GitHubProject project) {
    return PROJECT_LINE_TEMPLATE
        .replace("%NAME%", nameOf(project))
        .replace("%STATUS%", statusOf(project))
        .replace("%NUMBER_OF_VIOLATED_RULES%", numberOfViolatedRulesIn(project));
  }

  private static String nameOf(GitHubProject project) {
    return String.format("[%s/%s](%s)",
        project.organization().name(), project.name(), project.scm().toString());
  }

  private static String statusOf(GitHubProject project) {
    return project.ratingValue().map(RatingValue::label).map(Label::name).orElse("UNKNOWN");
  }

  private static String numberOfViolatedRulesIn(GitHubProject project) {
    return "TODO";
  }

}
