package com.sap.oss.phosphor.fosstars.data.json;

import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class UnpatchedVulnerabilitiesStorageTest {

  @Test
  public void load() throws IOException {
    assertNotNull(UnpatchedVulnerabilitiesStorage.load());
  }

  @Test
  public void notExisting() {
    assertThrows(
        IOException.class, () -> UnpatchedVulnerabilitiesStorage.load("not/existing/file.json"));
  }

  @Test
  public void store() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    String projectUrl = "https://github.com/holy/moly";
    storage.add(
        projectUrl, newVulnerability("https://bugtracker/1").set(Resolution.UNPATCHED).make());
    Path tmp =
        Files.createTempFile(UnpatchedVulnerabilitiesStorageTest.class.getCanonicalName(), "test");
    String filename = tmp.toString();
    try {
      storage.store(filename);
      storage = UnpatchedVulnerabilitiesStorage.load(filename);
      Vulnerabilities vulnerabilities = storage.getFor(projectUrl);
      assertEquals(1, vulnerabilities.entries().size());
      assertTrue(
          vulnerabilities.entries().contains(newVulnerability("https://bugtracker/1").make()));
    } finally {
      Files.delete(tmp);
    }
  }

  @Test
  public void testApachePOI() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    Vulnerabilities vulnerabilities = storage.getFor("https://github.com/apache/poi");
    assertNotNull(vulnerabilities);
    assertTrue(vulnerabilities.entries().isEmpty());
  }

  @Test
  public void testApacheBatik() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    Vulnerabilities vulnerabilities = storage.getFor("https://github.com/apache/batik");
    assertNotNull(vulnerabilities);
    assertEquals(1, vulnerabilities.entries().size());
  }

  @Test
  public void testOdata4j() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    Vulnerabilities vulnerabilities = storage.getFor("https://github.com/odata4j/odata4j");
    assertNotNull(vulnerabilities);
    assertEquals(3, vulnerabilities.entries().size());
  }
}
