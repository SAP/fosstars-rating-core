package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_INNERSOURCE_TOPIC;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
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

public class InnerSourceRulesOfPlayScore extends FeatureBasedScore {

  /**
   * A score that is assigned if recommendations found.
   */
  public static final double SCORE_WITH_WARNING = 9.0;

  /**
   * A set of features that are expected to be true.
   */
  public static final Set<Feature<Boolean>> EXPECTED_TRUE = Collections.unmodifiableSet(Utils.setOf(
      HAS_README, HAS_INNERSOURCE_TOPIC
  ));

  /**
   * A set of features that are expected to be false.
   */
  public static final Set<Feature<Boolean>> EXPECTED_FALSE = Collections.emptySet();

  /**
   * A set of features that are recommended to be true.
   */
  public static final Set<Feature<Boolean>> RECOMMENDED_TRUE = Collections.emptySet();

  /**
   * A set of features that are recommended to be false.
   */
  public static final Set<Feature<Boolean>> RECOMMENDED_FALSE = Collections.emptySet();

  /**
   * Initializes a new score.
   */
  public InnerSourceRulesOfPlayScore() {
    super("InnerSource rules of play score",
        "The score shows wether an InnerSource project applies to rules or not.",
        merge(EXPECTED_TRUE, EXPECTED_FALSE, RECOMMENDED_TRUE, RECOMMENDED_FALSE));
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    List<Value<?>> usedValues = features().stream()
        .map(feature -> find(feature, values))
        .collect(Collectors.toList());

    List<Value<Boolean>> violatedRules = findViolatedRulesIn(usedValues);
    if (!violatedRules.isEmpty()) {
      return scoreValue(MIN, usedValues)
          .explain("Found %d violated rule%s",
              violatedRules.size(),
              violatedRules.size() == 1 ? "" : "s");
    }

    List<Value<Boolean>> warnings = findWarningsIn(usedValues);
    if (!warnings.isEmpty()) {
      return scoreValue(SCORE_WITH_WARNING, usedValues)
          .explain("Found %d recommendations%s",
              warnings.size(),
              warnings.size() == 1 ? "" : "s");
    }

    return scoreValue(MAX, usedValues).explain("No violated rules found.");
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    return this.getClass().equals(o.getClass());
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  /**
   * Looks for violated rules.
   *
   * @param rules A set of rules to be checked.
   * @param expectedValue An expected value.
   * @param usedValues A list of values to be checked.
   * @return A list of violated rules.
   */
  private static List<Value<Boolean>> findViolatedRulesIn(
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
  public static List<Value<Boolean>> findViolatedRulesIn(List<Value<?>> usedValues) {
    List<Value<Boolean>> violatedRules = new ArrayList<>();
    violatedRules.addAll(findViolatedRulesIn(EXPECTED_FALSE, false, usedValues));
    violatedRules.addAll(findViolatedRulesIn(EXPECTED_TRUE, true, usedValues));
    return violatedRules;
  }

  /**
   * Looks for recommendations.
   *
   * @param usedValues A list of values to be checked.
   * @return A list of recommendations.
   */
  public static List<Value<Boolean>> findWarningsIn(List<Value<?>> usedValues) {
    List<Value<Boolean>> violatedRules = new ArrayList<>();
    violatedRules.addAll(findViolatedRulesIn(RECOMMENDED_FALSE, false, usedValues));
    violatedRules.addAll(findViolatedRulesIn(RECOMMENDED_TRUE, true, usedValues));
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
