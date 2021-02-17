package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssArtifactMaintenanceScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

/**
 * This is a security rating for artifacts of an open-source project
 * that is based on open-source artifact and project specific security score.
 */
public class OssArtifactSecurityRating extends AbstractRating {

  /**
   * A set of labels for the rating.
   */
  public enum MaintenanceLabel implements Label {

    BAD, MODERATE, GOOD, UNCLEAR
  }

  /**
   * Thresholds for labels.
   */
  private final Thresholds thresholds;

  /**
   * Initializes a maintenance rating with defaults.
   */
  public OssArtifactSecurityRating() {
    this(new OssArtifactMaintenanceScore(), Thresholds.DEFAULT);
  }

  /**
   * Initializes a maintenance rating based on a maintenance score for open-source projects.
   *
   * @param score The maintenance score.
   * @param thresholds Thresholds for labels.
   */
  @JsonCreator
  public OssArtifactSecurityRating(
      @JsonProperty("score") OssArtifactMaintenanceScore score,
      @JsonProperty("thresholds") Thresholds thresholds) {

    super("maintenance rating for open-source projects", score);
    Objects.requireNonNull(thresholds, "Oh no! Thresholds is null!");
    this.thresholds = thresholds;
  }

  @Override
  public OssArtifactMaintenanceScore score() {
    return (OssArtifactMaintenanceScore) super.score();
  }

  @Override
  protected MaintenanceLabel label(ScoreValue scoreValue) {
    Objects.requireNonNull(scoreValue, "Oh no! Score value is null!");

    if (scoreValue.confidence() < thresholds.unclear) {
      return MaintenanceLabel.UNCLEAR;
    }

    double score = scoreValue.get();

    if (score < thresholds.moderate) {
      return MaintenanceLabel.BAD;
    }

    if (score < thresholds.good) {
      return MaintenanceLabel.MODERATE;
    }

    return MaintenanceLabel.GOOD;
  }

  /**
   * Holds thresholds for labels.
   */
  public static class Thresholds {

    /**
     * The default thresholds.
     */
    public static final Thresholds DEFAULT = new Thresholds(5.0, 8.0, 8.0);

    /**
     * A threshold for the moderate label (score value).
     */
    private final double moderate;

    /**
     * A threshold for the good label (score value).
     */
    private final double good;

    /**
     * A threshold for the unclear label (confidence).
     */
    private final double unclear;

    /**
     * Initialize thresholds.
     *
     * @param moderate A threshold for the moderate label.
     * @param good A threshold for the good label.
     * @param unclear A threshold for the unclear label.
     */
    @JsonCreator
    public Thresholds(
        @JsonProperty("moderate") double moderate,
        @JsonProperty("good") double good,
        @JsonProperty("unclear") double unclear) {

      Score.check(moderate);
      Score.check(good);
      Confidence.check(unclear);

      if (moderate >= good) {
        throw new IllegalArgumentException(
            "On ho! The moderate threshold is greater or equal to the good one!");
      }

      this.moderate = moderate;
      this.good = good;
      this.unclear = unclear;
    }
  }

}