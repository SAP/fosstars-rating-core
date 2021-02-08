package com.sap.oss.phosphor.fosstars.model;

/**
 * This is an interface of a visitor for browsing the internal structure of ratings and scores.
 * Visitors can visit the following objects:
 * <ul>
 *   <li>{@link Rating}</li>
 *   <li>{@link Score}</li>
 *   <li>{@link Feature}</li>
 *   <li>{@link Parameter}</li>
 * </ul>
 */
public interface Visitor {

  /**
   * Visit a {@link Rating}.
   *
   * @param rating The rating to be visited.
   */
  void visit(Rating rating);

  /**
   * Visit a {@link Score}.
   *
   * @param score The score to be visited.
   */
  void visit(Score score);

  /**
   * Visit a {@link Feature}.
   *
   * @param feature The feature to be visited.
   */
  void visit(Feature<?> feature);

  /**
   * Visit a {@link Parameter}.
   *
   * @param parameter The parameter to be visited.
   */
  void visit(Parameter parameter);
}
