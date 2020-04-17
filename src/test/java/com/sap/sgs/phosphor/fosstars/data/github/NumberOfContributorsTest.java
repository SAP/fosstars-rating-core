package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

public class NumberOfContributorsTest {

  @Test
  public void update() throws IOException {
    GHUser githubAuthor = mock(GHUser.class);
    when(githubAuthor.getLogin()).thenReturn("one");

    GHUser githubCommitter = mock(GHUser.class);
    when(githubCommitter.getLogin()).thenReturn("two");

    GHCommit commitWithGitHubUsers = mock(GHCommit.class);
    when(commitWithGitHubUsers.getAuthor()).thenReturn(githubAuthor);
    when(commitWithGitHubUsers.getCommitter()).thenReturn(githubCommitter);
    when(commitWithGitHubUsers.getCommitDate()).thenReturn(new Date());

    GHCommit.GHAuthor gitAuthor = mock(GHCommit.GHAuthor.class);
    when(gitAuthor.getName()).thenReturn("three");

    GHCommit.GHAuthor gitCommitter = mock(GHCommit.GHAuthor.class);
    when(gitCommitter.getName()).thenReturn("four");

    GHCommit.ShortInfo commitInfo = mock(GHCommit.ShortInfo.class);
    when(commitInfo.getAuthor()).thenReturn(gitAuthor);
    when(commitInfo.getCommitter()).thenReturn(gitCommitter);

    GHCommit commitWithGitUsers = mock(GHCommit.class);
    when(commitWithGitUsers.getAuthor()).thenReturn(null);
    when(commitWithGitUsers.getCommitter()).thenReturn(null);
    when(commitWithGitUsers.getCommitShortInfo()).thenReturn(commitInfo);
    when(commitWithGitUsers.getCommitDate()).thenReturn(new Date());

    List<GHCommit> list = new ArrayList<>();
    list.add(commitWithGitUsers);
    list.add(commitWithGitHubUsers);

    GitHub github = mock(GitHub.class);
    final GitHubProject project = new GitHubProject("test", "test");
    NumberOfContributors provider = new NumberOfContributors(github);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new GitHubProjectValueCache());
    
    GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);
    when(fetcher.commitsAfter(any(), eq(project), eq(github))).thenReturn(list);

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS));
    Optional<Value> something = values.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS);
    assertTrue(something.isPresent());
    Value numberOfContributors = something.get();
    assertEquals(4, numberOfContributors.get());
  }
}