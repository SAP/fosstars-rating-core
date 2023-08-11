package com.sap.oss.phosphor.fosstars.tool.format.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.advice.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * The Advice class that encloses @link {@link com.sap.oss.phosphor.fosstars.advice.Advice} to be
 * used in serialization.
 */
public class Advices {

  /**
   * The advice text.
   */
  private String text;

  /**
   * The feature of the advice.
   */
  private String feature;

  /**
   * The references for the advice.
   */
  private List<Link> links = new ArrayList<>();

  /**
   * Default constructor.
   */
  public Advices() {
  }

  /**
   * Initializes a Feature instance.
   *
   * @param text    The advice text.
   * @param feature of the advice.
   * @param links references of the advice.
   */
  @JsonCreator
  public Advices(@JsonProperty("text") String text, @JsonProperty("feature") String feature,
      @JsonProperty("links") List<Link> links) {
    this.text = text;
    this.feature = feature;
    this.links.addAll(links);
  }

  /**
   * Return the text of the advice.
   *
   * @return the text of the advice.
   */
  @JsonGetter("text")
  public String text() {
    return text;
  }

  /**
   * Set the text of the advice.
   *
   * @return the Advice instance.
   */
  public Advices text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Return the feature of the advice.
   *
   * @return the feature of the advice.
   */
  @JsonGetter("feature")
  public String feature() {
    return feature;
  }

  /**
   * Set the feature of the advice.
   *
   * @return the feature of the advice.
   */
  public Advices feature(String feature) {
    this.feature = feature;
    return this;
  }

  /**
   * Return the references of the advice.
   *
   * @return the references of the advice.
   */
  @JsonGetter("links")
  public List<Link> links() {
    return links;
  }

  /**
   * Set the references of the advice.
   *
   * @param links to be added as prt of the advice.
   * @return the Advice.
   */
  public Advices links(List<Link> links) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.addAll(links);
    return this;
  }
}