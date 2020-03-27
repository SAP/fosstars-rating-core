package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Test;

public class GitHubProjectCacheTest {

  @Test
  public void load() throws IOException {
    final String filename = "TestProjectRatingCache.json";
    try (InputStream is = getClass().getResourceAsStream(filename)) {
      GitHubProjectCache cache = GitHubProjectCache.load(is);
      assertNotNull(cache);
      assertEquals(3, cache.size());
      cache.lifetime(100000);

      GitHubProject project = new GitHubProject(new GitHubOrganization("netty"), "netty");
      Optional<RatingValue> something = cache.cachedRatingValueFor(project);
      assertTrue(something.isPresent());

      project = new GitHubProject(new GitHubOrganization("netty"), "netty-tcnative");
      something = cache.cachedRatingValueFor(project);
      assertTrue(something.isPresent());

      project = new GitHubProject(new GitHubOrganization("FasterXML"), "jackson-databind");
      something = cache.cachedRatingValueFor(project);
      assertTrue(something.isPresent());

      project = new GitHubProject(new GitHubOrganization("not-existing"), "test");
      something = cache.cachedRatingValueFor(project);
      assertFalse(something.isPresent());
    }
  }

  @Test
  public void add() {
    GitHubProjectCache cache = GitHubProjectCache.empty();
    assertNotNull(cache);
    assertEquals(0, cache.size());

    GitHubProject project = new GitHubProject(new GitHubOrganization("netty"), "netty");
    RatingValue ratingValue = new RatingValue(
        new ScoreValue(ExampleScores.SECURITY_SCORE_EXAMPLE),
        SecurityLabelExample.OKAY);
    project.set(ratingValue);

    Optional<RatingValue> something = cache.cachedRatingValueFor(project);
    assertFalse(something.isPresent());

    cache.add(project);

    something = cache.cachedRatingValueFor(project);
    assertTrue(something.isPresent());
  }
}