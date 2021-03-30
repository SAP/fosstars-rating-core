package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel.BAD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel.GOOD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel.MODERATE;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel.UNCLEAR;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.Thresholds;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssArtifactSecurityScore;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;
import org.junit.Test;

public class OssArtifactSecurityRatingTest {

  @Test
  public void testCalculate() {
    Rating rating = RatingRepository.INSTANCE.rating(OssArtifactSecurityRating.class);
    Set<Value<?>> values = Utils.allUnknown(rating.allFeatures());
    RatingValue ratingValue = rating.calculate(values);
    assertEquals(UNKNOWN, ratingValue.label());
  }

  @Test
  public void testEqualsAndHashCode() {
    OssArtifactSecurityRating one = new OssArtifactSecurityRating(new OssArtifactSecurityScore(),
        Thresholds.DEFAULT);
    OssArtifactSecurityRating two = new OssArtifactSecurityRating(new OssArtifactSecurityScore(),
        Thresholds.DEFAULT);

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testLabels() {
    OssArtifactSecurityScore score = mock(OssArtifactSecurityScore.class);
    OssArtifactSecurityRating rating = new OssArtifactSecurityRating(score,
        new Thresholds(1.0, 9.0, 7.0));
    assertEquals(BAD, rating.label(new ScoreValue(score).set(0.5).confidence(8.0)));
    assertEquals(MODERATE, rating.label(new ScoreValue(score).set(1.5).confidence(7.1)));
    assertEquals(GOOD, rating.label(new ScoreValue(score).set(9.5).confidence(9.0)));
    assertEquals(UNCLEAR, rating.label(new ScoreValue(score).set(4.5).confidence(6.0)));
  }
}