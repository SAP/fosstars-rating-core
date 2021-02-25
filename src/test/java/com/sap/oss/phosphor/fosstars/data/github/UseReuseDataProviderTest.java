package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.UseReuseDataProvider.REUSE_CONFIG;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class UseReuseDataProviderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  @Test
  public void testSupportedFeature() {
    assertEquals(USES_REUSE, new UseReuseDataProvider(fetcher).supportedFeature());
  }

  @Test
  public void testFetchValueFor() throws IOException {
    LocalRepository localRepository = mock(LocalRepository.class);
    TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);

    when(localRepository.hasFile(REUSE_CONFIG)).thenReturn(false);
    UseReuseDataProvider provider = new UseReuseDataProvider(fetcher);
    check(provider, false);

    when(localRepository.hasFile(REUSE_CONFIG)).thenReturn(true);
    check(provider, true);
  }

  private static void check(UseReuseDataProvider provider, boolean expectedValue)
      throws IOException {

    ValueSet values = provider.fetchValuesFor(PROJECT);
    assertEquals(1, values.size());
    Optional<Value<Boolean>> something = values.of(USES_REUSE);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expectedValue, value.get());
  }

}