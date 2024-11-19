package com.sap.oss.phosphor.fosstars.model.subject;

import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.OKAY;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_SCORE_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class AbstractSubjectTest {

  @Test
  public void testSetAndGet() {
    TestSubject subject = new TestSubject();
    assertFalse(subject.ratingValue().isPresent());
    assertFalse(subject.ratingValueDate().isPresent());

    subject.set(new RatingValue(new ScoreValue(SECURITY_SCORE_EXAMPLE), OKAY));
    assertTrue(subject.ratingValueDate().isPresent());
    assertTrue(subject.ratingValueDate().get().getTime() <= System.currentTimeMillis());
    assertTrue(subject.ratingValue().isPresent());
    assertEquals(SECURITY_SCORE_EXAMPLE, subject.ratingValue().get().scoreValue().score());
    assertEquals(OKAY, subject.ratingValue().get().label());
  }

  @Test
  public void testEqualsAndHashCode() {
    TestSubject firstSubject = new TestSubject();
    TestSubject secondSubject = new TestSubject();
    assertTrue(firstSubject.equals(secondSubject) && secondSubject.equals(firstSubject));
    assertEquals(firstSubject.hashCode(), secondSubject.hashCode());

    firstSubject.set(new RatingValue(new ScoreValue(SECURITY_SCORE_EXAMPLE), OKAY));
    assertNotEquals(firstSubject, secondSubject);

    Date date = new Date();
    firstSubject =
        new TestSubject(new RatingValue(new ScoreValue(SECURITY_SCORE_EXAMPLE), OKAY), date);
    secondSubject =
        new TestSubject(new RatingValue(new ScoreValue(SECURITY_SCORE_EXAMPLE), OKAY), date);
    assertEquals(firstSubject, secondSubject);
    assertEquals(firstSubject.hashCode(), secondSubject.hashCode());
  }

  private static class TestSubject extends AbstractSubject {

    public TestSubject(RatingValue ratingValue, Date ratingValueDate) {
      super(ratingValue, ratingValueDate);
    }

    public TestSubject() {
    }

    @Override
    public String purl() {
      return "scheme:type/namespace/name@version?qualifiers#subpath";
    }
  }
}
