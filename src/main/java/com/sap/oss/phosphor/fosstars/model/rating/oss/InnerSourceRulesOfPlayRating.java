package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.InnerSourceRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

public class InnerSourceRulesOfPlayRating extends AbstractRating {
  
  public enum InnerSourceRulesOfPlayLabel implements Label {
    PASSED, PASSED_WITH_WARNING, FAILED, UNCLEAR
  }

  public InnerSourceRulesOfPlayRating() {
    super("InnerSource rules of play rating.", new InnerSourceRulesOfPlayScore());
  }

  @Override
  protected Label label(ScoreValue scoreValue) {
    Objects.requireNonNull(scoreValue, "Oh no! Score value is null!");

    if (!scoreValue.score().equals(score())) {
      throw new IllegalArgumentException(
          String.format("Oh no! Invalid score value: %s", scoreValue.score().getClass()));
    }

    if (!InnerSourceRulesOfPlayScore.findViolatedRulesIn(scoreValue.usedValues()).isEmpty()) {
      return InnerSourceRulesOfPlayLabel.FAILED;
    }

    if (scoreValue.isUnknown() || Double.compare(scoreValue.confidence(), Confidence.MAX) != 0) {
      return InnerSourceRulesOfPlayLabel.UNCLEAR;
    }

    if (!InnerSourceRulesOfPlayScore.findWarningsIn(scoreValue.usedValues()).isEmpty()) {
      return InnerSourceRulesOfPlayLabel.PASSED_WITH_WARNING;
    }

    return InnerSourceRulesOfPlayLabel.PASSED;
  }
}
