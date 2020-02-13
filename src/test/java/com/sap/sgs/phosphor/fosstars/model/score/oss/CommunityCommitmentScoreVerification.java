package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class CommunityCommitmentScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    CommunityCommitmentScore.Verification.createFor(OssScores.COMMUNITY_COMMITMENT).run();
  }

}
