package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZING_ATTEMPTS_DONE;

import java.util.Objects;

/**
 * A value for the FuzzingAttemptsDone feature.
 */
public class FuzzingAttemptsDoneValue extends AbstractValue<FuzzingAttempts> {

  private final FuzzingAttempts fuzzingAttempts;

  /**
   * Initializes a {@link FuzzingAttemptsDoneValue} with a {@link FuzzingAttempts}.
   *
   * @param fuzzingAttempts The instance of {@link FuzzingAttempts}.
   */
  public FuzzingAttemptsDoneValue(FuzzingAttempts fuzzingAttempts) {
    super(FUZZING_ATTEMPTS_DONE);

    Objects.requireNonNull(fuzzingAttempts, "Fuzzing attempts can't be null!");
    this.fuzzingAttempts = fuzzingAttempts;
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
    if (!super.equals(o)) {
      return false;
    }
    FuzzingAttemptsDoneValue that = (FuzzingAttemptsDoneValue) o;
    return Objects.equals(fuzzingAttempts, that.fuzzingAttempts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), fuzzingAttempts);
  }
}
