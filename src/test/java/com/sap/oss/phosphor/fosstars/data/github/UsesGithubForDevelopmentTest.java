package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.UsesGithubForDevelopment.notGitHubUrl;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

public class UsesGithubForDevelopmentTest extends TestGitHubDataFetcherHolder {

  private static class RepositoryMockBuilder {

    GHRepository repository;
    Set<Integer> passedChecks;

    private final List<Consumer<Boolean>> checks = Arrays.asList(
        passed -> when(repository.getDescription())
          .thenReturn(passed ? "This is the main repository" : "This is a mirror"),
        passed -> when(repository.hasIssues()).thenReturn(passed),
        passed -> when(repository.hasWiki()).thenReturn(passed),
        passed -> {
          when(repository.getMirrorUrl()).thenReturn(passed ? "" : "https://test.com");
          when(repository.getSvnUrl()).thenReturn(passed ? "" : "https://test.com");
        },
        passed -> when(repository.isArchived()).thenReturn(!passed)
    );

    RepositoryMockBuilder() {
      init();
    }

    final void init() {
      repository = mock(GHRepository.class);
      passedChecks = new HashSet<>();
      checks.forEach(check -> check.accept(false));
    }

    int allChecks() {
      return checks.size();
    }

    int passedChecks() {
      return passedChecks.size();
    }

    void passCheck(int i) {
      checks.get(i).accept(true);
      passedChecks.add(i);
    }

    GHRepository repository() {
      return repository;
    }

  }

  @Test
  public void testVariousChecks() throws IOException {
    Random random = new Random();
    random.setSeed(0);

    RepositoryMockBuilder builder = new RepositoryMockBuilder();

    int i = 0;
    final double threshold = 0.5;
    while (i < builder.allChecks()) {
      builder.init();
      random.ints(i, 0, builder.allChecks())
          .forEach(builder::passCheck);
      boolean expected = (double) builder.passedChecks() / builder.allChecks() >= threshold;
      assertEquals(
          expected,
          UsesGithubForDevelopment.usesGitHubForDevelopment(
              builder.repository(), threshold));
      i++;
    }
  }

  @Test
  public void testAllPassedChecks() throws IOException {
    RepositoryMockBuilder builder = new RepositoryMockBuilder();
    for (int i = 0; i < builder.allChecks(); i++) {
      builder.passCheck(i);
    }
    assertEquals(builder.allChecks(), builder.passedChecks());
    assertTrue(
        UsesGithubForDevelopment.usesGitHubForDevelopment(
            builder.repository(), 0.99));
  }

  @Test
  public void testAllFailedChecks() throws IOException {
    RepositoryMockBuilder builder = new RepositoryMockBuilder();
    assertFalse(
        UsesGithubForDevelopment.usesGitHubForDevelopment(
            builder.repository(), 0.01));
  }

  @Test
  public void testWhenGettingRepositoryFails() throws IOException {
    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new SubjectValueCache());

    GitHubProject project = new GitHubProject("org", "test");
    when(fetcher.github().getRepository(any())).thenThrow(new IOException());

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).get().isUnknown());
  }

  @Test
  public void testWithGithubDirectory() throws IOException {
    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new SubjectValueCache());

    GHRepository repository = mock(GHRepository.class);
    when(repository.getDirectoryContent(".github"))
        .thenReturn(Collections.singletonList(mock(GHContent.class)));

    GitHubProject project = new GitHubProject("org", "test");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertEquals(Boolean.TRUE, values.of(USES_GITHUB_FOR_DEVELOPMENT).get().get());
  }

  @Test
  public void testWithoutGithubDirectory() throws IOException {
    UsesGithubForDevelopment provider = new UsesGithubForDevelopment(fetcher);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new SubjectValueCache());

    GHRepository repository = mock(GHRepository.class);
    when(repository.getDirectoryContent(".github"))
        .thenThrow(new IOException("no directory found"));
    when(repository.isAllowMergeCommit()).thenReturn(true);

    GitHubProject project = new GitHubProject("org", "test");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_GITHUB_FOR_DEVELOPMENT));
    assertTrue(values.of(USES_GITHUB_FOR_DEVELOPMENT).isPresent());
    assertEquals(Boolean.TRUE, values.of(USES_GITHUB_FOR_DEVELOPMENT).get().get());
  }

  @Test
  public void testNotGitHubUrl() {
    assertTrue(notGitHubUrl("invalid"));
    assertTrue(notGitHubUrl(""));
    assertTrue(notGitHubUrl("https://test.com/test"));
    assertFalse(notGitHubUrl("https://github.com/apache/nifi"));
  }
}