package com.sap.sgs.phosphor.fosstars.tool;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.List;

/**
 * The class print a pretty rating value.
 */
public class PrettyPrinter {

  private static final String INDENT_STEP = "  ";

  /**
   * Print a formatted rating value.
   *
   * @param ratingValue The rating value to be printed.
   */
  public String print(RatingValue ratingValue) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("[+] Rating: %2.2f out of %2.2f -> %s%n",
        ratingValue.score(), Score.MAX, ratingValue.label()));
    sb.append(String.format("[+] Confidence: %2.2f out of %2.2f%n",
        ratingValue.confidence(), Confidence.MAX));
    sb.append(String.format("[+] Here is how the rating was calculated:%n"));
    sb.append(print(ratingValue.scoreValue(), INDENT_STEP));
    return sb.toString();
  }

  private String print(ScoreValue scoreValue, String indent) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("[+] %sScore:........%s%n", indent, scoreValue.score().name()));

    sb.append(String.format("[+] %sValue:........%s out of %2.2f%n",
        indent,
        append(String.format("%.2f", scoreValue.get()), ' ', 4),
        Score.MAX));
    sb.append(String.format("[+] %sConfidence:...%s out of %2.2f%n",
        indent,
        append(String.format("%.2f", scoreValue.confidence()), ' ', 4),
        Confidence.MAX));

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
      sb.append(String.format(
          "[+] %sBased on:.....%d sub-scores:%n", indent, subScoreValues.size()));
      for (ScoreValue usedValue : subScoreValues) {
        sb.append(print(usedValue, indent + INDENT_STEP + INDENT_STEP));
        sb.append("[+]\n");
      }
    }

    if (!featureValues.isEmpty()) {
      sb.append(String.format("[+] %s  Based on:...%d features:%n", indent, featureValues.size()));
      int maxLength = maxFeatureNameLength(featureValues) + 3;
      for (Value usedValue : featureValues) {
        sb.append(String.format("[+] %s  %s%s%n",
            indent + INDENT_STEP, append(usedValue.feature().name() + ":", '.', maxLength),
            usedValue.get()));
      }
    }

    return sb.toString();
  }

  private static int maxFeatureNameLength(List<Value> featureValues) {
    int maxLength = 0;
    for (Value usedValue : featureValues) {
      int length = usedValue.feature().name().length();
      if (length > maxLength) {
        maxLength = length;
      }
    }
    return maxLength;
  }

  private static String append(String string, char c, int length) {
    StringBuilder sb = new StringBuilder(string);
    while (sb.length() <= length) {
      sb.append(c);
    }
    return sb.toString();
  }

}
