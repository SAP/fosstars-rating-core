package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

/**
 * This rating checks whether an open source project violates certain rules or not.
 */
public class OssRulesOfPlayRating extends AbstractRating {

  /**
   * A set of labels for the rating.
   */
  public enum OssRulesOfPlayLabel implements Label {

    PASS, FAIL, UNCLEAR
  }

  /**
   * Initializes a new rating.
   */
  public OssRulesOfPlayRating() {
    super("Open source rules of play rating", new OssRulesOfPlayScore());
  }

  @Override
  protected OssRulesOfPlayLabel label(ScoreValue scoreValue) {
    Objects.requireNonNull(scoreValue, "Oh no! Score value is null!");

    if (!scoreValue.score().equals(score())) {
      throw new IllegalArgumentException(
          String.format("Oh no! Invalid score value: %s", scoreValue.score().getClass()));
    }

    if (Double.compare(scoreValue.get(), Score.MAX) != 0) {
      return OssRulesOfPlayLabel.FAIL;
    }

    if (scoreValue.isUnknown() || Double.compare(scoreValue.confidence(), Confidence.MAX) != 0) {
      return OssRulesOfPlayLabel.UNCLEAR;
    }

    return OssRulesOfPlayLabel.PASS;
  }
}
