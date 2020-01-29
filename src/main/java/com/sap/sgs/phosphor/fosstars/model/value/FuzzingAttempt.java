package com.sap.sgs.phosphor.fosstars.model.value;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * Holds an information about an attempt of a fuzz testing.
 */
public final class FuzzingAttempt {

  /**
   * A URL to a document which describes the fuzzing setup and the results.
   */
  public final URL link;

  /**
   * A date when fuzzing was done.
   */
  public final Date when;

  /**
   * Who did fuzzing.
   */
  public final String who;

  public FuzzingAttempt(URL link, Date when, String who) {
    this.link = link;
    this.when = when;
    this.who = who;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != FuzzingAttempt.class) {
      return false;
    }
    FuzzingAttempt that = (FuzzingAttempt) o;
    return Objects.equals(link, that.link) &&
        Objects.equals(when, that.when) &&
        Objects.equals(who, that.who);
  }

  @Override
  public int hashCode() {
    return Objects.hash(link, when, who);
  }
}
