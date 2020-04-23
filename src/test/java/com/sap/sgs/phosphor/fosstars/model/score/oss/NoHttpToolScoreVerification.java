package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class NoHttpToolScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    NoHttpToolScore.Verification.createFor(new NoHttpToolScore()).run();
  }

}