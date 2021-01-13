package com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"identifiers", "databaseId", "description", "ghsaId", "id", "origin",
    "permalink", "publishedAt", "references", "severity", "summary", "updatedAt", "withdrawnAt"})
public class Advisory {

  @JsonProperty("identifiers")
  private List<Identifier> identifiers = null;

  @JsonProperty("databaseId")
  private Integer databaseId;

  @JsonProperty("description")
  private String description;

  @JsonProperty("ghsaId")
  private String ghsaId;

  @JsonProperty("id")
  private String id;

  @JsonProperty("origin")
  private String origin;

  @JsonProperty("permalink")
  private String permalink;

  @JsonProperty("publishedAt")
  private String publishedAt;

  @JsonProperty("references")
  private List<AdvisoryReference> references = null;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("updatedAt")
  private String updatedAt;

  @JsonProperty("withdrawnAt")
  private Object withdrawnAt;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("identifiers")
  public List<Identifier> getIdentifiers() {
    return identifiers;
  }

  @JsonProperty("identifiers")
  public void setIdentifiers(List<Identifier> identifiers) {
    this.identifiers = identifiers;
  }

  @JsonProperty("databaseId")
  public Integer getDatabaseId() {
    return databaseId;
  }

  @JsonProperty("databaseId")
  public void setDatabaseId(Integer databaseId) {
    this.databaseId = databaseId;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("ghsaId")
  public String getGhsaId() {
    return ghsaId;
  }

  @JsonProperty("ghsaId")
  public void setGhsaId(String ghsaId) {
    this.ghsaId = ghsaId;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("origin")
  public String getOrigin() {
    return origin;
  }

  @JsonProperty("origin")
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  @JsonProperty("permalink")
  public String getPermalink() {
    return permalink;
  }

  @JsonProperty("permalink")
  public void setPermalink(String permalink) {
    this.permalink = permalink;
  }

  @JsonProperty("publishedAt")
  public String getPublishedAt() {
    return publishedAt;
  }

  @JsonProperty("publishedAt")
  public void setPublishedAt(String publishedAt) {
    this.publishedAt = publishedAt;
  }

  @JsonProperty("references")
  public List<AdvisoryReference> getReferences() {
    return references;
  }

  @JsonProperty("references")
  public void setReferences(List<AdvisoryReference> references) {
    this.references = references;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  @JsonProperty("summary")
  public void setSummary(String summary) {
    this.summary = summary;
  }

  @JsonProperty("updatedAt")
  public String getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @JsonProperty("withdrawnAt")
  public Object getWithdrawnAt() {
    return withdrawnAt;
  }

  @JsonProperty("withdrawnAt")
  public void setWithdrawnAt(Object withdrawnAt) {
    this.withdrawnAt = withdrawnAt;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
