package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.util.Set;
import org.junit.Test;

public class ProjectActivityScoreTest {

  private static final ProjectActivityScore PROJECT_ACTIVITY = new ProjectActivityScore();

  @Test
  public void calculate() {
    assertScore(Score.MIN, PROJECT_ACTIVITY, setOf(
            UnknownValue.of(NUMBER_OF_COMMITS_LAST_THREE_MONTHS),
            UnknownValue.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS)));

    assertScore(Score.MIN, PROJECT_ACTIVITY, setOf(
            NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(0),
            UnknownValue.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS)));

    assertScore(Score.MIN, PROJECT_ACTIVITY, setOf(
            UnknownValue.of(NUMBER_OF_COMMITS_LAST_THREE_MONTHS),
            NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(0)));

    assertScore(Score.MIN, PROJECT_ACTIVITY, values(0, 0));

    // I wish all open-source projects were like that
    assertScore(Score.MAX, PROJECT_ACTIVITY, values(10000, 500));

    // or, even like that
    assertScore(Score.MAX, PROJECT_ACTIVITY, values(Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void commitsWithoutContributors() {
    PROJECT_ACTIVITY.calculate(values(1, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void contributorsWithoutCommits() {
    PROJECT_ACTIVITY.calculate(values(0, 1));
  }

  @Test
  public void moreContributorsThanCommits() {
    PROJECT_ACTIVITY.calculate(values(1, 2));
  }
  
  @Test
  public void moreCommitsThanContributors() {
    PROJECT_ACTIVITY.calculate(values(2, 1));
  }

  @Test
  public void description() {
    assertNotNull(PROJECT_ACTIVITY.description());
    assertFalse(PROJECT_ACTIVITY.description().isEmpty());
  }

  private static Set<Value> values(int commits, int contributors) {
    return setOf(
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(commits),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(contributors));
  }
}