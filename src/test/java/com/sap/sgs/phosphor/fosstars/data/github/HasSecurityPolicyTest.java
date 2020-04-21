package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class HasSecurityPolicyTest {

  @Before
  @After
  public void cleanup() {
    GitHubDataFetcher.instance().repositoryCache().clear();
    GitHubDataFetcher.instance().commitsCache().clear();
  }

  @Test
  public void testHasPolicy() throws IOException {
    GHContent content = mock(GHContent.class);
    when(content.isFile()).thenReturn(true);
    when(content.getSize()).thenReturn(1000L);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getFileContent("SECURITY.md")).thenReturn(content);

    GitHub github = mock(GitHub.class);
    when(github.getRepository(any())).thenReturn(repository);

    HasSecurityPolicy provider = new HasSecurityPolicy(github);
    provider.set(new GitHubProjectValueCache());

    check(provider, true);
  }

  @Test
  public void testNoPolicy() throws IOException {
    GHRepository repository = mock(GHRepository.class);
    when(repository.getFileContent("SECURITY.md")).thenThrow(IOException.class);

    GitHub github = mock(GitHub.class);
    when(github.getRepository(any())).thenReturn(repository);

    HasSecurityPolicy provider = new HasSecurityPolicy(github);
    provider.set(new GitHubProjectValueCache());

    check(provider, false);
  }

  private static void check(HasSecurityPolicy provider, boolean expectedValue) throws IOException {
    ValueHashSet values = new ValueHashSet();
    GitHubProject project = new GitHubProject("org", "test");
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(HAS_SECURITY_POLICY));
    Optional<Value> something = values.of(HAS_SECURITY_POLICY);
    assertNotNull(something);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertNotNull(value);
    assertEquals(expectedValue, value.get());
  }
}