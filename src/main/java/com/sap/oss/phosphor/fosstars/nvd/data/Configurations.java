package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the set of product configurations for a NVD applicability statement.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CVE_data_version",
    "nodes"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "CVE_data_version"
})
public class Configurations {

  @JsonProperty("CVE_data_version")
  private String cveDataVersion;

  @JsonProperty("nodes")
  private List<Node> nodes = new ArrayList<>();

  @JsonProperty("nodes")
  public List<Node> getNodes() {
    return nodes;
  }
}
