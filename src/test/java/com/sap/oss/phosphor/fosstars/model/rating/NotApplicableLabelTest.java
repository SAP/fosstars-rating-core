package com.sap.oss.phosphor.fosstars.model.rating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class NotApplicableLabelTest {

  @Test
  public void testName() {
    assertFalse(new NotApplicableLabel().name().isEmpty());
  }

  @Test
  public void testIsNotApplicable() {
    assertTrue(new NotApplicableLabel().isNotApplicable());
  }

  @Test
  public void testEqualsAndHashCode() {
    NotApplicableLabel one = new NotApplicableLabel();
    NotApplicableLabel two = new NotApplicableLabel();
    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    NotApplicableLabel label = new NotApplicableLabel();
    NotApplicableLabel clone = Json.read(
        Json.toBytes(label), NotApplicableLabel.class);
    assertEquals(label, clone);
  }

}