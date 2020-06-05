package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class StaticAnalysisScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    StaticAnalysisScore.Verification.createFor(new StaticAnalysisScore()).run();
  }

}
