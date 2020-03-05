package com.sap.sgs.phosphor.fosstars.model.rating.example;

import static com.sap.sgs.phosphor.fosstars.model.Version.SECURITY_RATING_EXAMPLE_1_1;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Parameter;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SecurityRatingExampleTest {

  @Test
  public void calculate() {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 7));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 2));
    values.add(new BooleanValue(SECURITY_REVIEW_DONE_EXAMPLE, true));
    values.add(new BooleanValue(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false));

    Rating rating = RatingRepository.INSTANCE.rating(SECURITY_RATING_EXAMPLE_1_1);
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertEquals(SecurityRatingExample.SecurityLabelExample.OKAY, ratingValue.label());
  }

  @Test
  public void version() {
    Rating rating = RatingRepository.INSTANCE.rating(SECURITY_RATING_EXAMPLE_1_1);
    assertEquals(SECURITY_RATING_EXAMPLE_1_1, rating.version());
  }

  @Test
  public void equalsAndHashCode() {
    SecurityRatingExample one = new SecurityRatingExample(SECURITY_RATING_EXAMPLE_1_1);
    SecurityRatingExample two = new SecurityRatingExample(SECURITY_RATING_EXAMPLE_1_1);

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void allFeatures() {
    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    Set<Feature> features = rating.allFeatures();
    assertNotNull(features);
    assertEquals(4, features.size());
    assertTrue(features.contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(features.contains(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE));
    assertTrue(features.contains(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(features.contains(STATIC_CODE_ANALYSIS_DONE_EXAMPLE));
  }

  @Test
  public void count() {
    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    assertNotNull(rating);
    Counter counter = new Counter();
    rating.accept(counter);
    assertEquals(1, counter.ratings);
    assertEquals(2, counter.weights);
    assertEquals(3, counter.scores);
    assertEquals(4, counter.features);
  }

  private static class Counter implements Visitor {

    int features = 0;
    int scores = 0;
    int weights = 0;
    int ratings = 0;

    @Override
    public void visit(Rating rating) {
      assertNotNull(rating);
      ratings++;
    }

    @Override
    public void visit(Score score) {
      assertNotNull(score);
      scores++;
    }

    @Override
    public void visit(Feature feature) {
      assertNotNull(feature);
      features++;
    }

    @Override
    public void visit(Parameter parameter) {
      assertNotNull(parameter);
      weights++;
    }
  }

}