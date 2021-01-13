package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "name",
    "refsource",
    "tags"
})
public class ReferenceLink {

  @JsonProperty("url")
  private String url;

  @JsonProperty("name")
  private String name;

  @JsonProperty("refsource")
  private String refsource;

  @JsonProperty("tags")
  private List<String> tags;

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("name")
  public String getName() {
    return url;
  }

  /**
   * Return {@link URI} of {@link ReferenceLink#url}.
   * 
   * @return type {@link URI} or an {@link Optional#empty()}.
   */
  public Optional<URI> url() {
    try {
      return Optional.ofNullable(new URI(url));
    } catch (URISyntaxException e) {
      return Optional.empty();
    }
  }
}
