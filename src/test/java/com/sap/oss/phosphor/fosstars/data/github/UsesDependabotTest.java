package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

public class UsesDependabotTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  @Test
  public void testWithDependabotInAuthorName() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("dependabot");
    when(commit.committerName()).thenReturn("GitHub");
    commits.add(commit);

    Commit otherCommit = mock(Commit.class);
    when(otherCommit.date()).thenReturn(new Date());
    when(otherCommit.authorName()).thenReturn("Mr. Test");
    when(otherCommit.committerName()).thenReturn("Mr. Test");
    commits.add(otherCommit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testWithDependabotInCommitterName() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Commit otherCommit = mock(Commit.class);
      when(otherCommit.date()).thenReturn(new Date());
      when(otherCommit.authorName()).thenReturn("Someone");
      when(otherCommit.committerName()).thenReturn("Someone");
      commits.add(otherCommit);
    }

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("GitHub");
    when(commit.committerName()).thenReturn("dependabot");
    commits.add(commit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testWithDependabotInCommitMessage() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("GitHub");
    when(commit.committerName()).thenReturn("GitHub");
    when(commit.message()).thenReturn(Arrays.asList("Commit message", "Signed-off-by: dependabot"));
    commits.add(commit);

    Commit otherCommit = mock(Commit.class);
    when(otherCommit.date()).thenReturn(new Date());
    when(otherCommit.authorName()).thenReturn("Mr. Pink");
    when(otherCommit.committerName()).thenReturn("Mr. Pink");
    commits.add(otherCommit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testWithOldDependabotConfig() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".dependabot/config.yml"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testWithNewDependabotConfig() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".github/dependabot.yml"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testNoDependabot() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("Mr. Orange");
    when(commit.committerName()).thenReturn("Mr. Orange");
    when(commit.message()).thenReturn(Arrays.asList("Commit message", "Signed-off-by: Mr. Pink"));
    commits.add(commit);

    commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("Mr. Pink");
    when(commit.committerName()).thenReturn("Mr. Pink");
    when(commit.message()).thenReturn(Arrays.asList("Commit message", "Signed-off-by: Mr. Orange"));
    commits.add(commit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".dependabot/config.yml")).thenReturn(Optional.empty());
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(
        USES_DEPENDABOT.value(false), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(false));
  }

  @Test
  public void testWithOpenPullRequestFromDependabot() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".github/dependabot.yml"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);
    GHPullRequest pullRequest = mock(GHPullRequest.class);
    GHUser user = mock(GHUser.class);
    when(user.getName()).thenReturn("dependabot");
    when(pullRequest.getUser()).thenReturn(user);
    when(githubRepository.getPullRequests(any()))
        .thenReturn(Collections.singletonList(pullRequest));

    testProvider(
        USES_DEPENDABOT.value(true), HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(true));
  }
  
  private void testProvider(Value<Boolean>... expectedValues) throws IOException {
    UsesDependabot provider = new UsesDependabot(fetcher);
    provider.set(new SubjectValueCache());

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(PROJECT, values);
    assertEquals(expectedValues.length, values.size());
    for (Value<Boolean> expectedValue : expectedValues) {
      Feature<Boolean> feature = expectedValue.feature();
      assertTrue(values.has(feature));
      Optional<Value<Boolean>> actualValue = values.of(feature);
      assertTrue(actualValue.isPresent());
      assertEquals(expectedValue, actualValue.get());
    }
  }
}