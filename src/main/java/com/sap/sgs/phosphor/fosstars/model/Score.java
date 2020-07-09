package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.score.AverageCompositeScore;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.sgs.phosphor.fosstars.model.score.example.ProjectActivityScoreExample;
import com.sap.sgs.phosphor.fosstars.model.score.example.SecurityScoreExample;
import com.sap.sgs.phosphor.fosstars.model.score.example.SecurityTestingScoreExample;
import com.sap.sgs.phosphor.fosstars.model.score.oss.CommunityCommitmentScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.DependencyScanScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.FindSecBugsScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.NoHttpToolScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectActivityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectPopularityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectSecurityAwarenessScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.StaticAnalysisScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.UnpatchedVulnerabilitiesScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.VulnerabilityDiscoveryAndSecurityTestingScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.VulnerabilityLifetimeScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;

/**
 * <p>This is an interface for a score.
 * A score can be based on features and other scores (sub-scores).
 * A score takes a number of features, and calculates a score values based on them.
 * The score value has to belong to the interval [0, 10].</p>
 *
 * <p>A score itself is a feature which holds a score value (a double in the range [0, 10])
 * for a specific score.</p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SecurityScoreExample.class),
    @JsonSubTypes.Type(value = ProjectActivityScoreExample.class),
    @JsonSubTypes.Type(value = SecurityTestingScoreExample.class),
    @JsonSubTypes.Type(value = WeightedCompositeScore.class),
    @JsonSubTypes.Type(value = AverageCompositeScore.class),
    @JsonSubTypes.Type(value = ProjectActivityScore.class),
    @JsonSubTypes.Type(value = ProjectPopularityScore.class),
    @JsonSubTypes.Type(value = CommunityCommitmentScore.class),
    @JsonSubTypes.Type(value = ProjectSecurityAwarenessScore.class),
    @JsonSubTypes.Type(value = ProjectSecurityTestingScore.class),
    @JsonSubTypes.Type(value = UnpatchedVulnerabilitiesScore.class),
    @JsonSubTypes.Type(value = VulnerabilityLifetimeScore.class),
    @JsonSubTypes.Type(value = OssSecurityScore.class),
    @JsonSubTypes.Type(value = DependencyScanScore.class),
    @JsonSubTypes.Type(value = LgtmScore.class),
    @JsonSubTypes.Type(value = NoHttpToolScore.class),
    @JsonSubTypes.Type(value = MemorySafetyTestingScore.class),
    @JsonSubTypes.Type(value = FindSecBugsScore.class),
    @JsonSubTypes.Type(value = FuzzingScore.class),
    @JsonSubTypes.Type(value = StaticAnalysisScore.class),
    @JsonSubTypes.Type(value = VulnerabilityDiscoveryAndSecurityTestingScore.class)
})
public interface Score extends Feature<Double> {

  /**
   * The minimum value of a score.
   */
  double MIN = 0.00;

  /**
   * The maximum value of a score.
   */
  double MAX = 10.0;

  /**
   * A valid interval for a score value.
   */
  Interval INTERVAL = DoubleInterval.init().from(0).to(10).closed().make();

  /**
   * Get a name of the score.
   *
   * @return A name of the score.
   */
  String name();

  /**
   * Get a description of the score.
   *
   * @return A description of the score.
   */
  String description();

  /**
   * Get a set of features which the score directly uses.
   *
   * @return A set of features which the score uses directly.
   */
  Set<Feature> features();

  /**
   * Returns all features which are used by the score.
   *
   * @return A number of features.
   */
  Set<Feature> allFeatures();

  /**
   * Get sub-scores which the score directly uses.
   *
   * @return A set of sub-scores which the score uses directly.
   */
  Set<Score> subScores();

  /**
   * Takes a set of values and calculates a score.
   *
   * @param values A set of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(Set<Value> values);

  /**
   * Takes a number of values and calculates a score.
   *
   * @param values A number of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(Value... values);

  /**
   * Takes an instance of {@link ValueSet} and calculates a score.
   *
   * @param values A set of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(ValueSet values);

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);

  /**
   * Checks if a score is correct.
   *
   * @param score The score to be checked.
   * @return The same score if it's correct.
   * @throws IllegalArgumentException If the score is not correct.
   */
  static double check(double score) {
    if (score < MIN || score > MAX) {
      throw new IllegalArgumentException(
          String.format("Score is not in the range [0, 1]: %f", score));
    }
    return score;
  }

  /**
   * Checks if a score is in the valid range, and returns an adjusted value if necessary.
   *
   * @param value A score to be checked.
   * @return {@link #MIN} if the score is less than {@link Score#MIN},
   *         {@link #MAX} if the score is greater than {@link Score#MAX},
   *         or the original score otherwise.
   */
  static double adjust(double value) {
    if (value < Score.MIN) {
      return Score.MIN;
    }
    if (value > Score.MAX) {
      return Score.MAX;
    }
    return value;
  }

}
