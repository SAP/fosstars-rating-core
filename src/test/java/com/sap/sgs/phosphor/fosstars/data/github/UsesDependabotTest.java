package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class UsesDependabotTest extends TestGitHubDataFetcherHolder {

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

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(true, project);
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

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(true, project);
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

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(true, project);
  }

  @Test
  public void testWithOldDependabotConfig() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".dependabot/config.yml"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(true, project);
  }

  @Test
  public void testWithNewDependabotConfig() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(".github/dependabot.yml"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(true, project);
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

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    testProvider(false, project);
  }
  
  private void testProvider(boolean expected, GitHubProject project) throws IOException {
    UsesDependabot provider = new UsesDependabot(fetcher);
    provider.set(new GitHubProjectValueCache());

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