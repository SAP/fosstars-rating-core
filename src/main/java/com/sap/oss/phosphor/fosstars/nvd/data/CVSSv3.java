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
  private Version version;

  @JsonProperty("vectorString")
  private String vectorString;

  @JsonProperty("attackVector")
  private AttackVectorType attackVector;

  @JsonProperty("attackComplexity")
  private AttackComplexityType attackComplexity;

  @JsonProperty("privilegesRequired")
  private PrivilegesRequiredType privilegesRequired;

  @JsonProperty("userInteraction")
  private UserInteractionType userInteraction;

  @JsonProperty("scope")
  private ScopeType scope;

  @JsonProperty("confidentialityImpact")
  private CiaType confidentialityImpact;

  @JsonProperty("integrityImpact")
  private CiaType integrityImpact;

  @JsonProperty("availabilityImpact")
  private CiaType availabilityImpact;

  @JsonProperty("baseScore")
  private Double baseScore;

  @JsonProperty("baseSeverity")
  private SeverityType baseSeverity;

  @JsonProperty("exploitCodeMaturity")
  private ExploitCodeMaturityType exploitCodeMaturity;

  @JsonProperty("remediationLevel")
  private RemediationLevelType remediationLevel;

  @JsonProperty("reportConfidence")
  private ConfidenceType reportConfidence;

  @JsonProperty("temporalScore")
  private Double temporalScore;

  @JsonProperty("temporalSeverity")
  private SeverityType temporalSeverity;

  @JsonProperty("confidentialityRequirement")
  private CiaRequirementType confidentialityRequirement;

  @JsonProperty("integrityRequirement")
  private CiaRequirementType integrityRequirement;

  @JsonProperty("availabilityRequirement")
  private CiaRequirementType availabilityRequirement;

  @JsonProperty("modifiedAttackVector")
  private ModifiedAttackVectorType modifiedAttackVector;

  @JsonProperty("modifiedAttackComplexity")
  private ModifiedAttackComplexityType modifiedAttackComplexity;

  @JsonProperty("modifiedPrivilegesRequired")
  private ModifiedPrivilegesRequiredType modifiedPrivilegesRequired;

  @JsonProperty("modifiedUserInteraction")
  private ModifiedUserInteractionType modifiedUserInteraction;

  @JsonProperty("modifiedScope")
  private ModifiedScopeType modifiedScope;

  @JsonProperty("modifiedConfidentialityImpact")
  private ModifiedCiaType modifiedConfidentialityImpact;

  @JsonProperty("modifiedIntegrityImpact")
  private ModifiedCiaType modifiedIntegrityImpact;

  @JsonProperty("modifiedAvailabilityImpact")
  private ModifiedCiaType modifiedAvailabilityImpact;

  @JsonProperty("environmentalScore")
  private Double environmentalScore;

  @JsonProperty("environmentalSeverity")
  private SeverityType environmentalSeverity;

  @JsonProperty("version")
  public Version getVersion() {
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

    private static final Map<String, AttackComplexityType> CONSTANTS = new HashMap<>();

    static {
      for (AttackComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AttackComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static AttackComplexityType fromValue(String value) {
      AttackComplexityType constant = CONSTANTS.get(value);
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

    private static final Map<String, AttackVectorType> CONSTANTS = new HashMap<>();

    static {
      for (AttackVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    AttackVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static AttackVectorType fromValue(String value) {
      AttackVectorType constant = CONSTANTS.get(value);
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
    LOW("LOW"),
    HIGH("HIGH");

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

  public enum ConfidenceType {

    UNKNOWN("UNKNOWN"),
    REASONABLE("REASONABLE"),
    CONFIRMED("CONFIRMED"),
    NOT_DEFINED("NOT_DEFINED");

    private static final Map<String, ConfidenceType> CONSTANTS = new HashMap<>();

    static {
      for (ConfidenceType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ConfidenceType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ConfidenceType fromValue(String value) {
      ConfidenceType constant = CONSTANTS.get(value);
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

    private static final Map<String, ExploitCodeMaturityType> CONSTANTS = new HashMap<>();

    static {
      for (ExploitCodeMaturityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ExploitCodeMaturityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ExploitCodeMaturityType fromValue(String value) {
      ExploitCodeMaturityType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedAttackComplexityType> CONSTANTS
        = new HashMap<>();

    static {
      for (ModifiedAttackComplexityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedAttackComplexityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedAttackComplexityType fromValue(String value) {
      ModifiedAttackComplexityType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedAttackVectorType> CONSTANTS = new HashMap<>();

    static {
      for (ModifiedAttackVectorType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedAttackVectorType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedAttackVectorType fromValue(String value) {
      ModifiedAttackVectorType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedCiaType> CONSTANTS = new HashMap<>();

    static {
      for (ModifiedCiaType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedCiaType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedCiaType fromValue(String value) {
      ModifiedCiaType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedPrivilegesRequiredType> CONSTANTS
        = new HashMap<>();

    static {
      for (ModifiedPrivilegesRequiredType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedPrivilegesRequiredType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedPrivilegesRequiredType fromValue(String value) {
      ModifiedPrivilegesRequiredType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedScopeType> CONSTANTS = new HashMap<>();

    static {
      for (ModifiedScopeType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedScopeType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedScopeType fromValue(String value) {
      ModifiedScopeType constant = CONSTANTS.get(value);
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

    private static final Map<String, ModifiedUserInteractionType> CONSTANTS
        = new HashMap<>();

    static {
      for (ModifiedUserInteractionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ModifiedUserInteractionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ModifiedUserInteractionType fromValue(String value) {
      ModifiedUserInteractionType constant = CONSTANTS.get(value);
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

    private static final Map<String, PrivilegesRequiredType> CONSTANTS = new HashMap<>();

    static {
      for (PrivilegesRequiredType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    PrivilegesRequiredType(String value) {
      this.value = value;
    }

    @JsonCreator
    static PrivilegesRequiredType fromValue(String value) {
      PrivilegesRequiredType constant = CONSTANTS.get(value);
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

  public enum ScopeType {

    UNCHANGED("UNCHANGED"),
    CHANGED("CHANGED");

    private static final Map<String, ScopeType> CONSTANTS = new HashMap<>();

    static {
      for (ScopeType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    ScopeType(String value) {
      this.value = value;
    }

    @JsonCreator
    static ScopeType fromValue(String value) {
      ScopeType constant = CONSTANTS.get(value);
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

    private static final Map<String, SeverityType> CONSTANTS = new HashMap<>();

    static {
      for (SeverityType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    SeverityType(String value) {
      this.value = value;
    }

    @JsonCreator
    static SeverityType fromValue(String value) {
      SeverityType constant = CONSTANTS.get(value);
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

    private static final Map<String, UserInteractionType> CONSTANTS = new HashMap<>();

    static {
      for (UserInteractionType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    UserInteractionType(String value) {
      this.value = value;
    }

    @JsonCreator
    static UserInteractionType fromValue(String value) {
      UserInteractionType constant = CONSTANTS.get(value);
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
