package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcherTest;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

public class AbstractStaticScanToolsDataProviderTest extends GitHubDataFetcherTest {

  private static GitHubDataFetcher FETCHER = mock(GitHubDataFetcher.class);

  @Test
  public void testBanditSupportedFeatures() {
    Set<Feature<?>> features = setOf(RUNS_BANDIT_SCANS, USES_BANDIT_SCAN_CHECKS);
    SastDataProvider provider = new SastDataProvider(FETCHER, features);

    assertEquals(2, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().containsAll(features));
  }

  @Test
  public void testCodeqlSupportedFeatures() {
    Set<Feature<?>> features = setOf(RUNS_CODEQL_SCANS, USES_CODEQL_CHECKS);
    SastDataProvider provider = new SastDataProvider(FETCHER, features);

    assertEquals(2, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().containsAll(features));
  }

  private static class SastDataProvider extends AbstractStaticScanToolsDataProvider {

    public SastDataProvider(
        GitHubDataFetcher fetcher,
        Set<Feature<?>> supportedFeatures) {
      super(fetcher, supportedFeatures);
    }

    @Override
    protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
      throw new NotImplementedException("The method is not implemented in this test class");
    }
  }
}