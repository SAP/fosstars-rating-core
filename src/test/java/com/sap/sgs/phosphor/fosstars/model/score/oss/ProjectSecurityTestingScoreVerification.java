package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class ProjectSecurityTestingScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    ProjectSecurityTestingScore.Verification.createFor(
        OssScores.PROJECT_SECURITY_TESTING).run();
  }

}
