package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class UsesGithubForDevelopmentTest {

  @Test
  public void testWithMirrorUrl() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHRepository repository = mock(GHRepository.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);

    GitHubProject project = mock(GitHubProject.class);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);
    when(repository.getMirrorUrl()).thenReturn("https://other.scm.com/org/original_repository");

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertFalse(values.of(USES_GITHUB_FOR_DEVELOPMENT).get().isUnknown());
    assertEquals(USES_GITHUB_FOR_DEVELOPMENT.value(false),
        values.of(USES_GITHUB_FOR_DEVELOPMENT).get());
  }

  @Test
  public void testWithoutMirrorUrl() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHRepository repository = mock(GHRepository.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);

    GitHubProject project = mock(GitHubProject.class);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);
    when(repository.getMirrorUrl()).thenReturn(null);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertFalse(values.of(USES_GITHUB_FOR_DEVELOPMENT).get().isUnknown());
    assertEquals(USES_GITHUB_FOR_DEVELOPMENT.value(true),
        values.of(USES_GITHUB_FOR_DEVELOPMENT).get());
  }

  @Test
  public void testWithEmptyMirrorUrl() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHRepository repository = mock(GHRepository.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);

    GitHubProject project = mock(GitHubProject.class);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);
    when(repository.getMirrorUrl()).thenReturn(StringUtils.EMPTY);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertFalse(values.of(USES_GITHUB_FOR_DEVELOPMENT).get().isUnknown());
    assertEquals(USES_GITHUB_FOR_DEVELOPMENT.value(true),
        values.of(USES_GITHUB_FOR_DEVELOPMENT).get());
  }

  @Test
  public void testWhenGettingRepositoryFails() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);

    GitHubProject project = mock(GitHubProject.class);
    when(fetcher.repositoryFor(project, github)).thenThrow(new IOException());

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).get().isUnknown());
  }
}
