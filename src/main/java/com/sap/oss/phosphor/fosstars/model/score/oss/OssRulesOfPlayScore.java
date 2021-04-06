package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ENABLED_VULNERABILITY_ALERTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ADMIN_TEAM_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_ADMINS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAMS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REUSE_LICENSES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_UNRESOLVED_VULNERABILITY_ALERTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.README_HAS_REUSE_INFO;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.REGISTERED_IN_REUSE;
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
  public static final Set<Feature<Boolean>> EXPECTED_TRUE = Collections.unmodifiableSet(setOf(
      HAS_LICENSE,
      ALLOWED_LICENSE,
      HAS_README,
      HAS_CONTRIBUTING_GUIDELINE,
      HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE,
      HAS_ENOUGH_TEAMS_ON_GITHUB,
      HAS_ADMIN_TEAM_ON_GITHUB,
      HAS_ENOUGH_ADMINS_ON_GITHUB,
      HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB,
      HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB,
      ENABLED_VULNERABILITY_ALERTS_ON_GITHUB,
      HAS_SECURITY_POLICY,
      README_HAS_REUSE_INFO,
      HAS_REUSE_LICENSES,
      REGISTERED_IN_REUSE,
      IS_REUSE_COMPLIANT
  ));

  /**
   * A list of features that are expected to be false.
   */
  public static final Set<Feature<Boolean>> EXPECTED_FALSE = Collections.unmodifiableSet(setOf(
      LICENSE_HAS_DISALLOWED_CONTENT,
      HAS_UNRESOLVED_VULNERABILITY_ALERTS
  ));

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

    return scoreValue(MIN, usedValues).explain("Found %d violated rule%s",
        violatedRules.size(),
        violatedRules.size() == 1 ? "" : "s");
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
