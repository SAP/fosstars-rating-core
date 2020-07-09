package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.Score.MIN;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade.D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import org.junit.Test;

public class StaticAnalysisScoreTest {

  private static final double DELTA = 0.01;
  
  @Test
  public void testCalculateWithFeatureValues() {
    StaticAnalysisScore score = new StaticAnalysisScore();

    ScoreValue scoreValue = score.calculate(
        WORST_LGTM_GRADE.value(D),
        USES_LGTM_CHECKS.value(true),
        LANGUAGES.value(Languages.of(JAVA)),
        USES_FIND_SEC_BUGS.value(false));

    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertSame(score, scoreValue.score());
    assertEquals(2, scoreValue.usedValues().size());
  }

  @Test
  public void testCalculateWithAllUnknown() {
    StaticAnalysisScore score = new StaticAnalysisScore();

    ScoreValue scoreValue = score.calculate(
        WORST_LGTM_GRADE.unknown(),
        USES_LGTM_CHECKS.unknown(),
        LANGUAGES.unknown(),
        USES_FIND_SEC_BUGS.unknown());

    assertTrue(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MIN, scoreValue.get(), DELTA);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    assertSame(score, scoreValue.score());
    assertEquals(2, scoreValue.usedValues().size());
  }

  @Test
  public void testCalculateWithAllNotApplicable() {
    StaticAnalysisScore score = new StaticAnalysisScore();

    ScoreValue lgtmScoreValue = new ScoreValue(new LgtmScore())
        .makeNotApplicable()
        .confidence(Confidence.MAX);
    ScoreValue findSecBugsScoreValue = new ScoreValue(new FindSecBugsScore())
        .makeNotApplicable()
        .confidence(Confidence.MAX);

    ScoreValue scoreValue = score.calculate(lgtmScoreValue, findSecBugsScoreValue);
    assertFalse(scoreValue.isUnknown());
    assertTrue(scoreValue.isNotApplicable());
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertSame(score, scoreValue.score());
    assertEquals(2, scoreValue.usedValues().size());
    assertTrue(scoreValue.usedValues().contains(lgtmScoreValue));
    assertTrue(scoreValue.usedValues().contains(findSecBugsScoreValue));
  }

  @Test
  public void testCalculateWithSubScoreValues() {
    StaticAnalysisScore score = new StaticAnalysisScore();

    ScoreValue lgtmScoreValue = new ScoreValue(new LgtmScore())
        .set(MIN)
        .confidence(Confidence.MAX);
    ScoreValue findSecBugsScoreValue = new ScoreValue(new FindSecBugsScore())
        .set(MIN)
        .confidence(Confidence.MAX);

    ScoreValue scoreValue = score.calculate(lgtmScoreValue, findSecBugsScoreValue);
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MIN, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertSame(score, scoreValue.score());
    assertEquals(2, scoreValue.usedValues().size());
    assertTrue(scoreValue.usedValues().contains(lgtmScoreValue));
    assertTrue(scoreValue.usedValues().contains(findSecBugsScoreValue));
  }

  @Test
  public void testCalculateWithFindSecBugsNotApplicable() {
    StaticAnalysisScore score = new StaticAnalysisScore();

    final double value = 5.5;

    ScoreValue lgtmScoreValue = new ScoreValue(new LgtmScore())
        .set(value)
        .confidence(Confidence.MAX);
    ScoreValue findSecBugsScoreValue = new ScoreValue(new FindSecBugsScore())
        .makeNotApplicable()
        .confidence(Confidence.MAX);

    ScoreValue scoreValue = score.calculate(lgtmScoreValue, findSecBugsScoreValue);
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(value, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    assertSame(score, scoreValue.score());
    assertEquals(2, scoreValue.usedValues().size());
    assertTrue(scoreValue.usedValues().contains(lgtmScoreValue));
    assertTrue(scoreValue.usedValues().contains(findSecBugsScoreValue));
  }

  @Test
  public void testEqualsAndHashCode() {
    StaticAnalysisScore one = new StaticAnalysisScore();
    StaticAnalysisScore two = new StaticAnalysisScore();
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testSerialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    StaticAnalysisScore score = new StaticAnalysisScore();
    StaticAnalysisScore clone = mapper.readValue(
        mapper.writeValueAsBytes(score), StaticAnalysisScore.class);
    assertEquals(score, clone);
  }

}