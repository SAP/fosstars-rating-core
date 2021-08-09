package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
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

public class DependabotScoreTest {

  private static final DependabotScore SCORE = new DependabotScore();

  @Test
  public void testCalculate() {
    assertScore(
        Score.INTERVAL,
        SCORE,
        setOf(
            USES_GITHUB_FOR_DEVELOPMENT.value(true),
            USES_DEPENDABOT.value(true),
            LANGUAGES.value(Languages.of(JAVA)),
            PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN))
        ));
  }

  @Test
  public void testCalculateWithAllUnknown() {
    ScoreValue scoreValue = SCORE.calculate(Utils.allUnknown(SCORE.allFeatures()));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }
}