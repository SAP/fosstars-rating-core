package com.sap.sgs.phosphor.fosstars.model.value;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import java.util.Objects;

/**
 * A value for the FuzzingAttemptsDone feature.
 */
public class FuzzingAttemptsDoneValue implements Value<FuzzingAttempts> {

  private final FuzzingAttempts fuzzingAttempts;

  public FuzzingAttemptsDoneValue(FuzzingAttempts fuzzingAttempts) {
    Objects.requireNonNull(fuzzingAttempts, "Fuzzing attempts can't be null!");
    this.fuzzingAttempts = fuzzingAttempts;
  }

  @Override
  public Feature<FuzzingAttempts> feature() {
    return OssFeatures.FUZZING_ATTEMPTS_DONE;
  }

  @Override
  public boolean isUnknown() {
    return false;
  }

  @Override
  public FuzzingAttempts get() {
    return fuzzingAttempts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof FuzzingAttemptsDoneValue == false) {
      return false;
    }
    FuzzingAttemptsDoneValue that = (FuzzingAttemptsDoneValue) o;
    return Objects.equals(fuzzingAttempts, that.fuzzingAttempts);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(fuzzingAttempts);
  }
}
