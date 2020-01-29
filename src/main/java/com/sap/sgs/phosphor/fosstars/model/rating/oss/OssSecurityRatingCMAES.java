package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import com.sap.sgs.phosphor.fosstars.model.Version;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.CMAESWeightsOptimization;
import java.io.IOException;
import java.util.List;

public class OssSecurityRatingCMAES extends CMAESWeightsOptimization {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating_1_0.json";

  OssSecurityRatingCMAES(OssSecurityRating rating, List<TestVector> vectors, String path) {
    super(rating, vectors, path);
  }

  public static void main(String... args) throws IOException, VerificationFailedException {
    OssSecurityRating rating = new OssSecurityRating(Version.OSS_SECURITY_RATING_1_0);
    OssSecurityRatingVerification verification = OssSecurityRatingVerification.createFor(rating);
    new OssSecurityRatingCMAES(rating, verification.vectors(), PATH).run();
  }
}
