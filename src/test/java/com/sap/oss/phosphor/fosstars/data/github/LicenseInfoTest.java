package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class LicenseInfoTest extends TestGitHubDataFetcherHolder {

  private class LicenseInfoMock extends LicenseInfo {

    protected Map<String, String> licenseMetadataMock = new HashMap<String, String>();

    public LicenseInfoMock(GitHubDataFetcher fetcher) throws IOException {
      super(fetcher);
      allowedLicenses("Apache-2.0", "CC-BY-4.0", "MIT", "EPL-2.0");
    }

    @Override
    Map<String, String> licenseMetadata(GitHubProject project) {
      return licenseMetadataMock;
    }

    public void setLicensePath(String path) {
      licenseMetadataMock.put(LICENSE_PATH, path);
    }

    public void setSpdxId(String spdxId) {
      licenseMetadataMock.put(SPDX_ID, spdxId);
    }

  }

  @Test
  public void testLicenseContent() throws IOException {
    LicenseInfoMock provider = new LicenseInfoMock(fetcher);
    provider.disallowedLicensePatterns("Don't trouble trouble till trouble troubles you");

    provider.setLicensePath("LICENSE");
    provider.setSpdxId("Apache-2.0");
    ValueSet values = provider.analyzeLicenseContent(
        String.join("\n", "", "Apache License", "", "Here should be the text.", ""));
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.analyzeLicenseContent(
        String.join("\n", "MIT License", "", "Here should be the text.", ""));
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    values = provider.analyzeLicenseContent(String.join("\n", "MIT License", "",
        "Here should be the text.", "Don't trouble trouble till trouble troubles you", ""));
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, true);
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
  }

  @Test
  public void testSupportedFeatures() throws IOException {
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
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n", "   ", "Apache License 2.0",
            "", "This is the text.", "Don't trouble"))))
        .thenReturn(Optional.of(IOUtils.toInputStream(
            String.join("\n", "   ", "Apache License", "", "This is the text.", "Extra text"))));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfoMock provider = new LicenseInfoMock(fetcher);
    provider.disallowedLicensePatterns("Don't trouble trouble till trouble troubles you");
    provider.setLicensePath("LICENSE");
    provider.setSpdxId("Apache-2.0");

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    provider.setSpdxId("GPL-3.0-only");
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

    LicenseInfoMock provider = new LicenseInfoMock(fetcher);

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
        "---\n" + "allowedLicenses:\n" + "  - Apache-2.0\n" + "  - CC-BY-4.0\n" + "  - MIT\n"
            + "  - EPL-2.0\n" + "disallowedLicensePatterns:\n" + "  - Disallowed text\n"
            + "repositoryExceptions:\n" + "  - https://github.com/SAP/SapMachine\n"));
    assertEquals(4, provider.allowedLicenses().size());
    assertEquals(provider.allowedLicenses().get(0), "Apache-2.0");
    assertEquals(provider.allowedLicenses().get(1), "CC-BY-4.0");
    assertEquals(provider.allowedLicenses().get(2), "MIT");
    assertEquals(provider.allowedLicenses().get(3), "EPL-2.0");
    assertEquals(1, provider.disallowedLicensePatterns().size());
    assertEquals(provider.disallowedLicensePatterns().get(0).pattern(), "Disallowed text");
    assertEquals(1, provider.repositoryExceptions().size());
    assertEquals(provider.repositoryExceptions().get(0), "https://github.com/SAP/SapMachine");
  }

  @Test
  public void testConfigureWithEmptyAllowedLicensePatterns() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: []"));
    assertTrue(provider.allowedLicenses().isEmpty());
  }

  @Test
  public void testConfigureWithNoAllowedLicensePatterns() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream("---\n" + "something: else"));
    assertTrue(provider.allowedLicenses().isEmpty());
  }

  @Test
  public void testConfigureWithOneAllowedLicensePattern() throws IOException {
    LicenseInfo provider = new LicenseInfo(fetcher);
    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: [ 'Apache-2.0' ]"));
    assertEquals(1, provider.allowedLicenses().size());
    assertTrue(provider.allowedLicenses().get(0).equals("Apache-2.0"));
  }

  @Test
  public void testProviderWithLoadedConfig() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read("LICENSE"))
        .thenReturn(Optional
            .of(IOUtils.toInputStream("\n" + "                                 Apache License\n"
                + "                           Version 2.0, January 2004\n"
                + "                        http://www.apache.org/licenses/\n" + "\n"
                + "   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION")))
        .thenReturn(Optional.of(IOUtils.toInputStream("The MIT License (MIT)\n" + "\n"
            + "Copyright (c) 1851 Fedor Dostoevsky\n" + "\n"
            + "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
            + "of this software and associated documentation files (the \"Software\"), to deal\n"
            + "in the Software without restriction, including without limitation the rights\n"
            + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
            + "copies of the Software, and to permit persons to whom the Software is\n"
            + "furnished to do so, subject to the following conditions:")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfoMock provider = new LicenseInfoMock(fetcher);
    provider.setLicensePath("LICENSE");
    provider.setSpdxId("Apache-2.0");

    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: Apache-2.0\n"
        + "disallowedLicensePatterns: Fedor Dostoevsky\n"));

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    provider = new LicenseInfoMock(fetcher);
    provider.setLicensePath("LICENSE");
    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: Apache-2.0\n"
        + "disallowedLicensePatterns: Fedor Dostoevsky\n"));
    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, true);
  }

  @Test
  public void testRepositoryException() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read("LICENSE"))
        .thenReturn(Optional.of(IOUtils.toInputStream("\n" + "General Public License...\n")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    LicenseInfoMock provider = new LicenseInfoMock(fetcher);
    provider.setLicensePath("LICENSE");
    provider.setSpdxId("GPL-3.0-only");

    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: Apache-2.0\n"));

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, false);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);

    provider.configure(IOUtils.toInputStream("---\n" + "allowedLicenses: Apache-2.0\n"
        + "repositoryExceptions: https://github.com/test/project\n"));

    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_LICENSE, true);
    checkValue(values, ALLOWED_LICENSE, true);
    checkValue(values, LICENSE_HAS_DISALLOWED_CONTENT, false);
  }

  @Test
  public void testLoadingDefaultConfig() throws IOException {
    Path config = Paths.get(String.format("%s.config.yml", LicenseInfo.class.getSimpleName()));
    String content = "---\n" + "allowedLicenses:\n" + "  - Apache-2.0\n" + "  - MIT\n"
        + "disallowedLicensePatterns:\n" + "  - Disallowed text";
    Files.write(config, content.getBytes());
    try {
      LicenseInfo provider = new LicenseInfo(fetcher);
      assertEquals(2, provider.allowedLicenses().size());
      assertEquals(provider.allowedLicenses().get(0), "Apache-2.0");
      assertEquals(provider.allowedLicenses().get(1), "MIT");
      assertEquals(1, provider.disallowedLicensePatterns().size());
      assertEquals(provider.disallowedLicensePatterns().get(0).pattern(), "Disallowed text");
    } finally {
      FileUtils.forceDeleteOnExit(config.toFile());
    }
  }
}
