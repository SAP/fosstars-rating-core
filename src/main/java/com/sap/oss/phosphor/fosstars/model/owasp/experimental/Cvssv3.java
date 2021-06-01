package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"baseScore", "attackVector", "attackComplexity", "privilegesRequired",
    "userInteraction", "scope", "confidentialityImpact", "integrityImpact", "availabilityImpact",
    "baseSeverity", "exploitabilityScore", "impactScore", "version"})
@Generated("jsonschema2pojo")
public class Cvssv3 implements Serializable {

  @JsonProperty("baseScore")
  private float baseScore;

  @JsonProperty("attackVector")
  private String attackVector;

  @JsonProperty("attackComplexity")
  private String attackComplexity;

  @JsonProperty("privilegesRequired")
  private String privilegesRequired;

  @JsonProperty("userInteraction")
  private String userInteraction;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("confidentialityImpact")
  private String confidentialityImpact;

  @JsonProperty("integrityImpact")
  private String integrityImpact;

  @JsonProperty("availabilityImpact")
  private String availabilityImpact;

  @JsonProperty("baseSeverity")
  private String baseSeverity;

  @JsonProperty("exploitabilityScore")
  private String exploitabilityScore;

  @JsonProperty("impactScore")
  private String impactScore;

  @JsonProperty("version")
  private String version;

  @JsonProperty("baseScore")
  public float getBaseScore() {
    return baseScore;
  }

  @JsonProperty("baseScore")
  public void setBaseScore(float baseScore) {
    this.baseScore = baseScore;
  }

  public Cvssv3 withBaseScore(float baseScore) {
    this.baseScore = baseScore;
    return this;
  }

  @JsonProperty("attackVector")
  public String getAttackVector() {
    return attackVector;
  }

  @JsonProperty("attackVector")
  public void setAttackVector(String attackVector) {
    this.attackVector = attackVector;
  }

  public Cvssv3 withAttackVector(String attackVector) {
    this.attackVector = attackVector;
    return this;
  }

  @JsonProperty("attackComplexity")
  public String getAttackComplexity() {
    return attackComplexity;
  }

  @JsonProperty("attackComplexity")
  public void setAttackComplexity(String attackComplexity) {
    this.attackComplexity = attackComplexity;
  }

  public Cvssv3 withAttackComplexity(String attackComplexity) {
    this.attackComplexity = attackComplexity;
    return this;
  }

  @JsonProperty("privilegesRequired")
  public String getPrivilegesRequired() {
    return privilegesRequired;
  }

  @JsonProperty("privilegesRequired")
  public void setPrivilegesRequired(String privilegesRequired) {
    this.privilegesRequired = privilegesRequired;
  }

  public Cvssv3 withPrivilegesRequired(String privilegesRequired) {
    this.privilegesRequired = privilegesRequired;
    return this;
  }

  @JsonProperty("userInteraction")
  public String getUserInteraction() {
    return userInteraction;
  }

  @JsonProperty("userInteraction")
  public void setUserInteraction(String userInteraction) {
    this.userInteraction = userInteraction;
  }

  public Cvssv3 withUserInteraction(String userInteraction) {
    this.userInteraction = userInteraction;
    return this;
  }

  @JsonProperty("scope")
  public String getScope() {
    return scope;
  }

  @JsonProperty("scope")
  public void setScope(String scope) {
    this.scope = scope;
  }

  public Cvssv3 withScope(String scope) {
    this.scope = scope;
    return this;
  }

  @JsonProperty("confidentialityImpact")
  public String getConfidentialityImpact() {
    return confidentialityImpact;
  }

  @JsonProperty("confidentialityImpact")
  public void setConfidentialityImpact(String confidentialityImpact) {
    this.confidentialityImpact = confidentialityImpact;
  }

  public Cvssv3 withConfidentialityImpact(String confidentialityImpact) {
    this.confidentialityImpact = confidentialityImpact;
    return this;
  }

  @JsonProperty("integrityImpact")
  public String getIntegrityImpact() {
    return integrityImpact;
  }

