package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;


/**
 * JSON Schema for Common Vulnerability Scoring System version 3.x (BETA)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "vectorString",
    "attackVector",
    "attackComplexity",
    "privilegesRequired",
    "userInteraction",
    "scope",
    "confidentialityImpact",
    "integrityImpact",
    "availabilityImpact",
    "baseScore",
    "baseSeverity",
    "exploitCodeMaturity",
    "remediationLevel",
    "reportConfidence",
    "temporalScore",
    "temporalSeverity",
    "confidentialityRequirement",
    "integrityRequirement",
    "availabilityRequirement",
    "modifiedAttackVector",
    "modifiedAttackComplexity",
    "modifiedPrivilegesRequired",
    "modifiedUserInteraction",
    "modifiedScope",
    "modifiedConfidentialityImpact",
    "modifiedIntegrityImpact",
    "modifiedAvailabilityImpact",
    "environmentalScore",
    "environmentalSeverity"
})
public class CVSSv3 {

  @JsonProperty("version")
  private CVSSv3.Version version;

  @JsonProperty("vectorString")
  private String vectorString;

  @JsonProperty("attackVector")
  private CVSSv3.AttackVectorType attackVector;

  @JsonProperty("attackComplexity")
  private CVSSv3.AttackComplexityType attackComplexity;

  @JsonProperty("privilegesRequired")
  private CVSSv3.PrivilegesRequiredType privilegesRequired;

  @JsonProperty("userInteraction")
  private CVSSv3.UserInteractionType userInteraction;

  @JsonProperty("scope")
  private CVSSv3.ScopeType scope;

  @JsonProperty("confidentialityImpact")
  private CVSSv3.CiaType confidentialityImpact;

  @JsonProperty("integrityImpact")
  private CVSSv3.CiaType integrityImpact;

  @JsonProperty("availabilityImpact")
  private CVSSv3.CiaType availabilityImpact;

  @JsonProperty("baseScore")
  private Double baseScore;

  @JsonProperty("baseSeverity")
  private CVSSv3.SeverityType baseSeverity;

  @JsonProperty("exploitCodeMaturity")
  private CVSSv3.ExploitCodeMaturityType exploitCodeMaturity;

  @JsonProperty("remediationLevel")
  private CVSSv3.RemediationLevelType remediationLevel;

  @JsonProperty("reportConfidence")
  private CVSSv3.ConfidenceType reportConfidence;

  @JsonProperty("temporalScore")
  private Double temporalScore;

  @JsonProperty("temporalSeverity")
  private CVSSv3.SeverityType temporalSeverity;

  @JsonProperty("confidentialityRequirement")
  private CVSSv3.CiaRequirementType confidentialityRequirement;

  @JsonProperty("integrityRequirement")
  private CVSSv3.CiaRequirementType integrityRequirement;

  @JsonProperty("availabilityRequirement")
  private CVSSv3.CiaRequirementType availabilityRequirement;

  @JsonProperty("modifiedAttackVector")
  private CVSSv3.ModifiedAttackVectorType modifiedAttackVector;

  @JsonProperty("modifiedAttackComplexity")
  private CVSSv3.ModifiedAttackComplexityType modifiedAttackComplexity;

  @JsonProperty("modifiedPrivilegesRequired")
  private CVSSv3.ModifiedPrivilegesRequiredType modifiedPrivilegesRequired;

  @JsonProperty("modifiedUserInteraction")
  private CVSSv3.ModifiedUserInteractionType modifiedUserInteraction;

  @JsonProperty("modifiedScope")
  private CVSSv3.ModifiedScopeType modifiedScope;

  @JsonProperty("modifiedConfidentialityImpact")
  private CVSSv3.ModifiedCiaType modifiedConfidentialityImpact;

  @JsonProperty("modifiedIntegrityImpact")
  private CVSSv3.ModifiedCiaType modifiedIntegrityImpact;

  @JsonProperty("modifiedAvailabilityImpact")
  private CVSSv3.ModifiedCiaType modifiedAvailabilityImpact;

  @JsonProperty("environmentalScore")
  private Double environmentalScore;

  @JsonProperty("environmentalSeverity")
  private CVSSv3.SeverityType environmentalSeverity;

  @JsonProperty("version")
  public CVSSv3.Version getVersion() {
    return version;
  }

  @JsonProperty("baseScore")
  public double getBaseScore() {
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

  public enum AttackComplexityType {

    HIGH("HIGH"),
    LOW("LOW");

    private static final Map<String, CVSSv3.AttackComplexityType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.AttackComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AttackComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.AttackComplexityType fromValue(String value) {
      CVSSv3.AttackComplexityType constant = CONSTANTS.get(value);
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

  public enum AttackVectorType {

    NETWORK("NETWORK"),
    ADJACENT_NETWORK("ADJACENT_NETWORK"),
    LOCAL("LOCAL"),
    PHYSICAL("PHYSICAL");

    private static final Map<String, CVSSv3.AttackVectorType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.AttackVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AttackVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.AttackVectorType fromValue(String value) {
      CVSSv3.AttackVectorType constant = CONSTANTS.get(value);
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

    private static final Map<String, CVSSv3.CiaRequirementType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.CiaRequirementType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaRequirementType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.CiaRequirementType fromValue(String value) {
      CVSSv3.CiaRequirementType constant = CONSTANTS.get(value);
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
    LOW("LOW"),
    HIGH("HIGH");

    private static final Map<String, CVSSv3.CiaType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.CiaType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    CiaType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.CiaType fromValue(String value) {
      CVSSv3.CiaType constant = CONSTANTS.get(value);
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

  public enum ConfidenceType {

    UNKNOWN("UNKNOWN"),
    REASONABLE("REASONABLE"),
    CONFIRMED("CONFIRMED"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ConfidenceType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ConfidenceType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ConfidenceType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ConfidenceType fromValue(String value) {
      CVSSv3.ConfidenceType constant = CONSTANTS.get(value);
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

  public enum ExploitCodeMaturityType {

    UNPROVEN("UNPROVEN"),
    PROOF_OF_CONCEPT("PROOF_OF_CONCEPT"),
    FUNCTIONAL("FUNCTIONAL"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ExploitCodeMaturityType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ExploitCodeMaturityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ExploitCodeMaturityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ExploitCodeMaturityType fromValue(String value) {
      CVSSv3.ExploitCodeMaturityType constant = CONSTANTS.get(value);
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

  public enum ModifiedAttackComplexityType {

    HIGH("HIGH"),
    LOW("LOW"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedAttackComplexityType> CONSTANTS
        = new HashMap<>();

    static {
      for (CVSSv3.ModifiedAttackComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedAttackComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedAttackComplexityType fromValue(String value) {
      CVSSv3.ModifiedAttackComplexityType constant = CONSTANTS.get(value);
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

  public enum ModifiedAttackVectorType {

    NETWORK("NETWORK"),
    ADJACENT_NETWORK("ADJACENT_NETWORK"),
    LOCAL("LOCAL"),
    PHYSICAL("PHYSICAL"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedAttackVectorType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ModifiedAttackVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedAttackVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedAttackVectorType fromValue(String value) {
      CVSSv3.ModifiedAttackVectorType constant = CONSTANTS.get(value);
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

  public enum ModifiedCiaType {

    NONE("NONE"),
    LOW("LOW"),
    HIGH("HIGH"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedCiaType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ModifiedCiaType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedCiaType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedCiaType fromValue(String value) {
      CVSSv3.ModifiedCiaType constant = CONSTANTS.get(value);
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

  public enum ModifiedPrivilegesRequiredType {

    HIGH("HIGH"),
    LOW("LOW"),
    NONE("NONE"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedPrivilegesRequiredType> CONSTANTS
        = new HashMap<>();

    static {
      for (CVSSv3.ModifiedPrivilegesRequiredType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedPrivilegesRequiredType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedPrivilegesRequiredType fromValue(String value) {
      CVSSv3.ModifiedPrivilegesRequiredType constant = CONSTANTS.get(value);
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

  public enum ModifiedScopeType {

    UNCHANGED("UNCHANGED"),
    CHANGED("CHANGED"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedScopeType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ModifiedScopeType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedScopeType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedScopeType fromValue(String value) {
      CVSSv3.ModifiedScopeType constant = CONSTANTS.get(value);
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

  public enum ModifiedUserInteractionType {

    NONE("NONE"),
    REQUIRED("REQUIRED"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, CVSSv3.ModifiedUserInteractionType> CONSTANTS
        = new HashMap<>();

    static {
      for (CVSSv3.ModifiedUserInteractionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedUserInteractionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ModifiedUserInteractionType fromValue(String value) {
      CVSSv3.ModifiedUserInteractionType constant = CONSTANTS.get(value);
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

  public enum PrivilegesRequiredType {

    HIGH("HIGH"),
    LOW("LOW"),
    NONE("NONE");

    private static final Map<String, CVSSv3.PrivilegesRequiredType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.PrivilegesRequiredType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    PrivilegesRequiredType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.PrivilegesRequiredType fromValue(String value) {
      CVSSv3.PrivilegesRequiredType constant = CONSTANTS.get(value);
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

    private static final Map<String, CVSSv3.RemediationLevelType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.RemediationLevelType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    RemediationLevelType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.RemediationLevelType fromValue(String value) {
      CVSSv3.RemediationLevelType constant = CONSTANTS.get(value);
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

  public enum ScopeType {

    UNCHANGED("UNCHANGED"),
    CHANGED("CHANGED");

    private static final Map<String, CVSSv3.ScopeType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.ScopeType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ScopeType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.ScopeType fromValue(String value) {
      CVSSv3.ScopeType constant = CONSTANTS.get(value);
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

  public enum SeverityType {

    NONE("NONE"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    private static final Map<String, CVSSv3.SeverityType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.SeverityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    SeverityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.SeverityType fromValue(String value) {
      CVSSv3.SeverityType constant = CONSTANTS.get(value);
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

  public enum UserInteractionType {

    NONE("NONE"),
    REQUIRED("REQUIRED");

    private static final Map<String, CVSSv3.UserInteractionType> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.UserInteractionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    UserInteractionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.UserInteractionType fromValue(String value) {
      CVSSv3.UserInteractionType constant = CONSTANTS.get(value);
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

    _3_0("3.0"),
    _3_1("3.1");

    private static final Map<String, CVSSv3.Version> CONSTANTS = new HashMap<>();

    static {
      for (CVSSv3.Version c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    Version(String value) {
      this.value = value;
    }

    @JsonCreator
    static CVSSv3.Version fromValue(String value) {
      CVSSv3.Version constant = CONSTANTS.get(value);
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
