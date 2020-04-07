package com.sap.sgs.phosphor.fosstars.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import org.junit.Test;

public class CompositeDataProviderTest {

  private static class DataProviderImpl implements DataProvider {

    private final String featureName;

    UserCallback callback;

    DataProviderImpl(String featureName) {
      this.featureName = featureName;
    }

    @Override
    public DataProvider update(Object object, ValueSet values) {
      values.update(new BooleanValue(new BooleanFeature(featureName), true));
      return this;
    }

    @Override
    public ValueCache cache() {
      return null;
    }

    @Override
    public DataProvider set(UserCallback callback) {
      this.callback = callback;
      return this;
    }

    @Override
    public DataProvider set(ValueCache cache) {
      return null;
    }
  }

  @Test
  public void testAllProvidersCalled() throws IOException {
    CompositeDataProvider compositeDataProvider = new CompositeDataProvider(
        new DataProviderImpl("feature 1"),
        new DataProviderImpl("feature 2"),
        new DataProviderImpl("feature 3")
    );
    assertEquals(3, compositeDataProvider.providers().size());

    for (Object provider : compositeDataProvider.providers()) {
      DataProviderImpl providerImpl = (DataProviderImpl) provider;
      assertNull(providerImpl.callback);
    }
    compositeDataProvider.set(new Terminal());
    for (Object provider : compositeDataProvider.providers()) {
      DataProviderImpl providerImpl = (DataProviderImpl) provider;
      assertNotNull(providerImpl.callback);
    }

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());


    compositeDataProvider.update(new Object(), values);

    assertEquals(3, values.size());
    for (Value value : values.toArray()) {
      BooleanValue booleanValue = (BooleanValue) value;
      assertFalse(booleanValue.isUnknown());
      assertTrue(booleanValue.feature().name().startsWith("feature "));
      assertTrue(booleanValue.get());
    }
  }

}