package com.sap.oss.phosphor.fosstars.model.qa;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class TestVectorResultTest {

  public enum TestLabel implements Label {
    BAD, GOOD
  }

  private static final Interval ALMOST_MIN
      = DoubleInterval.init().from(Score.MIN).to(0.001).closed().make();

  @Test
  public void smoke() {
    StandardTestVector vector = newTestVector()
        .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
        .expectedScore(ALMOST_MIN)
        .expectedLabel(TestLabel.BAD)
        .alias("bad")
        .make();
    TestVectorResult testVectorResult = new TestVectorResult(
        vector, 0, new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(0.5),
        Status.FAILED, "Alles kaputt!");
    assertEquals(0, testVectorResult.index);
    assertEquals(Status.FAILED, testVectorResult.status);
    assertEquals("Alles kaputt!", testVectorResult.message);
    assertEquals(ALMOST_MIN, testVectorResult.vector.expectedScore());
    assertEquals(TestLabel.BAD, testVectorResult.vector.expectedLabel());
  }

  @Test
  public void equalsAndHashCode() {
    StandardTestVector testVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.BAD)
            .alias("1")
            .make();
    StandardTestVector sameTestVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.BAD)
            .alias("1")
            .make();
    StandardTestVector differentTestVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.GOOD)
            .alias("2")
            .make();

    assertEquals(testVector, sameTestVector);
    assertEquals(testVector.hashCode(), sameTestVector.hashCode());
    assertNotEquals(testVector, differentTestVector);
    assertNotEquals(testVector.hashCode(), differentTestVector.hashCode());
    assertNotEquals(sameTestVector, differentTestVector);
    assertNotEquals(sameTestVector.hashCode(), differentTestVector.hashCode());

    assertEquals(
        new TestVectorResult(testVector, 0,
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(4.0),
            Status.PASSED,"Alles gut!"),
        new TestVectorResult(testVector, 0,
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(4.0),
            Status.PASSED,"Alles gut!"));
    assertEquals(
        new TestVectorResult(testVector, 0,
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(4.0),
            Status.PASSED,"Alles gut!"),
        new TestVectorResult(sameTestVector, 0,
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(4.0),
            Status.PASSED,"Alles gut!"));
  }

}