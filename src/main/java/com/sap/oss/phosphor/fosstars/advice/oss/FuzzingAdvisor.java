package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * An advisor for features related fuzzing.
 */
public class FuzzingAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advices.
   */
  public FuzzingAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviseFor(
      Subject subject, List<Value> usedValues, OssAdviceContext context) {

    Optional<ScoreValue> fuzzingScoreValue = findSubScoreValue(subject, FuzzingScore.class);

    if (!fuzzingScoreValue.isPresent()) {
      return Collections.emptyList();
    }

    if (fuzzingScoreValue.get().isNotApplicable()) {
      return Collections.emptyList();
    }

    return Stream.of(FUZZED_IN_OSS_FUZZ)
        .map(feature -> adviseForBooleanFeature(usedValues, feature, subject, context))
        .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
  }
}
