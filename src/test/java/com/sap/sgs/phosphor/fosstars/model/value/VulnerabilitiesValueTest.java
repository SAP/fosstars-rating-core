package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class VulnerabilitiesValueTest {

  @Test
  public void serialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    VulnerabilitiesValue vulnerabilityValue =
        new VulnerabilitiesValue(
            new Vulnerabilities(
                newVulnerability("https://bugtracker/1").make(),
                newVulnerability("https://bugtracker/2").make()));
    byte[] bytes = mapper.writeValueAsBytes(vulnerabilityValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    String content = new String(bytes);
    assertTrue(content.contains("https://bugtracker/1"));
    assertTrue(content.contains("https://bugtracker/2"));
    VulnerabilitiesValue clone = mapper.readValue(bytes, VulnerabilitiesValue.class);
    assertEquals(vulnerabilityValue, clone);
    assertEquals(vulnerabilityValue.hashCode(), clone.hashCode());
  }
}
