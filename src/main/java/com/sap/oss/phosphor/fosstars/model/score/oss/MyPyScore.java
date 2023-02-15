package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_MYPY_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MYPY_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with MyPy.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link OssFeatures#USES_MYPY_SCAN_CHECKS}</li>
 *  <li>{@link OssFeatures#RUNS_MYPY_SCANS}</li>
 *  <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class MyPyScore extends FeatureBasedScore {

  /**
   * Programming languages supported by MyPy.
   *
   * @see <a href="http://www.mypy-lang.org/">MyPy
   * overview</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(PYTHON);

  /**
   * Defines how the score value is increased if a project runs MyPy scans.
   */
  private static final double MYPY_SCANS_POINTS = 6.0;

  /**
   * Defines how the score value is increased if a project runs MyPy checks for commits.
   */
  private static final double MYPY_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link MyPyScore}.
   */
  MyPyScore() {
    super("How a project uses MyPy", USES_MYPY_SCAN_CHECKS, RUNS_MYPY_SCANS, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesMyPyChecks = findValue(values, USES_MYPY_SCAN_CHECKS,
        "Hey! You have to tell me if the project uses MyPy checks!");
    Value<Boolean> runsMyPyScans = findValue(values, RUNS_MYPY_SCANS,
        "Hey! You have to tell me if the project runs MyPy scans!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesMyPyChecks, runsMyPyScans, languages);

    if (allUnknown(usesMyPyChecks, runsMyPyScans, languages)) {
      return scoreValue.makeUnknown().explain(
          "The score value is unknown because all required features are unknown.");
    }

    if (languages.isUnknown()) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project does not confirm which languages are used.");
    }

    if (!SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by MyPy.");
    }

    if (usesMyPyChecks.orElse(false)) {
      scoreValue.increase(MYPY_CHECKS_POINTS);
    }

    if (runsMyPyScans.orElse(false)) {
      scoreValue.increase(MYPY_SCANS_POINTS);
    }

    return scoreValue;
  }
}