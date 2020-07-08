package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import org.junit.Test;

public class VulnerabilitiesTest {

  @Test
  public void jsonSerializationAndDeserialization() throws IOException {
    serializationAndDeserialization(new ObjectMapper(), vulnerabilities());
  }

  @Test
  public void yamlSerializationAndDeserialization() throws IOException {
    YAMLFactory factory = new YAMLFactory();
    factory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    ObjectMapper mapper = new ObjectMapper(factory);
    mapper.findAndRegisterModules();
    serializationAndDeserialization(mapper, vulnerabilities());
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
            .set(CVSS.v2(5.0))
            .set(Resolution.UNPATCHED)
            .make(),
        newVulnerability("https://bugtracker/2")
            .description("test")
            .set(CVSS.v2(5.0))
            .set(Arrays.asList(
                new Reference("text1", new URL("https://vuln.com/1")),
                new Reference("text2", new URL("https://vuln.com/2"))))
            .set(Resolution.PATCHED)
            .introduced(new Date())
            .fixed(new Date())
            .make());
  }

}