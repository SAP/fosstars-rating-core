package com.sap.sgs.phosphor.fosstars.nvd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.github.VendorDataMatcher;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class NVDTest {

  @Test
  public void get() throws IOException {
    NVD nvd = new NVD();
    nvd = spy(nvd);
    try (InputStream content = getClass().getResourceAsStream("NVD_part_2014.json")) {
      when(nvd.contents()).thenReturn(Collections.singletonList(content));
      assertFalse(nvd.downloadFailed());
      nvd.parse();
      Optional<NvdEntry> something = nvd.get("CVE-2014-0171");
      assertTrue(something.isPresent());
      NvdEntry nvdEntry = something.get();
      assertNotNull(nvdEntry);
      assertEquals("CVE-2014-0171", nvdEntry.getCve().getCveDataMeta().getID());
      assertNotNull(nvdEntry.getConfigurations());
      assertEquals(2, nvdEntry.getConfigurations().getNodes().size());
    }
  }

  @Test
  public void find() throws IOException {
    NVD nvd = new NVD();
    nvd = spy(nvd);
    try (InputStream content = getClass().getResourceAsStream("NVD_part_2014.json")) {
      when(nvd.contents()).thenReturn(Collections.singletonList(content));
      nvd.parse();

      List<NvdEntry> entries = nvd.search(
          VendorDataMatcher.with(
              new GitHubProject("odata4j_project", "odata4j")));
      assertNotNull(entries);

      assertEquals(1, entries.size());

      entries = nvd.search(
          VendorDataMatcher.with(
              new GitHubProject("not_existing", "odata4j")));
      assertNotNull(entries);
      assertEquals(0, entries.size());
    }
  }
}
