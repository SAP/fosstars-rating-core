package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "confidence", "source", "name", "value"})
@Generated("jsonschema2pojo")
public class VendorEvidence implements Serializable {

  @JsonProperty("type")
  private String type;

  @JsonProperty("confidence")
  private String confidence;

  @JsonProperty("source")
  private String source;

  @JsonProperty("name")
  private String name;

  @JsonProperty("value")
  private String value;

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  public VendorEvidence withType(String type) {
    this.type = type;
    return this;
  }

  @JsonProperty("confidence")
  public String getConfidence() {
    return confidence;
  }

  @JsonProperty("confidence")
  public void setConfidence(String confidence) {
    this.confidence = confidence;
  }

  public VendorEvidence withConfidence(String confidence) {
    this.confidence = confidence;
    return this;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  public VendorEvidence withSource(String source) {
    this.source = source;
    return this;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public VendorEvidence withName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  public VendorEvidence withValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(VendorEvidence.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("type");
    sb.append('=');
    sb.append(((this.type == null) ? "<null>" : this.type));
    sb.append(',');
    sb.append("confidence");
    sb.append('=');
    sb.append(((this.confidence == null) ? "<null>" : this.confidence));
    sb.append(',');
    sb.append("source");
    sb.append('=');
    sb.append(((this.source == null) ? "<null>" : this.source));
    sb.append(',');
    sb.append("name");
    sb.append('=');
    sb.append(((this.name == null) ? "<null>" : this.name));
    sb.append(',');
    sb.append("value");
    sb.append('=');
    sb.append(((this.value == null) ? "<null>" : this.value));
    sb.append(']');
    return sb.toString();
  }
}
