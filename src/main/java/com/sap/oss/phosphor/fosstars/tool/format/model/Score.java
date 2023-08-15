package com.sap.oss.phosphor.fosstars.tool.format.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.List;

/**
 * The Score class that encloses @link {@link ScoreValue} to be used in serialization.
 */
public class Score {

  /**
   * The name of the score.
   */
  private String name;

  /**
   * The value of the score.
   */
  private String value;

  /**
   * The sub-scores summarized to calculate the score.
   */
  private List<Score> subScores = new ArrayList<>();

  /**
   * The features used to calculate the score.
   */
  private List<Feature> features = new ArrayList<>();

  /**
   * The weight of the score.
   */
  private String weight;

  /**
   * The confidence of the score.
   */
  private String confidence;

  /**
   * Default constructor.
   */
  public Score() {
  }

  /**
   * Initializes a Score instance.
   *
   * @param name       the name of the score.
   * @param value      the value of the score.
   * @param weight     the weight of the score.
   * @param confidence the confidence of the score.
   */
  @JsonCreator
  public Score(@JsonProperty("name") String name, @JsonProperty("value") String value,
      @JsonProperty("weight") String weight, @JsonProperty("confidence") String confidence) {
    this.name = name;
    this.value = value;
    this.weight = weight;
    this.confidence = confidence;
  }

  /**
   * Return the name of the score.
   *
   * @return the name of the score.
   */
  @JsonGetter("name")
  public String name() {
    return name;
  }

  /**
   * Set the name of the score.
   *
   * @param name the name of the score.
   * @return the score.
   */
  public Score name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Return the value of the score.
   *
   * @return the value of the score.
   */
  @JsonGetter("value")
  public String value() {
    return value;
  }

  /**
   * Set the value of the score.
   *
   * @param value the value of the score.
   * @return the score.
   */
  public Score value(String value) {
    this.value = value;
    return this;
  }

  /**
   * Set the sub-scores of the score.
   *
   * @param subScore to be added to the list of subScores.
   * @return the score.
   */
  public Score subScore(Score subScore) {
    if (this.subScores == null) {
      this.subScores = new ArrayList<>();
    }
    this.subScores.add(subScore);
    return this;
  }

  /**
   * Set the features of the score.
   *
   * @param feature to be added to the list of features.
   * @return the score.
   */
  public Score feature(Feature feature) {
    if (this.features == null) {
      this.features = new ArrayList<>();
    }
    this.features.add(feature);
    return this;
  }

  /**
   * Return the weight of the score.
   *
   * @return the weight of the score.
   */
  @JsonGetter("weight")
  public String weight() {
    return weight;
  }

  /**
   * Set the weight of the score.
   *
   * @param weight the weight of the score.
   * @return the score.
   */
  public Score weight(String weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Return the confidence of the score.
   *
   * @return the confidence of the score.
   */
  @JsonGetter("confidence")
  public String confidence() {
    return confidence;
  }

  /**
   * Set the confidence of the score.
   *
   * @param confidence the confidence of the score.
   * @return the score.
   */
  public Score confidence(String confidence) {
    this.confidence = confidence;
    return this;
  }

  /**
   * Return the sub-scores of the score.
   *
   * @return the sub-scores of the score.
   */
  @JsonGetter("subScores")
  public List<Score> subscores() {
    return subScores;
  }

  /**
   * Return the features of the score.
   *
   * @return the features of the score.
   */
  @JsonGetter("features")
  public List<Feature> features() {
    return features;
  }
}