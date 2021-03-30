package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.SemanticVersion.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

public class SemanticVersionTest {

  @Test
  public void parse() {
    Optional<SemanticVersion> invalidVersion2 = SemanticVersion.parse("MIGHTY-1.2");
    assertFalse(invalidVersion2.isPresent());
    Optional<SemanticVersion> toLessDigits = SemanticVersion.parse("1.2");
    assertFalse(toLessDigits.isPresent());

    Optional<SemanticVersion> validVersion = SemanticVersion.parse("2.0.2");
    assertTrue(validVersion.isPresent());
    SemanticVersion semVerValid = validVersion.get();
    assertEquals(2, semVerValid.getMajor());
    assertEquals(0, semVerValid.getMinor());
    assertEquals(2, semVerValid.getMicro());

    Optional<SemanticVersion> validVersionWithSuffix = SemanticVersion.parse("1.0.0-MIGHTY");
    assertTrue(validVersionWithSuffix.isPresent());
    SemanticVersion semVerSuffix = validVersionWithSuffix.get();
    assertEquals(1, semVerSuffix.getMajor());
    assertEquals(0, semVerSuffix.getMinor());
    assertEquals(0, semVerSuffix.getMicro());

    Optional<SemanticVersion> validVersionHighNumbers = SemanticVersion.parse("1232.2134234.23423");
    assertTrue(validVersionHighNumbers.isPresent());
    SemanticVersion semVerHigh = validVersionHighNumbers.get();
    assertEquals(1232, semVerHigh.getMajor());
    assertEquals(2134234, semVerHigh.getMinor());
    assertEquals(23423, semVerHigh.getMicro());
  }

  @Test
  public void isInRange() {
    SemanticVersion version = create(1, 5, 0);

    assertTrue(version.isInRange(create(1, 0, 0), create(2, 0, 0)));
    assertTrue(version.isInRange(create(1, 4, 10), create(1, 5, 1)));
    assertTrue(version.isInRange(create(1, 5, 0), create(1, 5, 0)));

    assertFalse(version.isInRange(create(1, 8, 0), create(2, 5, 0)));
    assertFalse(version.isInRange(create(0, 8, 0), create(1, 4, 99)));
  }
}