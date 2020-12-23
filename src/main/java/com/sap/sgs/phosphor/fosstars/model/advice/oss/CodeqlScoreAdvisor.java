package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.oss.CodeqlScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An advisor for {@link CodeqlScore}.
 */
public class CodeqlScoreAdvisor extends AbstractOssScoreAdvisor<CodeqlScore> {

  /**
   * Create a new advisor.
   */
  public CodeqlScoreAdvisor() {
    super(OssAdviceContentStorage.DEFAULT, CodeqlScore.class);
  }

  @Override
  List<Advice> adviseFor(ScoreValue scoreValue, AdviceFactory factory) {
    List<Advice> advices = new ArrayList<>();
    advices.addAll(adviseFor(scoreValue, USES_LGTM_CHECKS, factory));
    advices.addAll(adviseFor(scoreValue, USES_CODEQL_CHECKS, factory));
    advices.addAll(adviseFor(scoreValue, RUNS_CODEQL_SCANS, factory));

    return advices;
  }

  /**
   * Returns a list of advices for a feature from a score value.
   *
   * @param scoreValue The score value.
   * @param feature The feature.
   * @param factory A factory that can create an instance of {@link Advice}.
   * @return A list of advices.
   */
  private List<Advice> adviseFor(
      ScoreValue scoreValue, Feature<Boolean> feature, AdviceFactory factory) {

    return findValue(scoreValue.usedFeatureValues(), feature)
        .filter(CodeqlScoreAdvisor::knownFalseValue)
        .map(value -> adviceStorage.advicesFor(value.feature())
            .stream()
            .map(content -> factory.createAdvice(value, content))
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
