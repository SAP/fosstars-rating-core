package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * CPE match string or range.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cpe22Uri",
    "cpe23Uri",
    "versionStartExcluding",
    "versionStartIncluding",
    "versionEndExcluding",
    "versionEndIncluding"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "vulnerable",
    "cpe_name"
})
public class CpeMatch {

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

  @JsonProperty("versionEndExcluding")
  public String getVersionEndExcluding() {
    return versionEndExcluding;
  }

  @JsonProperty("versionEndIncluding")
  public String getVersionEndIncluding() {
    return versionEndIncluding;
  }

  @JsonProperty("versionStartIncluding")
  public String getVersionStartIncluding() {
    return versionStartIncluding;
  }

  /**
   * Get the {@link CpeUri} instance if available.
   * It will check which CPE URI format is applicable and return the appropriate instance.
   * 
   * @return A {@link CpeUri} if available.
   */
  public Optional<CpeUri> getCpeUri() {
    if (!StringUtils.isEmpty(cpe23Uri)) {
      return Optional.of(new Cpe23Uri(cpe23Uri));
    }

    if (!StringUtils.isEmpty(cpe22Uri)) {
      return Optional.of(new Cpe22Uri(cpe22Uri));
    }

    return Optional.empty();
  }
}
