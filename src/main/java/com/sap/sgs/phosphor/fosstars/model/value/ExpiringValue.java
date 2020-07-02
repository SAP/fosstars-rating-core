package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Date;
import java.util.Objects;

/**
 * This is a wrapper for a feature value which adds an expiration date for the value.
 *
 * @param <T> Type of the value.
 */
public class ExpiringValue<T> implements Value<T> {

  /**
   * This constant means that the value has no expiration date.
   */
  public static final Date NO_EXPIRATION = null;

  /**
   * The wrapped value.
   */
  private final Value<T> value;

  /**
   * The expiration date for the value.
   */
  private final Date expiration;

  /**
   * Initializes an expiring value.
   *
   * @param value A value to wrap.
   * @param expiration An expiration date for the value.
   */
  public ExpiringValue(
      @JsonProperty("value") Value<T> value,
      @JsonProperty("expiration") Date expiration) {

    this.value = value;
    this.expiration = expiration;
  }

  @Override
  public Feature feature() {
    return value.feature();
  }

  @Override
  @JsonIgnore
  public boolean isUnknown() {
    return value.isUnknown();
  }

  @Override
  @JsonIgnore
  public boolean isNotApplicable() {
    return value.isNotApplicable();
  }

  @Override
  public T get() {
    return value.get();
  }

  @Override
  public T orElse(T other) {
    return value.orElse(other);
  }

  @Override
  public Value<T> processIfKnown(Processor<T> processor) {
    value.processIfKnown(processor);
    return this;
  }

  @Override
  public Value<T> processIfUnknown(Runnable processor) {
    value.processIfUnknown(processor);
    return this;
  }

  /**
   * Tells whether the value never expires or not.
   *
   * @return True if the value never expires, false otherwise.
   */
  public boolean neverExpires() {
    return expiration == NO_EXPIRATION;
  }

  /**
   * Tells whether the value is expired or not.
   *
   * @return True if the value has expired, false otherwise.
   */
  public boolean expired() {
    Date now = new Date();
    return now.after(expiration);
  }

  /**
   * Get the expiration date.
   *
   * @return The expiration date.
   */
  @JsonGetter("expiration")
  public Date expiration() {
    return expiration;
  }

  /**
   * Get the original feature value.
   *
   * @return The original feature value.
   */
  @JsonGetter("value")
  public Value original() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ExpiringValue == false) {
      return false;
    }
    ExpiringValue<?> that = (ExpiringValue<?>) o;
    return Objects.equals(value, that.value) && Objects.equals(expiration, that.expiration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, expiration);
  }
}
