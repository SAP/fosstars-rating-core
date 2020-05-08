package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class FindSecBugsScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    FindSecBugsScore.Verification.createFor(new FindSecBugsScore()).run();
  }

}