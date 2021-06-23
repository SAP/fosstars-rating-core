package com.sap.oss.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import org.junit.Test;

public class UnpatchedVulnerabilitiesTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUnpatchedVulnerability() throws IOException {
    UnpatchedVulnerabilities provider = new UnpatchedVulnerabilities(fetcher);
    GitHubProject project = new GitHubProject("apache", "batik");
    Value<Vulnerabilities> value = provider.fetchValueFor(project);
    assertFalse(value.isUnknown());
    Vulnerabilities vulnerabilities = value.get();
    assertEquals(1, vulnerabilities.size());
    Vulnerability vulnerability = vulnerabilities.iterator().next();
    assertEquals("https://issues.apache.org/jira/browse/BATIK-1189", vulnerability.id());
    assertEquals(Resolution.UNPATCHED, vulnerability.resolution());
    assertFalse(vulnerability.fixed().isPresent());
  }

}