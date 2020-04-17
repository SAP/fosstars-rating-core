package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

public class UsesDependabotTest {

  @Test
  public void foundCommits() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesDependabot provider = new UsesDependabot(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHUser user = mock(GHUser.class);
    when(user.getLogin()).thenReturn("dependabot");

    GHCommit.ShortInfo commitInfo = mock(GHCommit.ShortInfo.class);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getAuthor()).thenReturn(user);
    when(commit.getCommitShortInfo()).thenReturn(commitInfo);
    when(commit.getCommitDate()).thenReturn(Date.from(Instant.now().minus(Duration.ofDays(100))));
    List<GHCommit> list = new ArrayList<>();
    list.add(commit);

    GitHubProject project = mock(GitHubProject.class);

    GHRepository repository = mock(GHRepository.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);
    when(fetcher.commitsAfter(any(), eq(project), eq(github))).thenReturn(list);

    testProvider(true, github, project, provider);
  }


  @Test
  public void foundConfig() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesDependabot provider = new UsesDependabot(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

    GHContent content = mock(GHContent.class);
    when(content.isFile()).thenReturn(true);
    when(content.getSize()).thenReturn(100L);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getFileContent(".dependabot/config.yml")).thenReturn(content);

    GitHubProject project = mock(GitHubProject.class);

    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);

    testProvider(true, github, project, provider);
  }

  @Test
  public void noDependabot() throws IOException {
    GitHub github = mock(GitHub.class);

    UsesDependabot provider = new UsesDependabot(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());

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

    List<GHCommit> list = new ArrayList<>();
    list.add(commit);

    GitHubProject project = mock(GitHubProject.class);

    GHRepository repository = mock(GHRepository.class);
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.repositoryFor(project, github)).thenReturn(repository);
    when(fetcher.commitsAfter(any(), eq(project), eq(github))).thenReturn(list);

    testProvider(false, github, project, provider);
  }
  
  private static void testProvider(boolean expected, GitHub github, GitHubProject project,
      UsesDependabot provider)
      throws IOException {

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(USES_DEPENDABOT));
    Optional<Value> something = values.of(USES_DEPENDABOT);
    assertTrue(something.isPresent());
    Value numberOfContributors = something.get();
    assertEquals(expected, numberOfContributors.get());
  }
}