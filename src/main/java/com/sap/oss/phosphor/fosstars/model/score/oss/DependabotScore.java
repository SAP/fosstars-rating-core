package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.F_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PHP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.oss.phosphor.fosstars.model.value.Language.RUBY;
import static com.sap.oss.phosphor.fosstars.model.value.Language.SCALA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.VISUALBASIC;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.COMPOSER;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.DOTNET;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.PIP;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.RUBYGEMS;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.YARN;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManager;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.HashMap;
import java.util.Map;

/**
 * The scores assesses how an open-source project uses Dependabot to scan
 * dependencies for known vulnerabilities. It is based on the following features:
 * <ul>
 *   <li>{@link OssFeatures#USES_DEPENDABOT}</li>
 *   <li>{@link OssFeatures#USES_GITHUB_FOR_DEVELOPMENT}</li>
 *   <li>{@link OssFeatures#PACKAGE_MANAGERS}</li>
 *   <li>{@link OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class DependabotScore extends FeatureBasedScore {

  /**
   * The supported package ecosystems and languages
   * that GitHub can detect vulnerabilities and dependencies for.
   *
   * @see <a href="https://help.github.com/en/github/managing-security-vulnerabilities/about-security-alerts-for-vulnerable-dependencies#alerts-and-automated-security-updates-for-vulnerable-dependencies">
   *   Alerts and automated security updates for vulnerable dependencies</a>
   * @see <a href="https://help.github.com/en/github/visualizing-repository-data-with-graphs/listing-the-packages-that-a-repository-depends-on#supported-package-ecosystems">
   *   Supported package ecosystems</a>
   */
  private static final Map<PackageManager, Languages> SUPPORTED_LANGUAGES = new HashMap<>();

  static {
    SUPPORTED_LANGUAGES.put(MAVEN, Languages.of(JAVA, SCALA));
    SUPPORTED_LANGUAGES.put(NPM, Languages.of(JAVASCRIPT));
    SUPPORTED_LANGUAGES.put(YARN, Languages.of(JAVASCRIPT));
    SUPPORTED_LANGUAGES.put(DOTNET, Languages.of(C_SHARP, F_SHARP, CPP, VISUALBASIC));
    SUPPORTED_LANGUAGES.put(PIP, Languages.of(PYTHON));
    SUPPORTED_LANGUAGES.put(RUBYGEMS, Languages.of(RUBY));
    SUPPORTED_LANGUAGES.put(COMPOSER, Languages.of(PHP));
  }

  /**
   * A score value that is returned if it's likely
   * that a project uses the security alerts on GitHub.
   */
  private static final double GITHUB_ALERTS_SCORE_VALUE = 5.0;

  /**
   * Initializes a new score.
   */
  public DependabotScore() {
    super("How a project uses Dependabot",
        USES_DEPENDABOT,
        USES_GITHUB_FOR_DEVELOPMENT,
        PACKAGE_MANAGERS,
        LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesDependabot = find(USES_DEPENDABOT, values);
    Value<Boolean> usesGithub = find(USES_GITHUB_FOR_DEVELOPMENT, values);
    Value<Languages> languages = find(LANGUAGES, values);
    Value<PackageManagers> packageManagers = find(PACKAGE_MANAGERS, values);

    ScoreValue scoreValue = scoreValue(Score.MIN,
        usesDependabot, usesGithub, packageManagers, languages);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    // if the project uses Dependabot,
    // then we're happy
    if (usesDependabot.orElse(false)) {
      return scoreValue.set(Score.MAX);
    }

    // if the projects uses GitHub for development,
    // then there is a chance that it takes advantage of security alerts,
    // although we can't tell for sure
    if (usesGithub.orElse(false)) {
      boolean dependabotCanBeUsed = packageManagers.orElse(PackageManagers.empty()).list().stream()
          .anyMatch(SUPPORTED_LANGUAGES::containsKey);

      // if the project does not use any package ecosystem that is supported by Dependabot,
      // then a score is not applicable
      if (!dependabotCanBeUsed) {
        return scoreValue.makeNotApplicable();
      }

      Languages usedLanguages = languages.orElse(Languages.empty());
      for (PackageManager packageManager : packageManagers.orElse(PackageManagers.empty())) {
        Languages supportedLanguages
            = SUPPORTED_LANGUAGES.getOrDefault(packageManager, Languages.empty());
        if (supportedLanguages.containsAnyOf(usedLanguages)) {
          return scoreValue.set(GITHUB_ALERTS_SCORE_VALUE);
        }
      }
    }

    // otherwise, return the minimal score
    return scoreValue.set(Score.MIN);
  }
}
