package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

/**
 * This is a security rating for open-source projects
 * that is based on a security score for open-source project.
 */
public class OssSecurityRating extends AbstractRating {

  /**
   * A set of labels for the rating.
   */
  public enum SecurityLabel implements Label {

    BAD, MODERATE, GOOD, UNCLEAR
  }

  /**
   * Thresholds for labels.
   */
  private final Thresholds thresholds;

  /**
   * Initializes a security rating with defaults.
   */
  public OssSecurityRating() {
    this(new OssSecurityScore(), Thresholds.DEFAULT);
  }

  /**
   * Initializes a security rating based on a security score for open-source projects.
   *
   * @param score The security score.
   * @param thresholds Thresholds for labels.
   */
  @JsonCreator
  public OssSecurityRating(
      @JsonProperty("score") OssSecurityScore score,
      @JsonProperty("thresholds") Thresholds thresholds) {

    super("Security rating for open-source projects", score);
    Objects.requireNonNull(thresholds, "Oh no! Thresholds is null!");
    this.thresholds = thresholds;
  }

  @Override
  public OssSecurityScore score() {
    return (OssSecurityScore) super.score();
  }

  @Override
  protected SecurityLabel label(ScoreValue scoreValue) {
    Objects.requireNonNull(scoreValue, "Oh no! Score value is null!");

    if (scoreValue.confidence() < thresholds.unclear) {
      return SecurityLabel.UNCLEAR;
    }

    double score = scoreValue.get();

    if (score < thresholds.moderate) {
      return SecurityLabel.BAD;
    }

    if (score < thresholds.good) {
      return SecurityLabel.MODERATE;
    }

    return SecurityLabel.GOOD;
  }

  /**
   * Return thresholds for the labels.
   *
   * @return The thresholds for the labels.
   */
  public Thresholds thresholds() {
    return thresholds;
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

    /**
     * Returns the threshold for {@link SecurityLabel#MODERATE} label.
     *
     * @return The threshold.
     */
    public double forModerate() {
      return moderate;
    }

    /**
     * Returns the threshold for {@link SecurityLabel#GOOD} label.
     *
     * @return The threshold.
     */
    public double forGood() {
      return good;
    }

    /**
     * Returns the threshold for {@link SecurityLabel#UNCLEAR} label.
     *
     * @return The threshold.
     */
    public double forUnclear() {
      return unclear;
    }
  }

}
