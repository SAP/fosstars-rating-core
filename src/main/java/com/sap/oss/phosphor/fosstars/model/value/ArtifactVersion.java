package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import org.apache.maven.artifact.versioning.ComparableVersion;

/**
 * This class represents a specific version of an artifact that is produced by an open source
 * project. For example, it may be a jar file.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class ArtifactVersion {

  /**
   * An empty artifact version (immutable).
   */
  public static final ArtifactVersion EMPTY = new ArtifactVersion("", LocalDateTime.MIN);

  /**
   * Comparator for artifact versions release date and version.
   */
  static final Comparator<ArtifactVersion> RELEASE_DATE_VERSION_COMPARISON = (a, b) -> {
    final int compareDate = b.releaseDate().compareTo(a.releaseDate());
    if (compareDate != 0) {
      return compareDate;
    }

    final ComparableVersion aVersion = new ComparableVersion(a.version());
    final ComparableVersion bVersion = new ComparableVersion(b.version());
    return bVersion.compareTo(aVersion);
  };

  /**
   * A version of the artifact.
   */
  private final String version;

  /**
   * When the artifact was released.
   */
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private final LocalDateTime releaseDate;

  /**
   * A semantic version if available.
   */
  @JsonIgnore
  private final Optional<SemanticVersion> semanticVersion;

  /**
   * Initialize an artifact version based on version tag and release date.
   *
   * @param version The version tag.
   * @param releaseDate The release date.
   */
  public ArtifactVersion(
      @JsonProperty("version") String version,
      @JsonProperty("releaseDate") LocalDateTime releaseDate) {

    Objects.requireNonNull(version, "Version must be set");
    Objects.requireNonNull(releaseDate, "Release date must be set");

    this.version = version;
    this.releaseDate = releaseDate;
    this.semanticVersion = SemanticVersion.parse(version);
  }

  /**
   * Returns a semantic version.
   *
   * @return A semantic version if available.
   */
  public Optional<SemanticVersion> getSemanticVersion() {
    return semanticVersion;
  }

  /**
   * Checks if the artifact versions has a semantic version.
   *
   * @return True if the version is semantic, false otherwise.
   */
  @JsonIgnore
  public boolean hasValidSemanticVersion() {
    return semanticVersion.isPresent();
  }

  /**
   * Returns a version string of the artifact.
   *
   * @return A version string of the artifact.
   */
  @JsonGetter("version")
  public String version() {
    return version;
  }

  /**
   * Returns a release date of the artifact.
   *
   * @return A release date of the artifact.
   */
  @JsonGetter("releaseDate")
  public LocalDateTime releaseDate() {
    return releaseDate;
  }

  @Override
  public String toString() {
    return version + "@" + releaseDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArtifactVersion compareVersion = (ArtifactVersion) o;
    return version.equals(compareVersion.version) && releaseDate.equals(compareVersion.releaseDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, releaseDate);
  }

  /**
   * LocalDate to Date deserializer used by Jackson Databind for JSON parsing.
   */
  private static class LocalDateDeserializer extends StdDeserializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    protected LocalDateDeserializer() {
      super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
      String text = jp.readValueAs(String.class);
      return LocalDateTime.parse(text);
    }
  }

  /**
   * LocalDate to Date serializer used by Jackson Databind for JSON writing.
   */
  private static class LocalDateSerializer extends StdSerializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public LocalDateSerializer() {
      super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider sp)
        throws IOException {
      gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
  }
}
