package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.BoundedDoubleFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.BoundedIntegerFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.DateFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.LgtmGradeFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import com.sap.sgs.phosphor.fosstars.model.feature.OwaspDependencyCheckUsageFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.example.NumberOfCommitsLastMonthExample;
import com.sap.sgs.phosphor.fosstars.model.feature.example.NumberOfContributorsLastMonthExample;
import com.sap.sgs.phosphor.fosstars.model.feature.example.SecurityReviewDoneExample;
import com.sap.sgs.phosphor.fosstars.model.feature.example.StaticCodeAnalysisDoneExample;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.LanguagesFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.PackageManagersFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.VulnerabilitiesInProject;

/**
 * An interface for a feature.
 * All implementations must:
 * <ul>
 *   <li>provide equals() and hashCode() methods.</li>
 *   <li>be stateless</li>
 *   <li>support serialization to JSON with Jackson</li>
 * </ul>
 *
 * @param <T> Type of data of a feature
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PositiveIntegerFeature.class),
    @JsonSubTypes.Type(value = DoubleFeature.class),
    @JsonSubTypes.Type(value = BooleanFeature.class),
    @JsonSubTypes.Type(value = BoundedIntegerFeature.class),
    @JsonSubTypes.Type(value = BoundedDoubleFeature.class),
    @JsonSubTypes.Type(value = DateFeature.class),
    @JsonSubTypes.Type(value = EnumFeature.class),
    @JsonSubTypes.Type(value = VulnerabilitiesInProject.class),
    @JsonSubTypes.Type(value = SecurityReviewDoneExample.class),
    @JsonSubTypes.Type(value = StaticCodeAnalysisDoneExample.class),
    @JsonSubTypes.Type(value = NumberOfCommitsLastMonthExample.class),
    @JsonSubTypes.Type(value = NumberOfContributorsLastMonthExample.class),
    @JsonSubTypes.Type(value = LgtmGradeFeature.class),
    @JsonSubTypes.Type(value = LanguagesFeature.class),
    @JsonSubTypes.Type(value = PackageManagersFeature.class),
    @JsonSubTypes.Type(value = OwaspDependencyCheckUsageFeature.class),
    @JsonSubTypes.Type(value = OwaspDependencyCheckCvssThreshold.class)
})
public interface Feature<T> {

  /**
   * Get a name of the feature.
   *
   * @return A name of the feature.
   */
  String name();

  /**
   * Tries to convert an object to a feature value.
   * Throws an exception if the object can't be converted.
   *
   * @param object The object to be converted to a value.
   * @return An instance of {@link Value} initialized with the object.
   * @throws IllegalArgumentException If a value can't be initialized with the object.
   */
  Value<T> value(T object);

  /**
   * Tries to convert a string to a feature value.
   * Throws an exception if the object can't be converted.
   *
   * @param string The string to be converted.
   * @return An instance of {@link Value} initialized with the object.
   * @throws IllegalArgumentException If a value can't be initialized with the object.
   */
  Value<T> parse(String string);

  /**
   * Get an unknown value of the feature.
   *
   * @return An unknown value of the feature.
   */
  Value<T> unknown();

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);
}
