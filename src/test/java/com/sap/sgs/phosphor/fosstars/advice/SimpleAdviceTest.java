package com.sap.sgs.phosphor.fosstars.advice;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
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

}