package com.sap.sgs.phosphor.fosstars.model.qa;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import org.junit.Test;

public class FailedTestVectorTest {

  public enum TestLabel implements Label {
    BAD, GOOD
  }

  private static final Interval ALMOST_MIN
      = DoubleInterval.init().from(Score.MIN).to(0.001).closed().make();

  @Test
  public void smoke() {
    TestVector vector = newTestVector()
        .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
        .expectedScore(ALMOST_MIN)
        .expectedLabel(TestLabel.BAD)
        .make();
    FailedTestVector failedTestVector
        = new FailedTestVector(vector, 0, "Alles kaputt!");
    assertEquals(0, failedTestVector.index);
    assertEquals("Alles kaputt!", failedTestVector.reason);
    assertEquals(ALMOST_MIN, failedTestVector.vector.expectedScore());
    assertEquals(TestLabel.BAD, failedTestVector.vector.expectedLabel());
  }

  @Test
  public void equalsAndHashCode() {
    TestVector testVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.BAD)
            .make();
    TestVector sameTestVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.BAD)
            .make();
    TestVector differentTestVector =
        newTestVector()
            .set(allUnknown(OssFeatures.HAS_SECURITY_TEAM))
            .expectedScore(ALMOST_MIN)
            .expectedLabel(TestLabel.GOOD)
            .make();

    assertEquals(testVector, sameTestVector);
    assertEquals(testVector.hashCode(), sameTestVector.hashCode());
    assertNotEquals(testVector, differentTestVector);
    assertNotEquals(testVector.hashCode(), differentTestVector.hashCode());
    assertNotEquals(sameTestVector, differentTestVector);
    assertNotEquals(sameTestVector.hashCode(), differentTestVector.hashCode());

    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(testVector, 0, "Alles kaputt!"));
    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(sameTestVector, 0, "Alles kaputt!"));

    // index doesn't matter
    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(testVector, 1, "Alles kaputt!"));
    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(sameTestVector, 1, "Alles kaputt!"));

    // reason doesn't matter
    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(testVector, 0, "Alles gut!"));
    assertEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(sameTestVector, 0, "Alles gut!"));

    // only test vector matters
    assertNotEquals(
        new FailedTestVector(testVector, 0, "Alles kaputt!"),
        new FailedTestVector(differentTestVector, 0, "Alles kaputt!"));
    assertNotEquals(
        new FailedTestVector(sameTestVector, 0, "Alles kaputt!"),
        new FailedTestVector(differentTestVector, 0, "Alles kaputt!"));
  }

}