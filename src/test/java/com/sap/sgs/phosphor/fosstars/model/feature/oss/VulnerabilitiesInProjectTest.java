package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import java.util.Collections;
import org.junit.Test;

public class VulnerabilitiesInProjectTest {

  @Test
  public void value() {
    VulnerabilitiesInProject feature = new VulnerabilitiesInProject("test");
    assertNotNull(feature.name());
    Value<Vulnerabilities> value = feature.value(new Vulnerabilities(
        new Vulnerability("1", "test", CVSS.v3(5.1), Collections.emptyList(), UNPATCHED)));
    assertNotNull(value);
    assertFalse(value.isUnknown());
    Vulnerabilities vulnerabilities = value.get();
    assertNotNull(vulnerabilities);
    assertNotNull(vulnerabilities.entries());
    assertEquals(1, vulnerabilities.entries().size());
    assertTrue(vulnerabilities.entries().contains(
        new Vulnerability("1", "test", CVSS.v3(5.1), Collections.emptyList(), UNPATCHED)));
  }

  @Test
  public void unknown() {
    VulnerabilitiesInProject feature = new VulnerabilitiesInProject("test");
    Value value = feature.unknown();
    assertNotNull(value);
    assertTrue(value.isUnknown());
  }
}