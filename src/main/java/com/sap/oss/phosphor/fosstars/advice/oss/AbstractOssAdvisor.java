package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.util.Collections.emptyList;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.AdviceContext;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A base class for advisors for ratings for open-source projects.
 * It gets advice for a feature from an {@link OssAdviceContentYamlStorage}
 * and decides if the advice are applicable for specific feature values.
 */
public abstract class AbstractOssAdvisor implements Advisor {

  /**
   * A storage with advice for open-source projects.
   */
  protected final OssAdviceContentYamlStorage adviceStorage;

  /**
   * A factory that provides contexts for advice.
   */
  protected final OssAdviceContextFactory contextFactory;

  /**
   * Creates a new instance.
   *
   * @param adviceStorage A storage with advice for open-source projects.
   * @param contextFactory A factory that provides contexts for advice.
   */
  protected AbstractOssAdvisor(
      OssAdviceContentYamlStorage adviceStorage, OssAdviceContextFactory contextFactory) {

    Objects.requireNonNull(adviceStorage, "Oh no! Advice storage is null!");
    Objects.requireNonNull(contextFactory, "Oh no! Context factory is null!");

    this.adviceStorage = adviceStorage;
    this.contextFactory = contextFactory;
  }

  @Override
  public final List<Advice> adviceFor(Subject subject) throws MalformedURLException {
    if (!subject.ratingValue().isPresent()) {
      return emptyList();
    }

    return adviceFor(
        subject,
        subject.ratingValue().get().scoreValue().usedFeatureValues(),
        contextFactory.contextFor(subject));
  }

  /**
   * Returns a list of advice for a subject.
   *
   * @param subject The subject.
   * @param usedValues A list of value that were used to calculate a rating for the subject.
   * @param context An advice context.
   * @return A list of advice.
   * @throws MalformedURLException If the method couldn't parse URLs.
   */
  protected abstract List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException;

  /**
   * Returns advice for a feature if it is present in a list of values.
   *
   * @param values The values.
   * @param feature The feature.
   * @param subject The subject for advice.
   * @param context A context for advice.
   * @return A list of advice.
   * @throws MalformedURLException If the method couldn't parse URLs.
   */
  protected List<Advice> adviceForBooleanFeature(
      List<Value<?>> values, Feature<Boolean> feature, Subject subject, OssAdviceContext context)
      throws MalformedURLException {

    return adviceForFeature(values, feature, subject, context, AbstractOssAdvisor::knownFalseValue);
  }

  /**
   * Returns advice for a feature if it is present in a list of values.
   *
   * @param values The values.
   * @param feature The feature.
   * @param subject The subject for advice.
   * @param context A context for advice.
   * @param criteria A condition that shows whether a value needs an advice or not.
   * @param <T> Type of data held by the feature.
   * @return A list of advice.
   * @throws MalformedURLException If the method couldn't parse URLs.
   */
  protected <T> List<Advice> adviceForFeature(List<Value<?>> values, Feature<T> feature,
      Subject subject, AdviceContext context, Predicate<Value<T>> criteria)
      throws MalformedURLException {

    Optional<Value<T>> value = findValue(values, feature).filter(criteria);
    if (!value.isPresent()) {
      return emptyList();
    }

    return adviceStorage.adviceFor(value.get().feature(), context).stream()
        .map(content -> new SimpleAdvice(subject, value.get(), content))
        .map(Advice.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Checks if a boolean value is known and false.
   *
   * @param value The value to be checked.
   * @return True if the value is known and false, false otherwise.
   */
  protected static boolean knownFalseValue(Value<?> value) {
    return !value.isUnknown() && Boolean.FALSE.equals(value.get());
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
