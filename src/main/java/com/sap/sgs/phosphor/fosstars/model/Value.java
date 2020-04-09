package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.DateValue;
import com.sap.sgs.phosphor.fosstars.model.value.DoubleValue;
import com.sap.sgs.phosphor.fosstars.model.value.EnumValue;
import com.sap.sgs.phosphor.fosstars.model.value.ExpiringValue;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import com.sap.sgs.phosphor.fosstars.model.value.LanguagesValue;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGradeValue;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagersValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviewsDoneValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.VulnerabilitiesValue;

/**
 * An interface for a feature value of specific type.
 *
 * @param <T> Type of date that the feature provides.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IntegerValue.class),
    @JsonSubTypes.Type(value = DoubleValue.class),
    @JsonSubTypes.Type(value = BooleanValue.class),
    @JsonSubTypes.Type(value = DateValue.class),
    @JsonSubTypes.Type(value = ScoreValue.class),
    @JsonSubTypes.Type(value = ExpiringValue.class),
    @JsonSubTypes.Type(value = VulnerabilitiesValue.class),
    @JsonSubTypes.Type(value = UnknownValue.class),
    @JsonSubTypes.Type(value = SecurityReviewsDoneValue.class),
    @JsonSubTypes.Type(value = EnumValue.class),
    @JsonSubTypes.Type(value = LgtmGradeValue.class),
    @JsonSubTypes.Type(value = LanguagesValue.class),
    @JsonSubTypes.Type(value = PackageManagersValue.class)
})
public interface Value<T> {

  /**
   * Returns a feature which the value is for.
   */
  Feature feature();

  /**
   * Returns true if the value is unknown, false otherwise.
   */
  boolean isUnknown();

  /**
   * Returns the value.
   */
  T get();

  /**
   * Call a processor to process the value if it's known.
   *
   * @param processor The processor to be called.
   * @return This Value instance.
   */
  Value<T> processIfKnown(Processor<T> processor);

  /**
   * Call a processor to process the value if it's unknown.
   *
   * @param processor The processor to be called.
   * @return This Value instance.
   */
  Value<T> processIfUnknown(Runnable processor);

  /**
   * An interface of a processor which can process a value.
   *
   * @param <T> A type of a value.
   */
  @FunctionalInterface
  interface Processor<T> {

    /**
     * Process a value.
     *
     * @param value The value to be processed.
     */
    void process(T value);
  }
}
