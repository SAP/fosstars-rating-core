package com.sap.sgs.phosphor.fosstars.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class UnpatchedVulnerabilitiesStorageTest {

  @Test
  public void load() throws IOException {
    assertNotNull(UnpatchedVulnerabilitiesStorage.load());
  }

  @Test(expected = IOException.class)
  public void notExisting() throws IOException {
    UnpatchedVulnerabilitiesStorage.load("not/existing/file.json");
  }

  @Test
  public void store() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    String projectUrl = "https://github.com/holy/moly";
    storage.add(projectUrl, new Vulnerability(
        "https://bugtracker/1",
        Vulnerability.NO_DESCRIPTION,
        CVSS.UNKNOWN,
        Vulnerability.NO_REFERENCES,
        Resolution.UNPATCHED));
    Path tmp = Files.createTempFile(
        UnpatchedVulnerabilitiesStorageTest.class.getCanonicalName(), "test");
    String filename = tmp.toString();
    try {
      storage.store(filename);
      storage = UnpatchedVulnerabilitiesStorage.load(filename);
      Vulnerabilities vulnerabilities = storage.get(projectUrl);
      assertEquals(1, vulnerabilities.entries().size());
      assertTrue(vulnerabilities.entries().contains(new Vulnerability("https://bugtracker/1")));
    } finally {
      Files.delete(tmp);
    }
  }

  @Test
  public void testApachePOI() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    Vulnerabilities vulnerabilities = storage.get("https://github.com/apache/poi");
    assertNotNull(vulnerabilities);
    assertTrue(vulnerabilities.entries().isEmpty());
  }

  @Test
  public void testApacheBatik() throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    Vulnerabilities vulnerabilities = storage.get("https://github.com/apache/batik");
    assertNotNull(vulnerabilities);
    assertEquals(1, vulnerabilities.entries().size());
  }
}