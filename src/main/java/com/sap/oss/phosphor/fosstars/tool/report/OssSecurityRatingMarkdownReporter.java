package com.sap.oss.phosphor.fosstars.tool.report;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssSecurityRatingMarkdownFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

/**
 * This reporter takes a number of projects and generates a markdown report.
 */
public class OssSecurityRatingMarkdownReporter extends AbstractReporter<GitHubProject> {

  /**
   * No extra projects.
   */
  private static final String NO_EXTRA_SOURCE = null;

  /**
   * This value shows that a number of stars is unknown.
   */
  private static final int UNKNOWN_NUMBER_OF_STARS = -1;

  /**
   * A file where a report is going to be stored.
   */
  static final String REPORT_FILENAME = "README.md";

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
   * A template for a table row in the report.
   */
  private static final String PROJECT_LINE_TEMPLATE
      = "| %NAME% | %STARS% | %SCORE% | %LABEL% | %CONFIDENCE% | %DATE% |";

  /**
   * A template for links in Markdown.
   */
  private static final String LINK_TEMPLATE = "[%s](%s)";
  
  /**
   * A length of line in a project name.
   */
  private static final int NAME_LINE_LENGTH = 42;

  /**
   * A date formatter.
   */
  private static final SimpleDateFormat DATE_FORMAT
      = new SimpleDateFormat("MMM d, yyyy", Locale.US);

