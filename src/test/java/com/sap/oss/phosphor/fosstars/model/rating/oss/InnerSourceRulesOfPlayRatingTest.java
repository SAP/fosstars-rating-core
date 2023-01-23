package com.sap.oss.phosphor.fosstars.model.rating.oss;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.rating.oss.InnerSourceRulesOfPlayRating.InnerSourceRulesOfPlayLabel;
import com.sap.oss.phosphor.fosstars.model.score.oss.InnerSourceRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

public class InnerSourceRulesOfPlayRatingTest {
  private InnerSourceRulesOfPlayRating testee;

  @Before
  public void setUp() throws Exception {
    this.testee = new InnerSourceRulesOfPlayRating();
  }

  @Test(expected = NullPointerException.class)
  public void testLabelNullScore() throws Exception {
    testee.label(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLabelWrongScore() throws Exception {
    ScoreValue scoreValue = Mockito.mock(ScoreValue.class);
    Score score = Mockito.mock(Score.class);
    Mockito.when(scoreValue.score()).thenReturn(score);

    testee.label(scoreValue);
  }

  @Test
  public void testLabelViolatedRules() throws Exception {
    ScoreValue scoreValue = Mockito.mock(ScoreValue.class);
    Mockito.when(scoreValue.score()).thenReturn(new InnerSourceRulesOfPlayScore());

    BooleanValue value = new BooleanValue(OssFeatures.HAS_README, false);
    Mockito.when(scoreValue.usedValues()).thenReturn(Collections.singletonList(value));

    Assert.assertEquals(InnerSourceRulesOfPlayLabel.FAILED, testee.label(scoreValue));
  }

  @Test
  public void testLabelUnclear() throws Exception {
    ScoreValue scoreValue = Mockito.mock(ScoreValue.class);
    Mockito.when(scoreValue.score()).thenReturn(new InnerSourceRulesOfPlayScore());
    Mockito.when(scoreValue.confidence()).thenReturn(Confidence.MIN);

    BooleanValue value = new BooleanValue(OssFeatures.HAS_README, true);
    Mockito.when(scoreValue.usedValues()).thenReturn(Collections.singletonList(value));

    Assert.assertEquals(InnerSourceRulesOfPlayLabel.UNCLEAR, testee.label(scoreValue));
  }

  @Test
  public void testLabelPassed() throws Exception {
    ScoreValue scoreValue = Mockito.mock(ScoreValue.class);
    Mockito.when(scoreValue.score()).thenReturn(new InnerSourceRulesOfPlayScore());
    Mockito.when(scoreValue.confidence()).thenReturn(Confidence.MAX);

    BooleanValue value = new BooleanValue(OssFeatures.HAS_README, true);
    Mockito.when(scoreValue.usedValues()).thenReturn(Collections.singletonList(value));

    Assert.assertEquals(InnerSourceRulesOfPlayLabel.PASSED, testee.label(scoreValue));
  }
}
