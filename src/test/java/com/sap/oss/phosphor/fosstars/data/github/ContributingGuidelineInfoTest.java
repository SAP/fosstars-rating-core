package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ContributingGuidelineInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() throws IOException {
    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_CONTRIBUTING_GUIDELINE));
    assertTrue(provider.supportedFeatures().contains(HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE));
  }

  @Test
  public void testProjectWithContributingGuideline() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read("CONTRIBUTING.md"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "Here is how to contribute to the project.", "This is the text.", "Extra text"))));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);

    provider.requiredContentPatterns("Extra text");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, true);

    when(localRepository.read("HOW_TO_CONTRIBUTE.md"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "Here is how to contribute to the project.", "This is the text."))));

    provider.knownContributingGuidelineFiles("HOW_TO_CONTRIBUTE.md");
    provider.requiredContentPatterns("Extra text.");
    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, false);
  }

  @Test
  public void testProjectWithoutContributingGuideline() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read(anyString())).thenReturn(Optional.empty());
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, false);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, false);
  }

  @Test
  public void testLoadingConfig() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.read("CONTRIBUTING.md"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "Here is how to contribute to the project.",
            "## Contributor License Agreement",
            "This is the text."))))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "Here is how to contribute to the project.",
            "## Developer Certificate of Origin",
            "This is the text."))));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
                    "---\n"
                  + "requiredContentPatterns:\n"
                  + "  - \"Developer Certificate of Origin\"\n"
                  + "  - \"(?!Contributor(\\\\s+)License(\\\\s+)Agreement)\""));

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, false);

    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, true);
  }

  @Test
  public void testLoadingDefaultConfig() throws IOException {
    Path config = Paths.get(String.format("%s.config.yml",
        ContributingGuidelineInfo.class.getSimpleName()));
    String content =
              "---\n"
            + "requiredContentPatterns:\n"
            + "  - \"Developer Certificate of Origin\"\n"
            + "  - \"(?!Contributor(\\\\s+)License(\\\\s+)Agreement)\"";
    Files.write(config, content.getBytes());
    try {
      ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);
      assertEquals(2, provider.requiredContentPatterns().size());
      assertEquals(
          "Developer Certificate of Origin",
          provider.requiredContentPatterns().get(0).pattern());
      assertEquals(
          "(?!Contributor(\\s+)License(\\s+)Agreement)",
          provider.requiredContentPatterns().get(1).pattern());
    } finally {
      FileUtils.forceDeleteOnExit(config.toFile());
    }
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
  }

}