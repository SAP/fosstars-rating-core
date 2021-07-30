package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class VulnerabilitiesValueTest {

  @Test
  public void testSerialization() throws IOException {
    VulnerabilitiesValue vulnerabilityValue =
        new VulnerabilitiesValue(
            VULNERABILITIES_IN_PROJECT,
            new Vulnerabilities(
                newVulnerability("https://bugtracker/1").make(),
                newVulnerability("https://bugtracker/2").make()));
    byte[] bytes = Json.toBytes(vulnerabilityValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    String content = new String(bytes);
    assertTrue(content.contains("https://bugtracker/1"));
    assertTrue(content.contains("https://bugtracker/2"));
    VulnerabilitiesValue clone = Json.read(bytes, VulnerabilitiesValue.class);
    assertEquals(vulnerabilityValue, clone);
    assertEquals(vulnerabilityValue.hashCode(), clone.hashCode());
  }
}
