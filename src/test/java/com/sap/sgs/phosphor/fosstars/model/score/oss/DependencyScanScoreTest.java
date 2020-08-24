package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class DependencyScanScoreTest {

  @Test
  public void smokeTest() {
    DependencyScanScore score = new DependencyScanScore();
    ScoreValue scoreValue = score.calculate(setOf(
        OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.0),
        USES_GITHUB_FOR_DEVELOPMENT.value(true),
        USES_DEPENDABOT.value(true),
        LANGUAGES.value(Languages.of(JAVA)),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN))
    ));

    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(2, scoreValue.usedValues().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }

}