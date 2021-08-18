package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V2.Impact;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import org.junit.Test;

public class VulnerabilitiesTest {

  @Test
  public void testJsonSerializationAndDeserialization() throws IOException {
    serializationAndDeserialization(Json.mapper(), vulnerabilities());
  }

  @Test
  public void testYamlSerializationAndDeserialization() throws IOException {
    serializationAndDeserialization(Yaml.mapper(), vulnerabilities());
  }

  private static void serializationAndDeserialization(
      ObjectMapper mapper, Vulnerabilities vulnerabilities) throws IOException {

    byte[] bytes = mapper.writeValueAsBytes(vulnerabilities);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    Vulnerabilities clone = mapper.readValue(bytes, Vulnerabilities.class);
    assertEquals(vulnerabilities, clone);
    assertEquals(vulnerabilities.hashCode(), clone.hashCode());
  }

  private static Vulnerabilities vulnerabilities() throws MalformedURLException {
    return new Vulnerabilities(
        newVulnerability("https://bugtracker/1")
            .description("test")
            .set(new CVSS.V2(5.0, Impact.COMPLETE, Impact.PARTIAL, Impact.NONE))
            .set(Resolution.UNPATCHED)
            .make(),
        newVulnerability("https://bugtracker/2")
            .description("test")
            .set(new CVSS.V2(5.0, Impact.COMPLETE, Impact.PARTIAL, Impact.NONE))
            .references(Arrays.asList(
                new Reference("text1", new URL("https://vuln.com/1")),
                new Reference("text2", new URL("https://vuln.com/2"))))
            .set(Resolution.PATCHED)
            .introduced(new Date())
            .fixed(new Date())
            .make());
  }

}