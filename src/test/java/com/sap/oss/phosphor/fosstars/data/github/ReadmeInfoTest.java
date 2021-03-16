package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.junit.Test;

public class ReadmeInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeature() {
    assertEquals(HAS_README, new ReadmeInfo(fetcher).supportedFeature());
  }

  @Test
  public void testWithReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile("README")).thenReturn(true);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    Value<Boolean> value = provider.fetchValueFor(project);
    assertEquals(HAS_README, value.feature());
    assertFalse(value.isUnknown());
    assertTrue(value.get());
  }

  @Test
  public void testWithoutReadme() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    LocalRepository localRepository = mock(LocalRepository.class);
    when(localRepository.hasFile(any())).thenReturn(false);
    TestGitHubDataFetcher.addForTesting(project, localRepository);

    ReadmeInfo provider = new ReadmeInfo(fetcher);
    Value<Boolean> value = provider.fetchValueFor(project);
    assertEquals(HAS_README, value.feature());
    assertFalse(value.isUnknown());
    assertFalse(value.get());
  }
}