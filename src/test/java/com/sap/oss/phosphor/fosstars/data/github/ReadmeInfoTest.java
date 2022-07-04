package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.INCOMPLETE_README;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ReadmeInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() throws IOException {
    Set<Feature<?>> features =  new ReadmeInfo(fetcher).supportedFeatures();
    assertEquals(2, features.size());
    assertTrue(features.contains(HAS_README));
    assertTrue(features.contains(INCOMPLETE_README));
  }

  @Test
  public void testWithReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile("README")).thenReturn(true);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    provider.requiredContentPatterns("# Mandatory header", "^((?!Prohibited phrase).)*$");
    provider.set(NoValueCache.create());

    when(localRepository.readTextFrom("README"))
        .thenReturn(Optional.of(String.join("\n",
            "This is README",
            "",
            "# Mandatory header",
            "",
            "Don't trouble trouble till trouble troubles you."
        )));
    ValueSet values = provider.fetchValuesFor(project);
    Value<Boolean> value = checkValue(values, HAS_README, true);
    assertTrue(value.explanation().isEmpty());
    value = checkValue(values, INCOMPLETE_README, false);
    assertTrue(value.explanation().isEmpty());

    when(localRepository.readTextFrom("README"))
        .thenReturn(Optional.of(String.join("\n",
            "This is README",
            "",
            "# Another header"
        )));
    values = provider.fetchValuesFor(project);
    value = checkValue(values, HAS_README, true);
    assertTrue(value.explanation().isEmpty());
    value = checkValue(values, INCOMPLETE_README, true);
    assertFalse(value.explanation().isEmpty());
    assertTrue(value.explanation().get(0).contains("Mandatory header"));

    when(localRepository.readTextFrom("README"))
        .thenReturn(Optional.of(String.join("\n",
            "This is README",
            "",
            "# Mandatory header",
            "",
            "Prohibited phrase",
            ""
        )));
    values = provider.fetchValuesFor(project);
    value = checkValue(values, HAS_README, true);
    assertTrue(value.explanation().isEmpty());
    value = checkValue(values, INCOMPLETE_README, true);
    assertFalse(value.explanation().isEmpty());
    assertTrue(value.explanation().get(0).contains("Prohibited phrase"));
  }

  @Test
  public void testWithoutReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile(any())).thenReturn(false);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    ValueSet values = provider.fetchValuesFor(project);
    Value<Boolean> value = checkValue(values, HAS_README, false);
    assertFalse(value.explanation().isEmpty());
    value = checkValue(values, INCOMPLETE_README, true);
    assertFalse(value.explanation().isEmpty());
  }
  
  @Test
  public void testWithRstReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile("README.rst")).thenReturn(true);
    TestGitHubDataFetcher.addForTesting(project, localRepository);
    
    ReadmeInfo provider = new ReadmeInfo(fetcher);
    provider.set(NoValueCache.create());

    when(localRepository.readTextFrom("README.rst"))
        .thenReturn(Optional.of(String.join("\n",
            "This is README.rst"
        )));
    ValueSet values = provider.fetchValuesFor(project);
    assertTrue(checkValue(values, HAS_README, true).get());
  }

  @Test
  public void testLowercaseReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");

    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile("readme.md")).thenReturn(true);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    provider.set(NoValueCache.create());

    when(localRepository.readTextFrom("readme.md"))
        .thenReturn(Optional.of(String.join("\n",
            "This is readme.md"
        )));
    ValueSet values = provider.fetchValuesFor(project);
    assertTrue(checkValue(values, HAS_README, true).get());
  }

  void readmMeAdocTest(String fileName) throws IOException {
    GitHubProject project = new GitHubProject("test", "project");

    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile(fileName)).thenReturn(true);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    provider.set(NoValueCache.create());

    when(localRepository.readTextFrom(fileName))
        .thenReturn(Optional.of(String.join("\n",
            "This is " + fileName
        )));
    ValueSet values = provider.fetchValuesFor(project);
    assertTrue(checkValue(values, HAS_README, true).get());
  }

  @Test
  public void testReadmeAdoc() throws IOException {
    readmMeAdocTest("readme.adoc");
    readmMeAdocTest("README.adoc");
  }

  @Test
  public void testLoadingDefaultConfig() throws IOException {
    Path config = Paths.get(String.format("%s.config.yml", ReadmeInfo.class.getSimpleName()));
    String content = "---\n"
        + "requiredContentPatterns:\n"
        + "  - \"one two\"\n"
        + "  - \"three\"\n"
        + "  - \"[Tt]est\"\n";
    Files.write(config, content.getBytes());
    try {
      ReadmeInfo provider = new ReadmeInfo(fetcher);
      assertEquals(3, provider.requiredContentPatterns().size());
      assertEquals("one two", provider.requiredContentPatterns().get(0).pattern());
      assertEquals("three", provider.requiredContentPatterns().get(1).pattern());
      assertEquals("[Tt]est", provider.requiredContentPatterns().get(2).pattern());
    } finally {
      FileUtils.forceDeleteOnExit(config.toFile());
    }
  }

  private static Value<Boolean> checkValue(
      ValueSet values, Feature<Boolean> feature, boolean expected) {

    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
    return value;
  }
}
