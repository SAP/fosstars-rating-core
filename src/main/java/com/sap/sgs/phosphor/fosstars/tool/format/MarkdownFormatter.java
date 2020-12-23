package com.sap.sgs.phosphor.fosstars.tool.format;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Advisor;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.advice.Link;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The class prints a rating value in Markdown.
 */
public class MarkdownFormatter extends CommonFormatter {

  /**
   * A resource with a Markdown template.
   */
  private static final String RESOURCE = "MarkdownRatingValueTemplate.md";

  /**
   * A Markdown template for a rating value.
   */
  private static final String TEMPLATE = loadFrom(RESOURCE);

  /**
   * An indent for building nested lists.
   */
  private static final String LIST_INDENT_STEP = "    ";

  /**
   * Print out an empty string if no advice is available for a rating.
   */
  private static final String NO_ADVICES = StringUtils.EMPTY;

  /**
   * A formatter for doubles.
   */
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
  }

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   */
  public MarkdownFormatter(Advisor advisor) {
    super(advisor);
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return print(subject.ratingValue().get(), advicesFor(subject));
  }

  @Override
  public String print(RatingValue ratingValue) {
    return print(ratingValue, NO_ADVICES);
  }

  /**
   * Print a rating value and advices.
   *
   * @param ratingValue The rating value.
   * @param advices The advices.
   * @return A string to be displayed.
   */
  private String print(RatingValue ratingValue, String advices) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return TEMPLATE
        .replaceAll("%MAX_SCORE%", formatted(Score.MAX))
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%SCORE_VALUE%", formatted(scoreValue.get()))
        .replace("%RATING_LABEL%", ratingValue.label().name())
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%MAIN_SCORE_NAME%", scoreValue.score().name().toLowerCase())
        .replace("%MAIN_SCORE_VALUE_DETAILS%", highLevelDescriptionOf(scoreValue))
        .replace("%MAIN_SCORE_DESCRIPTION%", scoreValue.score().description())
        .replace("%MAIN_SCORE_EXPLANATION%", explanationOf(scoreValue))
        .replace("%SUB_SCORE_DETAILS%", descriptionOfSubScoresIn(scoreValue))
        .replace("%ADVICES%", advices);
  }

  /**
   * Print out advices for a subject.
   *
   * @param subject The subject.
   * @return Advices to be displayed.
   */
  private String advicesFor(Subject subject) {
    List<Advice> advices = advisor.adviseFor(subject);
    if (advices.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("## How to improve the rating\n\n");
    int i = 1;
    for (Advice advice : advices) {
      sb.append(String.format("%d.  %s", i++, advice.content().text()));
      if (!advice.content().links().isEmpty()) {
        sb.append(" More info:").append("\n");
        int j = 1;
        for (Link link : advice.content().links()) {
          sb.append(String.format("    %d.  [%s](%s)%n", j++, link.name, link.url));
        }
      }
    }

    return sb.toString();
  }

  /**
   * Builds a string with explanations for a score value if they are available.
   *
   * @param scoreValue The score value.
   * @return A formatted string.
   */
  private static String explanationOf(ScoreValue scoreValue) {
    return String.join("\n", scoreValue.explanation());
  }

  /**
   * Looks for sub-score values that were used in a specified score value.
   *
   * @param scoreValue The score value.
   * @return A list of sub-score values.
   */
  private static List<ScoreValue> usedSubScoreValuesIn(ScoreValue scoreValue) {
    return scoreValue.usedValues().stream()
        .filter(value -> value instanceof ScoreValue)
        .map(ScoreValue.class::cast)
        .sorted(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)))
        .collect(Collectors.toList());
  }

  /**
   * Builds a list of short descriptions of sub-score values for a specified score value.
   *
   * @param scoreValue The score value.
   * @return A formatted text.
   */
  private static String highLevelDescriptionOf(ScoreValue scoreValue) {
    return highLevelDescriptionOf(scoreValue, "");
  }

  /**
   * Builds a list of short descriptions of sub-score values for a specified score value.
   *
   * @param scoreValue The score value.
   * @param indent An indent for the list.
   * @return A formatted list.
   */
  private static String highLevelDescriptionOf(ScoreValue scoreValue, String indent) {
    StringBuilder sb = new StringBuilder();

    usedSubScoreValuesIn(scoreValue).forEach(subScoreValue -> {
      sb.append(String.format("%s1.  %s%n", indent, shortDescriptionOf(subScoreValue)));
      sb.append(highLevelDescriptionOf(subScoreValue, indent + LIST_INDENT_STEP));
    });

    return sb.toString();
  }

  /**
   * Builds a short description of a score value.
   *
   * @param scoreValue The score value.
   * @return A formatted text.
   */
  private static String shortDescriptionOf(ScoreValue scoreValue) {
    return String.format("**%s**: %s, confidence is %s (%s), importance is %s (%s)",
        anchorFor(nameOf(scoreValue.score())),
        actualValueOf(scoreValue),
        formatted(scoreValue.confidence()),
        confidenceLabelFor(scoreValue.confidence()).toLowerCase(),
        formatted(scoreValue.weight()),
        importanceLabel(scoreValue.weight()).toLowerCase()
    );
  }

  /**
   * Builds a Markdown anchor for a section.
   *
   * @param section The section's name.
   * @return The anchor.
   */
  private static String anchorFor(String section) {
    return String.format("[%s](#%s)", section, section.toLowerCase().replaceAll("\\s+", "-"));
  }

  /**
   * Formats a double.
   *
   * @param n The double to be formatted.
   * @return A formatted string.
   */
  private static String formatted(double n) {
    return DECIMAL_FORMAT.format(n);
  }

  /**
   * Build details about sub-scores in a specified score value.
   * Each sub-score is printed out in a separate section.
   * The method implements BFS, therefore it prints out the direct sub-scores first.
   *
   * @param scoreValue The score value to be printed out.
   * @return A formatted text.
   */
  private static String descriptionOfSubScoresIn(ScoreValue scoreValue) {
    StringBuilder sb = new StringBuilder();

    Set<Score> processedScores = new HashSet<>();
    Queue<ScoreValue> queue = new LinkedList<>(usedSubScoreValuesIn(scoreValue));
    while (!queue.isEmpty()) {
      ScoreValue subScoreValue = queue.poll();
      if (processedScores.contains(subScoreValue.score())) {
        continue;
      }
      processedScores.add(subScoreValue.score());

      queue.addAll(usedSubScoreValuesIn(subScoreValue));

      sb.append(detailsOf(subScoreValue));
    }

    return sb.toString();
  }

  /**
   * Build details of a score value in a separate section.
   * The method prints out sub-scores and feature that were are used in the score value.
   *
   * @param scoreValue The score value to be printed.
   * @return A formatted score value.
   */
  private static String detailsOf(ScoreValue scoreValue) {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("### %s%n%n", nameOf(scoreValue.score())));

    sb.append(String.format("Score: %s, confidence is %s (%s), importance is %s (%s)%n%n",
        actualValueOf(scoreValue),
        formatted(scoreValue.confidence()),
        confidenceLabelFor(scoreValue.confidence()).toLowerCase(),
        formatted(scoreValue.weight()),
        importanceLabel(scoreValue.weight()).toLowerCase()));

    sb.append(scoreValue.score().description());
    sb.append("\n\n");

    sb.append(explanationOf(scoreValue));
    sb.append("\n\n");

    List<ScoreValue> subScoreValues = new ArrayList<>();
    List<Value> featureValues = new ArrayList<>();
    for (Value usedValue : scoreValue.usedValues()) {
      if (usedValue instanceof ScoreValue) {
        subScoreValues.add((ScoreValue) usedValue);
      } else {
        featureValues.add(usedValue);
      }
    }

    if (!subScoreValues.isEmpty()) {
      subScoreValues.sort(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)));

      sb.append(String.format("The sub-score uses the following sub-score%s:%n%n",
          subScoreValues.size() == 1 ? "" : "s"));
      sb.append(highLevelDescriptionOf(scoreValue));
      sb.append("\n");
    }

    if (!featureValues.isEmpty()) {
      sb.append(String.format("The sub-score uses %d feature%s:%n%n",
          featureValues.size(), featureValues.size() == 1 ? "" : "s"));

      Map<String, Object> nameToValue = new TreeMap<>(String::compareTo);
      for (Value usedValue : featureValues) {
        String name = nameOf(usedValue.feature());

        if (!name.endsWith("?")) {
          name += ":";
        }

        nameToValue.put(name, CommonFormatter.actualValueOf(usedValue));
      }

      for (Map.Entry<String, Object> entry : nameToValue.entrySet()) {
        sb.append(String.format("1.  %s %s%n", entry.getKey(), entry.getValue()));
      }
    }

    sb.append("\n");

    return sb.toString();
  }

  /**
   * Build a string that shows an actual value of a score value. The method takes care about
   * unknown and not-applicable score values.
   *
   * @param scoreValue The score value.
   * @return A string that represents the score value.
   */
  public static String actualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return "N/A";
    }

    if (scoreValue.isUnknown()) {
      return "unknown";
    }

    return formatted(scoreValue.get());
  }

  /**
   * Loads a resource.
   *
   * @param resource A name of the resource.
   * @return The content of the resource.
   */
  static String loadFrom(String resource) {
    try (InputStream is = CommonFormatter.class.getResourceAsStream(resource)) {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Holy moly! Could not load template!", e);
    }
  }
}
