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
import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;

public class ContributingGuidelineInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() {
    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_CONTRIBUTING_GUIDELINE));
    assertTrue(provider.supportedFeatures().contains(HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE));
  }

  @Test
  public void testProjectWithContributingGuideline() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.readLinesOf("CONTRIBUTING.md"))
        .thenReturn(Optional.of(Arrays.asList(
            "Here is how to contribute to the project.", "This is the text.", "Extra text")));
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);

    provider.requiredContentPatterns("Extra text");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, true);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, true);

    when(localRepository.readLinesOf("HOW_TO_CONTRIBUTE.md"))
        .thenReturn(Optional.of(Arrays.asList(
            "Here is how to contribute to the project.", "This is the text.")));

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
    when(localRepository.readLinesOf(anyString())).thenReturn(Optional.empty());
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ContributingGuidelineInfo provider = new ContributingGuidelineInfo(fetcher);

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_CONTRIBUTING_GUIDELINE, false);
    checkValue(values, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE, false);
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
  }

}