package com.sap.oss.phosphor.fosstars.data.github.experimental.owasp.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"isVirtual", "fileName", "filePath", "md5", "sha1", "sha256", "description",
    "license", "evidenceCollected", "packages", "vulnerabilityIds", "vulnerabilities"})
@Generated("jsonschema2pojo")
public class Dependency implements Serializable {

  @JsonProperty("isVirtual")
  private boolean isVirtual;

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("filePath")
  private String filePath;

  @JsonProperty("md5")
  private String md5;

  @JsonProperty("sha1")
  private String sha1;

  @JsonProperty("sha256")
  private String sha256;

  @JsonProperty("description")
  private String description;

  @JsonProperty("license")
  private String license;

  @JsonProperty("evidenceCollected")
  private EvidenceCollected evidenceCollected;

  @JsonProperty("packages")
  private List<Package> packages = null;

  @JsonProperty("vulnerabilityIds")
  private List<VulnerabilityId> vulnerabilityIds = null;

  @JsonProperty("vulnerabilities")
  private List<OwaspDependencyCheckVuln> vulnerabilities = null;

  @JsonProperty("isVirtual")
  public boolean isIsVirtual() {
    return isVirtual;
  }

  @JsonProperty("isVirtual")
  public void setIsVirtual(boolean isVirtual) {
    this.isVirtual = isVirtual;
  }

  public Dependency withIsVirtual(boolean isVirtual) {
    this.isVirtual = isVirtual;
    return this;
  }

  @JsonProperty("fileName")
  public String getFileName() {
    return fileName;
  }

  @JsonProperty("fileName")
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Dependency withFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  @JsonProperty("filePath")
  public String getFilePath() {
    return filePath;
  }

  @JsonProperty("filePath")
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Dependency withFilePath(String filePath) {
    this.filePath = filePath;
    return this;
  }

  @JsonProperty("md5")
  public String getMd5() {
    return md5;
  }

  @JsonProperty("md5")
  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public Dependency withMd5(String md5) {
    this.md5 = md5;
    return this;
  }

  @JsonProperty("sha1")
  public String getSha1() {
    return sha1;
  }

  @JsonProperty("sha1")
  public void setSha1(String sha1) {
    this.sha1 = sha1;
  }

  public Dependency withSha1(String sha1) {
    this.sha1 = sha1;
    return this;
  }

  @JsonProperty("sha256")
  public String getSha256() {
    return sha256;
  }

  @JsonProperty("sha256")
  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  public Dependency withSha256(String sha256) {
    this.sha256 = sha256;
    return this;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  public Dependency withDescription(String description) {
    this.description = description;
    return this;
  }

  @JsonProperty("license")
  public String getLicense() {
    return license;
  }

  @JsonProperty("license")
  public void setLicense(String license) {
    this.license = license;
  }

  public Dependency withLicense(String license) {
    this.license = license;
    return this;
  }

  @JsonProperty("evidenceCollected")
  public EvidenceCollected getEvidenceCollected() {
    return evidenceCollected;
  }

  @JsonProperty("evidenceCollected")
  public void setEvidenceCollected(EvidenceCollected evidenceCollected) {
    this.evidenceCollected = evidenceCollected;
  }

  public Dependency withEvidenceCollected(EvidenceCollected evidenceCollected) {
    this.evidenceCollected = evidenceCollected;
    return this;
  }

  @JsonProperty("packages")
  public List<Package> getPackages() {
    return packages;
  }

  @JsonProperty("packages")
  public void setPackages(List<Package> packages) {
    this.packages = packages;
  }

  public Dependency withPackages(List<Package> packages) {
    this.packages = packages;
    return this;
  }

  @JsonProperty("vulnerabilityIds")
  public List<VulnerabilityId> getVulnerabilityIds() {
    return vulnerabilityIds;
  }

  @JsonProperty("vulnerabilityIds")
  public void setVulnerabilityIds(List<VulnerabilityId> vulnerabilityIds) {
    this.vulnerabilityIds = vulnerabilityIds;
  }

  public Dependency withVulnerabilityIds(List<VulnerabilityId> vulnerabilityIds) {
    this.vulnerabilityIds = vulnerabilityIds;
    return this;
  }

  @JsonProperty("vulnerabilities")
  public List<OwaspDependencyCheckVuln> getVulnerabilities() {
    return vulnerabilities;
  }

  @JsonProperty("vulnerabilities")
  public void setVulnerabilities(List<OwaspDependencyCheckVuln> vulnerabilities) {
    this.vulnerabilities = vulnerabilities;
  }

  public Dependency withVulnerabilities(List<OwaspDependencyCheckVuln> vulnerabilities) {
    this.vulnerabilities = vulnerabilities;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Dependency.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("isVirtual");
    sb.append('=');
    sb.append(this.isVirtual);
    sb.append(',');
    sb.append("fileName");
    sb.append('=');
    sb.append(((this.fileName == null) ? "<null>" : this.fileName));
    sb.append(',');
    sb.append("filePath");
    sb.append('=');
    sb.append(((this.filePath == null) ? "<null>" : this.filePath));
    sb.append(',');
    sb.append("md5");
    sb.append('=');
    sb.append(((this.md5 == null) ? "<null>" : this.md5));
    sb.append(',');
    sb.append("sha1");
    sb.append('=');
    sb.append(((this.sha1 == null) ? "<null>" : this.sha1));
    sb.append(',');
    sb.append("sha256");
    sb.append('=');
    sb.append(((this.sha256 == null) ? "<null>" : this.sha256));
    sb.append(',');
    sb.append("description");
    sb.append('=');
    sb.append(((this.description == null) ? "<null>" : this.description));
    sb.append(',');
    sb.append("license");
    sb.append('=');
    sb.append(((this.license == null) ? "<null>" : this.license));
    sb.append(',');
    sb.append("evidenceCollected");
    sb.append('=');
    sb.append(((this.evidenceCollected == null) ? "<null>" : this.evidenceCollected));
    sb.append(',');
    sb.append("packages");
    sb.append('=');
    sb.append(((this.packages == null) ? "<null>" : this.packages));
    sb.append(',');
    sb.append("vulnerabilityIds");
    sb.append('=');
    sb.append(((this.vulnerabilityIds == null) ? "<null>" : this.vulnerabilityIds));
    sb.append(',');
    sb.append("vulnerabilities");
    sb.append('=');
    sb.append(((this.vulnerabilities == null) ? "<null>" : this.vulnerabilities));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}
