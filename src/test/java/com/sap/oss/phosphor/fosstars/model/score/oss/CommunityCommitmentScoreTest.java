package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;
import org.junit.Test;

public class CommunityCommitmentScoreTest {

  private static final CommunityCommitmentScore SCORE = new CommunityCommitmentScore();

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutValueForCompanySupport() {
    SCORE.calculate(IS_APACHE.value(true), IS_ECLIPSE.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutValueForApache() {
    SCORE.calculate(SUPPORTED_BY_COMPANY.value(false), IS_ECLIPSE.value(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutValueForEclipse() {
    SCORE.calculate(SUPPORTED_BY_COMPANY.value(true), IS_APACHE.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithBothApacheAndEclipse() {
    SCORE.calculate(
        SUPPORTED_BY_COMPANY.value(false), IS_APACHE.value(true), IS_ECLIPSE.value(true));
  }

  @Test
  public void testCalculate() {
    assertScore(Score.MIN, SCORE, values(false, false, false));
    assertScore(7.0, SCORE, values(false, true, false));
    assertScore(7.0, SCORE, values(false, false, true));
    assertScore(8.0, SCORE, values(true, false, false));
    assertScore(Score.MAX, SCORE, values(true, true, false));
    assertScore(Score.MAX, SCORE, values(true, false, true));
  }

  @Test
  public void testCalculateWithAllUnknown() {
    ScoreValue scoreValue = SCORE.calculate(Utils.allUnknown(SCORE.allFeatures()));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
  }

  private static Set<Value<?>> values(boolean company, boolean apache, boolean eclipse) {
    return setOf(
        SUPPORTED_BY_COMPANY.value(company), IS_APACHE.value(apache), IS_ECLIPSE.value(eclipse));
  }

}