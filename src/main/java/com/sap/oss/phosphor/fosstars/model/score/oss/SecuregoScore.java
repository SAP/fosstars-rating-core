package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_SECUREGO_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with Securego.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link OssFeatures#RUNS_SECUREGO_SCANS}</li>
 *  <li>{@link OssFeatures#USES_SECUREGO_SCAN_CHECKS}</li>
 *  <li>{@link OssFeatures#USES_SECUREGO_WITH_RULES}</li>
 *  <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class SecuregoScore extends FeatureBasedScore {

  /**
   * Programming languages supported by Securego.
   *
   * @see <a href="https://github.com/securego/gosec#gosec---golang-security-checker">Securego
   * overview</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(GO);

  /**
   * Defines how the score value is increased if a project runs Securego checks for commits.
   */
  private static final double SECUREGO_SCANS_SELECTED_RULES_POINTS = 2.0;

  /**
   * Defines how the score value is increased if a project runs Securego scans.
   */
  private static final double SECUREGO_SCANS_POINTS = 7.0;

  /**
   * Defines how the score value is increased if a project runs Securego checks for commits.
   */
  private static final double SECUREGO_CHECKS_POINTS = 8.0;

  /**
   * Initializes a new {@link SecuregoScore}.
   */
  SecuregoScore() {
    super("How a project uses Securego", USES_SECUREGO_SCAN_CHECKS,
        RUNS_SECUREGO_SCANS, USES_SECUREGO_WITH_RULES, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesSecuregoChecks = findValue(values, USES_SECUREGO_SCAN_CHECKS,
        "Hey! You have to tell me if the project uses Securego checks!");
    Value<Boolean> runsSecuregoScans = findValue(values, RUNS_SECUREGO_SCANS,
        "Hey! You have to tell me if the project runs Securego scans!");
    Value<Boolean> usesSecuregoWithSeclectedRules = findValue(values,
        USES_SECUREGO_WITH_RULES,
        "Hey! You have to tell me if the project runs Securego scans with rules!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesSecuregoChecks, runsSecuregoScans, usesSecuregoWithSeclectedRules, languages);

    if (allUnknown(usesSecuregoChecks, runsSecuregoScans, usesSecuregoWithSeclectedRules,
        languages)) {
      return scoreValue.makeUnknown().explain(
          "The score value is unknown because all required features are unknown.");
    }

    if (languages.isUnknown()) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project does not confirm which languages are used.");
    }

    if (!languages.isUnknown() && !SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by "
              + "Securego.");
    }

    if (usesSecuregoChecks.orElse(false)) {
      scoreValue.increase(SECUREGO_CHECKS_POINTS);
    }

    if (usesSecuregoWithSeclectedRules.orElse(false)) {
      scoreValue.decrease(SECUREGO_SCANS_SELECTED_RULES_POINTS);
    }

    if (runsSecuregoScans.orElse(false)) {
      scoreValue.increase(SECUREGO_SCANS_POINTS);
    }

    return scoreValue;
  }
}