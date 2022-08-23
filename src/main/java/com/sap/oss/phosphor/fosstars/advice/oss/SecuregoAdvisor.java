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
import java.util.Optional;

/**
 * An advisor for features related to Securego.
 */
public class SecuregoAdvisor extends AbstractOssAdvisor {

  /**
   * A list of features that the advisor advices if not used.
   */
  private static final List<Feature<Boolean>> POSITIVE_FEATURES
      = asList(RUNS_SECUREGO_SCANS, USES_SECUREGO_SCAN_CHECKS);

  /**
   * A feature that the advisor advices if used.
   */
  private static final Feature<Boolean> NEGATIVE_FEATURE = USES_SECUREGO_WITH_RULES;

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public SecuregoAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  /**
   * Checks if a boolean value is known and True.
   *
   * @param value The value to be checked.
   * @return True if the value is known and True, false otherwise.
   */
  protected static boolean knownTrueValue(Value<?> value) {
    return !value.isUnknown() && Boolean.TRUE.equals(value.get());
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    List<Advice> advice = new ArrayList<>();
    for (Feature<Boolean> feature : POSITIVE_FEATURES) {
      advice.addAll(adviceForBooleanFeature(usedValues, feature, subject, context));
    }
    advice.addAll(adviceForFeature(usedValues, NEGATIVE_FEATURE, subject, context,
        SecuregoAdvisor::knownTrueValue));

    return advice;
  }
}