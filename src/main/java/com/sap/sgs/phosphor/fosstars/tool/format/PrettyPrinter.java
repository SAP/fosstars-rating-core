package com.sap.sgs.phosphor.fosstars.tool.format;

import static com.sap.sgs.phosphor.fosstars.tool.format.CommonFormatter.confidenceLabelFor;
import static com.sap.sgs.phosphor.fosstars.tool.format.CommonFormatter.importanceLabel;
import static com.sap.sgs.phosphor.fosstars.tool.format.CommonFormatter.nameOf;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
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
import org.apache.commons.text.WordUtils;

/**
 * The class prints a pretty rating value.
 */
public class PrettyPrinter implements Formatter {

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
    return new PrettyPrinter(false);
  }

  /**
   * Creates a new {@link PrettyPrinter} that prints out all the details.
   *
   * @return A new {@link PrettyPrinter}.
   */
  public static PrettyPrinter withVerboseOutput() {
    return new PrettyPrinter(true);
  }

  /**
   * A flag that turns on verbose output.
   */
  private final boolean verbose;

  /**
   * Creates a new {@link PrettyPrinter}.
   *
   * @param verbose A flag that turns on verbose output.
   */
  private PrettyPrinter(boolean verbose) {
    this.verbose = verbose;
  }

  @Override
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
      String[] lines = WordUtils
          .wrap(scoreValue.score().description(), DESCRIPTION_WRAP_LENGTH)
          .split("\n");
      sb.append(String.format("%sDescription:..%s%n", indent, lines[0]));
      for (int i = 1; i < lines.length; i++) {
        sb.append(String.format("%s              %s%n", indent, lines[i]));
      }
    }

    if (!isMainScore) {
      sb.append(String.format("%sImportance:...%s (weight %s)%n",
          indent,
          importanceLabel(scoreValue.weight()),
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
    List<Value> featureValues = new ArrayList<>();
    for (Value usedValue : scoreValue.usedValues()) {
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
      for (Value usedValue : featureValues) {
        String name = nameOf(usedValue.feature());
        nameToValue.put(name, CommonFormatter.actualValueOf(usedValue));

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
   * Checks if the pretty printer should print out a description of a score value.
   *
   * @param scoreValue The score value to be checked.
   * @return True if the pretty printer should print a description, false otherwise.
   */
  private boolean shouldPrintDescriptionFor(ScoreValue scoreValue) {
    return verbose && !scoreValue.feature().description().isEmpty();
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
  private boolean shouldPrint(List<Value> featureValues) {
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
