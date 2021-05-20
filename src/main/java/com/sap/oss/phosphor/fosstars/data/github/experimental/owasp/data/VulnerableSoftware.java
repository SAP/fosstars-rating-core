package com.sap.oss.phosphor.fosstars.data.github.experimental.owasp.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"software"})
@Generated("jsonschema2pojo")
public class VulnerableSoftware implements Serializable {

  @JsonProperty("software")
  private Software software;

  @JsonProperty("software")
  public Software getSoftware() {
    return software;
  }

  @JsonProperty("software")
  public void setSoftware(Software software) {
    this.software = software;
  }

  public VulnerableSoftware withSoftware(Software software) {
    this.software = software;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(VulnerableSoftware.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("software");
    sb.append('=');
    sb.append(((this.software == null) ? "<null>" : this.software));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}