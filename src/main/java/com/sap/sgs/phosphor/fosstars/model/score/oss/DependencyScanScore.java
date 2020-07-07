package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.F_SHARP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.PHP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.RUBY;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.SCALA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.VISUALBASIC;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.COMPOSER;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.DOTNET;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.PIP;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.RUBYGEMS;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.YARN;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManager;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The scores assesses how well an open-source project scans dependencies
 * for known vulnerabilities. It is based on the following features.</p>
 * <ul>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures
 *     #SCANS_FOR_VULNERABLE_DEPENDENCIES}
 *   </li>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_DEPENDABOT}
 *   </li>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures
 *     #USES_GITHUB_FOR_DEVELOPMENT}
 *   </li>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}
 *   </li>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#PACKAGE_MANAGERS}
 *   </li>
 * </ul>
 */
public class DependencyScanScore extends FeatureBasedScore {

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
  private static final double GITHUB_ALERTS_SCORE_VALUE = 6.0;

  /**
   * Initializes a new {@link DependencyScanScore}.
   */
  DependencyScanScore() {
    super("How a project scans its dependencies for vulnerabilities",
        SCANS_FOR_VULNERABLE_DEPENDENCIES,
        USES_DEPENDABOT,
        USES_GITHUB_FOR_DEVELOPMENT,
        PACKAGE_MANAGERS,
        LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> scansDependencies = find(SCANS_FOR_VULNERABLE_DEPENDENCIES, values);
    Value<Boolean> usesDependabot = find(USES_DEPENDABOT, values);
    Value<Boolean> usesGithub = find(USES_GITHUB_FOR_DEVELOPMENT, values);
    Value<Languages> languages = find(LANGUAGES, values);
    Value<PackageManagers> packageManagers = find(PACKAGE_MANAGERS, values);

    ScoreValue scoreValue = scoreValue(Score.MIN,
        scansDependencies, usesDependabot, usesGithub, packageManagers, languages);

    // if the project scans for vulnerable dependencies,
    // then we're happy
    if (scansDependencies.orElse(false)) {
      return scoreValue.set(Score.MAX);
    }

    // if the project uses Dependabot,
    // then we're happy
    if (usesDependabot.orElse(false)) {
      return scoreValue.set(Score.MAX);
    }

    // if the projects uses GitHub for development,
    // then there is a chance that it takes advantage of security alerts
    // although we can't tell for sure
    if (usesGithub.orElse(false)) {
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
    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link DependencyScanScore}.
   * The class loads test vectors, and provides methods to verify a {@link DependencyScanScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "DependencyScanScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link DependencyScanScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(DependencyScanScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(DependencyScanScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
