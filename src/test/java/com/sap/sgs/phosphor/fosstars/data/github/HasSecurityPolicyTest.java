package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class HasSecurityPolicyTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testHasPolicy() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file("SECURITY.md"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));

    GitHubProject project = new GitHubProject("org", "test");

    addForTesting(project, repository);

    HasSecurityPolicy provider = new HasSecurityPolicy(fetcher);
    provider.set(new GitHubProjectValueCache());

    check(provider, true);
  }

  @Test
  public void testNoPolicy() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file("SECURITY.md")).thenReturn(Optional.empty());

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    HasSecurityPolicy provider = new HasSecurityPolicy(fetcher);
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