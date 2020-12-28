package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade.A_PLUS;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An advisor for {@link LgtmScore}.
 */
public class LgtmScoreAdvisor extends AbstractOssScoreAdvisor<LgtmScore> {

  /**
   * Create a new advisor.
   */
  public LgtmScoreAdvisor() {
    super(OssAdviceContentStorage.DEFAULT, LgtmScore.class);
  }

  @Override
  List<Advice> adviseFor(ScoreValue scoreValue, AdviceFactory factory) {
    return findValue(scoreValue.usedFeatureValues(), WORST_LGTM_GRADE)
        .filter(LgtmScoreAdvisor::isKnown)
        .filter(LgtmScoreAdvisor::notTheBest)
        .map(value -> adviceStorage.advicesFor(value.feature())
            .stream()
            .map(content -> factory.createAdvice(value, content))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  /**
   * Checks if a value is known.
   *
   * @param value The value to be checked.
   * @return True if the value is known, false otherwise.
   */
  private static boolean isKnown(Value<LgtmGrade> value) {
    return !value.isUnknown();
  }

  /**
   * Check if an LGTM grade from a value is not the best one.
   *
   * @param value THe value to be checked.
   * @return True if the LGTM grade is not the best one, false otherwise.
   */
  private static boolean notTheBest(Value<LgtmGrade> value) {
    return value.get() != A_PLUS;
  }
}
