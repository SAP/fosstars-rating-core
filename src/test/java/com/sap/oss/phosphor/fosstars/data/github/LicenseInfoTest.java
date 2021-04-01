package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class LicenseInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testIsWrong() {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.disallowedLicenseContentPatterns("stop word", "wrong");
    assertTrue(provider.isWrong("This is a stop word."));
    assertTrue(provider.isWrong("Wrong content"));
    assertFalse(provider.isWrong("This is okay"));
    assertFalse(provider.isWrong(""));
  }

  @Test
  public void testHeaderOf() {
    assertEquals(
        "This is a header",
        LicenseInfo.headerOf(Collections.singletonList("This is a header")));
    assertEquals(
        "This is a header",
        LicenseInfo.headerOf(Arrays.asList("  ", "This is a header", "This is not a header")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeaderOfWithNoHeader() {
    LicenseInfo.headerOf(Collections.singletonList("   "));
  }

  @Test
  public void testInfoAboutLicense() {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.allowedLicenseHeaders("Apache License");
    provider.disallowedLicenseContentPatterns("Don't trouble trouble till trouble troubles you");

    ValueSet values = provider.infoAboutLicense(
        Arrays.asList("", "Apache License", "", "Here should be the text.", ""));
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.infoAboutLicense(
        Arrays.asList("", "MIT License", "", "Here should be the text.", ""));
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.infoAboutLicense(
        Arrays.asList("", "MIT License", "", "Here should be the text.",
            "Don't trouble trouble till trouble troubles you", ""));
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, true);
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
  }

  @Test
  public void testSupportedFeatures() {
    LicenseInfo provider = new LicenseInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_LICENSE));
    assertTrue(provider.supportedFeatures().contains(ALLOWED_LICENSE));
    assertTrue(provider.supportedFeatures().contains(LICENSE_HAS_DISALLOWED_CONTENT));
  }

  @Test
  public void testProjectWithLicense() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.readLinesOf("LICENSE"))
        .thenReturn(Optional.of(
            Arrays.asList("", "Apache License", "", "This is the text.", "Extra text")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfo provider = new LicenseInfo(fetcher);

    provider.allowedLicenseHeaders("Apache License");
    provider.disallowedLicenseContentPatterns("Don't trouble trouble till trouble troubles you");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    provider.allowedLicenseHeaders("MIT License");
    provider.disallowedLicenseContentPatterns("Extra text");
    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, true);
  }

  @Test
  public void testProjectWithoutLicense() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.readLinesOf(Paths.get("LICENSE"))).thenReturn(Optional.empty());
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfo provider = new LicenseInfo(fetcher);

    provider.knownLicenseFiles("LICENSE");
    provider.allowedLicenseHeaders("Apache License");
    provider.disallowedLicenseContentPatterns("Don't trouble trouble till trouble troubles you");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, false);
    Optional<Value<Boolean>> something = values.of(ALLOWED_LICENSE);
    assertTrue(something.isPresent());
    assertTrue(something.get().isUnknown());
    something = values.of(LICENSE_HAS_DISALLOWED_CONTENT);
    assertTrue(something.isPresent());
    assertTrue(something.get().isUnknown());
  }

  @Test
  public void testConfigureWithCorrectConfig() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "  allowedLicenseHeaders:\n"
            + "    - Apache License 2.0\n"
            + "    - MIT License"));
    assertEquals(2, provider.allowedLicenseHeaders().size());
    assertThat(provider.allowedLicenseHeaders(), hasItem("Apache License 2.0"));
    assertThat(provider.allowedLicenseHeaders(), hasItem("MIT License"));
  }

  @Test
  public void testConfigureWithEmptyAllowedLicenseHeaders() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "  allowedLicenseHeaders: []"));
    assertTrue(provider.allowedLicenseHeaders().isEmpty());
  }

  @Test
  public void testConfigureWithNoAllowedLicenseHeaders() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "  something: else"));
    assertTrue(provider.allowedLicenseHeaders().isEmpty());
  }

  @Test
  public void testConfigureWithOneAllowedLicenseHeader() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "  allowedLicenseHeaders: Apache License 2.0"));
    assertEquals(1, provider.allowedLicenseHeaders().size());
    assertThat(provider.allowedLicenseHeaders(), hasItem("Apache License 2.0"));
  }

  @Test(expected = IOException.class)
  public void testConfigureWithInvalidAllowedLicenseHeaders() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "  allowedLicenseHeaders:\n"
            + "    object:\n"
            + "      something: else"));
    assertEquals(1, provider.allowedLicenseHeaders().size());
    assertThat(provider.allowedLicenseHeaders(), hasItem("Apache License 2.0"));
  }
}