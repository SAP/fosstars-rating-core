package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class ProjectSecurityAwarenessScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    ProjectSecurityAwarenessScore.Verification.createFor(
        OssScores.PROJECT_SECURITY_AWARENESS).run();
  }

}
