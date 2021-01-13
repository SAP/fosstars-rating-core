package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data_type",
    "data_format",
    "data_version",
    "CVE_data_meta",
    "affects",
    "problemtype",
    "references",
    "description"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "problemtype",
    "data_version",
    "data_format",
    "data_type"
})
public class CVE {

  @JsonProperty("data_type")
  private CVE.DataType dataType;

  @JsonProperty("data_format")
  private CVE.DataFormat dataFormat;

  @JsonProperty("data_version")
  private CVE.DataVersion dataVersion;

  @JsonProperty("CVE_data_meta")
  private CveMetaData cveDataMetaData;

  @JsonProperty("affects")
  private Affects affects;

  @JsonProperty("problemtype")
  private ProblemType problemType;

  @JsonProperty("references")
  private References references;

  @JsonProperty("description")
  private Description description;

  @JsonProperty("data_format")
  public CVE.DataFormat getDataFormat() {
    return dataFormat;
  }

  @JsonProperty("CVE_data_meta")
  public CveMetaData getCveDataMeta() {
    return cveDataMetaData;
  }

  @JsonProperty("affects")
  public Affects getAffects() {
    return affects;
  }

  @JsonProperty("references")
  public References getReferences() {
    return references;
  }

  @JsonProperty("description")
  public Description getDescription() {
    return description;
  }

  public enum DataFormat {

    MITRE("MITRE");

    private static final Map<String, CVE.DataFormat> CONSTANTS = new HashMap<>();

    static {
      for (CVE.DataFormat c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    DataFormat(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVE.DataFormat fromValue(String value) {
      CVE.DataFormat constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @Override
    public String toString() {
      return this.value;
    }

    @JsonValue
    public String value() {
      return this.value;
    }

  }

  public enum DataType {

    CVE("CVE");

    private static final Map<String, DataType> CONSTANTS = new HashMap<>();

    static {
      for (DataType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    DataType(String value) {
      this.value = value;
    }

    @JsonCreator
    static DataType fromValue(String value) {
      DataType constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @Override
    public String toString() {
      return this.value;
    }

    @JsonValue
    public String value() {
      return this.value;
    }

  }

  public enum DataVersion {

    _4_0("4.0");

    private static final Map<String, CVE.DataVersion> CONSTANTS = new HashMap<>();

    static {
      for (CVE.DataVersion c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    DataVersion(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVE.DataVersion fromValue(String value) {
      CVE.DataVersion constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @Override
    public String toString() {
      return this.value;
    }

    @JsonValue
    public String value() {
      return this.value;
    }
  }

}
