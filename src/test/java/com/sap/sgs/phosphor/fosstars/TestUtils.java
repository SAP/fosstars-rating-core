package com.sap.sgs.phosphor.fosstars;

import static org.junit.Assert.assertEquals;

import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;

public class TestUtils {

  public static void assertScore(double expectedScore, ScoreValue scoreValue, double delta) {
    assertEquals(expectedScore, scoreValue.get(), delta);
  }

  public static void assertScore(double expectedScore, ScoreValue scoreValue) {
    assertEquals(expectedScore, scoreValue.get(), 0.0);
  }
}
