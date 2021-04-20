package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import org.junit.Test;

public class ArtifactVersionTest {

  @Test
  public void testIsValidSemanticVersion() {
    ArtifactVersion invalidVersion2 = new ArtifactVersion("MIGHTY-1.2", LocalDateTime.now());
    assertFalse(invalidVersion2.isValidSemanticVersion());
    ArtifactVersion toLessDigits = new ArtifactVersion("2.0", LocalDateTime.now());
    assertFalse(toLessDigits.isValidSemanticVersion());

    ArtifactVersion validVersion = new ArtifactVersion("2.0.2", LocalDateTime.now());
    assertTrue(validVersion.isValidSemanticVersion());
    SemanticVersion semVerValid = validVersion.getSemanticVersion().get();
    assertEquals(2, semVerValid.getMajor());
    assertEquals(0, semVerValid.getMinor());
    assertEquals(2, semVerValid.getMicro());

    ArtifactVersion validVersionWithSuffix =
        new ArtifactVersion("1.0.0-MIGHTY", LocalDateTime.now());
    assertTrue(validVersionWithSuffix.isValidSemanticVersion());
    SemanticVersion semVerSuffix = validVersionWithSuffix.getSemanticVersion().get();
    assertEquals(1, semVerSuffix.getMajor());
    assertEquals(0, semVerSuffix.getMinor());
    assertEquals(0, semVerSuffix.getMicro());

    ArtifactVersion validVersionHighNumbers =
        new ArtifactVersion("1232.2134234.23423", LocalDateTime.now());
    assertTrue(validVersionHighNumbers.isValidSemanticVersion());

    SemanticVersion semVerHigh = validVersionHighNumbers.getSemanticVersion().get();
    assertEquals(1232, semVerHigh.getMajor());
    assertEquals(2134234, semVerHigh.getMinor());
    assertEquals(23423, semVerHigh.getMicro());
  }
}