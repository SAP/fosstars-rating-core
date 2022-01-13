
package com.sap.oss.phosphor.fosstars.data.npmaudit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Advisory {

  @JsonProperty("cves")
  private List<String> cves = null;

  @JsonProperty("patched_versions")
  private String patchedVersions;

  @JsonProperty("cves")
  public List<String> getCves() {
    return cves;
  }

  @JsonProperty("patched_versions")
  public String getPatchedVersions() {
    return patchedVersions;
  }
}