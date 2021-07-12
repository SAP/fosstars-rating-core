package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.Link;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * A base class for Markdown formatters.
 */
public abstract class AbstractMarkdownFormatter extends CommonFormatter {

  /**
   * An indent for building nested lists.
   */
  private static final String LIST_INDENT_STEP = "    ";

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   */
  protected AbstractMarkdownFormatter(Advisor advisor) {
    super(advisor);
  }

  /**
   * Print out a rating value with advice.
   *
   * @param ratingValue The rating value.
   * @param advice The advice.
   * @return Formatted rating value with advice.
   */
  protected abstract String print(RatingValue ratingValue, String advice);

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return print(subject.ratingValue().get(), markdownAdviceFor(subject));
  }

  /**
   * Returns a header for the advice section.
   *
   * @return A header for the advice section.
   */
  protected String markdownAdviceHeader() {
    return "## How to improve the rating";
  }

  /**
   * Print out advice for a subject.
   *
   * @param subject The subject.
   * @return Advice to be displayed.
   */
  protected String markdownAdviceFor(Subject subject) {
    List<Advice> adviceList;
    try {
      adviceList = advisor.adviceFor(subject);
    } catch (IOException e) {
      throw new UncheckedIOException("Oops! Could not print advice!", e);
    }

    if (adviceList.isEmpty()) {
      return StringUtils.EMPTY;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(markdownAdviceHeader()).append("\n\n");
    int i = 1;
    for (Advice advice : adviceList) {
      sb.append(format("%d.  %s%n", i++, advice.content().text()));
      if (!advice.content().links().isEmpty()) {
        sb.append("    More info:").append("\n");
        int j = 1;
        for (Link link : advice.content().links()) {
          sb.append(String.format("    %d.  [%s](%s)%n", j++, link.name, link.url));
        }
      }
    }

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

  @Override
  protected String actualValueOf(Value<?> value) {
    if (!value.isUnknown() && value.get() instanceof Vulnerabilities) {
      Vulnerabilities vulnerabilities = (Vulnerabilities) value.get();
      if (vulnerabilities.isEmpty()) {
        return "Not found";
      } else {
        return format("%s, [details below](#known-vulnerabilities)", vulnerabilities);
      }
    }

    return super.actualValueOf(value);
  }

  /**
   * Prints info about vulnerabilities that was used for calculating a score value.
   *
   * @param scoreValue The score value.
   * @return Formatted info about vulnerabilities.
   */
  String infoAboutVulnerabilitiesIn(ScoreValue scoreValue) {
    Set<Vulnerability> uniqueVulnerabilities = new TreeSet<>((first, second) -> {
      if (first.id().equals(second.id())) {
        return 0;
      }

      if (first.published().isPresent() && second.published().isPresent()) {
        return first.published().get().compareTo(second.published().get());
      }

      return first.id().compareTo(second.id());
    });

    for (Value<?> value : scoreValue.usedFeatureValues()) {
      if (value.isUnknown()) {
        continue;
      }

      if (value.get() instanceof Vulnerabilities) {
        Vulnerabilities vulnerabilities = (Vulnerabilities) value.get();
        for (Vulnerability vulnerability : vulnerabilities) {
          uniqueVulnerabilities.add(vulnerability);
        }
      }
    }

    if (uniqueVulnerabilities.isEmpty()) {
      return "No vulnerabilities found";
    }

    StringBuilder info = new StringBuilder();
    for (Vulnerability vulnerability : uniqueVulnerabilities) {
      if (vulnerability.description().isPresent()) {
        info.append(format("1.  %s: %s%n", linkFor(vulnerability), vulnerability.description()));
      } else {
        info.append(format("1.  %s%n", linkFor(vulnerability)));
      }
    }
    return info.toString();
  }

  /**
   * Prints a link for a vulnerability if possible.
   *
   * @param vulnerability The vulnerability.
   * @return A link for the vulnerability if available, or its identifier otherwise.
   */
  private static String linkFor(Vulnerability vulnerability) {
    if (vulnerability.id().startsWith("CVE-")) {
      return format("[%s](https://nvd.nist.gov/vuln/detail/%s)",
          vulnerability.id(), vulnerability.id());
    }

    return vulnerability.id();
  }

  /**
   * Builds a string with explanations for a score value if they are available.
   *
   * @param scoreValue The score value.
   * @return A formatted string.
   */
  static String explanationOf(ScoreValue scoreValue) {
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
  String highLevelDescriptionOf(ScoreValue scoreValue) {
    return highLevelDescriptionOf(scoreValue, StringUtils.EMPTY);
  }

  /**
   * Builds a list of short descriptions of sub-score values for a specified score value.
   *
   * @param scoreValue The score value.
   * @param indent An indent for the list.
   * @return A formatted list.
   */
  private String highLevelDescriptionOf(ScoreValue scoreValue, String indent) {
    StringBuilder sb = new StringBuilder();

    usedSubScoreValuesIn(scoreValue).forEach(subScoreValue -> {
      sb.append(format("%s1.  %s%n", indent, shortDescriptionOf(subScoreValue)));
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
  private String shortDescriptionOf(ScoreValue scoreValue) {
    return format("**%s**: **%s** (weight is %s)",
        anchorFor(nameOf(scoreValue.score())),
        actualValueOf(scoreValue),
        formatted(scoreValue.weight())
    );
  }

  /**
   * Builds a Markdown anchor for a section.
   *
   * @param section The section's name.
   * @return The anchor.
   */
  private static String anchorFor(String section) {
    return format("[%s](#%s)", section, section.toLowerCase().replaceAll("\\s+", "-"));
  }

  /**
   * Build details about sub-scores in a specified score value.
   * Each sub-score is printed out in a separate section.
   * The method implements BFS, therefore it prints out the direct sub-scores first.
   *
   * @param scoreValue The score value to be printed out.
   * @return A formatted text.
   */
  String descriptionOfSubScoresIn(ScoreValue scoreValue) {
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
  String detailsOf(ScoreValue scoreValue) {
    StringBuilder sb = new StringBuilder();

    sb.append(format("### %s%n%n", nameOf(scoreValue.score())));

    sb.append(format("Score: **%s**, confidence is %s (%s), weight is %s (%s)%n%n",
        actualValueOf(scoreValue),
        formatted(scoreValue.confidence()),
        confidenceLabelFor(scoreValue.confidence()).toLowerCase(),
        formatted(scoreValue.weight()),
        weightLabel(scoreValue.weight()).toLowerCase()));

    sb.append(scoreValue.score().description());
    sb.append("\n\n");

    sb.append(explanationOf(scoreValue));
    sb.append("\n\n");

    List<ScoreValue> subScoreValues = new ArrayList<>();
    List<Value<?>> featureValues = new ArrayList<>();
    for (Value<?> usedValue : scoreValue.usedValues()) {
      if (usedValue instanceof ScoreValue) {
        subScoreValues.add((ScoreValue) usedValue);
      } else {
        featureValues.add(usedValue);
      }
    }

    if (!subScoreValues.isEmpty()) {
      subScoreValues.sort(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)));

      sb.append(format("This sub-score is based on the following sub-score%s:%n%n",
          subScoreValues.size() == 1 ? StringUtils.EMPTY : "s"));
      sb.append(highLevelDescriptionOf(scoreValue));
      sb.append("\n");
    }

    if (!featureValues.isEmpty()) {
      sb.append(format("This sub-score is based on %d feature%s:%n%n",
          featureValues.size(), featureValues.size() == 1 ? StringUtils.EMPTY : "s"));

      Map<String, String> nameToValue = new TreeMap<>(String::compareTo);
      for (Value<?> usedValue : featureValues) {
        String name = nameOf(usedValue.feature());

        if (!name.endsWith("?")) {
          name += ":";
        }

        nameToValue.put(name, actualValueOf(usedValue));
      }

      for (Map.Entry<String, String> entry : nameToValue.entrySet()) {
        sb.append(format("1.  %s **%s**%n", entry.getKey(), entry.getValue()));
      }
    }

    sb.append("\n");

    return sb.toString();
  }

}
