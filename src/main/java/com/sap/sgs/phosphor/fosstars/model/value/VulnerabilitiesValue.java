package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * This is a value for the
 * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES} feature.
 */
public class VulnerabilitiesValue extends AbstractValue<Vulnerabilities> {

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
   * Initializes a new value with a collection of vulnerabilities.
   *
   * @param vulnerabilities The vulnerabilities.
   */
  @JsonCreator
  public VulnerabilitiesValue(@JsonProperty("vulnerabilities") Vulnerabilities vulnerabilities) {
    super(VULNERABILITIES);

    Objects.requireNonNull(vulnerabilities, "Vulnerabilities can't be null!");
    this.vulnerabilities = vulnerabilities;
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
}
