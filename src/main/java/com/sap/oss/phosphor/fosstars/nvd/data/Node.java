package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a node or sub-node in an NVD applicability statement.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "operator",
    "negate",
    "children",
    "cpe_match"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "operator",
    "negate"
})
public class Node {

  @JsonProperty("operator")
  private String operator;

  @JsonProperty("negate")
  private Boolean negate;

  @JsonProperty("children")
  private List<Node> children = new ArrayList<>();

  @JsonProperty("cpe_match")
  private List<CpeMatch> cpeMatch = new ArrayList<>();

  @JsonProperty("cpe_match")
  public List<CpeMatch> getCpeMatches() {
    return cpeMatch;
  }

  @JsonProperty("children")
  public List<Node> getChildren() {
    return children;
  }
}
