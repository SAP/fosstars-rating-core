package com.sap.sgs.phosphor.fosstars.model.rating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class NotApplicableLabelTest {

  @Test
  public void name() {
    assertFalse(new NotApplicableLabel().name().isEmpty());
  }

  @Test
  public void isNotApplicable() {
    assertTrue(new NotApplicableLabel().isNotApplicable());
  }

  @Test
  public void equalsAndHashCode() {
    NotApplicableLabel one = new NotApplicableLabel();
    NotApplicableLabel two = new NotApplicableLabel();
    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    NotApplicableLabel label = new NotApplicableLabel();
    NotApplicableLabel clone = mapper.readValue(
        mapper.writeValueAsBytes(label), NotApplicableLabel.class);
    assertEquals(label, clone);
  }

}