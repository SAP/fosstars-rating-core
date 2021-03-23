package com.sap.oss.phosphor.fosstars.advice;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.junit.Test;

public class AdviceContentTest {

  @Test
  public void testJsonSerialization() throws IOException {
    AdviceContent adviceContent = new AdviceContent(
        SECURITY_REVIEW_DONE_EXAMPLE,
        "test",
        Arrays.asList(
            new Link("first", new URL("https://test/1")),
            new Link("second", new URL("https://test/info"))));

    AdviceContent clone = Json.read(Json.toBytes(adviceContent), AdviceContent.class);
    assertEquals(adviceContent, clone);
  }

  @Test
  public void testYamlSerialization() throws IOException {
    AdviceContent adviceContent = new AdviceContent(
        SECURITY_REVIEW_DONE_EXAMPLE,
        "test",
        Arrays.asList(
            new Link("first", new URL("https://test/1")),
            new Link("second", new URL("https://test/info"))));
    byte[] bytes = Yaml.mapper().writeValueAsBytes(adviceContent);
    assertTrue(bytes.length > 0);
    AdviceContent clone = Yaml.mapper().readValue(bytes, AdviceContent.class);
    assertEquals(adviceContent, clone);
  }

  @Test
  public void testEqualsAndHashCode() throws MalformedURLException {
    AdviceContent adviceContent = new AdviceContent(
        SECURITY_REVIEW_DONE_EXAMPLE,
        "test",
        Arrays.asList(
            new Link("first", new URL("https://test/1")),
            new Link("second", new URL("https://test/info"))));
    AdviceContent theSame = new AdviceContent(
        SECURITY_REVIEW_DONE_EXAMPLE,
        "test",
        Arrays.asList(
            new Link("first", new URL("https://test/1")),
            new Link("second", new URL("https://test/info"))));
    assertTrue(adviceContent.equals(theSame) && theSame.equals(adviceContent));
    assertEquals(adviceContent.hashCode(), theSame.hashCode());

    AdviceContent another = new AdviceContent(
        SECURITY_REVIEW_DONE_EXAMPLE,
        "another",
        Arrays.asList(
            new Link("first", new URL("https://test/1")),
            new Link("second", new URL("https://test/info"))));
    assertNotEquals(another, adviceContent);
  }
}