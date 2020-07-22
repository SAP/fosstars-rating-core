package com.sap.sgs.phosphor.fosstars.tool.format;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.score.oss.CommunityCommitmentScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.DependencyScanScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.NoHttpToolScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectActivityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectPopularityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectSecurityAwarenessScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.StaticAnalysisScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.UnpatchedVulnerabilitiesScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.VulnerabilityDiscoveryAndSecurityTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.VulnerabilityLifetimeScore;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.text.WordUtils;

/**
 * The class print a pretty rating value.
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
   * Maps a class of feature to its shorter name which should be used in output.
   */
  private static final Map<Class, String> FEATURE_CLASS_TO_NAME = new HashMap<>();

  static {
    FEATURE_CLASS_TO_NAME.put(OssSecurityScore.class, "Security of project");
    FEATURE_CLASS_TO_NAME.put(CommunityCommitmentScore.class, "Community commitment");
    FEATURE_CLASS_TO_NAME.put(ProjectActivityScore.class, "Project activity");
    FEATURE_CLASS_TO_NAME.put(ProjectPopularityScore.class, "Project popularity");
    FEATURE_CLASS_TO_NAME.put(ProjectSecurityAwarenessScore.class, "Security awareness");
    FEATURE_CLASS_TO_NAME.put(ProjectSecurityTestingScore.class, "Security testing");
    FEATURE_CLASS_TO_NAME.put(UnpatchedVulnerabilitiesScore.class, "Unpatched vulnerabilities");
    FEATURE_CLASS_TO_NAME.put(VulnerabilityLifetimeScore.class, "Vulnerability lifetime");
    FEATURE_CLASS_TO_NAME.put(MemorySafetyTestingScore.class, "Memory-safety testing");
    FEATURE_CLASS_TO_NAME.put(DependencyScanScore.class, "Dependency testing");
    FEATURE_CLASS_TO_NAME.put(FuzzingScore.class, "Fuzzing");
    FEATURE_CLASS_TO_NAME.put(StaticAnalysisScore.class, "Static analysis");
    FEATURE_CLASS_TO_NAME.put(NoHttpToolScore.class, "nohttp tool");
    FEATURE_CLASS_TO_NAME.put(VulnerabilityDiscoveryAndSecurityTestingScore.class,
        "Vulnerability discovery and security testing");
  }

  /**
   * Maps a feature to its shorter name which should be used in output.
   */
  private static final Map<Feature, String> FEATURE_TO_NAME = new HashMap<>();

  static {
    FEATURE_TO_NAME.put(OssFeatures.HAS_SECURITY_TEAM, "Does it have a security team?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_SECURITY_POLICY, "Does it have a security policy?");
    FEATURE_TO_NAME.put(OssFeatures.USES_SIGNED_COMMITS, "Does it use verified signed commits?");
    FEATURE_TO_NAME.put(OssFeatures.VULNERABILITIES, "Info about vulnerabilities");
    FEATURE_TO_NAME.put(OssFeatures.IS_APACHE, "Does it belong to Apache?");
    FEATURE_TO_NAME.put(OssFeatures.IS_ECLIPSE, "Does it belong to Eclipse?");
    FEATURE_TO_NAME.put(OssFeatures.SUPPORTED_BY_COMPANY, "Is it supported by a company?");
    FEATURE_TO_NAME.put(OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES,
        "Does it scan for vulnerable dependencies?");
    FEATURE_TO_NAME.put(OssFeatures.USES_GITHUB_FOR_DEVELOPMENT,
        "Does it use GitHub as the main development platform?");
    FEATURE_TO_NAME.put(OssFeatures.USES_ADDRESS_SANITIZER, "Does it use AddressSanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_MEMORY_SANITIZER, "Does it use MemorySanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER,
        "Does it use UndefinedBehaviorSanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_NOHTTP, "Does it use nohttp?");
    FEATURE_TO_NAME.put(OssFeatures.USES_FIND_SEC_BUGS, "Does it use FindSecBugs?");
    FEATURE_TO_NAME.put(OssFeatures.USES_DEPENDABOT, "Does it use Dependabot?");
    FEATURE_TO_NAME.put(OssFeatures.USES_LGTM_CHECKS, "Does it use LGTM?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_BUG_BOUNTY_PROGRAM, "Does it have a bug bounty program?");
    FEATURE_TO_NAME.put(OssFeatures.SIGNS_ARTIFACTS, "Does it sign artifacts?");
  }

  @Override
  public String print(RatingValue ratingValue) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Here is how the rating was calculated:%n"));
    sb.append(print(ratingValue.scoreValue(), INDENT_STEP, true, new HashSet<>()));
    sb.append(String.format("Rating: %2.2f out of %2.2f -> %s%n",
        ratingValue.score(), Score.MAX, ratingValue.label()));
    sb.append(String.format("Confidence: %2.2f out of %2.2f%n",
        ratingValue.confidence(), Confidence.MAX));
    return sb.toString();
  }

  /**
   * Print out a formatted score value with a specified indent.
   *
   * @param scoreValue The score value to be printed.
   * @param indent The indent.
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

    if (!scoreValue.score().description().isEmpty()) {
      String[] lines = WordUtils
          .wrap(scoreValue.score().description(), DESCRIPTION_WRAP_LENGTH)
          .split("\n");
      sb.append(String.format("%sDescription:..%s%n", indent, lines[0]));
      for (int i = 1; i < lines.length; i++) {
        sb.append(String.format("%s              %s%n", indent, lines[i]));
      }
    }

    if (!isMainScore) {
      sb.append(String.format("%sImportance:...%s (weight %2.2f out of %2.2f)%n",
          indent, importance(scoreValue.weight()), scoreValue.weight(), Weight.MAX));
    }

    sb.append(String.format("%sValue:........%s%n",
        indent,
        append(String.format("%s", tellMeActualValueOf(scoreValue)), ' ', 4)));
    sb.append(String.format("%sConfidence:...%s out of %2.2f%n",
        indent,
        append(String.format("%.2f", scoreValue.confidence()), ' ', 4),
        Confidence.MAX));

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
          "%sBased on:.....%d sub-scores:%n", indent, subScoreValues.size()));
      for (ScoreValue usedValue : subScoreValues) {
        sb.append(
            print(usedValue, indent + INDENT_STEP + INDENT_STEP, false, printedScores));
        sb.append("\n");
      }
    }

    if (!featureValues.isEmpty()) {
      sb.append(String.format("%sBased on:...%d features:%n", indent, featureValues.size()));

      Map<String, Object> nameToValue = new TreeMap<>(String::compareTo);
      int maxLength = 0;
      for (Value usedValue : featureValues) {
        String name = nameOf(usedValue.feature());

        if (usedValue.isUnknown()) {
          nameToValue.put(name, "unknown");
        } else if (usedValue instanceof BooleanValue) {
          BooleanValue booleanValue = (BooleanValue) usedValue;
          nameToValue.put(name, booleanValue.get() ? "Yes" : "No");
        } else {
          nameToValue.put(name, usedValue.get());
        }

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

    if (!scoreValue.explanation().isEmpty()) {
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
    return String.format("%2.2f out of %2.2f", scoreValue.get(), Score.MAX);
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

  /**
   * Figures out how a name of a feature should be printed out.
   *
   * @param feature The feature.
   * @return A name of the feature.
   */
  static String nameOf(Feature feature) {
    for (Map.Entry<Class, String> entry : FEATURE_CLASS_TO_NAME.entrySet()) {
      if (feature.getClass() == entry.getKey()) {
        return entry.getValue();
      }
    }
    for (Map.Entry<Feature, String> entry : FEATURE_TO_NAME.entrySet()) {
      if (feature.equals(entry.getKey())) {
        return entry.getValue();
      }
    }
    return feature.name();
  }

  /**
   * Returns a human-readable label for a weight.
   *
   * @param weight The weight.
   * @return A human-readable label.
   */
  private static String importance(double weight) {
    if (weight < 0.3) {
      return "Low";
    }
    if (weight < 0.66) {
      return "Medium";
    }
    return "High";
  }

}
