package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import org.junit.Test;

public class VulnerabilitiesTest {

  @Test
  public void serialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Vulnerabilities vulnerabilities = new Vulnerabilities(
        new Vulnerability("https://bugtracker/1"), new Vulnerability("https://bugtracker/2"));
    byte[] bytes = mapper.writeValueAsBytes(vulnerabilities);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    String content = new String(bytes);
    assertTrue(content.contains("https://bugtracker/1"));
    assertTrue(content.contains("https://bugtracker/2"));

    Iterator<Vulnerability> iterator = vulnerabilities.entries().iterator();
    Vulnerability vulnerability = iterator.next();
    assertEquals("https://bugtracker/1", vulnerability.id());
    assertEquals(Resolution.UNKNOWN, vulnerability.resolution());
    assertNotNull(vulnerability.references());
    assertTrue(vulnerability.references().isEmpty());
    assertEquals(Optional.empty(), vulnerability.introduced());
    assertEquals(Optional.empty(), vulnerability.fixed());

    vulnerability = iterator.next();
    assertEquals("https://bugtracker/2", vulnerability.id());
    assertEquals(Resolution.UNKNOWN, vulnerability.resolution());
    assertNotNull(vulnerability.references());
    assertTrue(vulnerability.references().isEmpty());
    assertEquals(Optional.empty(), vulnerability.introduced());
    assertEquals(Optional.empty(), vulnerability.fixed());

    Vulnerabilities clone = mapper.readValue(bytes, Vulnerabilities.class);
    assertEquals(vulnerabilities, clone);
    assertEquals(vulnerabilities.hashCode(), clone.hashCode());
  }

}