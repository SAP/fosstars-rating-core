package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.InnerSourceRulesOfPlayRating;
import java.util.Set;

public class InnerSourceRulesOfPlayRatingHandler extends AbstractHandler {

  public InnerSourceRulesOfPlayRatingHandler() {
    super(RatingRepository.INSTANCE.rating(InnerSourceRulesOfPlayRating.class));
  }

  @Override
  public String supportedRatingName() {
    return "innersource-rules-of-play";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return Utils.setOf("--url");
  }

  @Override
  void processMaven(String coordinates) {
    throw new UnsupportedOperationException("Oops! I don't support GAV coordinates!");
  }
}
