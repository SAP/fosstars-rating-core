package com.sap.oss.phosphor.fosstars.tool.format.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.util.ArrayList;
import java.util.List;

/**
 * The Rating class that encloses @link {@link RatingValue} to be used in serialization.
 */
public class Rating {

  /**
   * The purl of the identifier.
   */
  private String purl;

  /**
   * The label of the rating.
   */
  private String label;

  /**
   * The {@link Score} of the rating.
   */
  private Score score;

  /**
   * The list of {@link Advice}s of the rating.
   */
  private List<Advices> advices = new ArrayList<>();

  /**
   * Initializes a Rating instance.
   *
   * @param purl  the purl of the identifier.
   * @param label of the rating.
   * @param score of the rating.
   */
  @JsonCreator
  public Rating(@JsonProperty("purl") String purl,
      @JsonProperty("label") String label,
      @JsonProperty("score") Score score) {
    this.purl = purl;
    this.label = label;
    this.score = score;
  }

  /**
   * Default constructor.
   */
  public Rating() {
  }

  /**
   * Return the purl of the identifier.
   *
   * @return the purl of the identifier.
   */
  @JsonGetter("purl")
  public String purl() {
    return purl;
  }

  /**
   * Set the purl of the identifier.
   *
   * @param purl the purl of the identifier.
   * @return this.
   */
  public Rating purl(String purl) {
    this.purl = purl;
    return this;
  }

  /**
   * Return the label of the rating.
   *
   * @return the label of the rating.
   */
  @JsonGetter("label")
  public String label() {
    return label;
  }

  /**
   * Set the label of the rating.
   *
   * @param label the label of the rating.
   * @return this.
   */
  public Rating label(String label) {
    this.label = label;
    return this;
  }

  /**
   * Return the {@link Score} of the rating.
   *
   * @return the {@link Score} of the rating.
   */
  @JsonGetter("score")
  public Score score() {
    return score;
  }

  /**
   * Set the {@link Score} of the rating.
   *
   * @param score the {@link Score} of the rating.
   * @return this.
   */
  public Rating score(Score score) {
    this.score = score;
    return this;
  }

  /**
   * Set the advices of the rating.
   *
   * @param advices to be added to the list of advices.
   * @return the Rating.
   */
  public Rating advice(Advices advices) {
    if (this.advices == null) {
      this.advices = new ArrayList<>();
    }
    this.advices.add(advices);
    return this;
  }

  /**
   * Return the list of {@link Advice}s of the rating.
   *
   * @return the list of {@link Advice}s of the rating.
   */
  @JsonGetter("advices")
  public List<Advices> advices() {
    return advices;
  }

  /**
   * Set the advices of the rating.
   *
   * @param advices to be added to the list of advices.
   * @return the Rating.
   */
  public Rating advices(List<Advices> advices) {
    if (this.advices == null) {
      this.advices = new ArrayList<>();
    }
    this.advices.addAll(advices);
    return this;
  }
}