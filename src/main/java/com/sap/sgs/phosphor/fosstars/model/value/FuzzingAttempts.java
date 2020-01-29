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

  public FuzzingAttempts(FuzzingAttempt... attempts) {
    this(setOf("You've provided a duplicate fuzzing attempt!", attempts));
  }

  public FuzzingAttempts(Set<FuzzingAttempt> attempts) {
    this.attempts = Collections.unmodifiableSet(attempts);
  }

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
