package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with Bandit.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link OssFeatures#USES_BANDIT_SCAN_CHECKS}</li>
 *  <li>{@link OssFeatures#RUNS_BANDIT_SCANS}</li>
 *  <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class BanditScore extends FeatureBasedScore {

  /**
   * Programming languages supported by Bandit.
   *
   * @see <a href="https://bandit.readthedocs.io/en/latest/#welcome-to-bandit-s-developer-documentation">Bandit
   * overview</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(PYTHON);

  /**
   * Defines how the score value is increased if a project runs Bandit scans.
   */
  private static final double BANDIT_SCANS_POINTS = 6.0;

  /**
   * Defines how the score value is increased if a project runs Bandit checks for commits.
   */
  private static final double BANDIT_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link BanditScore}.
   */
  BanditScore() {
    super("How a project uses Bandit", USES_BANDIT_SCAN_CHECKS, RUNS_BANDIT_SCANS, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesBanditChecks = findValue(values, USES_BANDIT_SCAN_CHECKS,
        "Hey! You have to tell me if the project uses Bandit checks!");
    Value<Boolean> runsBanditScans = findValue(values, RUNS_BANDIT_SCANS,
        "Hey! You have to tell me if the project runs Bandit scans!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesBanditChecks, runsBanditScans, languages);

    if (allUnknown(usesBanditChecks, runsBanditScans, languages)) {
      return scoreValue.makeUnknown().explain(
          "The score value is unknown because all required features are unknown.");
    }

    if (languages.isUnknown()) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project does not confirm which languages are used.");
    }

    if (!languages.isUnknown() && !SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by Bandit.");
    }

    if (usesBanditChecks.orElse(false)) {
      scoreValue.increase(BANDIT_CHECKS_POINTS);
    }

    if (runsBanditScans.orElse(false)) {
      scoreValue.increase(BANDIT_SCANS_POINTS);
    }

    return scoreValue;
  }
}