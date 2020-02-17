package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class OssSecurityScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    OssSecurityScore.Verification.createFor(OssScores.SECURITY_SCORE).run();
  }

}
