package com.sap.sgs.phosphor.fosstars.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.other.ImmutabilityChecker;
import com.sap.sgs.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class RatingRepositoryTest {

  @Test
  public void testOssSecurityRatingIsImmutable() {
    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    assertNotNull(rating);
    ImmutabilityChecker checker = new ImmutabilityChecker();
    rating.accept(checker);
    assertTrue(checker.allImmutable());
  }

  @Test
  public void testGetByClass() {
    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    assertThat(rating.name(), is("Security rating (example)"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getByVersionAndClassWrongClass() {
    RatingRepository.INSTANCE.rating(TestRating.class);
  }

  /**
   * Class used for tests.
   */
  private static class TestRating extends AbstractRating {

    public TestRating(Score score) {
      super("test rating", score);
    }

    @Override
    public Label label(ScoreValue scoreValue) {
      return null;
    }
  }
}