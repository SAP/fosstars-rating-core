package com.sap.oss.phosphor.fosstars.model.weight;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.Weight;

/**
 * A base class for weights.
 */
abstract class AbstractWeight implements Weight {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Interval boundaries() {
    return Weight.INTERVAL;
  }

}
