package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An advisor for {@link LgtmScore}.
 */
public class LgtmScoreAdvisor extends AbstractOssScoreAdvisor {

  /**
   * Create a new advisor.
   */
  public LgtmScoreAdvisor() {
    super(OssAdviceContentStorage.DEFAULT, LgtmScore.class);
  }

  @Override
  List<Advice> adviseFor(ScoreValue scoreValue, AdviceFactory factory) {
    return findValue(scoreValue.usedFeatureValues(), WORST_LGTM_GRADE)
        .filter(worseLgtmGrade -> !worseLgtmGrade.isUnknown())
        .filter(worseLgtmGrade -> worseLgtmGrade.get() != LgtmGrade.A_PLUS)
        .map(value -> adviceStorage.advicesFor(value.feature())
            .stream()
            .map(content -> factory.createAdvice(value, content))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
