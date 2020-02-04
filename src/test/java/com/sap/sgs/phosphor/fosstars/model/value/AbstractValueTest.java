package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.AbstractFeature;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class AbstractValueTest {

  private static class FeatureImpl extends AbstractFeature {

    FeatureImpl(String name) {
      super(name);
    }

    @Override
    public Value value(Object object) {
      return new ValueImpl(this, object);
    }

    @Override
    public Value parse(String string) {
      throw new UnsupportedOperationException();
    }
  }

  private static class ValueImpl extends AbstractValue {

    final Object value;

    ValueImpl(Feature feature, Object value) {
      super(feature);
      this.value = value;
    }

    @Override
    public Object get() {
      return value;
    }
  }

  @Test
  public void processIfKnown() {
    ValueImpl value = new ValueImpl(new FeatureImpl("feature"), "test");
    assertFalse(value.isUnknown());

    List processedValues = new ArrayList();

    value.processIfKnown(object -> {
      assertEquals("test", object);
      processedValues.add(object);
    });

    Value unknown = new FeatureImpl("feature").unknown();
    unknown.processIfKnown(object -> {
      fail("this should not be reached");
    });

    assertEquals(1, processedValues.size());
    assertEquals("test", processedValues.get(0));
  }
}