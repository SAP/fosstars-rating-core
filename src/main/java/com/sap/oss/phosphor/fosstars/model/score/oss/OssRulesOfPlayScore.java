package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This score shows how well an open source project follows certain rules.
 * The score is based on a set of features that represent rules of play for an open source project.
 */
public class OssRulesOfPlayScore extends FeatureBasedScore {

  /**
   * A list of features that are expected to be true.
   */
  public static final Set<Feature<Boolean>> EXPECTED_TRUE
      = Collections.unmodifiableSet(setOf(USES_REUSE, HAS_SECURITY_POLICY));

  /**
   * A list of features that are expected to be false.
   */
  public static final Set<Feature<Boolean>> EXPECTED_FALSE
      = Collections.singleton(HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT);

  /**
   * Initializes a new score.
   */
  public OssRulesOfPlayScore() {
    super(
        "Open source rules or play score",
        "The score shows whether an open source project violates certain rules or not.",
        merge(EXPECTED_TRUE, EXPECTED_FALSE));
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    List<Value<?>> usedValues = features().stream()
        .map(feature -> find(feature, values))
        .collect(Collectors.toList());

    List<Value<Boolean>> violatedRules = findViolatedRules(usedValues);

    if (violatedRules.isEmpty()) {
      return scoreValue(MAX, usedValues).explain("No violated rules found.");
    }

    return scoreValue(MIN, usedValues).explain("Found %d violated rule%s:%n%s",
        violatedRules.size(),
        violatedRules.size() == 1 ? "" : "s",
        violatedRules.stream()
            .map(value -> String.format("  %s%n", value.feature().name()))
            .collect(Collectors.joining("\n")));
  }

  /**
   * Looks for violated rules.
   *
   * @param rules A set of rules to be checked.
   * @param expectedValue An expected value.
   * @param usedValues A list of values to be checked.
   * @return A list of violated rules.
   */
  private static List<Value<Boolean>> findViolatedRules(
      Set<Feature<Boolean>> rules, boolean expectedValue, List<Value<?>> usedValues) {

    return usedValues.stream()
        .filter(value -> !value.isUnknown())
        .filter(value -> rules.contains(value.feature()))
        .map(BooleanValue.class::cast)
        .filter(value -> value.get() != expectedValue)
        .collect(Collectors.toList());
  }

  /**
   * Looks for violated rules.
   *
   * @param usedValues A list of values to be checked.
   * @return A list of violated rules.
   */
  public static List<Value<Boolean>> findViolatedRules(List<Value<?>> usedValues) {
    List<Value<Boolean>> violatedRules = new ArrayList<>();
    violatedRules.addAll(findViolatedRules(EXPECTED_FALSE, false, usedValues));
    violatedRules.addAll(findViolatedRules(EXPECTED_TRUE, true, usedValues));
    return violatedRules;
  }

  /**
   * Merge multiple sets.
   *
   * @param sets The sets to be merged.
   * @return A new set that contains elements from the specified sets.
   */
  private static Set<Feature<?>> merge(Set<Feature<Boolean>>... sets) {
    Objects.requireNonNull(sets, "Oops! Sets can't be null!");
    Set<Feature<?>> result = new HashSet<>();
    for (Set<Feature<Boolean>> set : sets) {
      result.addAll(set);
    }
    return result;
  }
}
