package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import org.junit.Test;

public class VulnerabilitiesFeatureTest {

  @Test
  public void value() {
    VulnerabilitiesFeature feature = new VulnerabilitiesFeature("test");
    assertNotNull(feature.name());
    Value<Vulnerabilities> value = feature.value(
        new Vulnerabilities(
            newVulnerability("1")
                .description("test")
                .set(new CVSS.V3(5.1, Impact.HIGH, Impact.LOW, Impact.NONE))
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
            .set(new CVSS.V3(5.1, Impact.HIGH, Impact.LOW, Impact.NONE))
            .set(UNPATCHED)
            .make()));
  }

  @Test
  public void unknown() {
    VulnerabilitiesFeature feature = new VulnerabilitiesFeature("test");
    Value value = feature.unknown();
    assertNotNull(value);
    assertTrue(value.isUnknown());
  }
}