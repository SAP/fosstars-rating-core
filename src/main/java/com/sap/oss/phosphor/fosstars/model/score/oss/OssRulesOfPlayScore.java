package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USE_REUSE;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This score shows how well an open source project follows certain rules.
 * The score is based on a set of features that represent rules of play for an open source project.
 */
public class OssRulesOfPlayScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public OssRulesOfPlayScore() {
    super(
        "Open source rules or play score",
        "The score shows whether an open source project violates violates certain rules or not.",
        USE_REUSE,
        HAS_SECURITY_POLICY,
        HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    List<Value<?>> usedValues = features().stream()
        .map(feature -> find(feature, values))
        .collect(Collectors.toList());

    List<Value<Boolean>> violatedRules = usedValues.stream()
        .filter(value -> !value.isUnknown())
        .map(BooleanValue.class::cast)
        .filter(BooleanValue::isFalse)
        .collect(Collectors.toList());

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
}
