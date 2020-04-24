package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.junit.Test;
import org.kohsuke.github.GitHub;

public class UnpatchedVulnerabilitiesTest {

  @Test
  public void testUnpatchedVulnerability() throws IOException {
    NVD nvd = new NVD();
    nvd = spy(nvd);
    try (InputStream content = getClass().getResourceAsStream("NVD_part_CVE-2014-0171.json")) {
      when(nvd.contents()).thenReturn(Collections.singletonList(content));
      nvd.parse();

      GitHub github = mock(GitHub.class);
      UnpatchedVulnerabilities provider = new UnpatchedVulnerabilities(github, nvd);

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