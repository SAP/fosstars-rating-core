package com.sap.oss.phosphor.fosstars.model.rating.example;

import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_SCORE_EXAMPLE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Tunable;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.example.SecurityScoreExample;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.List;
import java.util.Objects;

/**
 * This is a sample implementation of a security rating. Only for demo purposes.
 * The rating is based on SecurityScoreExample.
 */
public class SecurityRatingExample extends AbstractRating implements Tunable {

  @Override
  public List<? extends Parameter> parameters() {
    return score().parameters();
  }

  @Override
  @JsonIgnore
  public boolean isImmutable() {
    return score().isImmutable();
  }

  @Override
  public void makeImmutable() {
    score().makeImmutable();
  }

  public enum SecurityLabelExample implements Label {
    AWFUL, OKAY, AWESOME
  }

  /**
   * Initializes a security rating with SecurityScoreExample.
   */
  SecurityRatingExample() {
    super("Security rating (example)", SECURITY_SCORE_EXAMPLE);
  }

  @Override
  public SecurityScoreExample score() {
    return (SecurityScoreExample) super.score();
  }

  /**
   * Implements a mapping from a score to a label.
   */
  @Override
  protected SecurityLabelExample label(ScoreValue scoreValue) {
    Objects.requireNonNull(scoreValue, "Oh no! Score value is null!");

    double score = scoreValue.get();

    if (score < 3.0) {
      return SecurityLabelExample.AWFUL;
    }

    if (score < 8.0) {
      return SecurityLabelExample.OKAY;
    }

    return SecurityLabelExample.AWESOME;
  }

}
