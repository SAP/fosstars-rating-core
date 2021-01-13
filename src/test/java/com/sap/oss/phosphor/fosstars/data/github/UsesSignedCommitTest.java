package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

public class UsesSignedCommitTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testCommitWithSignature() throws IOException {
    testProvider(true);
  }

  @Test
  public void testCommitWithoutSignature() throws IOException {
    testProvider(false);
  }

  private void testProvider(boolean value) throws IOException {
    final UsesSignedCommits provider = new UsesSignedCommits(fetcher);

    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("Mr. Test");
    when(commit.committerName()).thenReturn("Mr. Test");
    when(commit.isSigned()).thenReturn(value);
    commits.add(commit);

    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsWithin(any())).thenReturn(commits);

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_SIGNED_COMMITS));
    assertTrue(values.of(USES_SIGNED_COMMITS).isPresent());
    assertFalse(values.of(USES_SIGNED_COMMITS).get().isUnknown());
    assertEquals(USES_SIGNED_COMMITS.value(value),
        values.of(USES_SIGNED_COMMITS).get());
  }

  @Test
  public void testWhenGettingRepositoryFails() throws IOException {
    final UsesSignedCommits provider = new UsesSignedCommits(fetcher);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsWithin(any())).thenThrow(new IOException());

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_SIGNED_COMMITS));
    assertTrue(values.of(USES_SIGNED_COMMITS).isPresent());
    assertTrue(values.of(USES_SIGNED_COMMITS).get().isUnknown());
  }
}
