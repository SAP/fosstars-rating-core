package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.Subject;

/**
 * Abstract class implementing the interface {@link Statistics}.
 */
public class AbstractStatistics<T extends Subject> implements Statistics<T> {

  /**
   * Total number of projects.
   */
  int total;

  /**
   * A number of projects with an unknown rating.
   */
  int unknownRatings = 0;

  /**
   * A number of projects with a good rating.
   */
  int goodRatings = 0;

  /**
   * A number of projects with a moderate rating.
   */
  int moderateRatings = 0;

  /**
   * A number of projects with a bad rating.
   */
  int badRatings = 0;

  /**
   * A number of projects with unclear ratings.
   */
  int unclearRatings = 0;

  @Override
  public void add(T subject) {
    throw new UnsupportedOperationException("Oops! I don't support addition of this subject!");
  }

  @Override
  public double badRatingsPercent() {
    return (double) badRatings * 100 / total;
  }

  @Override
  public double moderateRatingsPercent() {
    return (double) moderateRatings * 100 / total;
  }

  @Override
  public double goodRatingsPercent() {
    return (double) goodRatings * 100 / total;
  }

  @Override
  public double unknownRatingsPercent() {
    return (double) unknownRatings * 100 / total;
  }

  @Override
  public double unclearRatingsPercent() {
    return (double) unclearRatings * 100 / total;
  }

  @Override
  public int total() {
    return total;
  }

  @Override
  public int unknownRatings() {
    return unknownRatings;
  }

  @Override
  public int goodRatings() {
    return goodRatings;
  }

  @Override
  public int moderateRatings() {
    return moderateRatings;
  }

  @Override
  public int badRatings() {
    return badRatings;
  }

  @Override
  public int unclearRatings() {
    return unclearRatings;
  }
}
