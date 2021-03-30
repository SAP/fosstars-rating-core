package com.sap.oss.phosphor.fosstars.data.json;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.Test;

public class CompanySupportStorageTest {

  @Test
  public void testSpringSecurityOauth() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertTrue(storage.supports("https://github.com/spring-projects/spring-security-oauth"));
    assertTrue(storage.supports("https://github.com/spring-projects/spring-security-oauth/"));
  }

  @Test
  public void testGoogleLighthouse() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertTrue(storage.supports("https://github.com/GoogleChrome/lighthouse"));
    assertTrue(storage.supports("https://github.com/GoogleChrome/LightHouse"));
    assertTrue(storage.supports("https://github.com/googlechrome/lighthouse"));
    assertTrue(storage.supports(new URL("https://github.com/GoogleChrome/lighthouse")));
  }

  @Test
  public void testUnknownProject() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertFalse(storage.supports("https://github.com/black/horse"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHttpUrl() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    assertNotNull(storage);
    assertTrue(storage.supports("http://github.com/spring-projects/spring-security-oauth"));
  }

  @Test
  public void testCompanies() throws IOException {
    CompanySupportStorage storage = CompanySupportStorage.load();
    List<String> companies = storage.companies("https://github.com/openjdk");
    assertThat(companies, hasItem("Oracle"));
    assertThat(companies, hasItem("SAP"));
  }

}