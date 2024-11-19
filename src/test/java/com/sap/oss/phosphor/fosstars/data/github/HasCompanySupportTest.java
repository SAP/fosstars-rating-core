package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class HasCompanySupportTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeature() throws IOException {
    HasCompanySupport provider = new HasCompanySupport(fetcher);
    assertEquals(SUPPORTED_BY_COMPANY, provider.supportedFeature());
  }

  @Test
  public void testGoogleLighthouse() throws IOException {
    HasCompanySupport provider = new HasCompanySupport(fetcher);
    GitHubProject project = GitHubProject.parse("https://github.com/GoogleChrome/lighthouse");
    Value<Boolean> value = provider.fetchValueFor(project);
    assertFalse(value.isUnknown());
    assertEquals(true, value.get());
  }
}
