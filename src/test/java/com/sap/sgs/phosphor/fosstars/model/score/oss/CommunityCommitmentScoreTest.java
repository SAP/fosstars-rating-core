package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Set;
import org.junit.Test;

public class CommunityCommitmentScoreTest {

  private static final CommunityCommitmentScore COMMUNITY_COMMITMENT
      = new CommunityCommitmentScore();

  @Test(expected = IllegalArgumentException.class)
  public void noValueForCompanySupport() {
    COMMUNITY_COMMITMENT.calculate(IS_APACHE.value(true), IS_ECLIPSE.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noValueForApache() {
    COMMUNITY_COMMITMENT.calculate(SUPPORTED_BY_COMPANY.value(false), IS_ECLIPSE.value(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noValueForEclipse() {
    COMMUNITY_COMMITMENT.calculate(SUPPORTED_BY_COMPANY.value(true), IS_APACHE.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void bothApacheAndEclipse() {
    COMMUNITY_COMMITMENT
        .calculate(SUPPORTED_BY_COMPANY.value(false), IS_APACHE.value(true), IS_ECLIPSE
        .value(true));
  }

  @Test
  public void calculate() {
    assertScore(Score.MIN, COMMUNITY_COMMITMENT.calculate(values(false, false, false)));
    assertScore(7.0, COMMUNITY_COMMITMENT.calculate(values(false, true, false)));
    assertScore(7.0, COMMUNITY_COMMITMENT.calculate(values(false, false, true)));
    assertScore(8.0, COMMUNITY_COMMITMENT.calculate(values(true, false, false)));
    assertScore(Score.MAX, COMMUNITY_COMMITMENT.calculate(values(true, true, false)));
    assertScore(Score.MAX, COMMUNITY_COMMITMENT.calculate(values(true, false, true)));
  }

  private static Set<Value> values(boolean company, boolean apache, boolean eclipse) {
    return setOf(
        SUPPORTED_BY_COMPANY.value(company), IS_APACHE.value(apache), IS_ECLIPSE.value(eclipse));
  }

}