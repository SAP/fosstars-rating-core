package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.LgtmGrade.A_PLUS;
import static java.util.Collections.emptyList;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An advisor for features related to LGTM grade and findings.
 */
public class LgtmAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public LgtmAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    Optional<Value<LgtmGrade>> value = findValue(usedValues, WORST_LGTM_GRADE)
        .filter(LgtmAdvisor::isKnown)
        .filter(LgtmAdvisor::notTheBest);

    if (!value.isPresent()) {
      return emptyList();
    }

    return adviceStorage.adviceFor(value.get().feature(), context)
        .stream()
        .map(content -> new SimpleAdvice(subject, value.get(), content))
        .map(Advice.class::cast)
        .collect(Collectors.toList());
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
