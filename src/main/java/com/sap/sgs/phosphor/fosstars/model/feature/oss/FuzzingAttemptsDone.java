package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.sgs.phosphor.fosstars.model.value.FuzzingAttempts;
import com.sap.sgs.phosphor.fosstars.model.value.FuzzingAttemptsDoneValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;

/**
 * This feature contains information about fuzz testing
 * which have been performed for an open-source project.
 */
public class FuzzingAttemptsDone extends AbstractFeature<FuzzingAttempts> {

  /**
   * Package-private constructor.
   */
  FuzzingAttemptsDone() {
    super("Fuzzing attempts for an open-source project");
  }

  @Override
  public Value<FuzzingAttempts> value(FuzzingAttempts object) {
    return new FuzzingAttemptsDoneValue(object);
  }

  @Override
  public Value<FuzzingAttempts> parse(String string) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Value<FuzzingAttempts> unknown() {
    return new UnknownValue<>(this);
  }
}
