package com.sap.oss.phosphor.fosstars.advice;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.junit.Test;

public class SimpleAdviceTest {

  @Test
  public void testEqualsAndHashCode() {
    SimpleAdvice advice = new SimpleAdvice(
        new GitHubProject("org", "test"),
        SECURITY_REVIEW_DONE_EXAMPLE.value(false),
        new AdviceContent(
            SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.", Collections.emptyList()));
    SimpleAdvice theSameAdvice = new SimpleAdvice(
        new GitHubProject("org", "test"),
        SECURITY_REVIEW_DONE_EXAMPLE.value(false),
        new AdviceContent(
            SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.", Collections.emptyList()));
    assertTrue(advice.equals(theSameAdvice) && theSameAdvice.equals(advice));
    assertEquals(advice.hashCode(), theSameAdvice.hashCode());

    assertNotEquals(
        advice,
        new SimpleAdvice(
            new GitHubProject("org", "another"),
            SECURITY_REVIEW_DONE_EXAMPLE.value(false),
            new AdviceContent(
                SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.", Collections.emptyList())));

    assertNotEquals(
        advice,
        new SimpleAdvice(
            new GitHubProject("org", "test"),
            SECURITY_REVIEW_DONE_EXAMPLE.value(true),
            new AdviceContent(
                SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.", Collections.emptyList())));

    assertNotEquals(
        advice,
        new SimpleAdvice(
            new GitHubProject("org", "another"),
            SECURITY_REVIEW_DONE_EXAMPLE.value(false),
            new AdviceContent(
                SECURITY_REVIEW_DONE_EXAMPLE, "Another advice.", Collections.emptyList())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithDifferentFeatures() {
    new SimpleAdvice(
        new GitHubProject("org", "test"),
        SECURITY_REVIEW_DONE_EXAMPLE.value(false),
        new AdviceContent(
            STATIC_CODE_ANALYSIS_DONE_EXAMPLE, "This is an advice.", Collections.emptyList()));
  }

  @Test
  public void testJsonSerialization() throws IOException {
    SimpleAdvice advice = new SimpleAdvice(
        new GitHubProject("org", "test"),
        SECURITY_REVIEW_DONE_EXAMPLE.value(false),
        new AdviceContent(
            SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.",
            asList(
                new Link("First link", new URL("https://test.con/1")),
                new Link("Second link", new URL("https://test.con/2"))
        )));
    SimpleAdvice clone = Json.read(Json.toBytes(advice), SimpleAdvice.class);
    assertEquals(advice, clone);
  }

  @Test
  public void testYamlSerialization() throws IOException {
    SimpleAdvice advice = new SimpleAdvice(
        new GitHubProject("org", "test"),
        SECURITY_REVIEW_DONE_EXAMPLE.value(false),
        new AdviceContent(
            SECURITY_REVIEW_DONE_EXAMPLE, "This is an advice.",
            asList(
                new Link("First link", new URL("https://test.con/1")),
                new Link("Second link", new URL("https://test.con/2"))
            )));
    SimpleAdvice clone = Yaml.read(Yaml.toBytes(advice), SimpleAdvice.class);
    assertEquals(advice, clone);
  }

}