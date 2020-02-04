package com.sap.sgs.phosphor.fosstars.nvd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.nvd.data.NVDEntry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The test is unstable since it tries downloading data from NVD.
 * It needs to be improved to test only parsing data from NVD.
 * Meanwhile, the test is ignored.
 */
@Ignore
public class NVDTest {

  @Test
  public void download() throws IOException {
    NVD nvd = new NVD();
    nvd.download();

    if (nvd.downloadFailed()) {
      return;
    }

    assertNotNull(nvd.location());
    assertTrue(Files.exists(Paths.get(nvd.location())));

    List<String> files = nvd.jsonFiles();
    assertNotNull(files);

    assertTrue(files.size() > 0);
    for (String file : files) {
      Path path = Paths.get(file);
      assertTrue(Files.exists(path));
      assertTrue(Files.isRegularFile(path));
      assertFalse(Files.isDirectory(path));
      byte[] content = Files.readAllBytes(path);
      assertNotNull(content);
      assertTrue(content.length > 0);
    }
  }

  @Test
  public void find() throws IOException {
    NVD nvd = new NVD();
    nvd.download();

    if (nvd.downloadFailed()) {
      return;
    }

    List<NVDEntry> entries = nvd.find("apache", "poi");
    assertNotNull(entries);

    assertTrue(entries.size() >= 7);

    entries = nvd.find("not_existing", "poi");
    assertNotNull(entries);
    assertEquals(0, entries.size());

    entries = nvd.find("apache", "not_existing");
    assertNotNull(entries);
    assertEquals(0, entries.size());
  }
}
