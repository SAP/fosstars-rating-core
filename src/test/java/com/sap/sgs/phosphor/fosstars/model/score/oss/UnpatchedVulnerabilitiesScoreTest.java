package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.other.Utils;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import org.junit.Test;

public class UnpatchedVulnerabilitiesScoreTest {

  @Test
  public void calculateForAllUnknown() {
    Score score = new UnpatchedVulnerabilitiesScore();
    assertScore(Score.MIN, score, Utils.allUnknown(score.allFeatures()));
  }

  @Test
  public void calculate() {
    Score score = new UnpatchedVulnerabilitiesScore();
    assertScore(Score.INTERVAL, score, setOf(VULNERABILITIES.value(new Vulnerabilities())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutVulnerabilities() {
    assertScore(Score.INTERVAL, new UnpatchedVulnerabilitiesScore(), setOf());
  }
}