package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.Link;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.Weight;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/**
 * The class prints a pretty rating value.
 */
public class PrettyPrinter extends CommonFormatter {

  /**
   * An indent step.
   */
  private static final String INDENT_STEP = "  ";

  /**
   * A length of lines for wrapping descriptions.
   */
  private static final int DESCRIPTION_WRAP_LENGTH = 50;

  /**
   * A formatter for doubles.
   */
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
  }

  /**
   * Creates a new {@link PrettyPrinter} that doesn't print all the details.
   *
   * @return A new {@link PrettyPrinter}.
   */
  public static PrettyPrinter withoutVerboseOutput() {
    return new PrettyPrinter(false, Advisor.DUMMY);
  }

  /**
   * Creates a new {@link PrettyPrinter} that prints out all the details.
   *
   * @param advisor An advisor for calculated ratings.
   * @return A new {@link PrettyPrinter}.
   */
  public static PrettyPrinter withVerboseOutput(Advisor advisor) {
    return new PrettyPrinter(true, advisor);
  }

  /**
   * A flag that turns on verbose output.
   */
  private final boolean verbose;

  /**
   * Creates a new {@link PrettyPrinter}.
   *
   * @param verbose A flag that turns on verbose output.
   * @param advisor An advisor.
   */
  private PrettyPrinter(boolean verbose, Advisor advisor) {
    super(advisor);
    this.verbose = verbose;
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return String.format("%s%n%s", print(subject.ratingValue().get()), printAdviceFor(subject));
  }

  /**
   * Format a rating value.
   *
   * @param ratingValue THe rating value.
   * @return A formatted rating value.
   */
  public String print(RatingValue ratingValue) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Here is how the rating was calculated:%n"));
    sb.append(print(ratingValue.scoreValue(), INDENT_STEP, true, new HashSet<>()));
    sb.append("\n");
    sb.append(String.format("Rating:     %s -> %s%n",
        tellMeActualValueOf(ratingValue.scoreValue()), ratingValue.label()));
    sb.append(String.format("Confidence: %s (%s)%n",
        confidenceLabelFor(ratingValue.confidence()),
        printValueAndMax(ratingValue.confidence(), Confidence.MAX)));
    return sb.toString();
  }

  /**
   * Print out a formatted score value with a specified indent.
   *
   * @param scoreValue The score value to be printed.
   * @param indent The indent.
   * @param isMainScore Tells if the score is a top-level score in the rating.
   * @param printedScores A set of scores that have been already printed out.
   * @return A string to be displayed.
   */
  private String print(
      ScoreValue scoreValue, String indent, boolean isMainScore, Set<Score> printedScores) {

    StringBuilder sb = new StringBuilder();

    if (!isMainScore) {
      sb.append(String.format("%sSub-score:....%s%n", indent, nameOf(scoreValue.score())));
    } else {
      sb.append(String.format("%sScore:........%s%n", indent, nameOf(scoreValue.score())));
    }

    if (shouldPrintDescriptionFor(scoreValue)) {
      String[] lines = wrap(scoreValue.score().description());
      sb.append(String.format("%sDescription:..%s%n", indent, lines[0]));
      for (int i = 1; i < lines.length; i++) {
        sb.append(String.format("%s              %s%n", indent, lines[i]));
      }
    }

    if (!isMainScore) {
      sb.append(String.format("%sImportance:...%s (weight %s)%n",
          indent,
          weightLabel(scoreValue.weight()),
          printValueAndMax(scoreValue.weight(), Weight.MAX)));
    }

    sb.append(String.format("%sValue:........%s%n",
        indent,
        append(String.format("%s", tellMeActualValueOf(scoreValue)), ' ', 4)));
    sb.append(String.format("%sConfidence:...%s (%s)%n",
        indent,
        confidenceLabelFor(scoreValue.confidence()),
        printValueAndMax(scoreValue.confidence(), Confidence.MAX)));

    if (printedScores.contains(scoreValue.score())) {
      return sb.toString();
    }

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
      // sort the sub-score values by their importance
      subScoreValues.sort(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)));

      sb.append(String.format(
          "%sBased on:.....%d sub-scores%n", indent, subScoreValues.size()));

      sb.append(subScoreValues.stream()
          .map(value -> print(value, indent + INDENT_STEP + INDENT_STEP, false, printedScores))
          .collect(Collectors.joining("\n")));
    }

    if (!featureValues.isEmpty()) {
      sb.append(String.format("%sBased on:...%d features%n", indent, featureValues.size()));
    }

    if (shouldPrint(featureValues)) {
      Map<String, Object> nameToValue = new TreeMap<>(String::compareTo);
      int maxLength = 0;
      for (Value<?> usedValue : featureValues) {
        String name = nameOf(usedValue.feature());
        nameToValue.put(name, actualValueOf(usedValue));

        if (maxLength < name.length()) {
          maxLength = name.length();
        }
      }

      for (Map.Entry<String, Object> entry : nameToValue.entrySet()) {
        String name = entry.getKey();
        name += name.endsWith("?") ? "." : ":";
        sb.append(String.format("%s  %s%s%n",
            indent + INDENT_STEP,
            append(name, '.', maxLength + 3),
            entry.getValue()));
      }
    }

    if (shouldPrintExplanationFor(scoreValue)) {
      Iterator<String> iterator = scoreValue.explanation().iterator();
      sb.append(String.format("%sExplanation:..%s%n", indent, iterator.next()));
      while (iterator.hasNext()) {
        sb.append(String.format("%s              %s%n", indent, iterator.next()));
      }
    }

    printedScores.add(scoreValue.score());

    return sb.toString();
  }

  /**
   * Checks if the tool should print out advice for a calculated rating.
   *
   * @return True if the tool should print out advice for a calculated rating, false otherwise.
   */
  private boolean shouldPrintAdvice() {
    return verbose;
  }

  /**
   * Print out advice for a rating of a project.
   *
   * @param subject The project.
   * @return A string to be displayed.
   */
  private String printAdviceFor(Subject subject) {
    if (!shouldPrintAdvice() || !subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

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

    sb.append("Here is how the rating may be improved:\n");
    int i = 1;
    for (Advice advice : adviceList) {
      String[] text = wrap(advice.content().text());
      sb.append(String.format("%d. %s%n", i++, text[0]));
      for (int k = 1; k < text.length; k++) {
        sb.append(String.format("   %s%n", text[k]));
      }
      if (!advice.content().links().isEmpty()) {
        sb.append("   More info:\n");
        int j = 1;
        for (Link link : advice.content().links()) {
          sb.append(String.format("   %d. %s:%n", j++, link.name));
          sb.append(String.format("      %s%n", link.url));
        }
      }
    }

    return sb.append("\n").toString();
  }

  /**
   * Split a string to multiple lines.
   *
   * @param string The string.
   * @return An array of lines.
   */
  public String[] wrap(String string) {
    return WordUtils.wrap(string, DESCRIPTION_WRAP_LENGTH).split("\n");
  }

  /**
   * Checks if the pretty printer should print out a description of a score value.
   *
   * @param scoreValue The score value to be checked.
   * @return True if the pretty printer should print a description, false otherwise.
   */
  private boolean shouldPrintDescriptionFor(ScoreValue scoreValue) {
    return verbose && !scoreValue.score().description().isEmpty();
  }

  /**
   * Checks if the pretty printer should print out an explanation for a score value.
   *
   * @param scoreValue The score to be checked.
   * @return True if the pretty printer should print an explanation, false otherwise.
   */
  private boolean shouldPrintExplanationFor(ScoreValue scoreValue) {
    return verbose && !scoreValue.explanation().isEmpty();
  }

  /**
   * Checks if the pretty printer should print out feature values.
   *
   * @param featureValues The feature values to be checked.
   * @return True if the pretty printer should print the feature values, false otherwise.
   */
  private boolean shouldPrint(List<Value<?>> featureValues) {
    return verbose && !featureValues.isEmpty();
  }

  /**
   * Prints an actual value of a score value. The method takes care about
   * unknown and not-applicable score values.
   *
   * @param scoreValue The score value.
   * @return A string that represents the score value.
   */
  public static String tellMeActualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return "N/A";
    }

    if (scoreValue.isUnknown()) {
      return "unknown";
    }

    return printValueAndMax(scoreValue.get(), Score.MAX);
  }

  /**
   * Prints out a number with its max value.
   *
   * @param value The number.
   * @param max The max value.
   * @return A formatted string with the number and max value.
   */
  public static String printValueAndMax(double value, double max) {
    return String.format("%-4s out of %4s",
        DECIMAL_FORMAT.format(value), DECIMAL_FORMAT.format(max));
  }

  /**
   * Adds a number of specified characters to the end of a string
   * to make it fit to the specified length.
   *
   * @param string The original string.
   * @param c The character to be appended.
   * @param length The final length of the string.
   * @return A string with appended characters
   *         if the length of the original string is less than the specified length,
   *         otherwise the original string.
   */
  private static String append(String string, char c, int length) {
    StringBuilder sb = new StringBuilder(string);
    while (sb.length() <= length) {
      sb.append(c);
    }
    return sb.toString();
  }

}
