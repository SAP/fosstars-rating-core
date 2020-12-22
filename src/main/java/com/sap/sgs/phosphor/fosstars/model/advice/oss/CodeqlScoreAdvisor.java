package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An advisor for {@link com.sap.sgs.phosphor.fosstars.model.score.oss.CodeqlScore}.
 */
public class CodeqlScoreAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   */
  public CodeqlScoreAdvisor() {
    super(OssAdviceContentStorage.DEFAULT);
  }

  @Override
  List<Advice> adviseFor(RatingValue ratingValue, AdviceFactory builder) {
    List<Advice> advices = new ArrayList<>();

    findValue(ratingValue.scoreValue().usedFeatureValues(), USES_LGTM_CHECKS)
        .filter(CodeqlScoreAdvisor::knownFalseValue)
        .map(value -> adviceStorage.advicesFor(value.feature())
            .stream()
            .map(content -> builder.createAdvice(value, content))
            .collect(Collectors.toList()))
        .ifPresent(advices::addAll);

    return advices;
  }

  /**
   * Checks if a boolean value is known and false.
   *
   * @param value The value to be checked.
   * @return True if the value is known and false, false otherwise.
   */
  private static boolean knownFalseValue(Value<Boolean> value) {
    return !value.isUnknown() && !value.get();
  }
}
