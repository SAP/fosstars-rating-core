package com.sap.sgs.phosphor.fosstars.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.sap.sgs.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import java.io.IOException;
import org.junit.Test;

public class RatingRepositoryTest {

  @Test
  public void getByVersionAndClass() {
    Rating rating = RatingRepository.INSTANCE.get(
        Version.SECURITY_RATING_EXAMPLE_1_1, SecurityRatingExample.class);

    assertThat(rating.name(), is("Security rating (example)"));
    assertThat(rating.version().name(), is("SECURITY_RATING_EXAMPLE_1_1"));
    assertThat(rating.version().path, is(
        "com/sap/sgs/phosphor/fosstars/model/rating/example/SecurityRatingExample_1_1.json"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getByVersionAndClassWrongClass() {
    RatingRepository.INSTANCE.get(Version.SECURITY_RATING_EXAMPLE_1_1, TestRating.class);
  }

  @Test
  public void loadRatingFromResource() throws IOException {
    Rating r = RatingRepository.load(Version.OSS_SECURITY_RATING_1_0);
    assertThat(r.version().name(), is("OSS_SECURITY_RATING_1_0"));
  }

  /**
   * Class used for tests.
   */
  private static class TestRating extends AbstractRating {

    public TestRating(Score score, Version version) {
      super("test rating" ,score, version);
    }

    @Override
    public Label label(double score) {
      return null;
    }
  }
}