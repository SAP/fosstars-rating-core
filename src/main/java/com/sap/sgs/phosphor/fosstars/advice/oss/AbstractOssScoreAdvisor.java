package com.sap.sgs.phosphor.fosstars.advice.oss;

import com.sap.sgs.phosphor.fosstars.advice.Advisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;
import java.util.Optional;

/**
 * A base class for advisors for a score for open-source projects.
 * It gets advices for a feature from an {@link OssAdviceContentYamlStorage}
 * and decides if the advices are applicable for specific feature values.
 */
public abstract class AbstractOssScoreAdvisor implements Advisor {

  /**
   * A storage with advices for open-source projects.
   */
  protected final OssAdviceContentYamlStorage adviceStorage;

  /**
   * A factory that provides contexts for advices.
   */
  protected final ContextFactory contextFactory;

  /**
   * Creates a new instance.
   *
   * @param adviceStorage A storage with advices for open-source projects.
   * @param contextFactory A factory that provides contexts for advices.
   */
  protected AbstractOssScoreAdvisor(
      OssAdviceContentYamlStorage adviceStorage, ContextFactory contextFactory) {

    Objects.requireNonNull(adviceStorage, "Oh no! Advice storage is null!");
    Objects.requireNonNull(contextFactory, "Oh no! Context factory is null!");

    this.adviceStorage = adviceStorage;
    this.contextFactory = contextFactory;
  }

  /**
   * Looks for a value of a score in a rating value assigned to a subject.
   *
   * @param subject The subject that contains the rating value.
   * @param scoreClass A class of the score.
   * @return A value of the score if the rating value contains it.
   */
  Optional<ScoreValue> findScoreValueIn(Subject subject, Class<? extends Score> scoreClass) {
    if (!subject.ratingValue().isPresent()) {
      return Optional.empty();
    }

    Optional<ScoreValue> scoreValue
        = subject.ratingValue().get().scoreValue().findUsedSubScoreValue(scoreClass);

    if (!scoreValue.isPresent()
        || scoreValue.get().isUnknown() || scoreValue.get().isNotApplicable()) {

      return Optional.empty();
    }

    return scoreValue;
  }

  /**
   * A factory that provides advice contexts.
   */
  public interface ContextFactory {

    /**
     * A factory that provides empty contexts.
     */
    ContextFactory WITH_EMPTY_CONTEXT = subject -> new OssAdviceContext() {

      @Override
      public Optional<String> lgtmProjectLink() {
        return Optional.empty();
      }
    };

    /**
     * Create a context for a subject.
     *
     * @param subject The subject.
     * @return A context of the subject.
     */
    OssAdviceContext contextFor(Subject subject);
  }

}