  /**
   * A formatter for doubles.
   */
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(2);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
  }

  /**
   * This string is printed out if something is unknown.
   */
  static final String UNKNOWN = "Unknown";

  /**
   * An output directory.
   */
  private final Path outputDirectory;

  /**
   * A list of extra projects which should be added to the report.
   */
  private final List<GitHubProject> extraProjects;

  /**
   * A formatter for rating values.
   */
  private final Formatter formatter;

  /**
   * A rating used in a report.
   */
  private final OssSecurityRating rating;

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

    Objects.requireNonNull(rating, "Oh no! Rating is null");
    Objects.requireNonNull(advisor, "Oh no! Advisor is null");
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
    Statistics statistics = new Statistics();
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
      String nameLink = String.format(LINK_TEMPLATE, nameOf(project), relativePathToDetails);

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
   * Prints out a name of a project.
   *
   * @param project The project.
   * @return A name of the project.
   */
  private static String nameOf(GitHubProject project) {
    return insert("<br>", NAME_LINE_LENGTH,
        project.scm().getPath().replaceFirst("/", ""));
  }

  /**
   * Insert a string after each n characters.
   *
   * @param string The string to be inserted.
   * @param n The number of characters.
   * @param content The original string.
   * @return The updated string.
   */
  static String insert(String string, int n, String content) {
    if (content.length() <= n) {
      return content;
    }
    int start = 0;
    int end = n;
    StringBuilder sb = new StringBuilder();
    while (end < content.length()) {
      sb.append(content, start, end);
      sb.append(string);
      start = end;
      end += n;
    }
    sb.append(content, start, content.length());
    return sb.toString();
  }

  /**
   * Builds a report for projects.
   *
   * @param table A content of the table with projects.
   * @param statistics Statistics about the projects.
   * @return The report.
   * @throws IOException If something went wrong.
   */
  private String buildReportWith(String table, Statistics statistics) throws IOException {
    try (InputStream is = OssSecurityRatingMarkdownReporter.class
        .getResourceAsStream("OssSecurityRatingMarkdownReporterMainTemplate.md")) {

      String template = IOUtils.toString(is, StandardCharsets.UTF_8);
      return template
          .replace("%PROJECT_TABLE%", table)
          .replace("%NUMBER_OF_PROJECTS%", String.valueOf(statistics.total))
          .replace("%NUMBER_BAD_RATINGS%", String.valueOf(statistics.badRatings))
          .replace("%NUMBER_MODERATE_RATINGS%", String.valueOf(statistics.moderateRatings))
          .replace("%NUMBER_GOOD_RATINGS%", String.valueOf(statistics.goodRatings))
          .replace("%NUMBER_UNKNOWN_RATINGS%", String.valueOf(statistics.unknownRatings))
          .replace("%NUMBER_UNCLEAR_RATINGS%", String.valueOf(statistics.unclearRatings))
          .replace("%PERCENT_BAD_RATINGS%",
              printPercent(statistics.badRatingsPercent()))
          .replace("%PERCENT_MODERATE_RATINGS%",
              printPercent(statistics.moderateRatingsPercent()))
          .replace("%PERCENT_GOOD_RATINGS%",
              printPercent(statistics.goodRatingsPercent()))
          .replace("%PERCENT_UNCLEAR_RATINGS%",
              printPercent(statistics.unclearRatingsPercent()))
          .replace("%PERCENT_UNKNOWN_RATINGS%",
              printPercent(statistics.unknownRatingsPercent()))
          .replaceAll("%MODERATE_THRESHOLD%",
              format(rating.thresholds().forModerate()))
          .replaceAll("%GOOD_THRESHOLD%",
              format(rating.thresholds().forGood()))
          .replaceAll("%UNCLEAR_THRESHOLD%",
              format(rating.thresholds().forUnclear()));
    }
  }

  /**
   * Formats a percent value.
   *
   * @param value The value.
   * @return A formatted string.
   */
  private static String printPercent(double value) {
    return String.format("%2.1f", value);
  }

  /**
   * Format a double.
   *
   * @param value The number.
   * @return A formatter number.
   */
  private static String format(double value) {
    return DECIMAL_FORMAT.format(value);
  }

  /**
   * Prepares a description how a rating was calculated for a project.
   *
   * @param project The project.
   * @return The details of the rating calculation.
   */
  private String detailsOf(GitHubProject project) {
    if (!project.ratingValue().isPresent()) {
      return UNKNOWN;
    }
    return formatter.print(project);
  }

  /**
   * Formats a date when a rating was calculated for a project.
   */
  private static String lastUpdateOf(GitHubProject project) {
    return project.ratingValueDate().map(DATE_FORMAT::format).orElse(UNKNOWN);
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

    RatingValue ratingValue = something.get();
    ScoreValue scoreValue = ratingValue.scoreValue();
    return actualValueOf(scoreValue);
  }

  /**
   * Prints an actual value of a score value. The method takes care about
   * unknown and not-applicable score values.
   *
   * @param scoreValue The score value.
   * @return A string that represents the score value.
   */
  private static String actualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return "N/A";
    }

    if (scoreValue.isUnknown()) {
      return "unknown";
    }

    return format(scoreValue.get());
  }

  /**
   * Figures out how many stars a project has.
   *
   * @param project The project.
   * @return A number of stars or {@link #UNKNOWN_NUMBER_OF_STARS}
   *         if the number of stars is unknown.
   */
  private static int starsOf(GitHubProject project) {
    if (!project.ratingValue().isPresent()) {
      return UNKNOWN_NUMBER_OF_STARS;
    }
    return stars(project.ratingValue().get().scoreValue());
  }

  /**
   * Looks for a value of the
   * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#NUMBER_OF_GITHUB_STARS}
   * feature in a score value.
   *
   * @param scoreValue The score value.
   * @return The features value or {@link #UNKNOWN_NUMBER_OF_STARS}
   *         if the value is unknown.
   */
  private static int stars(ScoreValue scoreValue) {
    if (scoreValue.isUnknown()) {
      return UNKNOWN_NUMBER_OF_STARS;
    }

    for (Value value : scoreValue.usedValues()) {
      if (value.isUnknown()) {
        continue;
      }

      if (value.feature().equals(NUMBER_OF_GITHUB_STARS)) {
        return (int) value.get();
      }

      if (value instanceof ScoreValue) {
        int n = stars((ScoreValue) value);
        if (n >= 0) {
          return n;
        }
      }
    }

    return UNKNOWN_NUMBER_OF_STARS;
  }

  /**
   * This class holds statistics about projects.
   */
  private static class Statistics {

    /**
     * Total number of projects.
     */
    int total;

    /**
     * A number of projects with an unknown rating.
     */
    int unknownRatings = 0;

    /**
     * A number of projects with a good rating.
     */
    int goodRatings = 0;

    /**
     * A number of projects with a moderate rating.
     */
    int moderateRatings = 0;

    /**
     * A number of projects with a bad rating.
     */
    int badRatings = 0;

    /**
     * A number of projects with unclear ratings.
     */
    int unclearRatings = 0;

    /**
     * Adds a project to the statistics.
     *
     * @param project The project.
     */
    void add(GitHubProject project) {
      total++;

      if (!project.ratingValue().isPresent()) {
        unknownRatings++;
        return;
      }

      RatingValue ratingValue = project.ratingValue().get();

      if (ratingValue.label() instanceof SecurityLabel == false) {
        unknownRatings++;
        return;
      }

      SecurityLabel label = (SecurityLabel) project.ratingValue().get().label();
      switch (label) {
        case BAD:
          badRatings++;
          break;
        case MODERATE:
          moderateRatings++;
          break;
        case GOOD:
          goodRatings++;
          break;
        case UNCLEAR:
          unknownRatings++;
          break;
        default:
          throw new IllegalArgumentException(
              String.format("Hey! I don't know this label: %s", label));
      }
    }

    /**
     * Returns a percent of projects with bad rating.
     *
     * @return A percent of projects with bad rating.
     */
    double badRatingsPercent() {
      return (double) badRatings * 100 / total;
    }

    /**
     * Returns a percent of projects with moderate rating.
     *
     * @return A percent of projects with moderate rating.
     */
    double moderateRatingsPercent() {
      return (double) moderateRatings * 100 / total;
    }

    /**
     * Returns a percent of projects with good rating.
     *
     * @return A percent of projects with good rating.
     */
    double goodRatingsPercent() {
      return (double) goodRatings * 100 / total;
    }

    /**
     * Returns a percent of projects with unknown rating.
     *
     * @return A percent of projects with unknown rating.
     */
    double unknownRatingsPercent() {
      return (double) unknownRatings * 100 / total;
    }

    /**
     * Returns a percent of projects with unclear rating.
     *
     * @return A percent of projects with unclear rating.
     */
    double unclearRatingsPercent() {
      return (double) unclearRatings * 100 / total;
    }
  }

}
