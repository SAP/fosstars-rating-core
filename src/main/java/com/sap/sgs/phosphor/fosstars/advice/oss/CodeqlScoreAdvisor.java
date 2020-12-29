package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.advice.Advice;
import com.sap.sgs.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.oss.CodeqlScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An advisor for {@link CodeqlScore}.
 */
public class CodeqlScoreAdvisor extends AbstractOssScoreAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advices.
   */
  public CodeqlScoreAdvisor(ContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  public List<Advice> adviseFor(Subject subject) {
    Optional<ScoreValue> scoreValue = findScoreValueIn(subject, CodeqlScore.class);
    if (scoreValue.isPresent()) {
      OssAdviceContext context = contextFactory.contextFor(subject);
      List<Advice> advices = new ArrayList<>();
      advices.addAll(adviseFor(scoreValue.get(), USES_LGTM_CHECKS, subject, context));
      advices.addAll(adviseFor(scoreValue.get(), USES_CODEQL_CHECKS, subject, context));
      advices.addAll(adviseFor(scoreValue.get(), RUNS_CODEQL_SCANS, subject, context));
      return advices;
    }

    return Collections.emptyList();
  }

  /**
   * Returns a list of advices for a feature from a score value.
   *
   * @param scoreValue The score value.
   * @param feature The feature.
   * @param subject The subject for advices.
   * @param context A context for advices.
   * @return A list of advices.
   */
  private List<? extends Advice> adviseFor(
      ScoreValue scoreValue, Feature<Boolean> feature, Subject subject, OssAdviceContext context) {

    return findValue(scoreValue.usedFeatureValues(), feature)
        .filter(CodeqlScoreAdvisor::knownFalseValue)
        .map(value -> adviceStorage.advicesFor(value.feature(), context)
            .stream()
            .map(content -> new SimpleAdvice(subject, value, content))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
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
