package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An advisor for features related to memory sanitizers.
 */
public class MemorySafetyAdvisor extends AbstractOssAdvisor {

  /**
   * A list of features that are covered by the advisor.
   */
  private static final List<Feature<Boolean>> FEATURES = Arrays.asList(
      USES_ADDRESS_SANITIZER, USES_MEMORY_SANITIZER, USES_UNDEFINED_BEHAVIOR_SANITIZER);

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public MemorySafetyAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    Optional<ScoreValue> memorySafetyTestingScoreValue
        = findSubScoreValue(subject, MemorySafetyTestingScore.class);

    if (!memorySafetyTestingScoreValue.isPresent()) {
      return Collections.emptyList();
    }

    if (memorySafetyTestingScoreValue.get().isNotApplicable()) {
      return Collections.emptyList();
    }

    List<Advice> advice = new ArrayList<>();
    for (Feature<Boolean> feature : FEATURES) {
      advice.addAll(adviceForBooleanFeature(usedValues, feature, subject, context));
    }

    return advice;
  }
}
