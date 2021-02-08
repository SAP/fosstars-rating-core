package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.List;

/**
 * An advisor for
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_SECURITY_POLICY} feature.
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
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context) {

    return adviseForBooleanFeature(usedValues, HAS_SECURITY_POLICY, subject, context);
  }
}
