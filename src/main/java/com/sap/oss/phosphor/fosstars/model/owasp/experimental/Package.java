package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "confidence", "url"})
@Generated("jsonschema2pojo")
public class Package implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("confidence")
  private String confidence;

  @JsonProperty("url")
  private String url;

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  public Package withId(String id) {
    this.id = id;
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

  public Package withConfidence(String confidence) {
    this.confidence = confidence;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  public Package withUrl(String url) {
    this.url = url;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Package.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("id");
    sb.append('=');
    sb.append(((this.id == null) ? "<null>" : this.id));
    sb.append(',');
    sb.append("confidence");
    sb.append('=');
    sb.append(((this.confidence == null) ? "<null>" : this.confidence));
    sb.append(',');
    sb.append("url");
    sb.append('=');
    sb.append(((this.url == null) ? "<null>" : this.url));
    sb.append(']');
    return sb.toString();
  }
}
