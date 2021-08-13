package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import java.util.Set;

/**
 * This handler calculates {@link SecurityRiskIntroducedByOss}.
 */
public class SecurityRiskIntroducedByOssHandler extends AbstractHandler {

  /**
   * Initializes a handler.
   */
  public SecurityRiskIntroducedByOssHandler() {
    super(RatingRepository.INSTANCE.rating(SecurityRiskIntroducedByOss.class));
  }

  @Override
  public String supportedRatingName() {
    return "security-risk-from-oss";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--url");
  }
}