  @JsonProperty("integrityImpact")
  public void setIntegrityImpact(String integrityImpact) {
    this.integrityImpact = integrityImpact;
  }

  public Cvssv3 withIntegrityImpact(String integrityImpact) {
    this.integrityImpact = integrityImpact;
    return this;
  }

  @JsonProperty("availabilityImpact")
  public String getAvailabilityImpact() {
    return availabilityImpact;
  }

  @JsonProperty("availabilityImpact")
  public void setAvailabilityImpact(String availabilityImpact) {
    this.availabilityImpact = availabilityImpact;
  }

  public Cvssv3 withAvailabilityImpact(String availabilityImpact) {
    this.availabilityImpact = availabilityImpact;
    return this;
  }

  @JsonProperty("baseSeverity")
  public String getBaseSeverity() {
    return baseSeverity;
  }

  @JsonProperty("baseSeverity")
  public void setBaseSeverity(String baseSeverity) {
    this.baseSeverity = baseSeverity;
  }

  public Cvssv3 withBaseSeverity(String baseSeverity) {
    this.baseSeverity = baseSeverity;
    return this;
  }

  @JsonProperty("exploitabilityScore")
  public String getExploitabilityScore() {
    return exploitabilityScore;
  }

  @JsonProperty("exploitabilityScore")
  public void setExploitabilityScore(String exploitabilityScore) {
    this.exploitabilityScore = exploitabilityScore;
  }

  public Cvssv3 withExploitabilityScore(String exploitabilityScore) {
    this.exploitabilityScore = exploitabilityScore;
    return this;
  }

  @JsonProperty("impactScore")
  public String getImpactScore() {
    return impactScore;
  }

  @JsonProperty("impactScore")
  public void setImpactScore(String impactScore) {
    this.impactScore = impactScore;
  }

  public Cvssv3 withImpactScore(String impactScore) {
    this.impactScore = impactScore;
    return this;
  }

  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  public Cvssv3 withVersion(String version) {
    this.version = version;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Cvssv3.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("baseScore");
    sb.append('=');
    sb.append(this.baseScore);
    sb.append(',');
    sb.append("attackVector");
    sb.append('=');
    sb.append(((this.attackVector == null) ? "<null>" : this.attackVector));
    sb.append(',');
    sb.append("attackComplexity");
    sb.append('=');
    sb.append(((this.attackComplexity == null) ? "<null>" : this.attackComplexity));
    sb.append(',');
    sb.append("privilegesRequired");
    sb.append('=');
    sb.append(((this.privilegesRequired == null) ? "<null>" : this.privilegesRequired));
    sb.append(',');
    sb.append("userInteraction");
    sb.append('=');
    sb.append(((this.userInteraction == null) ? "<null>" : this.userInteraction));
    sb.append(',');
    sb.append("scope");
    sb.append('=');
    sb.append(((this.scope == null) ? "<null>" : this.scope));
    sb.append(',');
    sb.append("confidentialityImpact");
    sb.append('=');
    sb.append(((this.confidentialityImpact == null) ? "<null>" : this.confidentialityImpact));
    sb.append(',');
    sb.append("integrityImpact");
    sb.append('=');
    sb.append(((this.integrityImpact == null) ? "<null>" : this.integrityImpact));
    sb.append(',');
    sb.append("availabilityImpact");
    sb.append('=');
    sb.append(((this.availabilityImpact == null) ? "<null>" : this.availabilityImpact));
    sb.append(',');
    sb.append("baseSeverity");
    sb.append('=');
    sb.append(((this.baseSeverity == null) ? "<null>" : this.baseSeverity));
    sb.append(',');
    sb.append("exploitabilityScore");
    sb.append('=');
    sb.append(((this.exploitabilityScore == null) ? "<null>" : this.exploitabilityScore));
    sb.append(',');
    sb.append("impactScore");
    sb.append('=');
    sb.append(((this.impactScore == null) ? "<null>" : this.impactScore));
    sb.append(',');
    sb.append("version");
    sb.append('=');
    sb.append(((this.version == null) ? "<null>" : this.version));
    sb.append(']');
    return sb.toString();
  }
}