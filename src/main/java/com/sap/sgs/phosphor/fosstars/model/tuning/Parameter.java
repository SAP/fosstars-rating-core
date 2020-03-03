package com.sap.sgs.phosphor.fosstars.model.tuning;

import com.sap.sgs.phosphor.fosstars.model.Interval;

public interface Parameter {

  Interval boundaries();
  Parameter value(double v);
  Double value();
}
