package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.PROJECT_SECURITY_AWARENESS;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import org.junit.Test;

public class ProjectSecurityAwarenessScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSecurityAdvisories() {
    PROJECT_SECURITY_AWARENESS.calculate(HAS_SECURITY_TEAM.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSecurityTeam() {
    PROJECT_SECURITY_AWARENESS.calculate(HAS_SECURITY_POLICY.value(false));
  }

  @Test
  public void test() {
    assertScore(Score.MIN,
        PROJECT_SECURITY_AWARENESS.calculate(
            UnknownValue.of(HAS_SECURITY_TEAM),
            UnknownValue.of(HAS_SECURITY_POLICY)),
        0.0);

    assertScore(Score.MIN,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(false),
            HAS_SECURITY_POLICY.value(false)),
        0.0);

    assertScore(Score.MIN,
        PROJECT_SECURITY_AWARENESS.calculate(
            UnknownValue.of(HAS_SECURITY_TEAM),
            HAS_SECURITY_POLICY.value(false)),
        0.0);

    assertScore(Score.MIN,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(false),
            UnknownValue.of(HAS_SECURITY_POLICY)),
        0.0);

    assertScore(5.0,
        PROJECT_SECURITY_AWARENESS.calculate(
            UnknownValue.of(HAS_SECURITY_TEAM),
            HAS_SECURITY_POLICY.value(true)),
        0.0);

    assertScore(8.0,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(true),
            UnknownValue.of(HAS_SECURITY_POLICY)),
        0.0);

    assertScore(8.0,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(true),
            HAS_SECURITY_POLICY.value(false)),
        0.0);

    assertScore(5.0,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(false),
            HAS_SECURITY_POLICY.value(true)),
        0.0);

    assertScore(Score.MAX,
        PROJECT_SECURITY_AWARENESS.calculate(
            HAS_SECURITY_TEAM.value(true),
            HAS_SECURITY_POLICY.value(true)),
        0.0);
  }

}