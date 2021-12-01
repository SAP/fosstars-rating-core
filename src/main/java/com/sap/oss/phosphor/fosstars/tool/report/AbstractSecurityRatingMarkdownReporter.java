package com.sap.oss.phosphor.fosstars.tool.report;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

/**
 * This reporter takes a number of projects and generates a markdown report.
 */
abstract class AbstractSecurityRatingMarkdownReporter<T extends Subject>
    extends AbstractReporter<T> {

  /**
   * No extra projects.
   */
  static final String NO_EXTRA_SOURCE = null;

  /**
   * This value shows that a number of stars is unknown.
   */
  private static final int UNKNOWN_NUMBER_OF_STARS = -1;

  /**
   * A file where a report is going to be stored.
   */
  static final String REPORT_FILENAME = "README.md";

  /**
   * A template for a table row in the report.
   */
  static final String PROJECT_LINE_TEMPLATE
      = "| %NAME% | %STARS% | %SCORE% | %LABEL% | %CONFIDENCE% | %DATE% |";

  /**
   * A template for links in Markdown.
   */
  static final String LINK_TEMPLATE = "[%s](%s)";

  /**
   * A length of line in a project name.
   */
  private static final int NAME_LINE_LENGTH = 42;

  /**
   * A date formatter.
   */
  static final SimpleDateFormat DATE_FORMAT
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
  final Path outputDirectory;

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
  public AbstractSecurityRatingMarkdownReporter(
      String outputDirectory, Rating rating, Advisor advisor)
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
  }

  /**
   * Prints out a name of a project.
   *
   * @param content The subject content.
   * @return A name of the project.
   */
  static String nameOf(String content) {
    return insert("<br>", NAME_LINE_LENGTH,
        content);
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
   * Formats a percent value.
   *
   * @param value The value.
   * @return A formatted string.
   */
  static String printPercent(double value) {
    return String.format("%2.1f", value);
  }

  /**
   * Format a double.
   *
   * @param value The number.
   * @return A formatter number.
   */
  static String format(double value) {
    return DECIMAL_FORMAT.format(value);
  }

  /**
   * Formats a date when a rating was calculated for a mavenArtifact.
   */
  static <T extends Subject> String lastUpdateOf(T subject) {
    return subject.ratingValueDate().map(DATE_FORMAT::format).orElse(UNKNOWN);
  }

  /**
   * Formats a confidence of a rating of a mavenArtifact.
   */
  static <T extends Subject> String confidenceOf(T subject) {
    Optional<RatingValue> something = subject.ratingValue();
    if (!something.isPresent()) {
      return UNKNOWN;
    }

    return String.format("%2.2f", something.get().confidence());
  }

  /**
   * Formats a label of a rating of a mavenArtifact.
   */
  static <T extends Subject> String labelOf(T subject) {
    Optional<RatingValue> something = subject.ratingValue();
    if (!something.isPresent()) {
      return UNKNOWN;
    }

    return something.get().label().name();
  }

  /**
   * Formats a score of a mavenArtifact.
   */
  static <T extends Subject> String scoreOf(T subject) {
    Optional<RatingValue> something = subject.ratingValue();
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
  static String actualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return "N/A";
    }

    if (scoreValue.isUnknown()) {
      return "unknown";
    }

    return format(scoreValue.get());
  }

  /**
   * Figures out how many stars a mavenArtifact has.
   *
   * @param subject The mavenArtifact.
   * @return A number of stars or {@link #UNKNOWN_NUMBER_OF_STARS}
   *         if the number of stars is unknown.
   */
  static <T extends Subject> int starsOf(T subject) {
    if (!subject.ratingValue().isPresent()) {
      return UNKNOWN_NUMBER_OF_STARS;
    }
    return stars(subject.ratingValue().get().scoreValue());
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
   * Prepares a description how a rating was calculated for a mavenArtifact.
   *
   * @param subject The Subject.
   * @return The details of the rating calculation.
   */
  abstract String detailsOf(T subject);

  /**
   * Builds a report for projects.
   *
   * @param table A content of the table with projects.
   * @param statistics Statistics about the projects.
   * @return The report.
   * @throws IOException If something went wrong.
   */
  String buildReportWith(String table, Statistics<T> statistics) throws IOException {
    try (InputStream is = OssArtifactSecurityRatingMarkdownReporter.class
        .getResourceAsStream("OssSecurityRatingMarkdownReporterMainTemplate.md")) {

      String template = IOUtils.toString(is, StandardCharsets.UTF_8);
      return template
          .replace("%PROJECT_TABLE%", table)
          .replace("%NUMBER_OF_PROJECTS%", String.valueOf(statistics.total()))
          .replace("%NUMBER_BAD_RATINGS%", String.valueOf(statistics.badRatings()))
          .replace("%NUMBER_MODERATE_RATINGS%", String.valueOf(statistics.moderateRatings()))
          .replace("%NUMBER_GOOD_RATINGS%", String.valueOf(statistics.goodRatings()))
          .replace("%NUMBER_UNKNOWN_RATINGS%", String.valueOf(statistics.unknownRatings()))
          .replace("%NUMBER_UNCLEAR_RATINGS%", String.valueOf(statistics.unclearRatings()))
          .replace("%PERCENT_BAD_RATINGS%",
              printPercent(statistics.badRatingsPercent()))
          .replace("%PERCENT_MODERATE_RATINGS%",
              printPercent(statistics.moderateRatingsPercent()))
          .replace("%PERCENT_GOOD_RATINGS%",
              printPercent(statistics.goodRatingsPercent()))
          .replace("%PERCENT_UNCLEAR_RATINGS%",
              printPercent(statistics.unclearRatingsPercent()))
          .replace("%PERCENT_UNKNOWN_RATINGS%",
              printPercent(statistics.unknownRatingsPercent()));
    }
  }
}
