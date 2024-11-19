package com.sap.oss.phosphor.fosstars.data.json;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SecurityTeamStorageTest {

  @Test
  public void testSpringSecurityOAuth() throws IOException {
    SecurityTeamStorage storage = SecurityTeamStorage.load();
    assertTrue(storage.existsFor("https://github.com/spring-projects/spring-security-oauth"));
  }

  @Test
  public void testApachePoi() throws IOException {
    SecurityTeamStorage storage = SecurityTeamStorage.load();
    assertTrue(storage.existsFor("https://github.com/apache/poi"));
  }

  @Test
  public void testUnknown() throws IOException {
    SecurityTeamStorage storage = SecurityTeamStorage.load();
    assertFalse(storage.existsFor("https://github.com/unknown/project"));
  }
}
