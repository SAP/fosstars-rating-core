package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

/**
 * This reporter takes a number of projects and generates a markdown report.
 */
public class MarkdownReporter extends AbstractReporter<GitHubProject> {

  /**
   * A formatter for rating values.
   */
  private static final  PrettyPrinter PRETTY_PRINTER = new PrettyPrinter();

  /**
   * A file where a report is going to be stored.
   */
  private static final String REPORT_FILENAME = "README.md";

  /**
   * A template for a table row in the report.
   */
  private static final String PROJECT_LINE_TEMPLATE
      = "| %URL% | %SCORE% | %LABEL% | %CONFIDENCE% | %DATE% |";

  /**
   * A date formatter.
   */
  private static final SimpleDateFormat DATE_FORMAT
      = new SimpleDateFormat("MMM d, YYYY", Locale.US);

  /**
   * This string is printed out if something is unknown.
   */
  private static final String UNKNOWN = "unknown";

  /**
   * An output directory.
   */
  private final String outputDirectory;

  /**
   * A list of extra projects which should be added to the report.
   */
  private final List<GitHubProject> extraProjects;

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory An output directory.
   * @param extraSourceFileName A JSON file with serialized extra projects.
   * @throws IOException If something went wrong
   *                     (for example, the output directory doesn't exist,
   *                     or the extra projects couldn't be loaded).
   */
  MarkdownReporter(String outputDirectory, String extraSourceFileName)
      throws IOException {

    Objects.requireNonNull(outputDirectory, "Oh no! Output directory is null!");
    if (!Files.isDirectory(Paths.get(outputDirectory))) {
      throw new FileNotFoundException(
          String.format("Oh no! I could not find %s", outputDirectory));
    }
    this.outputDirectory = outputDirectory;
    this.extraProjects = loadProjects(extraSourceFileName);
  }

  @Override
  public void runFor(List<GitHubProject> projects) throws IOException {
    List<GitHubProject> allProjects = merge(projects, extraProjects);
    allProjects.sort(Comparator.comparing(project -> project.url().toString()));

    String projectDetailsTemplate;
    try (InputStream is = getClass().getResourceAsStream("MarkdownProjectDetailsTemplate.md")) {
      projectDetailsTemplate = IOUtils.toString(is, "UTF-8");
    }

    StringBuilder sb = new StringBuilder();
    for (GitHubProject project : allProjects) {
      String projectPath = project.url().getPath().replaceFirst("/", "");

      Path organizationDirectory = Paths.get(outputDirectory)
          .resolve(project.organization().name());
      if (!Files.isDirectory(organizationDirectory)) {
        Files.createDirectories(organizationDirectory);
      }

      String details = projectDetailsTemplate
          .replace("%PROJECT_NAME%", projectPath)
          .replace("%DETAILS%", detailsOf(project));

      String projectReportFilename = String.format("%s.md", project.name());
      Files.write(
          organizationDirectory.resolve(projectReportFilename),
          details.getBytes());

      String relativePathToDetails = String.format("%s/%s",
          project.organization().name(), projectReportFilename);
      String url = String.format("[%s](%s)", projectPath, project.url());
      String label = String.format("[%s](%s)", labelOf(project), relativePathToDetails);
      String line = PROJECT_LINE_TEMPLATE
          .replace("%URL%", url)
          .replace("%SCORE%", scoreOf(project))
          .replace("%LABEL%", label)
          .replace("%CONFIDENCE%", confidenceOf(project))
          .replace("%DATE%", lastUpdateOf(project));
      sb.append(line).append("\n");
    }

    try (InputStream is = getClass().getResourceAsStream("MarkdownReporterMainTemplate.md")) {
      String template = IOUtils.toString(is, "UTF-8");
      String content = template.replace("%PROJECT_TABLE%", sb.toString());

      Path path = Paths.get(outputDirectory).resolve(REPORT_FILENAME);
      System.out.printf("[+] Storing a report to %s%n", path);
      Files.write(path, content.getBytes());
    }
  }

  /**
   * Prepares a description how a rating was calculated for a project.
   *
   * @param project The project.
   * @return The details of the rating calculation.
   */
  private static String detailsOf(GitHubProject project) {
    if (!project.ratingValue().isPresent()) {
      return UNKNOWN;
    }
    return PRETTY_PRINTER.print(project.ratingValue().get());
  }

  /**
   * Formats a date when a rating was calculated for a project.
   */
  private static String lastUpdateOf(GitHubProject project) {
    if (project.ratingValueDate() == null) {
      return UNKNOWN;
    }
    return DATE_FORMAT.format(project.ratingValueDate());
  }

  /**
   * Formats a confidence of a rating of a project.
   */
  private static String confidenceOf(GitHubProject project) {
    Optional<RatingValue> something = project.ratingValue();
    if (!something.isPresent()) {
      return UNKNOWN;
    }
    return String.format("%2.2f", something.get().confidence());
  }

  /**
   * Formats a label of a rating of a project.
   */
  private static String labelOf(GitHubProject project) {
    Optional<RatingValue> something = project.ratingValue();
    if (!something.isPresent()) {
      return UNKNOWN;
    }
    return something.get().label().name();
  }

  /**
   * Formats a score of a project.
   */
  private static String scoreOf(GitHubProject project) {
    Optional<RatingValue> something = project.ratingValue();
    if (!something.isPresent()) {
      return UNKNOWN;
    }
    return String.format("%2.2f", something.get().scoreValue().get());
  }


}
