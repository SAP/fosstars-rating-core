package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade.A_PLUS;

import com.sap.sgs.phosphor.fosstars.advice.Advice;
import com.sap.sgs.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Subject;
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
public class LgtmScoreAdvisor extends AbstractOssScoreAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advices.
   */
  public LgtmScoreAdvisor(ContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  public List<Advice> adviseFor(Subject subject) {
    OssAdviceContext context = contextFactory.contextFor(subject);
    return findScoreValueIn(subject, LgtmScore.class)
        .map(scoreValue -> advicesFor(scoreValue, subject, context))
        .orElse(Collections.emptyList());
  }

  /**
   * Returns a list of advices for
   * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}
   * feature from a score value.
   *
   * @param scoreValue The score value.
   * @param subject The subject for advices.
   * @param context A context for advices.
   * @return A list of advices.
   */
  private List<Advice> advicesFor(
      ScoreValue scoreValue, Subject subject, OssAdviceContext context) {

    return findValue(scoreValue.usedFeatureValues(), WORST_LGTM_GRADE)
        .filter(LgtmScoreAdvisor::isKnown)
        .filter(LgtmScoreAdvisor::notTheBest)
        .map(value -> adviceStorage.advicesFor(value.feature(), context)
            .stream()
            .map(content -> new SimpleAdvice(subject, value, content))
            .map(Advice.class::cast)
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
