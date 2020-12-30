package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.advice.Advice;
import com.sap.sgs.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An advisor for
 * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_SECURITY_POLICY} feature.
 */
public class SecurityPolicyAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advices.
   */
  public SecurityPolicyAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviseFor(
      Subject subject, List<Value> usedValues, OssAdviceContext context) {

    return findValue(usedValues, HAS_SECURITY_POLICY)
        .filter(AbstractOssAdvisor::knownFalseValue)
        .map(value -> adviceStorage.advicesFor(value.feature(), context)
            .stream()
            .map(content -> new SimpleAdvice(subject, value, content))
            .map(Advice.class::cast)
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
