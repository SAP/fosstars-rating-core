package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import static com.sap.sgs.phosphor.fosstars.model.Version.OSS_SECURITY_RATING_1_0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.other.Utils;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.Set;
import org.junit.Test;

public class OssSecurityRatingTest {

  @Test
  public void calculate() {
    Rating rating = RatingRepository.INSTANCE.rating(OSS_SECURITY_RATING_1_0);
    Set<Value> values = Utils.allUnknown(rating.allFeatures());
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertEquals(SecurityLabel.BAD, ratingValue.label());
  }

  @Test
  public void version() {
    Rating rating = RatingRepository.INSTANCE.rating(OSS_SECURITY_RATING_1_0);
    assertEquals(OSS_SECURITY_RATING_1_0, rating.version());
  }

  @Test
  public void equalsAndHashCode() {
    OssSecurityRating one = new OssSecurityRating(new OssSecurityScore(), OSS_SECURITY_RATING_1_0);
    OssSecurityRating two = new OssSecurityRating(new OssSecurityScore(), OSS_SECURITY_RATING_1_0);

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }
}