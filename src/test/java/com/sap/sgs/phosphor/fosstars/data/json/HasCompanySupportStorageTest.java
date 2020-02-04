package com.sap.sgs.phosphor.fosstars.data.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;

public class HasCompanySupportStorageTest {

  @Test
  public void testSpringSecurityOauth() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertTrue(storage.supported("https://github.com/spring-projects/spring-security-oauth"));
  }

  @Test
  public void testUnknownProject() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertFalse(storage.supported("https://github.com/black/horse"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHttpUrl() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertTrue(storage.supported("http://github.com/spring-projects/spring-security-oauth"));
  }

}