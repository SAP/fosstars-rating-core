package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import java.util.Objects;

/**
 * This is a value for one of the features mentioned in the list.
 * <ol>
 *   <li>{@link OssFeatures#VULNERABILITIES_IN_PROJECT} feature.</li>
 *   <li>{@link OssFeatures#VULNERABILITIES_IN_ARTIFACT} feature.</li>
 * </ol>
 */
public class VulnerabilitiesValue extends AbstractKnownValue<Vulnerabilities> {

  /**
   * Vulnerabilities.
   */
  private final Vulnerabilities vulnerabilities;

  /**
   * Creates an empty value.
   */
  public VulnerabilitiesValue() {
    this(new Vulnerabilities());
  }

  /**
   * Initializes a new value with a collection of vulnerabilities. By default assign
   * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES_IN_PROJECT}
   * as the feature.
   *
   * @param vulnerabilities The vulnerabilities.
   */
  public VulnerabilitiesValue(Vulnerabilities vulnerabilities) {
    this(VULNERABILITIES_IN_PROJECT, vulnerabilities);
  }

  /**
   * Initializes a new value with a collection of vulnerabilities.
   *
   * @param feature The vulnerabilities feature.
   * @param vulnerabilities The vulnerabilities.
   */
  @JsonCreator
  public VulnerabilitiesValue(
      @JsonProperty("feature") Feature<Vulnerabilities> feature,
      @JsonProperty("vulnerabilities") Vulnerabilities vulnerabilities) {
    super(feature);

    Objects.requireNonNull(vulnerabilities, "Vulnerabilities can't be null!");
    this.vulnerabilities = vulnerabilities;
  }

  @Override
  @JsonGetter("vulnerabilities")
  public final Vulnerabilities get() {
    return vulnerabilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof VulnerabilitiesValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    VulnerabilitiesValue that = (VulnerabilitiesValue) o;
    return Objects.equals(vulnerabilities, that.vulnerabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), vulnerabilities);
  }

  @Override
  public String toString() {
    return vulnerabilities.toString();
  }
}
