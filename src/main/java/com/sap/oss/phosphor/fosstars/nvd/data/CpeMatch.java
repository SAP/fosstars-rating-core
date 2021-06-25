package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

/**
 * CPE match string or range.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cpe22Uri",
    "cpe23Uri",
    "versionStartIncluding",
    "versionEndIncluding"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "vulnerable",
    "cpe_name",
    "versionStartExcluding",
    "versionEndExcluding"
})
public class CpeMatch {

  private final Cpe22Uri cpe22Uri;
  private final Cpe23Uri cpe23Uri;

  private final String versionStartIncluding;
  private final String versionEndIncluding;

  private CpeMatch(
      @JsonProperty("cpe22Uri") @Nullable String cpe22Uri,
      @JsonProperty("cpe23Uri") @Nullable String cpe23Uri,
      @JsonProperty("versionStartIncluding") @Nullable String versionStartIncluding,
      @JsonProperty("versionEndIncluding") @Nullable String versionEndIncluding) {

    this.cpe22Uri = StringUtils.isEmpty(cpe22Uri) ? null : new Cpe22Uri(cpe22Uri);
    this.cpe23Uri = StringUtils.isEmpty(cpe23Uri) ? null : new Cpe23Uri(cpe23Uri);
    this.versionStartIncluding = versionStartIncluding;
    this.versionEndIncluding = versionEndIncluding;
  }

  public String versionEndIncluding() {
    return versionEndIncluding;
  }

  public String versionStartIncluding() {
    return versionStartIncluding;
  }

  /**
   * Get the {@link CpeUri} instance if available.
   * It will check which CPE URI format is applicable and return the appropriate instance.
   * 
   * @return A {@link CpeUri} if available.
   */
  public Optional<CpeUri> getCpeUri() {
    return cpe23Uri != null ? Optional.of(cpe23Uri) : Optional.ofNullable(cpe22Uri);
  }
}
