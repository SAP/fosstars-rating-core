package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CODE_OF_CONDUCT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE;
import static java.nio.charset.StandardCharsets.UTF_8;
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
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CodeOfConductGuidelineInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() throws IOException {
    CodeOfConductGuidelineInfo provider = new CodeOfConductGuidelineInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_CODE_OF_CONDUCT));
    assertTrue(provider.supportedFeatures()
        .contains(HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE));
  }
  
  @Test
  public void testProjectWithCodeOfConductGuidelineInfo() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.readTextFrom("CODE_OF_CONDUCT.md"))
        .thenReturn(Optional.of(String.join("\n",
            "Here is the code of conduct for our project.", "This is the text.", "Extra text")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    CodeOfConductGuidelineInfo provider = new CodeOfConductGuidelineInfo(fetcher);

    provider.requiredContentPatterns("Extra text");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CODE_OF_CONDUCT, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE, true);

    when(localRepository.readTextFrom("HOW_TO_CODE_OF_CONDUCT.md"))
        .thenReturn(Optional.of(String.join("\n",
            "Here is the code of conduct for our project.", "This is the text.")));

    provider.knownCodeofConductGuidelineFiles("HOW_TO_CODE_OF_CONDUCT.md");
    provider.requiredContentPatterns("Extra text.");
    values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CODE_OF_CONDUCT, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE, false);
  }

  @Test
  public void testLoadingConfig() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.readTextFrom("CODE_OF_CONDUCT.md"))
         .thenReturn(Optional.of(String.join("\n",
            "Here is how to contribute to the project.",
            "Contributor Covenant",
            "This is the Contributor Covenant")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    CodeOfConductGuidelineInfo provider = new CodeOfConductGuidelineInfo(fetcher);
    provider.configure(IOUtils.toInputStream(
                    "---\n"
                  + "requiredContentPatterns:\n"
                  + "  - \"Contributor Covenant\"", UTF_8.name()));

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CODE_OF_CONDUCT, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE, true);
  }

  @Test
  public void testLoadingDefaultConfig() throws IOException {
    Path config = Paths.get(String.format("%s.config.yml",
        CodeOfConductGuidelineInfo.class.getSimpleName()));
    String content =
        "---\n"
        + "requiredContentPatterns:\n"
        + "  - \"Contributor Covenant\"";
    Files.write(config, content.getBytes());
    try {
      CodeOfConductGuidelineInfo provider = new CodeOfConductGuidelineInfo(fetcher);
      assertEquals(1, provider.requiredContentPatterns().size());
      assertEquals(
          "Contributor Covenant",
          provider.requiredContentPatterns().get(0).pattern());
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