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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An advisor for features related to CodeQL.
 */
public class CodeqlAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advices.
   */
  public CodeqlAdvisor(ContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviseFor(
      Subject subject, List<Value> usedValues, OssAdviceContext context) {

    return Stream.of(USES_LGTM_CHECKS, USES_CODEQL_CHECKS, RUNS_CODEQL_SCANS)
        .map(feature -> adviseFor(usedValues, feature, subject, context))
        .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
  }

  /**
   * Returns advices for a feature from a list of values.
   *
   * @param values The values.
   * @param feature The feature.
   * @param subject The subject for advices.
   * @param context A context for advices.
   * @return A list of advices.
   */
  private List<? extends Advice> adviseFor(
      List<Value> values, Feature<Boolean> feature, Subject subject, OssAdviceContext context) {

    return findValue(values, feature)
        .filter(CodeqlAdvisor::knownFalseValue)
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
