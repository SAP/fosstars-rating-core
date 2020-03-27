package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
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
  public void serializeAndDeserialize() throws IOException {
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
}