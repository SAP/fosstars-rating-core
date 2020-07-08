package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import org.junit.Test;

public class VulnerabilitiesInProjectTest {

  @Test
  public void value() {
    VulnerabilitiesInProject feature = new VulnerabilitiesInProject("test");
    assertNotNull(feature.name());
    Value<Vulnerabilities> value = feature.value(
        new Vulnerabilities(
            newVulnerability("1")
                .description("test")
                .set(CVSS.v3(5.1))
                .set(UNPATCHED)
                .make()));
    assertNotNull(value);
    assertFalse(value.isUnknown());
    Vulnerabilities vulnerabilities = value.get();
    assertNotNull(vulnerabilities);
    assertNotNull(vulnerabilities.entries());
    assertEquals(1, vulnerabilities.entries().size());
    assertTrue(vulnerabilities.entries().contains(
        newVulnerability("1")
            .description("test")
            .set(CVSS.v3(5.1))
            .set(UNPATCHED)
            .make()));
  }

  @Test
  public void unknown() {
    VulnerabilitiesInProject feature = new VulnerabilitiesInProject("test");
    Value value = feature.unknown();
    assertNotNull(value);
    assertTrue(value.isUnknown());
  }
}