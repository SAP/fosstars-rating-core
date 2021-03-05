package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Contains version tag and release date for an artifact version.
 */
public class ArtifactVersion {

  public static final ArtifactVersion EMPTY = new ArtifactVersion("", LocalDate.now());

  private final String version;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private final LocalDate releaseDate;

  @JsonIgnore
  private int major;
  @JsonIgnore
  private int minor;
  @JsonIgnore
  private int micro;
  @JsonIgnore
  private boolean validSemVer = false;

  /**
   * Initialize the ArtifactVersion based on version tag and release date.
   *
   * @param version version tag
   * @param releaseDate release date
   */
  public ArtifactVersion(@JsonProperty("version") String version,
      @JsonProperty("releaseDate") LocalDate releaseDate) {

    Objects.requireNonNull(version, "Version must be set");
    Objects.requireNonNull(releaseDate, "Release date must be set");

    this.version = version;
    this.releaseDate = releaseDate;
    initSemVer(version);
  }

  /**
   * Check if given version is a semantic compatible version.
   *
   * @param version version to check
   * @return true if version follows the semantic version
   */
  public static boolean isSemVer(String version) {
    // TODO (mibo): Improve semVer handling
    return new ArtifactVersion(version, LocalDate.MIN).isValidSemanticVersion();
  }

  private void initSemVer(String version) {
    String[] versionSplit = version.split("\\.");
    if (versionSplit.length >= 3) {
      // take first three as major/minor/micro if they are numbers
      try {
        major = Integer.parseInt(versionSplit[0]);
        minor = Integer.parseInt(versionSplit[1]);
        micro = Integer.parseInt(versionSplit[2]);
        validSemVer = true;
      } catch (NumberFormatException e) {
        // FIXME: replace sout
        System.out.printf("Unable to parse %s as a semantic version%n", version);
      }
    }
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

  public int getMicro() {
    return micro;
  }

  @JsonIgnore
  public boolean isValidSemanticVersion() {
    return validSemVer;
  }

  public String getVersion() {
    return version;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  @Override
  public String toString() {
    return "{"
        + "version='" + version + '\''
        + ", releaseDate=" + releaseDate
        + '}';
  }

  // FIXME: only for quick test -> refactor ASAP
  public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = 1L;

    protected LocalDateDeserializer() {
      super(LocalDate.class);
    }


    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException {
      return LocalDate.parse(jp.readValueAs(String.class));
    }

  }

  public static class LocalDateSerializer extends StdSerializer<LocalDate> {

    private static final long serialVersionUID = 1L;

    public LocalDateSerializer() {
      super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider sp)
        throws IOException {
      gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
  }
}
