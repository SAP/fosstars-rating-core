package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class LicenseInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testInfoAboutLicense() {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.allowedLicensePatterns("Apache License");
    provider.disallowedLicensePatterns("Don't trouble trouble till trouble troubles you");

    ValueSet values = provider.infoAboutLicense(
        String.join("\n", "", "Apache License", "", "Here should be the text.", ""));
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.infoAboutLicense(
        String.join("\n", "MIT License", "", "Here should be the text.", ""));
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.infoAboutLicense(
        String.join("\n", "MIT License", "", "Here should be the text.",
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
    when(localRepository.read("LICENSE"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "   ", "Apache License 2.0", "", "This is the text.", "Don't trouble"))))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "   ", "Apache License", "", "This is the text.", "Extra text"))));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfo provider = new LicenseInfo(fetcher);

    provider.allowedLicensePatterns("^\\s*Apache License 2\\.0(.*)$");
    provider.disallowedLicensePatterns(
        "Don't trouble trouble till trouble troubles you");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    provider.allowedLicensePatterns("MIT License");
    provider.disallowedLicensePatterns("Extra text");
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
    provider.allowedLicensePatterns("Apache License");
    provider.disallowedLicensePatterns("Don't trouble trouble till trouble troubles you");
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
            + "allowedLicensePatterns:\n"
            + "  - Apache License 2.0\n"
            + "  - MIT License\n"
            + "disallowedLicensePatterns:\n"
            + "  - Disallowed text"));
    assertEquals(2, provider.allowedLicensePatterns().size());
    assertEquals(provider.allowedLicensePatterns().get(0).pattern(), "Apache License 2.0");
    assertEquals(provider.allowedLicensePatterns().get(1).pattern(), "MIT License");
    assertEquals(1, provider.disallowedLicensePatterns().size());
    assertEquals(provider.disallowedLicensePatterns().get(0).pattern(), "Disallowed text");
  }

  @Test
  public void testConfigureWithEmptyAllowedLicensePatterns() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "allowedLicensePatterns: []"));
    assertTrue(provider.allowedLicensePatterns().isEmpty());
  }

  @Test
  public void testConfigureWithNoAllowedLicensePatterns() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "something: else"));
    assertTrue(provider.allowedLicensePatterns().isEmpty());
  }

  @Test
  public void testConfigureWithOneAllowedLicensePattern() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "allowedLicensePatterns: \".*Apache.*\""));
    assertEquals(1, provider.allowedLicensePatterns().size());
    assertTrue(
        provider.allowedLicensePatterns().get(0).matcher("Apache License 2.0").matches());
  }

  @Test(expected = IOException.class)
  public void testConfigureWithInvalidAllowedLicensePatterns() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "allowedLicensePatterns:\n"
            + "  object:\n"
            + "    something: else"));
    assertEquals(1, provider.allowedLicensePatterns().size());
    assertThat(provider.allowedLicensePatterns(), hasItem(Pattern.compile("Apache License 2.0")));
  }

  @Test
  public void testProviderWithLoadedConfig() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read("LICENSE"))
        .thenReturn(Optional.of(IOUtils.toInputStream("\n"
            + "                                 Apache License\n"
            + "                           Version 2.0, January 2004\n"
            + "                        http://www.apache.org/licenses/\n"
            + "\n"
            + "   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION")))
        .thenReturn(Optional.of(IOUtils.toInputStream("The MIT License (MIT)\n"
            + "\n"
            + "Copyright (c) 1851 Fedor Dostoevsky\n"
            + "\n"
            + "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
            + "of this software and associated documentation files (the \"Software\"), to deal\n"
            + "in the Software without restriction, including without limitation the rights\n"
            + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
            + "copies of the Software, and to permit persons to whom the Software is\n"
            + "furnished to do so, subject to the following conditions:")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfo provider = new LicenseInfo(fetcher);

    provider.configure(IOUtils.toInputStream(
              "---\n"
            + "allowedLicensePatterns: Apache License(\\s*)Version 2.0\n"
            + "disallowedLicensePatterns: Fedor Dostoevsky\n"));

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, true);
  }
}