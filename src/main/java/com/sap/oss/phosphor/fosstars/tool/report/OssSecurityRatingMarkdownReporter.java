package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssSecurityRatingMarkdownFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This reporter takes a number of projects and generates a markdown report.
 */
public class OssSecurityRatingMarkdownReporter
    extends AbstractSecurityRatingMarkdownReporter<GitHubProject> {

  /**
   * A resource with a template for the report.
   */
  private static final String PROJECT_DETAILS_TEMPLATE_RESOURCE
      = "MarkdownProjectDetailsTemplate.md";

  /**
   * A template for the report.
   */
  private static final String PROJECT_DETAILS_TEMPLATE
      = loadFrom(PROJECT_DETAILS_TEMPLATE_RESOURCE, OssSecurityRatingMarkdownReporter.class);

  /**
   * A list of extra projects which should be added to the report.
   */
  private final List<GitHubProject> extraProjects;

  /**
   * A rating used in a report.
   */
  private final OssSecurityRating rating;

  /**
   * A formatter for rating values.
   */
  private final Formatter formatter;

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory An output directory.
   * @param rating A rating.
   * @param advisor An advisor for calculated ratings.
   * @throws IOException If something went wrong
   *                     (for example, the output directory doesn't exist,
   *                     or the extra projects couldn't be loaded).
   */
  public OssSecurityRatingMarkdownReporter(
      String outputDirectory, OssSecurityRating rating, Advisor advisor) throws IOException {

    this(outputDirectory, NO_EXTRA_SOURCE, rating, advisor);
  }

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory An output directory.
   * @param extraSourceFileName A JSON file with serialized extra projects.
   * @param rating A rating.
   * @param advisor An advisor for calculated ratings.
   * @throws IOException If something went wrong
   *                     (for example, the output directory doesn't exist,
   *                     or the extra projects couldn't be loaded).
   */
  public OssSecurityRatingMarkdownReporter(
      String outputDirectory, String extraSourceFileName, OssSecurityRating rating, Advisor advisor)
      throws IOException {

    super(outputDirectory, rating, advisor);
    this.extraProjects = loadProjects(extraSourceFileName);
    this.rating = rating;
    this.formatter = new OssSecurityRatingMarkdownFormatter(advisor);
  }

  @Override
  public void runFor(List<GitHubProject> projects) throws IOException {
    List<GitHubProject> allProjects = merge(projects, extraProjects);

    Map<GitHubProject, Integer> stars = new HashMap<>();
    for (GitHubProject project : allProjects) {
      stars.put(project, starsOf(project));
    }
    allProjects.sort(Collections.reverseOrder(Comparator.comparingInt(stars::get)));

    StringBuilder projectsTable = new StringBuilder();
    Statistics<GitHubProject> statistics = new ProjectStatistics();
    for (GitHubProject project : allProjects) {
      String projectPath = project.scm().getPath().replaceFirst("/", "");

      Path organizationDirectory = outputDirectory.resolve(project.organization().name());
      if (!Files.isDirectory(organizationDirectory)) {
        Files.createDirectories(organizationDirectory);
      }

      String details = PROJECT_DETAILS_TEMPLATE
          .replace("%PROJECT_URL%", project.scm().toString())
          .replace("%UPDATED_DATE%",
              project.ratingValueDate().map(DATE_FORMAT::format).orElse(UNKNOWN))
          .replace("%PROJECT_NAME%", projectPath)
          .replace("%DETAILS%", detailsOf(project));

      String projectReportFilename = String.format("%s.md", project.name());
      Files.write(
          organizationDirectory.resolve(projectReportFilename),
          details.getBytes());

      String relativePathToDetails = String.format("%s/%s",
          project.organization().name(), projectReportFilename);

      String labelLink = String.format(LINK_TEMPLATE, labelOf(project), relativePathToDetails);
      String nameLink = String.format(
          LINK_TEMPLATE,
          nameOf(project.scm().getPath().replaceFirst("/", "")),
          relativePathToDetails);

      Integer numberOfStars = stars.get(project);
      String numberOfStarsString
          = numberOfStars != null && numberOfStars >= 0 ? numberOfStars.toString() : UNKNOWN;
      String numberOfStarsLink = String.format(LINK_TEMPLATE, numberOfStarsString, project.scm());

      String line = PROJECT_LINE_TEMPLATE
          .replace("%NAME%", nameLink)
          .replace("%STARS%", numberOfStarsLink)
          .replace("%SCORE%", scoreOf(project))
          .replace("%LABEL%", labelLink)
          .replace("%CONFIDENCE%", confidenceOf(project))
          .replace("%DATE%", lastUpdateOf(project));
      projectsTable.append(line).append("\n");

      statistics.add(project);
    }

    Path path = outputDirectory.resolve(REPORT_FILENAME);
    logger.info("Storing a report to {}", path);
    Files.write(path, buildReportWith(projectsTable.toString(), statistics).getBytes());
  }

  /**
   * Builds a report for projects.
   *
   * @param table A content of the table with projects.
   * @param statistics Statistics about the projects.
   * @return The report.
   * @throws IOException If something went wrong.
   */
  @Override
  public String buildReportWith(String table, Statistics<GitHubProject> statistics)
      throws IOException {
    return super.buildReportWith(table, statistics)
        .replaceAll("%MODERATE_THRESHOLD%",
            format(rating.thresholds().forModerate()))
        .replaceAll("%GOOD_THRESHOLD%",
            format(rating.thresholds().forGood()))
        .replaceAll("%UNCLEAR_THRESHOLD%",
            format(rating.thresholds().forUnclear()));
  }

  /**
   * Prepares a description how a rating was calculated for a project.
   *
   * @param project The project.
   * @return The details of the rating calculation.
   */
  @Override
  String detailsOf(GitHubProject project) {
    if (!project.ratingValue().isPresent()) {
      return UNKNOWN;
    }
    return formatter.print(project);
  }
}
