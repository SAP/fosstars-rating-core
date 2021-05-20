package com.sap.oss.phosphor.fosstars.data.github.experimental.owasp.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"source", "url", "name"})
@Generated("jsonschema2pojo")
public class OwaspDependencyCheckReference implements Serializable {

  @JsonProperty("source")
  private String source;

  @JsonProperty("url")
  private String url;

  @JsonProperty("name")
  private String name;

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  public OwaspDependencyCheckReference withSource(String source) {
    this.source = source;
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

  public OwaspDependencyCheckReference withUrl(String url) {
    this.url = url;
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

  public OwaspDependencyCheckReference withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(OwaspDependencyCheckReference.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("source");
    sb.append('=');
    sb.append(((this.source == null) ? "<null>" : this.source));
    sb.append(',');
    sb.append("url");
    sb.append('=');
    sb.append(((this.url == null) ? "<null>" : this.url));
    sb.append(',');
    sb.append("name");
    sb.append('=');
    sb.append(((this.name == null) ? "<null>" : this.name));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}
