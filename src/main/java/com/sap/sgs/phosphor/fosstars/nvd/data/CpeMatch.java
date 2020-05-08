package com.sap.sgs.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * CPE match string or range.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "vulnerable",
    "cpe22Uri",
    "cpe23Uri",
    "versionStartExcluding",
    "versionStartIncluding",
    "versionEndExcluding",
    "versionEndIncluding",
    "cpe_name"
})
public class CpeMatch {

  @JsonProperty("vulnerable")
  private Boolean vulnerable;

  @JsonProperty("cpe22Uri")
  private String cpe22Uri;

  @JsonProperty("cpe23Uri")
  private String cpe23Uri;

  @JsonProperty("versionStartExcluding")
  private String versionStartExcluding;

  @JsonProperty("versionStartIncluding")
  private String versionStartIncluding;

  @JsonProperty("versionEndExcluding")
  private String versionEndExcluding;

  @JsonProperty("versionEndIncluding")
  private String versionEndIncluding;

  @JsonProperty("cpe_name")
  private List<CpeName> cpeName = new ArrayList<>();

  @JsonProperty("vulnerable")
  public Boolean getVulnerable() {
    return vulnerable;
  }

  @JsonProperty("versionEndExcluding")
  public String getVersionEndExcluding() {
    return versionEndExcluding;
  }

  @JsonProperty("versionEndIncluding")
  public String getVersionEndIncluding() {
    return versionEndIncluding;
  }

  @JsonProperty("cpe22Uri")
  public String getCpe22Uri() {
    return cpe22Uri;
  }

  @JsonProperty("cpe23Uri")
  public String getCpe23Uri() {
    return cpe23Uri;
  }
}
