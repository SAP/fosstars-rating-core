package com.sap.oss.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import org.junit.Test;

public class LocalRepositoryInfoTest {

  @Test
  public void testSerialization() throws IOException {
    LocalRepositoryInfo info = new LocalRepositoryInfo(
        Paths.get("."), new Date(), new URL("https://scm/org/test"));
    LocalRepositoryInfo clone = Json.mapper().readValue(
        Json.toBytes(info), LocalRepositoryInfo.class);
    assertEquals(info.updated(), clone.updated());
    assertEquals(info.path().toAbsolutePath(), clone.path().toAbsolutePath());
    assertEquals(info.url(), clone.url());
  }

}