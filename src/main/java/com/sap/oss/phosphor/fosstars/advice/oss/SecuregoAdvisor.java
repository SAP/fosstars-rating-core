package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_SECUREGO_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_WITH_RULES;
import static java.util.Arrays.asList;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * An advisor for features related to Securego.
 */
public class SecuregoAdvisor extends AbstractOssAdvisor {

  /**
   * A list of features that the advisor supports.
   */
  private static final List<Feature<Boolean>> FEATURES
      = asList(RUNS_SECUREGO_SCANS, USES_SECUREGO_SCAN_CHECKS, USES_SECUREGO_WITH_RULES);

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public SecuregoAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    List<Advice> advice = new ArrayList<>();
    for (Feature<Boolean> feature : FEATURES) {
      advice.addAll(adviceForBooleanFeature(usedValues, feature, subject, context));
    }

    return advice;
  }
}