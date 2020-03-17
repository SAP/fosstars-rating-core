package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class ProjectSecurityTestingScoreTest {

  private static ProjectSecurityTestingScore PROJECT_SECURITY_TESTING
      = new ProjectSecurityTestingScore();

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutDependencyScans() {
    PROJECT_SECURITY_TESTING.calculate();
  }

  @Test
  public void smokeTest() {
    assertScore(
        Score.INTERVAL,
        PROJECT_SECURITY_TESTING,
        setOf(SCANS_FOR_VULNERABLE_DEPENDENCIES.unknown()));

    assertScore(
        Score.INTERVAL,
        PROJECT_SECURITY_TESTING,
        setOf(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true)));
  }

  @Test
  public void explanation() {
    ScoreValue value = PROJECT_SECURITY_TESTING.calculate(
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true));

    assertTrue(value.score().description().isEmpty());
    assertEquals(1, value.explanation().size());
  }

}