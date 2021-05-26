package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"score", "accessVector", "accessComplexity", "authenticationr",
    "confidentialImpact", "integrityImpact", "availabilityImpact", "severity", "version",
    "exploitabilityScore", "impactScore", "userInteractionRequired"})
@Generated("jsonschema2pojo")
public class Cvssv2 implements Serializable {

  @JsonProperty("score")
  private float score;

  @JsonProperty("accessVector")
  private String accessVector;

  @JsonProperty("accessComplexity")
  private String accessComplexity;

  @JsonProperty("authenticationr")
  private String authenticationr;

  @JsonProperty("confidentialImpact")
  private String confidentialImpact;

  @JsonProperty("integrityImpact")
  private String integrityImpact;

  @JsonProperty("availabilityImpact")
  private String availabilityImpact;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("version")
  private String version;

  @JsonProperty("exploitabilityScore")
  private String exploitabilityScore;

  @JsonProperty("impactScore")
  private String impactScore;

  @JsonProperty("userInteractionRequired")
  private boolean userInteractionRequired;

  @JsonProperty("score")
  public float getScore() {
    return score;
  }

  @JsonProperty("score")
  public void setScore(float score) {
    this.score = score;
  }

  public Cvssv2 withScore(float score) {
    this.score = score;
    return this;
  }

  @JsonProperty("accessVector")
  public String getAccessVector() {
    return accessVector;
  }

  @JsonProperty("accessVector")
  public void setAccessVector(String accessVector) {
    this.accessVector = accessVector;
  }

  public Cvssv2 withAccessVector(String accessVector) {
    this.accessVector = accessVector;
    return this;
  }

  @JsonProperty("accessComplexity")
  public String getAccessComplexity() {
    return accessComplexity;
  }

  @JsonProperty("accessComplexity")
  public void setAccessComplexity(String accessComplexity) {
    this.accessComplexity = accessComplexity;
  }

  public Cvssv2 withAccessComplexity(String accessComplexity) {
    this.accessComplexity = accessComplexity;
    return this;
  }

  @JsonProperty("authenticationr")
  public String getAuthenticationr() {
    return authenticationr;
  }

  @JsonProperty("authenticationr")
  public void setAuthenticationr(String authenticationr) {
    this.authenticationr = authenticationr;
  }

  public Cvssv2 withAuthenticationr(String authenticationr) {
    this.authenticationr = authenticationr;
    return this;
  }

  @JsonProperty("confidentialImpact")
  public String getConfidentialImpact() {
    return confidentialImpact;
  }

  @JsonProperty("confidentialImpact")
  public void setConfidentialImpact(String confidentialImpact) {
    this.confidentialImpact = confidentialImpact;
  }

  public Cvssv2 withConfidentialImpact(String confidentialImpact) {
    this.confidentialImpact = confidentialImpact;
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

  public Cvssv2 withIntegrityImpact(String integrityImpact) {
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

  public Cvssv2 withAvailabilityImpact(String availabilityImpact) {
    this.availabilityImpact = availabilityImpact;
    return this;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public Cvssv2 withSeverity(String severity) {
    this.severity = severity;
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

  public Cvssv2 withVersion(String version) {
    this.version = version;
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

  public Cvssv2 withExploitabilityScore(String exploitabilityScore) {
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

  public Cvssv2 withImpactScore(String impactScore) {
    this.impactScore = impactScore;
    return this;
  }

  @JsonProperty("userInteractionRequired")
  public boolean getUserInteractionRequired() {
    return userInteractionRequired;
  }

  @JsonProperty("userInteractionRequired")
  public void setUserInteractionRequired(boolean userInteractionRequired) {
    this.userInteractionRequired = userInteractionRequired;
  }

  public Cvssv2 withUserInteractionRequired(boolean userInteractionRequired) {
    this.userInteractionRequired = userInteractionRequired;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Cvssv2.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("score");
    sb.append('=');
    sb.append(this.score);
    sb.append(',');
    sb.append("accessVector");
    sb.append('=');
    sb.append(((this.accessVector == null) ? "<null>" : this.accessVector));
    sb.append(',');
    sb.append("accessComplexity");
    sb.append('=');
    sb.append(((this.accessComplexity == null) ? "<null>" : this.accessComplexity));
    sb.append(',');
    sb.append("authenticationr");
    sb.append('=');
    sb.append(((this.authenticationr == null) ? "<null>" : this.authenticationr));
    sb.append(',');
    sb.append("confidentialImpact");
    sb.append('=');
    sb.append(((this.confidentialImpact == null) ? "<null>" : this.confidentialImpact));
    sb.append(',');
    sb.append("integrityImpact");
    sb.append('=');
    sb.append(((this.integrityImpact == null) ? "<null>" : this.integrityImpact));
    sb.append(',');
    sb.append("availabilityImpact");
    sb.append('=');
    sb.append(((this.availabilityImpact == null) ? "<null>" : this.availabilityImpact));
    sb.append(',');
    sb.append("severity");
    sb.append('=');
    sb.append(((this.severity == null) ? "<null>" : this.severity));
    sb.append(',');
    sb.append("version");
    sb.append('=');
    sb.append(((this.version == null) ? "<null>" : this.version));
    sb.append(',');
    sb.append("exploitabilityScore");
    sb.append('=');
    sb.append(((this.exploitabilityScore == null) ? "<null>" : this.exploitabilityScore));
    sb.append(',');
    sb.append("impactScore");
    sb.append('=');
    sb.append(((this.impactScore == null) ? "<null>" : this.impactScore));
    sb.append(',');
    sb.append("userInteractionRequired");
    sb.append('=');
    sb.append(((this.userInteractionRequired) ? "false" : this.userInteractionRequired));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}
