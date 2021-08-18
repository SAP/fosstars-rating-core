package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON Schema for Common Vulnerability Scoring System version 2.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "vectorString",
    "accessVector",
    "accessComplexity",
    "authentication",
    "confidentialityImpact",
    "integrityImpact",
    "availabilityImpact",
    "baseScore",
    "exploitability",
    "remediationLevel",
    "reportConfidence",
    "temporalScore",
    "collateralDamagePotential",
    "targetDistribution",
    "confidentialityRequirement",
    "integrityRequirement",
    "availabilityRequirement",
    "environmentalScore"
})
public class CVSSv2 {

  @JsonProperty("version")
  private CVSSv2.Version version;

  @JsonProperty("vectorString")
  private String vectorString;

  @JsonProperty("accessVector")
  private CVSSv2.AccessVectorType accessVector;

  @JsonProperty("accessComplexity")
  private CVSSv2.AccessComplexityType accessComplexity;

  @JsonProperty("authentication")
  private CVSSv2.AuthenticationType authentication;

  @JsonProperty("confidentialityImpact")
  private CVSSv2.CiaType confidentialityImpact;

  @JsonProperty("integrityImpact")
  private CVSSv2.CiaType integrityImpact;

  @JsonProperty("availabilityImpact")
  private CVSSv2.CiaType availabilityImpact;

  @JsonProperty("baseScore")
  private Double baseScore;

  @JsonProperty("exploitability")
  private CVSSv2.ExploitabilityType exploitability;

  @JsonProperty("remediationLevel")
  private CVSSv2.RemediationLevelType remediationLevel;

  @JsonProperty("reportConfidence")
  private CVSSv2.ReportConfidenceType reportConfidence;

  @JsonProperty("temporalScore")
  private Double temporalScore;

  @JsonProperty("collateralDamagePotential")
  private CVSSv2.CollateralDamagePotentialType collateralDamagePotential;

  @JsonProperty("targetDistribution")
  private CVSSv2.TargetDistributionType targetDistribution;

  @JsonProperty("confidentialityRequirement")
  private CVSSv2.CiaRequirementType confidentialityRequirement;

  @JsonProperty("integrityRequirement")
  private CVSSv2.CiaRequirementType integrityRequirement;

  @JsonProperty("availabilityRequirement")
  private CVSSv2.CiaRequirementType availabilityRequirement;

  @JsonProperty("environmentalScore")
  private Double environmentalScore;

  @JsonProperty("version")
  public CVSSv2.Version getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(CVSSv2.Version version) {
    this.version = version;
  }

  @JsonProperty("baseScore")
  public Double getBaseScore() {
    return baseScore;
  }

  @JsonProperty("confidentialityImpact")
  public CiaType confidentialityImpact() {
    return confidentialityImpact;
  }

  @JsonProperty("integrityImpact")
  public CiaType integrityImpact() {
    return integrityImpact;
  }

  @JsonProperty("availabilityImpact")
  public CiaType availabilityImpact() {
    return availabilityImpact;
  }

  public enum AccessComplexityType {

    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW");

    private static final Map<String, CVSSv2.AccessComplexityType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.AccessComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AccessComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.AccessComplexityType fromValue(String value) {
      CVSSv2.AccessComplexityType constant = CONSTANTS.get(value);
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

  public enum AccessVectorType {

    NETWORK("NETWORK"),
    ADJACENT_NETWORK("ADJACENT_NETWORK"),
    LOCAL("LOCAL");

    private static final Map<String, CVSSv2.AccessVectorType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.AccessVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AccessVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.AccessVectorType fromValue(String value) {
      CVSSv2.AccessVectorType constant = CONSTANTS.get(value);
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

  public enum AuthenticationType {

    MULTIPLE("MULTIPLE"),
    SINGLE("SINGLE"),
    NONE("NONE");

    private static final Map<String, CVSSv2.AuthenticationType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.AuthenticationType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AuthenticationType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.AuthenticationType fromValue(String value) {
      CVSSv2.AuthenticationType constant = CONSTANTS.get(value);
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

  public enum CiaRequirementType {

    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.CiaRequirementType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.CiaRequirementType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaRequirementType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.CiaRequirementType fromValue(String value) {
      CVSSv2.CiaRequirementType constant = CONSTANTS.get(value);
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

  public enum CiaType {

    NONE("NONE"),
    PARTIAL("PARTIAL"),
    COMPLETE("COMPLETE");

    private static final Map<String, CVSSv2.CiaType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.CiaType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.CiaType fromValue(String value) {
      CVSSv2.CiaType constant = CONSTANTS.get(value);
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

  public enum CollateralDamagePotentialType {

    NONE("NONE"),
    LOW("LOW"),
    LOW_MEDIUM("LOW_MEDIUM"),
    MEDIUM_HIGH("MEDIUM_HIGH"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.CollateralDamagePotentialType> CONSTANTS
        = new HashMap<>();

    static {
      for (CVSSv2.CollateralDamagePotentialType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CollateralDamagePotentialType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.CollateralDamagePotentialType fromValue(String value) {
      CVSSv2.CollateralDamagePotentialType constant = CONSTANTS.get(value);
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

  public enum ExploitabilityType {

    UNPROVEN("UNPROVEN"),
    PROOF_OF_CONCEPT("PROOF_OF_CONCEPT"),
    FUNCTIONAL("FUNCTIONAL"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.ExploitabilityType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.ExploitabilityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ExploitabilityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.ExploitabilityType fromValue(String value) {
      CVSSv2.ExploitabilityType constant = CONSTANTS.get(value);
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

  public enum RemediationLevelType {

    OFFICIAL_FIX("OFFICIAL_FIX"),
    TEMPORARY_FIX("TEMPORARY_FIX"),
    WORKAROUND("WORKAROUND"),
    UNAVAILABLE("UNAVAILABLE"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.RemediationLevelType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.RemediationLevelType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    RemediationLevelType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.RemediationLevelType fromValue(String value) {
      CVSSv2.RemediationLevelType constant = CONSTANTS.get(value);
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

  public enum ReportConfidenceType {

    UNCONFIRMED("UNCONFIRMED"),
    UNCORROBORATED("UNCORROBORATED"),
    CONFIRMED("CONFIRMED"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.ReportConfidenceType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.ReportConfidenceType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ReportConfidenceType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.ReportConfidenceType fromValue(String value) {
      CVSSv2.ReportConfidenceType constant = CONSTANTS.get(value);
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

  public enum TargetDistributionType {

    NONE("NONE"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv2.TargetDistributionType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.TargetDistributionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    TargetDistributionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.TargetDistributionType fromValue(String value) {
      CVSSv2.TargetDistributionType constant = CONSTANTS.get(value);
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


  /**
   * CVSS Version.
   */
  public enum Version {

    _2_0("2.0");

    private static final Map<String, CVSSv2.Version> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv2.Version c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    Version(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv2.Version fromValue(String value) {
      CVSSv2.Version constant = CONSTANTS.get(value);
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
