package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;

import com.sap.sgs.phosphor.fosstars.advice.Advice;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
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
   * @param contextFactory A factory that provides contexts for advices.
   */
  public MemorySafetyAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviseFor(
      Subject subject, List<Value> usedValues, OssAdviceContext context) {

    Optional<ScoreValue> memorySafetyTestingScoreValue
        = findSubScoreValue(subject, MemorySafetyTestingScore.class);

    if (!memorySafetyTestingScoreValue.isPresent()) {
      return Collections.emptyList();
    }

    if (memorySafetyTestingScoreValue.get().isNotApplicable()) {
      return Collections.emptyList();
    }

    return FEATURES.stream()
        .map(feature -> adviseForBooleanFeature(usedValues, feature, subject, context))
        .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
  }
}
