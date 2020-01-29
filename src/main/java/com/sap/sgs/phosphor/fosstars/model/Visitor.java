package com.sap.sgs.phosphor.fosstars.model;

/**
 * A visitor for the following objects:
 * <ul>
 *   <li>{@link Rating}</li>
 *   <li>{@link Score}</li>
 *   <li>{@link Feature}</li>
 *   <li>{@link Weight}</li>
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
  void visit(Feature feature);

  /**
   * Visit a {@link Weight}.
   *
   * @param weight The weight to be visited.
   */
  void visit(Weight weight);
}
