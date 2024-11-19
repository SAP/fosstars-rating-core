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
  private Version version;

  @JsonProperty("vectorString")
  private String vectorString;

  @JsonProperty("accessVector")
  private AccessVectorType accessVector;

  @JsonProperty("accessComplexity")
  private AccessComplexityType accessComplexity;

  @JsonProperty("authentication")
  private AuthenticationType authentication;

  @JsonProperty("confidentialityImpact")
  private CiaType confidentialityImpact;

  @JsonProperty("integrityImpact")
  private CiaType integrityImpact;

  @JsonProperty("availabilityImpact")
  private CiaType availabilityImpact;

  @JsonProperty("baseScore")
  private Double baseScore;

  @JsonProperty("exploitability")
  private ExploitabilityType exploitability;

  @JsonProperty("remediationLevel")
  private RemediationLevelType remediationLevel;

  @JsonProperty("reportConfidence")
  private ReportConfidenceType reportConfidence;

  @JsonProperty("temporalScore")
  private Double temporalScore;

  @JsonProperty("collateralDamagePotential")
  private CollateralDamagePotentialType collateralDamagePotential;

  @JsonProperty("targetDistribution")
  private TargetDistributionType targetDistribution;

  @JsonProperty("confidentialityRequirement")
  private CiaRequirementType confidentialityRequirement;

  @JsonProperty("integrityRequirement")
  private CiaRequirementType integrityRequirement;

  @JsonProperty("availabilityRequirement")
  private CiaRequirementType availabilityRequirement;

  @JsonProperty("environmentalScore")
  private Double environmentalScore;

  @JsonProperty("version")
  public Version getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(Version version) {
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

    private static final Map<String, AccessComplexityType> CONSTANTS = new HashMap<>();

    static {
      for (AccessComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AccessComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static AccessComplexityType fromValue(String value) {
      AccessComplexityType constant = CONSTANTS.get(value);
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

    private static final Map<String, AccessVectorType> CONSTANTS = new HashMap<>();

    static {
      for (AccessVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AccessVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static AccessVectorType fromValue(String value) {
      AccessVectorType constant = CONSTANTS.get(value);
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

    private static final Map<String, AuthenticationType> CONSTANTS = new HashMap<>();

    static {
      for (AuthenticationType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AuthenticationType(String value) {
      this.value = value;
    }

    @JsonCreator
    static AuthenticationType fromValue(String value) {
      AuthenticationType constant = CONSTANTS.get(value);
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

    private static final Map<String, CiaRequirementType> CONSTANTS = new HashMap<>();

    static {
      for (CiaRequirementType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaRequirementType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CiaRequirementType fromValue(String value) {
      CiaRequirementType constant = CONSTANTS.get(value);
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

    private static final Map<String, CiaType> CONSTANTS = new HashMap<>();

    static {
      for (CiaType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CiaType fromValue(String value) {
      CiaType constant = CONSTANTS.get(value);
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

    private static final Map<String, CollateralDamagePotentialType> CONSTANTS
        = new HashMap<>();

    static {
      for (CollateralDamagePotentialType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CollateralDamagePotentialType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CollateralDamagePotentialType fromValue(String value) {
      CollateralDamagePotentialType constant = CONSTANTS.get(value);
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

    private static final Map<String, ExploitabilityType> CONSTANTS = new HashMap<>();

    static {
      for (ExploitabilityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ExploitabilityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ExploitabilityType fromValue(String value) {
      ExploitabilityType constant = CONSTANTS.get(value);
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

    private static final Map<String, RemediationLevelType> CONSTANTS = new HashMap<>();

    static {
      for (RemediationLevelType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    RemediationLevelType(String value) {
      this.value = value;
    }

    @JsonCreator
    static RemediationLevelType fromValue(String value) {
      RemediationLevelType constant = CONSTANTS.get(value);
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

    private static final Map<String, ReportConfidenceType> CONSTANTS = new HashMap<>();

    static {
      for (ReportConfidenceType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ReportConfidenceType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ReportConfidenceType fromValue(String value) {
      ReportConfidenceType constant = CONSTANTS.get(value);
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

    private static final Map<String, TargetDistributionType> CONSTANTS = new HashMap<>();

    static {
      for (TargetDistributionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    TargetDistributionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static TargetDistributionType fromValue(String value) {
      TargetDistributionType constant = CONSTANTS.get(value);
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

    private static final Map<String, Version> CONSTANTS = new HashMap<>();

    static {
      for (Version c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    Version(String value) {
      this.value = value;
    }

    @JsonCreator
    static Version fromValue(String value) {
      Version constant = CONSTANTS.get(value);
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
