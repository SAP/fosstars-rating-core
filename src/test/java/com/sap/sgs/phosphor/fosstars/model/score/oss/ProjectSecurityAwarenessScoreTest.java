package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ProjectSecurityAwarenessScoreTest {

  private static final ProjectSecurityAwarenessScore PROJECT_SECURITY_AWARENESS
      = new ProjectSecurityAwarenessScore();

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSecurityAdvisories() {
    PROJECT_SECURITY_AWARENESS.calculate(
        HAS_SECURITY_TEAM.value(false),
        USES_VERIFIED_SIGNED_COMMITS.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSecurityTeam() {
    PROJECT_SECURITY_AWARENESS.calculate(
        HAS_SECURITY_POLICY.value(false),
        USES_VERIFIED_SIGNED_COMMITS.value(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSignedCommits() {
    PROJECT_SECURITY_AWARENESS.calculate(
        HAS_SECURITY_POLICY.value(false),
        HAS_SECURITY_TEAM.value(false));
  }

  @Test
  public void description() {
    assertNotNull(PROJECT_SECURITY_AWARENESS.description());
    assertFalse(PROJECT_SECURITY_AWARENESS.description().isEmpty());
  }

}