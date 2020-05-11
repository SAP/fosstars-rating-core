package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
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
        setOf(
            PROJECT_SECURITY_TESTING.score(LgtmScore.class).value(Score.MIN),
            PROJECT_SECURITY_TESTING.score(DependencyScanScore.class).value(Score.MIN),
            PROJECT_SECURITY_TESTING.score(NoHttpToolScore.class).value(Score.MIN),
            PROJECT_SECURITY_TESTING.score(MemorySafetyTestingScore.class).value(Score.MIN),
            PROJECT_SECURITY_TESTING.score(FindSecBugsScore.class).value(Score.MIN),
            PROJECT_SECURITY_TESTING.score(FuzzingScore.class).value(Score.MIN)));

    assertScore(
        Score.INTERVAL,
        PROJECT_SECURITY_TESTING,
        setOf(
            PROJECT_SECURITY_TESTING.score(LgtmScore.class).value(Score.MAX),
            PROJECT_SECURITY_TESTING.score(DependencyScanScore.class).value(Score.MAX),
            PROJECT_SECURITY_TESTING.score(NoHttpToolScore.class).value(Score.MAX),
            PROJECT_SECURITY_TESTING.score(MemorySafetyTestingScore.class).value(Score.MAX),
            PROJECT_SECURITY_TESTING.score(FindSecBugsScore.class).value(Score.MAX),
            PROJECT_SECURITY_TESTING.score(FuzzingScore.class).value(Score.MAX)));
  }

  @Test
  public void explanation() {
    ScoreValue value = PROJECT_SECURITY_TESTING.calculate(
        PROJECT_SECURITY_TESTING.score(LgtmScore.class).value(Score.MAX / 3),
        PROJECT_SECURITY_TESTING.score(DependencyScanScore.class).value(Score.MAX / 5),
        PROJECT_SECURITY_TESTING.score(NoHttpToolScore.class).value(Score.MAX / 2),
        PROJECT_SECURITY_TESTING.score(MemorySafetyTestingScore.class).value(Score.MAX / 4),
        PROJECT_SECURITY_TESTING.score(FindSecBugsScore.class).value(Score.MAX / 7),
        PROJECT_SECURITY_TESTING.score(FuzzingScore.class).value(Score.MIN / 2));

    assertTrue(value.score().description().isEmpty());
    assertTrue(value.explanation().isEmpty());
  }

  @Test
  public void testEqualsAndHashCode() {
    ProjectSecurityTestingScore one = new ProjectSecurityTestingScore();
    ProjectSecurityTestingScore two = new ProjectSecurityTestingScore();
    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }

}