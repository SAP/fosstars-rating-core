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
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ReadmeInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() {
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

    when(localRepository.read("README"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "This is README",
            "",
            "# Mandatory header",
            "",
            "Don't trouble trouble till trouble troubles you."
        ))));
    checkValues(provider.fetchValuesFor(project), true, false);

    when(localRepository.read("README"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "This is README",
            "",
            "# Another header"
        ))));
    checkValues(provider.fetchValuesFor(project), true, true);

    when(localRepository.read("README"))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n",
            "This is README",
            "",
            "# Mandatory header",
            "",
            "Prohibited phrase",
            ""
        ))));
    checkValues(provider.fetchValuesFor(project), true, true);
  }

  @Test
  public void testWithoutReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile(any())).thenReturn(false);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    checkValues(provider.fetchValuesFor(project), false, false);
  }

  private static void checkValues(
      ValueSet values, boolean expectedHasReadme, boolean expectedIncompleteReadme) {

    Optional<Value<Boolean>> something = values.of(HAS_README);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertFalse(value.isUnknown());
    assertEquals(expectedHasReadme, value.get());

    something = values.of(INCOMPLETE_README);
    assertTrue(something.isPresent());
    value = something.get();
    assertFalse(value.isUnknown());
    assertEquals(expectedIncompleteReadme, value.get());
  }
}