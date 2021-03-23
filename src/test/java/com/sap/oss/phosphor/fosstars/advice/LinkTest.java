package com.sap.oss.phosphor.fosstars.advice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;

public class LinkTest {

  @Test
  public void testJsonSerialization() throws IOException {
    Link link = new Link("test", new URL("https://test/path"));
    Link clone = Json.read(Json.toBytes(link), Link.class);
    assertEquals(link, clone);
  }

  @Test
  public void testYamlSerialization() throws IOException {
    Link link = new Link("test", new URL("https://test/path"));
    byte[] bytes = Yaml.mapper().writeValueAsBytes(link);
    assertTrue(bytes.length > 0);
    Link clone = Yaml.mapper().readValue(bytes, Link.class);
    assertEquals(link, clone);
  }

  @Test
  public void testEqualsAndHashCode() throws MalformedURLException {
    Link firstLink = new Link("first", new URL("https://test/first"));
    Link theSameLink = new Link("first", new URL("https://test/first"));
    assertTrue(firstLink.equals(theSameLink) && theSameLink.equals(firstLink));
    assertEquals(firstLink.hashCode(), theSameLink.hashCode());

    Link secondLink = new Link("second", new URL("https://test/first"));
    assertNotEquals(firstLink, secondLink);

    Link thirdLink = new Link("first", new URL("https://another/first"));
    assertNotEquals(firstLink, thirdLink);
  }
}