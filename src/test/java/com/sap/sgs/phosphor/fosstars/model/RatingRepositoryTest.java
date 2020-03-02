package com.sap.sgs.phosphor.fosstars.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.sap.sgs.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import org.junit.Test;

public class RatingRepositoryTest {

  @Test
  public void getByVersionAndClass() {
    Rating rating = RatingRepository.INSTANCE.get(
        Version.SECURITY_RATING_EXAMPLE_1_1, SecurityRatingExample.class);

    assertThat(rating.name(), is("Security rating (example)"));
    assertThat(rating.version().name(), is("SECURITY_RATING_EXAMPLE_1_1"));
    assertEquals(rating.version().clazz, SecurityRatingExample.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getByVersionAndClassWrongClass() {
    RatingRepository.INSTANCE.get(Version.SECURITY_RATING_EXAMPLE_1_1, TestRating.class);
  }

  /**
   * Class used for tests.
   */
  private static class TestRating extends AbstractRating {

    public TestRating(Score score, Version version) {
      super("test rating", score, version);
    }

    @Override
    public Label label(double score) {
      return null;
    }
  }
}