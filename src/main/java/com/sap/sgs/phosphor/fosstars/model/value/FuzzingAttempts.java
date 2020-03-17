package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A number of fuzzing attempts.
 */
public final class FuzzingAttempts {

  private final Set<FuzzingAttempt> attempts;

  /**
   * Initializes {@link FuzzingAttempts}.
   *
   * @param attempts A number of fuzzing attempts.
   */
  FuzzingAttempts(FuzzingAttempt... attempts) {
    this(setOf(attempts));
  }

  /**
   * Initializes {@link FuzzingAttempts}.
   *
   * @param attempts A set of fuzzing attempts.
   */
  FuzzingAttempts(Set<FuzzingAttempt> attempts) {
    this.attempts = Collections.unmodifiableSet(attempts);
  }

  /**
   * Returns a set of the fuzzing attempts.
   */
  public Set<FuzzingAttempt> get() {
    return attempts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof FuzzingAttempts == false) {
      return false;
    }
    FuzzingAttempts attempts1 = (FuzzingAttempts) o;
    return Objects.equals(attempts, attempts1.attempts);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(attempts);
  }
}
