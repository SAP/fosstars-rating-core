package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with GoSec.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link OssFeatures#RUNS_GOSEC_SCANS}</li>
 *  <li>{@link OssFeatures#USES_GOSEC_SCAN_CHECKS}</li>
 *  <li>{@link OssFeatures#USES_GOSEC_WITH_RULES}</li>
 *  <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class GoSecScore extends FeatureBasedScore {

  /**
   * Programming languages supported by GoSec.
   *
   * @see <a href="https://github.com/securego/gosec#gosec---golang-security-checker">GoSec
   * overview</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(GO);

  /**
   * Defines how the score value is increased if a project runs GoSec checks for commits.
   */
  private static final double GOSEC_SCANS_SELECTED_RULES_POINTS = 4.0;

  /**
   * Defines how the score value is increased if a project runs GoSec scans.
   */
  private static final double GOSEC_SCANS_POINTS = 6.0;

  /**
   * Defines how the score value is increased if a project runs GoSec checks for commits.
   */
  private static final double GOSEC_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link GoSecScore}.
   */
  GoSecScore() {
    super("How a project uses GoSec", USES_GOSEC_SCAN_CHECKS,
        RUNS_GOSEC_SCANS, USES_GOSEC_WITH_RULES, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesGoSecChecks = findValue(values, USES_GOSEC_SCAN_CHECKS,
        "Hey! You have to tell me if the project uses GoSec checks!");
    Value<Boolean> runsGoSecScans = findValue(values, RUNS_GOSEC_SCANS,
        "Hey! You have to tell me if the project runs GoSec scans!");
    Value<Boolean> usesGoSecWithSeclectedRules = findValue(values,
        USES_GOSEC_WITH_RULES,
        "Hey! You have to tell me if the project runs GoSec scans with rules!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesGoSecChecks, runsGoSecScans, usesGoSecWithSeclectedRules, languages);

    if (allUnknown(usesGoSecChecks, runsGoSecScans, usesGoSecWithSeclectedRules,
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
              + "GoSec.");
    }

    if (usesGoSecChecks.orElse(false)) {
      scoreValue.increase(GOSEC_CHECKS_POINTS);
    }

    if (usesGoSecWithSeclectedRules.orElse(false)) {
      scoreValue.decrease(GOSEC_SCANS_SELECTED_RULES_POINTS);
    }

    if (runsGoSecScans.orElse(false)) {
      scoreValue.increase(GOSEC_SCANS_POINTS);
    }

    return scoreValue;
  }
}