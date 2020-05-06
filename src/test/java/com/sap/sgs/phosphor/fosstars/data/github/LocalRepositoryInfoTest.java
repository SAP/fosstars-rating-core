package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import org.junit.Test;

public class LocalRepositoryInfoTest {

  @Test
  public void testSerialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    LocalRepositoryInfo info = new LocalRepositoryInfo(
        Paths.get("."), new Date(), new URL("https://scm/org/test"));
    LocalRepositoryInfo clone = mapper.readValue(
        mapper.writeValueAsBytes(info), LocalRepositoryInfo.class);
    assertEquals(info.updated(), clone.updated());
    assertEquals(info.path().toAbsolutePath(), clone.path().toAbsolutePath());
    assertEquals(info.url(), clone.url());
  }

}