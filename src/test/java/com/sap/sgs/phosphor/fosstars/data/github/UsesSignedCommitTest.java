package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.TestGitHubDataFetcherHolder;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.ShortInfo;
import org.kohsuke.github.GHVerification;

public class UsesSignedCommitTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testCommitWithSignature() throws IOException {
    UsesSignedCommits provider = new UsesSignedCommits(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHCommit commit = mock(GHCommit.class);
    List<GHCommit> list = new ArrayList<>();
    list.add(commit);

    ShortInfo info = mock(ShortInfo.class);
    when(commit.getCommitShortInfo()).thenReturn(info);

    GHVerification verification = mock(GHVerification.class);
    when(info.getVerification()).thenReturn(verification);
    when(verification.isVerified()).thenReturn(true);

    GitHubProject project = mock(GitHubProject.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.commitsAfter(any(), eq(project))).thenReturn(list);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
    assertFalse(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
    assertEquals(USES_VERIFIED_SIGNED_COMMITS.value(true),
        values.of(USES_VERIFIED_SIGNED_COMMITS).get());
  }

  @Test
  public void testCommitWithoutSignature() throws IOException {
    UsesSignedCommits provider = new UsesSignedCommits(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHCommit commit = mock(GHCommit.class);
    List<GHCommit> list = new ArrayList<>();
    list.add(commit);

    ShortInfo info = mock(ShortInfo.class);
    when(commit.getCommitShortInfo()).thenReturn(info);

    GHVerification verification = mock(GHVerification.class);
    when(info.getVerification()).thenReturn(verification);
    when(verification.isVerified()).thenReturn(false);

    GitHubProject project = mock(GitHubProject.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.commitsAfter(any(), eq(project))).thenReturn(list);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
    assertFalse(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
    assertEquals(USES_VERIFIED_SIGNED_COMMITS.value(false),
        values.of(USES_VERIFIED_SIGNED_COMMITS).get());
  }

  @Test
  public void testWhenGettingRepositoryFails() throws IOException {
    UsesSignedCommits provider = new UsesSignedCommits(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHCommit commit = mock(GHCommit.class);
    List<GHCommit> list = new ArrayList<>();
    list.add(commit);

    GitHubProject project = mock(GitHubProject.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.commitsAfter(any(), eq(project))).thenThrow(new IOException());

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
  }
}
