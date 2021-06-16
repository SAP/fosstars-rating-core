package com.sap.oss.phosphor.fosstars.tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubOrganization;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.Test;

public class SubjectCacheTest {

  @Test
  public void testStoreAndLoad() throws IOException {
    SubjectCache cache = SubjectCache.empty();

    GitHubProject project = new GitHubProject(new GitHubOrganization("netty"), "netty");
    RatingValue ratingValue = new RatingValue(
        new ScoreValue(ExampleScores.SECURITY_SCORE_EXAMPLE),
        SecurityLabelExample.OKAY);
    project.set(ratingValue);

    cache.add(project);
    Path filename = Files.createTempFile(SubjectCacheTest.class.getName(), "test");
    try {
      cache.store(filename);

      SubjectCache clone = SubjectCache.load(filename);
      Optional<RatingValue> something = clone.cachedRatingValueFor(project);
      assertTrue(something.isPresent());
      assertEquals(ratingValue, something.get());
    } finally {
      Files.delete(filename);
    }
  }

  @Test
  public void add() {
    SubjectCache cache = SubjectCache.empty();
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