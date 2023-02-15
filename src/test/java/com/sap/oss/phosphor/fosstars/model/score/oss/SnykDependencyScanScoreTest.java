package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GOMODULES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class SnykDependencyScanScoreTest {

  private static final SnykDependencyScanScore SCORE = new SnykDependencyScanScore();

  @Test
  public void testCalculateWhenSnykIsUsed() {
    assertScore(
        Score.makeInterval(9, 10),
        SCORE,
        setOf(
            USES_GITHUB_FOR_DEVELOPMENT.value(true),
            USES_SNYK.value(true),
            LANGUAGES.value(Languages.of(GO)),
            PACKAGE_MANAGERS.value(PackageManagers.from(GOMODULES))));
  }

  @Test
  public void testCalculateWhenSnykIsNotUsed() {
    assertScore(
        Score.makeInterval(0, 5),
        SCORE,
        setOf(
            USES_GITHUB_FOR_DEVELOPMENT.value(true),
            USES_SNYK.value(false),
            LANGUAGES.value(Languages.of(GO)),
            PACKAGE_MANAGERS.value(PackageManagers.from(GOMODULES))));
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