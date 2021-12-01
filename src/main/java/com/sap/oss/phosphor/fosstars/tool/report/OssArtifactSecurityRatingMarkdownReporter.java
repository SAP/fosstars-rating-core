package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
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
 * This reporter takes a number of artifacts and generates a markdown report.
 */
public class OssArtifactSecurityRatingMarkdownReporter
    extends AbstractSecurityRatingMarkdownReporter<MavenArtifact> {

  /**
   * A resource with a template for the report.
   */
  private static final String ARTIFACT_DETAILS_TEMPLATE_RESOURCE
      = "MarkdownArtifactDetailsTemplate.md";

  /**
   * A template for the report.
   */
  private static final String ARTIFACT_DETAILS_TEMPLATE
      = loadFrom(ARTIFACT_DETAILS_TEMPLATE_RESOURCE,
      OssArtifactSecurityRatingMarkdownReporter.class);

  /**
   * A list of extra artifacts which should be added to the report.
   */
  private final List<MavenArtifact> extraArtifacts;

  /**
   * A rating used in a report.
   */
  private final OssArtifactSecurityRating rating;

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
  public OssArtifactSecurityRatingMarkdownReporter(
      String outputDirectory, OssArtifactSecurityRating rating,
      Advisor advisor) throws IOException {

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
  public OssArtifactSecurityRatingMarkdownReporter(
      String outputDirectory, String extraSourceFileName,
      OssArtifactSecurityRating rating, Advisor advisor)
      throws IOException {

    super(outputDirectory, rating, advisor);
    this.extraArtifacts = loadProjects(extraSourceFileName);
    this.rating = rating;
    this.formatter = new OssSecurityRatingMarkdownFormatter(advisor);
  }

  @Override
  public void runFor(List<MavenArtifact> artifacts) throws IOException {
    List<MavenArtifact> allArtifacts = mergeArtifacts(artifacts, extraArtifacts);

    Map<MavenArtifact, Integer> stars = new HashMap<>();
    for (MavenArtifact artifact : allArtifacts) {
      stars.put(artifact, starsOf(artifact));
    }
    allArtifacts.sort(Collections.reverseOrder(Comparator.comparingInt(stars::get)));

    StringBuilder artifactsTable = new StringBuilder();
    Statistics<MavenArtifact> statistics = new MavenArtifactStatistics();
    for (MavenArtifact mavenArtifact : allArtifacts) {
      String projectPath = mavenArtifact.gav();

      Path organizationDirectory = outputDirectory.resolve(mavenArtifact.group());
      if (!Files.isDirectory(organizationDirectory)) {
        Files.createDirectories(organizationDirectory);
      }

      String details = ARTIFACT_DETAILS_TEMPLATE
          .replace("%MAVEN_ARTIFACT_ID%", mavenArtifact.gav())
          .replace("%UPDATED_DATE%",
              mavenArtifact.ratingValueDate().map(DATE_FORMAT::format).orElse(UNKNOWN))
          .replace("%ARTIFACT_NAME%", projectPath)
          .replace("%DETAILS%", detailsOf(mavenArtifact));

      String projectReportFilename = String.format("%s-%s.md",
          mavenArtifact.artifact(),
          mavenArtifact.version().get());
      Files.write(
          organizationDirectory.resolve(projectReportFilename),
          details.getBytes());

      String relativePathToDetails = String.format("%s/%s",
          mavenArtifact.group(), projectReportFilename);

      String labelLink
          = String.format(LINK_TEMPLATE, labelOf(mavenArtifact), relativePathToDetails);
      String nameLink
          = String.format(LINK_TEMPLATE, nameOf(mavenArtifact.gav()), relativePathToDetails);

      Integer numberOfStars = stars.get(mavenArtifact);
      String numberOfStarsString
          = numberOfStars != null && numberOfStars >= 0 ? numberOfStars.toString() : UNKNOWN;
      String numberOfStarsLink
          = String.format(LINK_TEMPLATE,
          numberOfStarsString,
          mavenArtifact.project().get().toString());

      String line = PROJECT_LINE_TEMPLATE
          .replace("%NAME%", nameLink)
          .replace("%STARS%", numberOfStarsLink)
          .replace("%SCORE%", scoreOf(mavenArtifact))
          .replace("%LABEL%", labelLink)
          .replace("%CONFIDENCE%", confidenceOf(mavenArtifact))
          .replace("%DATE%", lastUpdateOf(mavenArtifact));
      artifactsTable.append(line).append("\n");

      statistics.add(mavenArtifact);
    }

    Path path = outputDirectory.resolve(REPORT_FILENAME);
    logger.info("Storing a report to {}", path);
    Files.write(path, buildReportWith(artifactsTable.toString(), statistics).getBytes());
  }

  @Override
  public String buildReportWith(String table, Statistics<MavenArtifact> statistics)
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
   * Prepares a description how a rating was calculated for a mavenArtifact.
   *
   * @param mavenArtifact The mavenArtifact.
   * @return The details of the rating calculation.
   */
  @Override
  String detailsOf(MavenArtifact mavenArtifact) {
    if (!mavenArtifact.ratingValue().isPresent()) {
      return UNKNOWN;
    }
    return formatter.print(mavenArtifact);
  }
}
