package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"vendorEvidence", "productEvidence", "versionEvidence"})
@Generated("jsonschema2pojo")
public class EvidenceCollected implements Serializable {

  @JsonProperty("vendorEvidence")
  private List<VendorEvidence> vendorEvidence = null;

  @JsonProperty("productEvidence")
  private List<ProductEvidence> productEvidence = null;

  @JsonProperty("versionEvidence")
  private List<VersionEvidence> versionEvidence = null;

  @JsonProperty("vendorEvidence")
  public List<VendorEvidence> getVendorEvidence() {
    return vendorEvidence;
  }

  @JsonProperty("vendorEvidence")
  public void setVendorEvidence(List<VendorEvidence> vendorEvidence) {
    this.vendorEvidence = vendorEvidence;
  }

  public EvidenceCollected withVendorEvidence(List<VendorEvidence> vendorEvidence) {
    this.vendorEvidence = vendorEvidence;
    return this;
  }

  @JsonProperty("productEvidence")
  public List<ProductEvidence> getProductEvidence() {
    return productEvidence;
  }

  @JsonProperty("productEvidence")
  public void setProductEvidence(List<ProductEvidence> productEvidence) {
    this.productEvidence = productEvidence;
  }

  public EvidenceCollected withProductEvidence(List<ProductEvidence> productEvidence) {
    this.productEvidence = productEvidence;
    return this;
  }

  @JsonProperty("versionEvidence")
  public List<VersionEvidence> getVersionEvidence() {
    return versionEvidence;
  }

  @JsonProperty("versionEvidence")
  public void setVersionEvidence(List<VersionEvidence> versionEvidence) {
    this.versionEvidence = versionEvidence;
  }

  public EvidenceCollected withVersionEvidence(List<VersionEvidence> versionEvidence) {
    this.versionEvidence = versionEvidence;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(EvidenceCollected.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("vendorEvidence");
    sb.append('=');
    sb.append(((this.vendorEvidence == null) ? "<null>" : this.vendorEvidence));
    sb.append(',');
    sb.append("productEvidence");
    sb.append('=');
    sb.append(((this.productEvidence == null) ? "<null>" : this.productEvidence));
    sb.append(',');
    sb.append("versionEvidence");
    sb.append('=');
    sb.append(((this.versionEvidence == null) ? "<null>" : this.versionEvidence));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}
