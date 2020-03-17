package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Score;
import org.junit.Test;

public class DependencyScanScoreTest {

  @Test
  public void smokeTest() {
    assertScore(
        Score.INTERVAL,
        new DependencyScanScore(),
        setOf(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutScans() {
    new DependencyScanScore().calculate();
  }

}