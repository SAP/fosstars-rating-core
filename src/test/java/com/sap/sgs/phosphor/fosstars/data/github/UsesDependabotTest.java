package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class UsesDependabotTest {

  @Test
  public void foundCommits() throws IOException {
    GHUser user = mock(GHUser.class);
    when(user.getLogin()).thenReturn("dependabot");

    GHCommit.ShortInfo commitInfo = mock(GHCommit.ShortInfo.class);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getAuthor()).thenReturn(user);
    when(commit.getCommitShortInfo()).thenReturn(commitInfo);
    when(commit.getCommitDate()).thenReturn(Date.from(Instant.now().minus(Duration.ofDays(100))));

    PagedIterator<GHCommit> iterator = mock(PagedIterator.class);
    when(iterator.hasNext()).thenReturn(true, false);
    when(iterator.next()).thenReturn(commit);

    PagedIterable iterable = mock(PagedIterable.class);
    when(iterable.iterator()).thenReturn(iterator);

    GHCommitQueryBuilder builder = mock(GHCommitQueryBuilder.class);
    when(builder.since(any())).thenReturn(builder);
    when(builder.list()).thenReturn(iterable);

    GHRepository repository = mock(GHRepository.class);
    when(repository.queryCommits()).thenReturn(builder);

    GitHub github = mock(GitHub.class);
    when(github.getRepository(anyString())).thenReturn(repository);

    testProvider(true, github);
  }

  @Test
  public void foundConfig() throws IOException {
    GHContent content = mock(GHContent.class);
    when(content.isFile()).thenReturn(true);
    when(content.getSize()).thenReturn(100L);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getFileContent(".dependabot/config.yml")).thenReturn(content);

    GitHub github = mock(GitHub.class);
    when(github.getRepository(anyString())).thenReturn(repository);

    testProvider(true, github);
  }

  @Test
  public void noDependabot() throws IOException {
    GHUser githubUser = mock(GHUser.class);
    when(githubUser.getLogin()).thenReturn("someone");

    GHCommit.GHAuthor gitUser = mock(GHCommit.GHAuthor.class);
    when(gitUser.getName()).thenReturn("someone");

    GHCommit.ShortInfo commitInfo = mock(GHCommit.ShortInfo.class);
    when(commitInfo.getAuthor()).thenReturn(gitUser);
    when(commitInfo.getCommitter()).thenReturn(gitUser);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getAuthor()).thenReturn(githubUser);
    when(commit.getCommitter()).thenReturn(githubUser);
    when(commit.getCommitShortInfo()).thenReturn(commitInfo);
    when(commit.getCommitDate()).thenReturn(Date.from(Instant.now().minus(Duration.ofDays(100))));

    PagedIterator<GHCommit> iterator = mock(PagedIterator.class);
    when(iterator.hasNext()).thenReturn(true, false);
    when(iterator.next()).thenReturn(commit);

    PagedIterable iterable = mock(PagedIterable.class);
    when(iterable.iterator()).thenReturn(iterator);

    GHCommitQueryBuilder builder = mock(GHCommitQueryBuilder.class);
    when(builder.since(any())).thenReturn(builder);
    when(builder.list()).thenReturn(iterable);

    GHRepository repository = mock(GHRepository.class);
    when(repository.queryCommits()).thenReturn(builder);

    GitHub github = mock(GitHub.class);
    when(github.getRepository(anyString())).thenReturn(repository);

    testProvider(false, github);
  }

  private static void testProvider(boolean expected, GitHub github) {
    UsesDependabot provider = new UsesDependabot("test", "test", github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new ValueCache());

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(values);
    assertEquals(1, values.size());
    assertTrue(values.has(USES_DEPENDABOT));
    Optional<Value> something = values.of(USES_DEPENDABOT);
    assertTrue(something.isPresent());
    Value numberOfContributors = something.get();
    assertEquals(expected, numberOfContributors.get());
  }

}