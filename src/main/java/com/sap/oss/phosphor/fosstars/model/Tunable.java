package com.sap.oss.phosphor.fosstars.model;

import java.util.List;

/**
 * This interface describes an entity which may be tuned.
 */
public interface Tunable {

  /**
   * Get a list of parameters of the tunable object.
   *
   * @return A list of parameters which may be tuned.
   */
  List<? extends Parameter> parameters();

  /**
   * Checks if the entity is immutable.
   *
   * @return True if the entity is immutable, false otherwise.
   */
  boolean isImmutable();

  /**
   * Make the entity immutable.
   */
  void makeImmutable();
}
