package com.sap.oss.phosphor.fosstars.tool.github;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;

import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating.OssRulesOfPlayLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A Markdown reporter
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating}.
 */
public class OssRulesOfPlayMarkdownReporter extends AbstractReporter<GitHubProject> {

  /**
   * A file where a report is going to be stored.
   */
  static final String REPORT_FILENAME = "README.md";

  /**
   * A resource with a template for the report.
   */
  private static final String REPORT_TEMPLATE_RESOURCE
      = "OssRulesOfPlayMarkdownReporterTemplate.md";

  /**
   * A template for the report.
   */
  private static final String REPORT_TEMPLATE
      = loadFrom(REPORT_TEMPLATE_RESOURCE, OssSecurityRatingMarkdownReporter.class);

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
   * A formatter for rating values.
   */
  private final Formatter formatter;

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
    this.formatter = new OssRulesOfPlayRatingMarkdownFormatter();
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
      if (ratingValue.label() == OssRulesOfPlayLabel.PASSED) {
        numberOfPass++;
      } else if (ratingValue.label() == OssRulesOfPlayLabel.FAILED) {
        numberOfFail++;
      } else if (ratingValue.label() == OssRulesOfPlayLabel.UNCLEAR) {
        numberOfUnclear++;
      } else {
        throw new IllegalStateException(
            String.format("Oh no! Unexpected label: %s", ratingValue.label()));
      }
      total++;

      Path organizationDirectory = outputDirectory.resolve(project.organization().name());
      if (!Files.isDirectory(organizationDirectory)) {
        Files.createDirectories(organizationDirectory);
      }

      String projectReportFilename = String.format("%s.md", project.name());
      Files.write(
          organizationDirectory.resolve(projectReportFilename),
          formatter.print(project).getBytes());
    }

    double percentOfPass = (double) numberOfPass / total * 100;
    double percentOfFail = (double) numberOfFail / total * 100;
    double percentOfUnclear = (double) numberOfUnclear / total * 100;

    String content = REPORT_TEMPLATE
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

  /**
   * Builds a table with projects.
   *
   * @param projects The projects.
   * @return A table with the projects.
   */
  private String tableOf(List<GitHubProject> projects) {
    return projects.stream()
        .filter(project -> project.ratingValue().isPresent())
        .sorted(Comparator.comparing(project -> project.ratingValue().get().label().name()))
        .map(this::rowFor)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Builds a table row for a project.
   *
   * @param project The project.
   * @return A table row for the project.
   */
  private String rowFor(GitHubProject project) {
    return PROJECT_LINE_TEMPLATE
        .replace("%NAME%", nameOf(project))
        .replace("%STATUS%", statusOf(project))
        .replace("%NUMBER_OF_VIOLATED_RULES%", numberOfViolatedRulesIn(project));
  }

  /**
   * Prints a name of a project.
   *
   * @param project The project.
   * @return A formatted name of the project.
   */
  private static String nameOf(GitHubProject project) {
    return String.format("[%s/%s](%s)",
        project.organization().name(), project.name(), project.scm().toString());
  }

  /**
   * Prints a status of a project.
   *
   * @param project The project.
   * @return A status of the project.
   */
  private String statusOf(GitHubProject project) {
    return String.format("[%s](%s/%s.md)",
        project.ratingValue().map(RatingValue::label).map(Label::name).orElse("UNKNOWN"),
        project.organization().name(),
        project.name());
  }

  /**
   * Prints a formatted number of violated rules for a project.
   *
   * @param project The project.
   * @return A formatted number of violated rules for the project.
   */
  private static String numberOfViolatedRulesIn(GitHubProject project) {
    Optional<RatingValue> ratingValue = project.ratingValue();
    if (!ratingValue.isPresent()) {
      return "UNKNOWN";
    }

    int n = findViolatedRulesIn(ratingValue.get().scoreValue().usedValues()).size();

    if (n == 0) {
      return "No violated rules";
    }

    return String.format("%d violated rule%s", n, n > 1 ? "s" : "");
  }

}
