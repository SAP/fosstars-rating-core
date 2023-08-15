package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.format.JsonPrettyPrinter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This reporter takes a number of projects and generates a json report.
 */
public class OssSecurityRatingJsonReporter extends OssSecurityRatingMarkdownReporter {

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory An output directory.
   * @param rating          A rating.
   * @param advisor         An advisor for calculated ratings.
   * @throws IOException If something went wrong (for example, the output directory doesn't exist,
   *                     or the extra projects couldn't be loaded).
   */
  public OssSecurityRatingJsonReporter(
      String outputDirectory, OssSecurityRating rating, Advisor advisor) throws IOException {
    this(outputDirectory, NO_EXTRA_SOURCE, rating, advisor);
  }

  /**
   * Initializes a new reporter.
   *
   * @param outputDirectory     An output directory.
   * @param extraSourceFileName A JSON file with serialized extra projects.
   * @param rating              A rating.
   * @param advisor             An advisor for calculated ratings.
   * @throws IOException If something went wrong (for example, the output directory doesn't exist,
   *                     or the extra projects couldn't be loaded).
   */
  public OssSecurityRatingJsonReporter(
      String outputDirectory, String extraSourceFileName, OssSecurityRating rating, Advisor advisor)
      throws IOException {
    super(outputDirectory, extraSourceFileName, rating, advisor, new JsonPrettyPrinter(advisor));
  }

  @Override
  protected String writeReport(GitHubProject project, String projectPath,
      Path organizationDirectory)
      throws IOException {
    String details = formatter().print(project);

    String projectReportFilename = String.format("%s.json", project.name());
    Files.write(
        organizationDirectory.resolve(projectReportFilename),
        details.getBytes());

    return projectReportFilename;
  }
}
