package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V2.Impact.COMPLETE;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V2.Impact.PARTIAL;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.HIGH;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.LOW;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.NONE;
import static java.lang.String.format;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class EstimateImpactUsingKnownVulnerabilitiesTest extends TestGitHubDataFetcherHolder {

  private static class TestVulnerabilitiesProvider extends AbstractDataProvider {

    private final Vulnerabilities vulnerabilities = new Vulnerabilities();

    void add(Vulnerability vulnerability) {
      vulnerabilities.add(vulnerability);
    }

    @Override
    protected TestVulnerabilitiesProvider doUpdate(Subject subject, ValueSet values) {
      values.update(VULNERABILITIES_IN_PROJECT.value(vulnerabilities));
      return this;
    }

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return singleton(VULNERABILITIES_IN_PROJECT);
    }

    @Override
    public boolean supports(Subject subject) {
      return true;
    }

    @Override
    public boolean interactive() {
      return false;
    }
  }

  @Test
  public void testWithoutEnoughVulnerabilities() throws IOException {
    TestVulnerabilitiesProvider mockProvider = new TestVulnerabilitiesProvider();
    EstimateImpactUsingKnownVulnerabilities provider = new EstimateImpactUsingKnownVulnerabilities(
        new InfoAboutVulnerabilities(fetcher, mockProvider));
    ValueSet values = new ValueHashSet();

    for (int i = 0; i < 5; i++) {
      mockProvider.add(
          Vulnerability.Builder.newVulnerability(format("CVE-%d", i))
              .set(new CVSS.V3(2.5, NONE, LOW, HIGH))
              .make());
    }

    provider.update(PROJECT, values);
    assertUnknown(values, CONFIDENTIALITY_IMPACT);
    assertUnknown(values, INTEGRITY_IMPACT);
    assertUnknown(values, AVAILABILITY_IMPACT);
  }

  @Test
  public void testWithEnoughVulnerabilities() throws IOException {
    TestVulnerabilitiesProvider mockProvider = new TestVulnerabilitiesProvider();
    EstimateImpactUsingKnownVulnerabilities provider = new EstimateImpactUsingKnownVulnerabilities(
        new InfoAboutVulnerabilities(fetcher, mockProvider));
    ValueSet values = new ValueHashSet();

    for (int i = 0; i < 15; i++) {
      mockProvider.add(
          Vulnerability.Builder.newVulnerability(format("CVE-%d", i))
              .set(new CVSS.V3(2.5, NONE, NONE, NONE))
              .make());
    }

    provider.update(PROJECT, values);
    assertValue(values, CONFIDENTIALITY_IMPACT, Impact.NEGLIGIBLE);
    assertValue(values, INTEGRITY_IMPACT, Impact.NEGLIGIBLE);
    assertValue(values, AVAILABILITY_IMPACT, Impact.NEGLIGIBLE);

    mockProvider.add(
        Vulnerability.Builder.newVulnerability("CVE-101")
            .set(new CVSS.V3(5.0, LOW, NONE, NONE))
            .make());
    provider.update(PROJECT, values);
    assertValue(values, CONFIDENTIALITY_IMPACT, Impact.LOW);
    assertValue(values, INTEGRITY_IMPACT, Impact.NEGLIGIBLE);
    assertValue(values, AVAILABILITY_IMPACT, Impact.NEGLIGIBLE);

    mockProvider.add(
        Vulnerability.Builder.newVulnerability("CVE-102")
            .set(new CVSS.V3(5.0, NONE, LOW, NONE))
            .make());
    provider.update(PROJECT, values);
    assertValue(values, CONFIDENTIALITY_IMPACT, Impact.LOW);
    assertValue(values, INTEGRITY_IMPACT, Impact.LOW);
    assertValue(values, AVAILABILITY_IMPACT, Impact.NEGLIGIBLE);

    mockProvider.add(
        Vulnerability.Builder.newVulnerability("CVE-103")
            .set(new CVSS.V3(5.0, NONE, NONE, HIGH))
            .make());
    provider.update(PROJECT, values);
    assertValue(values, CONFIDENTIALITY_IMPACT, Impact.LOW);
    assertValue(values, INTEGRITY_IMPACT, Impact.LOW);
    assertValue(values, AVAILABILITY_IMPACT, Impact.HIGH);

    mockProvider.add(
        Vulnerability.Builder.newVulnerability("CVE-104")
            .set(new CVSS.V2(9.0, COMPLETE, COMPLETE, PARTIAL))
            .make());
    provider.update(PROJECT, values);
    assertValue(values, CONFIDENTIALITY_IMPACT, Impact.HIGH);
    assertValue(values, INTEGRITY_IMPACT, Impact.HIGH);
    assertValue(values, AVAILABILITY_IMPACT, Impact.HIGH);
  }

  private static void assertUnknown(ValueSet values, Feature<Impact> feature) {
    Optional<Value<Impact>> something = values.of(feature);
    if (!something.isPresent()) {
      fail("Could not find value!");
    }
    Value<Impact> value = something.get();
    assertTrue(value.isUnknown());
  }

  private static void assertValue(ValueSet values, Feature<Impact> feature, Impact expectedImpact) {
    Optional<Value<Impact>> something = values.of(feature);
    if (!something.isPresent()) {
      fail("Could not find value!");
    }
    Value<Impact> value = something.get();
    assertFalse(value.isUnknown());
    assertEquals(expectedImpact, value.get());
  }
}