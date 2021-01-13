package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class NumberOfContributorsTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUpdate() throws IOException {
    final List<Commit> commits = new ArrayList<>();

    Commit commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("Mr. Yellow");
    when(commit.committerName()).thenReturn("Mr. Black");
    commits.add(commit);

    commit = mock(Commit.class);
    when(commit.date()).thenReturn(new Date());
    when(commit.authorName()).thenReturn("Mr. White");
    when(commit.committerName()).thenReturn("Mr. Pink");
    commits.add(commit);

    LocalRepository repository = mock(LocalRepository.class);
    when(repository.commitsAfter(any())).thenReturn(commits);

    GitHubProject project = new GitHubProject("org", "test");

    addForTesting(project, repository);

    NumberOfContributors provider = new NumberOfContributors(fetcher);

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS));
    Optional<Value<Integer>> something = values.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS);
    assertTrue(something.isPresent());
    Value<Integer> numberOfContributors = something.get();
    assertEquals(4, (int) numberOfContributors.get());
  }
}