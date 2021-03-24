package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private Optional<SemanticVersion> semanticVersion;

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
  
  private void initSemVer(String version) {
    semanticVersion = SemanticVersion.parse(version);
  }

  public SemanticVersion getSemanticVersion() {
    return semanticVersion.orElse(null);
  }

  @JsonIgnore
  public boolean isValidSemanticVersion() {
    return semanticVersion.isPresent();
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

  /**
   * LocalDate to Date deserializer used by Jackson Databind for JSON parsing.
   */
  private static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = 1L;
    private static final Pattern TEST_DATE_PATTERN = Pattern.compile("TEST([+-])(\\d{1,4})d");

    protected LocalDateDeserializer() {
      super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException {
      // option to allow easier test cases
      // we never expect that real dates matches the 'TEST_DATE_PATTERN'
      String text = jp.readValueAs(String.class);
      Matcher matcher = TEST_DATE_PATTERN.matcher(text);
      if (matcher.matches()) {
        String sign = matcher.group(1);
        int value = Integer.parseInt(matcher.group(2));
        if ("+".equals(sign)) {
          return LocalDate.now().plusDays(value);
        } else {
          return LocalDate.now().minusDays(value);
        }
      }
      return LocalDate.parse(text);
    }

  }

  /**
   * LocalDate to Date serializer used by Jackson Databind for JSON writing.
   */
  private static class LocalDateSerializer extends StdSerializer<LocalDate> {

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
