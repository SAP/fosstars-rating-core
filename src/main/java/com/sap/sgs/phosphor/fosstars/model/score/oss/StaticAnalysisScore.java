package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.AbstractScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * The score shows how an open-source project uses static analysis for security testing.
 * It's based on the following sub-scores:
 * <ul>
 *   <li>{@link LgtmScore}</li>
 *   <li>{@link FindSecBugsScore}</li>
 * </ul>
 * The above sub-scores may not apply to all projects. The score considers only the sub-scores
 * that is applicable to a particular project.
 */
public class StaticAnalysisScore extends AbstractScore {

  /**
   * A score that shows how a project uses LGTM.
   */
  private final LgtmScore lgtmScore;

  /**
   * A score that shows how a project uses FindSecBugs.
   */
  private final FindSecBugsScore findSecBugsScore;

  /**
   * Initializes a new score.
   */
  public StaticAnalysisScore() {
    super("How a project uses static analysis for security testing");
    this.lgtmScore = new LgtmScore();
    this.findSecBugsScore = new FindSecBugsScore();
  }

  /**
   * The score doesn't use any feature directly
   * so that this method returns an empty set.
   *
   * @return An empty set of features.
   */
  @Override
  public Set<Feature> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(lgtmScore, findSecBugsScore);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    ScoreValue lgtmScoreValue = calculateIfNecessary(lgtmScore, values);
    ScoreValue findSecBugsScoreValue = calculateIfNecessary(findSecBugsScore, values);

    ScoreValue scoreValue = scoreValue(MIN, lgtmScoreValue, findSecBugsScoreValue);

    if (allUnknown(lgtmScoreValue, findSecBugsScoreValue)) {
      return scoreValue.makeUnknown();
    }

    if (allNotApplicable(lgtmScoreValue, findSecBugsScoreValue)) {
      return scoreValue.makeNotApplicable();
    }

    scoreValue.increase(lgtmScoreValue.orElse(MIN));
    scoreValue.increase(findSecBugsScoreValue.orElse(MIN));

    return scoreValue;
  }
}
