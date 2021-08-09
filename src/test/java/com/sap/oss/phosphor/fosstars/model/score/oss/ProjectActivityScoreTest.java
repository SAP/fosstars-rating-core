package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import java.util.Set;
import org.junit.Test;

public class ProjectActivityScoreTest {

  private static final ProjectActivityScore PROJECT_ACTIVITY = new ProjectActivityScore();

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = PROJECT_ACTIVITY.calculate(
        UnknownValue.of(NUMBER_OF_COMMITS_LAST_THREE_MONTHS),
        UnknownValue.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());

    scoreValue = PROJECT_ACTIVITY.calculate(
            NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(0),
            UnknownValue.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS));
    assertFalse(scoreValue.isUnknown());
    assertEquals(Score.MIN, scoreValue.get(), DELTA);
    assertTrue(scoreValue.confidence() > Confidence.MIN);
    assertTrue(scoreValue.confidence() < Confidence.MAX);
    assertFalse(scoreValue.explanation().isEmpty());

    scoreValue = PROJECT_ACTIVITY.calculate(
            UnknownValue.of(NUMBER_OF_COMMITS_LAST_THREE_MONTHS),
            NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(0));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());

    scoreValue = PROJECT_ACTIVITY.calculate(values(0,0));
    assertFalse(scoreValue.isUnknown());
    assertEquals(Score.MIN, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());

    scoreValue = PROJECT_ACTIVITY.calculate(values(1,1));
    assertFalse(scoreValue.isUnknown());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());

    // I wish all open-source projects were like that
    assertScore(Score.MAX, PROJECT_ACTIVITY, values(1000, 50));

    // or, even like that
    assertScore(Score.MAX, PROJECT_ACTIVITY, values(Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

  @Test
  public void testWithCommitsWithoutContributors() {
    ScoreValue scoreValue = PROJECT_ACTIVITY.calculate(values(1, 0));
    assertFalse(scoreValue.isUnknown());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertTrue(scoreValue.confidence() > Confidence.MIN);
    assertTrue(scoreValue.confidence() < Confidence.MAX);
    assertFalse(scoreValue.explanation().isEmpty());
  }

  @Test
  public void testWithContributorsWithoutCommits() {
    ScoreValue scoreValue = PROJECT_ACTIVITY.calculate(values(0, 1));
    assertFalse(scoreValue.isUnknown());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertTrue(scoreValue.confidence() > Confidence.MIN);
    assertTrue(scoreValue.confidence() < Confidence.MAX);
    assertFalse(scoreValue.explanation().isEmpty());
  }

  @Test
  public void testWithMoreContributorsThanCommits() {
    ScoreValue scoreValue = PROJECT_ACTIVITY.calculate(values(1, 2));
    assertFalse(scoreValue.isUnknown());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());
  }
  
  @Test
  public void testWithMoreCommitsThanContributors() {
    ScoreValue scoreValue = PROJECT_ACTIVITY.calculate(values(2, 1));
    assertFalse(scoreValue.isUnknown());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertFalse(scoreValue.explanation().isEmpty());
  }

  @Test
  public void testDescription() {
    assertNotNull(PROJECT_ACTIVITY.description());
    assertFalse(PROJECT_ACTIVITY.description().isEmpty());
  }

  private static Set<Value<?>> values(int commits, int contributors) {
    return setOf(
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(commits),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(contributors));
  }
}