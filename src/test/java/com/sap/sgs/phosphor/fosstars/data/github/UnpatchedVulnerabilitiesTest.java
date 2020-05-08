package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.nvd.TestNVD;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class UnpatchedVulnerabilitiesTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUnpatchedVulnerability() throws IOException {
    TestNVD nvd = new TestNVD();
    nvd = spy(nvd);
    try (InputStream content = getClass().getResourceAsStream("NVD_part_CVE-2014-0171.json")) {
      nvd.add("file.json", content);
      nvd.parse();

      UnpatchedVulnerabilities provider = new UnpatchedVulnerabilities(fetcher, nvd);

      GitHubProject project = new GitHubProject("odata4j", "odata4j");

      Vulnerabilities vulnerabilities = provider.vulnerabilitiesFromNvdFor(project);
      assertEquals(1, vulnerabilities.entries().size());
      Vulnerability vulnerability = vulnerabilities.entries().iterator().next();
      assertEquals("CVE-2014-0171", vulnerability.id());
      assertEquals(Resolution.UNPATCHED, vulnerability.resolution());

      project = new GitHubProject("apache", "olingo-odata4");

      vulnerabilities = provider.vulnerabilitiesFromNvdFor(project);
      assertEquals(0, vulnerabilities.entries().size());
    }
  }

}