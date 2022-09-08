package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class DependencyScanScoreTest {

  private static final DependencyScanScore SCORE = new DependencyScanScore();

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(setOf(
        OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.0),
        USES_GITHUB_FOR_DEVELOPMENT.value(true),
        USES_DEPENDABOT.value(true),
        USES_SNYK.value(false),
        LANGUAGES.value(Languages.of(JAVA)),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN))
    ));

    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(3, scoreValue.usedValues().size());
  }

  @Test
  public void testCalculateWithAllUnknown() {
    ScoreValue scoreValue = SCORE.calculate(Utils.allUnknown(SCORE.allFeatures()));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithNoInfo() {
    SCORE.calculate();
  }

}