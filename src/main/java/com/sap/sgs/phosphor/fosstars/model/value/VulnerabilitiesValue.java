package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import java.util.Objects;

/**
 * This is a value for the {@link OssFeatures#VULNERABILITIES} feature.
 */
public class VulnerabilitiesValue implements Value<Vulnerabilities> {

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
   * @param vulnerabilities The vulnerabilities.
   */
  @JsonCreator
  public VulnerabilitiesValue(@JsonProperty("vulnerabilities") Vulnerabilities vulnerabilities) {
    Objects.requireNonNull(vulnerabilities, "Vulnerabilities can't be null!");
    this.vulnerabilities = vulnerabilities;
  }

  @Override
  public final Feature feature() {
    return OssFeatures.VULNERABILITIES;
  }

  @Override
  @JsonIgnore
  public final boolean isUnknown() {
    return false;
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
    VulnerabilitiesValue that = (VulnerabilitiesValue) o;
    return Objects.equals(vulnerabilities, that.vulnerabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vulnerabilities);
  }

}
