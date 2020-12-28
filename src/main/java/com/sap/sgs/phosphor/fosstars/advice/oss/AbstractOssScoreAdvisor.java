package com.sap.sgs.phosphor.fosstars.advice.oss;

import com.sap.sgs.phosphor.fosstars.advice.AdviceContent;
import com.sap.sgs.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Advisor;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A base class for advisors for a score for open-source projects.
 * It gets advices for a feature from an {@link OssAdviceContentStorage}
 * and decides if the advices are applicable for specific feature values.
 *
 * @param <T> A type of the score.
 */
public abstract class AbstractOssScoreAdvisor<T extends Score> implements Advisor {

  /**
   * A storage with advices for open-source projects.
   */
  final OssAdviceContentStorage adviceStorage;

  /**
   * A type of the score.
   */
  final Class<T> scoreClass;

  /**
   * Creates a new instance.
   *
   * @param adviceStorage A storage with advices for open-source projects.
   * @param scoreClass A type of the score.
   */
  AbstractOssScoreAdvisor(OssAdviceContentStorage adviceStorage, Class<T> scoreClass) {
    Objects.requireNonNull(adviceStorage, "Oh no! Advice storage is null!");
    Objects.requireNonNull(scoreClass, "Oh no! Score class is null!");

    this.adviceStorage = adviceStorage;
    this.scoreClass = scoreClass;
  }

  /**
   * First, the method checks if the subject has a rating value. Then, it searches for a value
   * of the score in the rating value. Finally, it looks for advices for the feature values
   * that contributed to the score value.
   *
   * @param subject The subject.
   * @return A list of advices.
   */
  @Override
  public final List<Advice> adviseFor(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return Collections.emptyList();
    }

    Optional<ScoreValue> scoreValue
        = subject.ratingValue().get().scoreValue().findUsedSubScoreValue(scoreClass);

    if (!scoreValue.isPresent()
        || scoreValue.get().isUnknown() || scoreValue.get().isNotApplicable()) {

      return Collections.emptyList();
    }

    return adviseFor(
        scoreValue.get(),
        (value, content) -> new SimpleAdvice(subject, value, content));
  }

  /**
   * Returns a list of advices for feature values that contributed to a specified score value.
   *
   * @param scoreValue The score value.
   * @param builder A factory that can create an instance of {@link Advice}.
   * @return A list of advices.
   */
  abstract List<Advice> adviseFor(ScoreValue scoreValue, AdviceFactory builder);

  /**
   * A factory that can create an instance of {@link Advice}.
   */
  interface AdviceFactory {

    /**
     * Create an instance of {@link Advice} for a value.
     *
     * @param value The value.
     * @param content A content of the advice.
     * @return An instance of {@link Advice}.
     */
    Advice createAdvice(Value<?> value, AdviceContent content);
  }

}
