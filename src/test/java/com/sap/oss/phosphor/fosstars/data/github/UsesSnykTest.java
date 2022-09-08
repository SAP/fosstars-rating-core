package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

public class UsesSnykTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  @Test
  public void testWithSnykInAuthorName() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("snyk");
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

    testProvider(USES_SNYK.value(true), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(false));
  }

  @Test
  public void testWithSnykInCommitterName() throws IOException {
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
    when(commit.committerName()).thenReturn("snyk");
    commits.add(commit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(USES_SNYK.value(true), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(false));
  }

  @Test
  public void testWithSnykInCommitMessage() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("GitHub");
    when(commit.committerName()).thenReturn("GitHub");
    when(commit.message()).thenReturn(Arrays.asList("Commit message", "Signed-off-by: snyk"));
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

    testProvider(USES_SNYK.value(true), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(false));
  }

  @Test
  public void testWithSnykPolicyFile() throws IOException {
    Path tempDirectory = Files.createTempDirectory(getClass().getName());
    File snykPolicyFile = tempDirectory.resolve(".snyk").toFile();
    List<Path> snykFilePaths = new ArrayList<>();
    snykFilePaths.add(snykPolicyFile.toPath());
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.files(any())).thenReturn(snykFilePaths);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(USES_SNYK.value(true), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(false));
  }

  @Test
  public void testNoSnyk() throws IOException {
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
    when(repository.files(any())).thenReturn(new ArrayList<>());
    when(repository.commitsAfter(any())).thenReturn(commits);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);

    testProvider(USES_SNYK.value(false), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(false));
  }

  @Test
  public void testWithOpenPullRequestFromSnyk() throws IOException {
    Path tempDirectory = Files.createTempDirectory(getClass().getName());
    File snykPolicyFile = tempDirectory.resolve(".snyk").toFile();
    List<Path> snykFilePaths = new ArrayList<>();
    snykFilePaths.add(snykPolicyFile.toPath());
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.files(any())).thenReturn(snykFilePaths);
    addForTesting(PROJECT, repository);

    GHRepository githubRepository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(githubRepository);
    GHPullRequest pullRequest = mock(GHPullRequest.class);
    GHUser user = mock(GHUser.class);
    when(user.getName()).thenReturn("snyk");
    when(pullRequest.getUser()).thenReturn(user);
    when(githubRepository.getPullRequests(any()))
        .thenReturn(Collections.singletonList(pullRequest));

    testProvider(USES_SNYK.value(true), HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(true));
  }

  private void testProvider(Value<Boolean>... expectedValues) throws IOException {
    UsesSnyk provider = new UsesSnyk(fetcher);
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
