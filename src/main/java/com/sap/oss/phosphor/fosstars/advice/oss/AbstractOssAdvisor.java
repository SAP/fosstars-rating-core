package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A base class for advisors for ratings for open-source projects.
 * It gets advices for a feature from an {@link OssAdviceContentYamlStorage}
 * and decides if the advices are applicable for specific feature values.
 */
public abstract class AbstractOssAdvisor implements Advisor {

  /**
   * A storage with advices for open-source projects.
   */
  protected final OssAdviceContentYamlStorage adviceStorage;

  /**
   * A factory that provides contexts for advices.
   */
  protected final OssAdviceContextFactory contextFactory;

  /**
   * Creates a new instance.
   *
   * @param adviceStorage A storage with advices for open-source projects.
   * @param contextFactory A factory that provides contexts for advices.
   */
  protected AbstractOssAdvisor(
      OssAdviceContentYamlStorage adviceStorage, OssAdviceContextFactory contextFactory) {

    Objects.requireNonNull(adviceStorage, "Oh no! Advice storage is null!");
    Objects.requireNonNull(contextFactory, "Oh no! Context factory is null!");

    this.adviceStorage = adviceStorage;
    this.contextFactory = contextFactory;
  }

  @Override
  public final List<Advice> adviseFor(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return Collections.emptyList();
    }

    return adviseFor(
        subject,
        subject.ratingValue().get().scoreValue().usedFeatureValues(),
        contextFactory.contextFor(subject));
  }

  /**
   * Returns a list of advices for a subject.
   *
   * @param subject The subject.
   * @param usedValues A list of value that were used to calculate a rating for the subject.
   * @param context An advice context.
   * @return A list of advices.
   */
  protected abstract List<Advice> adviseFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context);

  /**
   * Returns advices for a boolean feature from a list of values.
   *
   * @param values The values.
   * @param feature The feature.
   * @param subject The subject for advices.
   * @param context A context for advices.
   * @return A list of advices.
   */
  protected List<Advice> adviseForBooleanFeature(
      List<Value<?>> values, Feature<Boolean> feature, Subject subject, OssAdviceContext context) {

    return findValue(values, feature)
        .filter(AbstractOssAdvisor::knownFalseValue)
        .map(value -> adviceStorage.advicesFor(value.feature(), context)
            .stream()
            .map(content -> new SimpleAdvice(subject, value, content))
            .map(Advice.class::cast)
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  /**
   * Checks if a boolean value is known and false.
   *
   * @param value The value to be checked.
   * @return True if the value is known and false, false otherwise.
   */
  protected static boolean knownFalseValue(Value<Boolean> value) {
    return !value.isUnknown() && !value.get();
  }

  /**
   * Looks for a sub-score value in a rating value assigned to a subject.
   *
   * @param subject The subject.
   * @param subScoreClass A class of the sub-score.
   * @return A sub-score value if present.
   */
  protected static Optional<ScoreValue> findSubScoreValue(
      Subject subject, Class<? extends Score> subScoreClass) {

    if (!subject.ratingValue().isPresent()) {
      return Optional.empty();
    }

    return findSubScoreValue(subject.ratingValue().get().scoreValue(), subScoreClass);
  }

  /**
   * Looks for a sub-score value in a score value.
   *
   * @param scoreValue The score value.
   * @param subScoreClass A class of the sub-score.
   * @return A sub-score value if present.
   */
  private static Optional<ScoreValue> findSubScoreValue(
      ScoreValue scoreValue, Class<? extends Score> subScoreClass) {

    if (scoreValue.score().getClass().equals(subScoreClass)) {
      return Optional.of(scoreValue);
    }

    for (Value<?> usedValue : scoreValue.usedValues()) {
      if (usedValue instanceof ScoreValue) {
        Optional<ScoreValue> result = findSubScoreValue((ScoreValue) usedValue, subScoreClass);
        if (result.isPresent()) {
          return result;
        }
      }
    }

    return Optional.empty();
  }

  /**
   * A factory that provides advice contexts for open-source projects.
   */
  public interface OssAdviceContextFactory {

    /**
     * A factory that provides empty contexts.
     */
    OssAdviceContextFactory WITH_EMPTY_CONTEXT = subject -> EMPTY_OSS_CONTEXT;

    /**
     * Create a context for a subject.
     *
     * @param subject The subject.
     * @return A context of the subject.
     */
    OssAdviceContext contextFor(Subject subject);
  }

}
