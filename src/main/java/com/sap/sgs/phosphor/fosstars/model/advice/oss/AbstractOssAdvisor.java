package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import com.sap.sgs.phosphor.fosstars.model.Advice;
import com.sap.sgs.phosphor.fosstars.model.Advisor;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.advice.AdviceContent;
import com.sap.sgs.phosphor.fosstars.model.advice.SimpleAdvice;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A base class for advisors for open-source projects.
 * It gets advices for a feature from an {@link OssAdviceContentStorage}
 * and decides if the advices are applicable for specific values of the feature.
 */
public abstract class AbstractOssAdvisor implements Advisor {

  /**
   * A storage with advices for open-source projects.
   */
  final OssAdviceContentStorage adviceStorage;

  /**
   * Creates a new instance.
   *
   * @param adviceStorage A storage with advices for open-source projects.
   */
  AbstractOssAdvisor(OssAdviceContentStorage adviceStorage) {
    this.adviceStorage = Objects.requireNonNull(
        adviceStorage, "Oh no! Advice storage is null!");
  }

  /**
   * If a subject has a rating value, the method returns a list of advices
   * for all scores and values in the rating value.
   *
   * @param subject The subject.
   * @return A list of advices.
   */
  @Override
  public final List<Advice> adviseFor(Subject subject) {
    if (subject.ratingValue().isPresent()) {
      return adviseFor(
          subject.ratingValue().get(),
          (value, content) -> new SimpleAdvice(subject, value, content));
    }

    return Collections.emptyList();
  }

  /**
   * Returns a list of advices for all scores and values in a rating value.
   *
   * @param ratingValue The rating value.
   * @param builder A factory that can create an instance of {@link Advice}.
   * @return A list of advices.
   */
  abstract List<Advice> adviseFor(RatingValue ratingValue, AdviceFactory builder);

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
