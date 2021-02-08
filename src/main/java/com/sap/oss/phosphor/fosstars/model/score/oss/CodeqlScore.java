package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.oss.phosphor.fosstars.model.value.Language.TYPESCRIPT;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if and how a project uses static analysis with CodeQL.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM_CHECKS}</li>
 *  <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_CODEQL_CHECKS}</li>
 *  <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RUNS_CODEQL_SCANS}</li>
 *  <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class CodeqlScore extends FeatureBasedScore {

  /**
   * Programming languages supported by CodeQL.
   *
   * @see <a href="https://help.semmle.com/QL/ql-support/language-support.html">CodeQL - Languages and compilers</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(
      C, CPP, JAVA, C_SHARP, PYTHON, GO, JAVASCRIPT, TYPESCRIPT);

  /**
   * Defines how the score value is increased if a project runs CodeQL scans.
   */
  private static final double CODEQL_SCANS_POINTS = 5.0;

  /**
   * Defines how the score value is increased if a project runs CodeQL checks for commits.
   */
  private static final double CODEQL_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link CodeqlScore}.
   */
  CodeqlScore() {
    super("How a project uses CodeQL",
        USES_LGTM_CHECKS, USES_CODEQL_CHECKS, RUNS_CODEQL_SCANS, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesLgtmChecks = findValue(values, USES_LGTM_CHECKS,
        "Hey! You have to tell me if the project uses LGTM checks!");
    Value<Boolean> usesCodeqlChecks = findValue(values, USES_CODEQL_CHECKS,
        "Hey! You have to tell me if the project uses CodeQL checks!");
    Value<Boolean> runsCodeqlScans = findValue(values, RUNS_CODEQL_SCANS,
        "Hey! You have to tell me if the project runs CodeQL scans!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN,
        usesLgtmChecks, usesCodeqlChecks, runsCodeqlScans, languages);

    if (allUnknown(usesLgtmChecks, usesCodeqlChecks, runsCodeqlScans, languages)) {
      return scoreValue.makeUnknown().explain(
          "The score value is unknown because all required features are unknown.");
    }

    if (!languages.isUnknown() && !SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by CodeQL.");
    }

    if (usesLgtmChecks.orElse(false) || usesCodeqlChecks.orElse(false)) {
      scoreValue.increase(CODEQL_CHECKS_POINTS);
    }

    if (runsCodeqlScans.orElse(false)) {
      scoreValue.increase(CODEQL_SCANS_POINTS);
    }

    return scoreValue;
  }
}
