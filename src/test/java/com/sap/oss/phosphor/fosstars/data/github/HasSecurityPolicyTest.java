package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class HasSecurityPolicyTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testIfProjectHasPolicy() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file("SECURITY.md"))
        .thenReturn(Optional.of(StringUtils.repeat("x", 1000)));

    GitHubProject project = new GitHubProject("org", "test");

    addForTesting(project, repository);

    HasSecurityPolicy provider = new HasSecurityPolicy(fetcher);
    provider.set(new SubjectValueCache());

    check(provider, true);
  }

  @Test
  public void testIfOrganizationHasPolicy() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file("SECURITY.md")).thenReturn(Optional.empty());

    GitHubProject project = new GitHubProject("org", "test");

    addForTesting(project, repository);

    StatusLine statusLine = mock(StatusLine.class);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);

    CloseableHttpClient client = mock(CloseableHttpClient.class);
    when(client.execute(any())).thenReturn(response);

    HasSecurityPolicy provider = new HasSecurityPolicy(fetcher);
    provider.set(NoValueCache.create());
    provider = spy(provider);
    when(provider.httpClient()).thenReturn(client);

    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    check(provider, false);

    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
    check(provider, true);
  }

  @Test
  public void testNoPolicy() throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.file("SECURITY.md")).thenReturn(Optional.empty());

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    HasSecurityPolicy provider = spy(new HasSecurityPolicy(fetcher));

    CloseableHttpClient client = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(client);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(client.execute(any(HttpGet.class))).thenReturn(response);

    StatusLine statusLine = mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);

    provider.set(new SubjectValueCache());

    check(provider, false);
  }

  private static void check(HasSecurityPolicy provider, boolean expectedValue) throws IOException {
    ValueHashSet values = new ValueHashSet();
    GitHubProject project = new GitHubProject("org", "test");
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(HAS_SECURITY_POLICY));
    Optional<Value<Boolean>> something = values.of(HAS_SECURITY_POLICY);
    assertNotNull(something);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertNotNull(value);
    assertEquals(expectedValue, value.get());
    if (!expectedValue) {
      assertFalse(value.explanation().isEmpty());
    }
  }
}