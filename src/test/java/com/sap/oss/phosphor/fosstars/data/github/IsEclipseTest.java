package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.junit.Test;


public class IsEclipseTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testProjects() throws IOException {
    IsEclipse provider = new IsEclipse(fetcher);
    assertTrue(provider.fetchValueFor(
        GitHubProject.parse("https://github.com/eclipse/jgit")).get());
    assertTrue(provider.fetchValueFor(
        GitHubProject.parse("https://github.com/eclipse-ee4j/eclipselink")).get());
    assertFalse(provider.fetchValueFor(
        GitHubProject.parse("https://github.com/apache/nifi")).get());
  }

  @Test
  public void testSupportedFeature() {
    assertEquals(IS_ECLIPSE, new IsEclipse(fetcher).supportedFeature());
  }
}