package com.sap.oss.phosphor.fosstars.model.rating.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.other.ImmutabilityChecker;
import com.sap.oss.phosphor.fosstars.model.other.MakeImmutable;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SecurityRatingExampleTest {

  @Test
  public void testSerializeAndDeserialize() throws IOException  {
    SecurityRatingExample rating = new SecurityRatingExample();
    SecurityRatingExample clone = Json.read(Json.toBytes(rating), SecurityRatingExample.class);
    assertEquals(rating, clone);
  }

  @Test
  public void testCalculate() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 7));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 2));
    values.add(new BooleanValue(SECURITY_REVIEW_DONE_EXAMPLE, true));
    values.add(new BooleanValue(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false));

    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertEquals(SecurityRatingExample.SecurityLabelExample.OKAY, ratingValue.label());
  }

  @Test
  public void testEqualsAndHashCode() {
    SecurityRatingExample one = new SecurityRatingExample();
    SecurityRatingExample two = new SecurityRatingExample();

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testAllFeatures() {
    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    Set<Feature<?>> features = rating.allFeatures();
    assertNotNull(features);
    assertEquals(4, features.size());
    assertTrue(features.contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(features.contains(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE));
    assertTrue(features.contains(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(features.contains(STATIC_CODE_ANALYSIS_DONE_EXAMPLE));
  }

  @Test
  public void testCount() {
    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    assertNotNull(rating);
    Counter counter = new Counter();
    rating.accept(counter);
    assertEquals(1, counter.ratings);
    assertEquals(2, counter.weights);
    assertEquals(3, counter.scores);
    assertEquals(4, counter.features);
  }

  @Test
  public void testMakeImmutableWithVisitor() {
    SecurityRatingExample r = new SecurityRatingExample();

    // first, check that the underlying score is mutable
    assertFalse(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertFalse(parameter.isImmutable());
    }
    ImmutabilityChecker checker = new ImmutabilityChecker();
    r.accept(checker);
    assertFalse(checker.allImmutable());

    // next, make it immutable
    r.accept(new MakeImmutable());

    // then, check that the underlying score became immutable
    assertTrue(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertTrue(parameter.isImmutable());
    }
    checker = new ImmutabilityChecker();
    r.accept(checker);
    assertTrue(checker.allImmutable());
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