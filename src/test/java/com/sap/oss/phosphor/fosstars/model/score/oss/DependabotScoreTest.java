package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import org.junit.Test;

public class DependabotScoreTest {

  @Test
  public void smokeTest() {
    assertScore(
        Score.INTERVAL,
        new DependabotScore(),
        setOf(
            USES_GITHUB_FOR_DEVELOPMENT.value(true),
            USES_DEPENDABOT.value(true),
            LANGUAGES.value(Languages.of(JAVA)),
            PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN))
        ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }
}