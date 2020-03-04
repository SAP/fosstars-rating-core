package com.sap.sgs.phosphor.fosstars.model;

import java.util.List;

/**
 * This interface describes an entity which may be tuned.
 */
public interface Tunable {

  /**
   * Returns a list of parameters which may be tuned.
   */
  List<? extends Parameter> parameters();
}
