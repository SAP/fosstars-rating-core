package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_PYLINT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_PYLINT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with Pylint.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link OssFeatures#USES_PYLINT_SCAN_CHECKS}</li>
 *  <li>{@link OssFeatures#RUNS_PYLINT_SCANS}</li>
 *  <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class PylintScore extends FeatureBasedScore {

  /**
   * Programming languages supported by Pylint.
   *
   * @see <a href="https://pylint.pycqa.org/en/latest/index.html">Pylint
   * overview</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(PYTHON);

  /**
   * Defines how the score value is increased if a project runs Pylint scans.
   */
  private static final double PYLINT_SCANS_POINTS = 6.0;

  /**
   * Defines how the score value is increased if a project runs Pylint checks for commits.
   */
  private static final double PYLINT_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link PylintScore}.
   */
  PylintScore() {
    super("How a project uses Pylint", USES_PYLINT_SCAN_CHECKS, RUNS_PYLINT_SCANS, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesPylintChecks = findValue(values, USES_PYLINT_SCAN_CHECKS,
        "Hey! You have to tell me if the project uses Pylint checks!");
    Value<Boolean> runsPylintScans = findValue(values, RUNS_PYLINT_SCANS,
        "Hey! You have to tell me if the project runs Pylint scans!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesPylintChecks, runsPylintScans, languages);

    if (allUnknown(usesPylintChecks, runsPylintScans, languages)) {
      return scoreValue.makeUnknown().explain(
          "The score value is unknown because all required features are unknown.");
    }

    if (languages.isUnknown()) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project does not confirm which languages are used.");
    }

    if (!SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by Pylint.");
    }

    if (usesPylintChecks.orElse(false)) {
      scoreValue.increase(PYLINT_CHECKS_POINTS);
    }

    if (runsPylintScans.orElse(false)) {
      scoreValue.increase(PYLINT_SCANS_POINTS);
    }

    return scoreValue;
  }
}