package com.sap.oss.phosphor.fosstars.tool.report;

/**
 * Interface for the statistics to be generated as part of final report.
 */
public interface Statistics<T> {

  /**
   * Adds a subject to the statistics.
   *
   * @param subject The Subject.
   */
  void add(T subject);

  /**
   * Returns a percent of projects with bad rating.
   *
   * @return A percent of projects with bad rating.
   */
  double badRatingsPercent();

  /**
   * Returns a percent of projects with moderate rating.
   *
   * @return A percent of projects with moderate rating.
   */
  double moderateRatingsPercent();

  /**
   * Returns a percent of projects with good rating.
   *
   * @return A percent of projects with good rating.
   */
  double goodRatingsPercent();

  /**
   * Returns a percent of projects with unknown rating.
   *
   * @return A percent of projects with unknown rating.
   */
  double unknownRatingsPercent();

  /**
   * Returns a percent of projects with unclear rating.
   *
   * @return A percent of projects with unclear rating.
   */
  double unclearRatingsPercent();

  /**
   * Returns the total number of projects.
   *
   * @return the total number of projects.
   */
  int total();

  /**
   * Returns the number of projects with an unknown rating.
   *
   * @return the number of projects with an unknown rating.
   */
  int unknownRatings();

  /**
   * Returns the number of projects with a good rating.
   *
   * @return the number of projects with a good rating.
   */
  int goodRatings();

  /**
   * Returns the number of projects with a moderate rating.
   *
   * @return the number of projects with a moderate rating.
   */
  int moderateRatings();

  /**
   * Returns the number of projects with a bad rating.
   *
   * @return the number of projects with a bad rating.
   */
  int badRatings();

  /**
   * Returns the number of projects with unclear rating.
   *
   * @return the number of projects with unclear rating.
   */
  int unclearRatings();
}
