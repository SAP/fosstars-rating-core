package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * An artifact version for testing.
 */
public class TestArtifactVersion extends ArtifactVersion {

  /**
   * Creates a new artifact version for testing.
   *
   * @param version A version string.
   * @param age An age of the artifact.
   */
  public TestArtifactVersion(
      @JsonProperty("version") String version,
      @JsonProperty("age") String age) {

    super(version, LocalDateTime.now().minus(Duration.parse(age)));
  }
}
