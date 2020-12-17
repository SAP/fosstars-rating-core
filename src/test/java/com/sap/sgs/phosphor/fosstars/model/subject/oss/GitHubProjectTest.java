package com.sap.sgs.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import org.junit.Test;

public class GitHubProjectTest {

  @Test
  public void testSerialization() throws IOException {
    GitHubOrganization apache = new GitHubOrganization("apache");
    GitHubProject project = new GitHubProject(apache, "nifi");
    project.set(
        new RatingValue(
            new ScoreValue(ExampleScores.SECURITY_SCORE_EXAMPLE),
            SecurityLabelExample.OKAY));
    ObjectMapper mapper = new ObjectMapper();
    byte[] bytes = mapper.writeValueAsBytes(project);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    GitHubProject clone = mapper.readValue(bytes, GitHubProject.class);
    assertNotNull(clone);
    assertEquals(project, clone);
    assertEquals(project.hashCode(), clone.hashCode());
  }

  @Test
  public void testParsingFromUrl() throws IOException {
    GitHubProject parsed = GitHubProject.parse("http://github.com/SAP/fosstars-rating-core");
    assertEquals(parsed.name(), "fosstars-rating-core");
    assertEquals(parsed.organization().name(), "SAP");
    assertEquals(parsed.path(), "SAP/fosstars-rating-core");
  }

  @Test
  public void testParsingFromUrlEndsWithDotGit() throws IOException {
    GitHubProject parsed = GitHubProject.parse("http://github.com/SAP/fosstars-rating-core.git");
    assertEquals(parsed.name(), "fosstars-rating-core");
    assertEquals(parsed.organization().name(), "SAP");
    assertEquals(parsed.path(), "SAP/fosstars-rating-core");
  }

  @Test
  public void testEqualsAndHashCode() throws IOException {
    GitHubProject firstProject = GitHubProject.parse("https://github.com/test/first");
    GitHubProject theSameProject = GitHubProject.parse("https://github.com/test/first");
    assertTrue(firstProject.equals(theSameProject) && theSameProject.equals(firstProject));
    assertEquals(firstProject.hashCode(), theSameProject.hashCode());

    GitHubProject secondProject = new GitHubProject(new GitHubOrganization("test"), "second");
    assertNotEquals(firstProject, secondProject);

    GitHubProject thirdProject = new GitHubProject(new GitHubOrganization("another"), "first");
    assertNotEquals(firstProject, secondProject);
  }
}
