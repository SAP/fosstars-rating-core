package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.nvd.data.Cpe22Uri;
import com.sap.oss.phosphor.fosstars.nvd.data.Cpe23Uri;
import java.util.Objects;

/**
 * The class holds the vendor and the product along with {@link VersionRange} of the vulnerable
 * artifact.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VulnerableArtifact {

  /**
   * Vendor name of the project.
   */
  private final String vendor;

  /**
   * Product name of the project.
   */
  private String product;

  /**
   * Start and end version {@link VersionRange} of the project.
   */
  private VersionRange versionRange;


  /**
   * Create a vulnerable artifact corresponding to {@link Cpe23Uri} or {@link Cpe22Uri}. It will
   * contain information like vendor, product and range of versions for which this artifact is
   * reported as vulnerable.
   *
   * @param vendor The vendor of the project.
   * @param product The product of the project.
   * @param versionRange The {@link VersionRange} of the project.
   */
  public VulnerableArtifact(@JsonProperty("vendor") String vendor,
      @JsonProperty("product") String product,
      @JsonProperty("versionRange") VersionRange versionRange) {
    this.vendor = vendor;
    this.product = product;
    this.versionRange = versionRange;
  }

  @JsonGetter("vednor")
  public String vendor() {
    return vendor;
  }

  @JsonGetter("product")
  public String product() {
    return product;
  }

  @JsonGetter("versionRange")
  public VersionRange versionRange() {
    return versionRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VulnerableArtifact that = (VulnerableArtifact) o;
    return Objects.equals(product, that.product) && Objects.equals(vendor, that.vendor)
        && Objects.equals(versionRange, that.versionRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendor, product, versionRange);
  }
}
