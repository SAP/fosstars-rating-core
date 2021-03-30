package com.sap.oss.phosphor.fosstars.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class CompositeDataProviderTest {

  private static class DataProviderImpl implements DataProvider<Object> {

    private final String featureName;

    UserCallback callback;

    DataProviderImpl(String featureName) {
      this.featureName = featureName;
    }

    @Override
    public DataProvider<Object> update(Object object, ValueSet values) {
      values.update(new BooleanValue(new BooleanFeature(featureName), true));
      return this;
    }

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    public ValueCache<Object> cache() {
      return null;
    }

    @Override
    public DataProvider<Object> set(UserCallback callback) {
      this.callback = callback;
      return this;
    }

    @Override
    public DataProvider<Object> set(ValueCache<Object> cache) {
      return this;
    }

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return Collections.singleton(new BooleanFeature(featureName));
    }

    @Override
    public DataProvider<Object> configure(Path config) {
      return this;
    }
  }

  @Test
  public void testThatAllProvidersCalled() throws IOException {
    CompositeDataProvider<Object> compositeDataProvider = new CompositeDataProvider<>(
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
    for (Value<?> value : values) {
      BooleanValue booleanValue = (BooleanValue) value;
      assertFalse(booleanValue.isUnknown());
      assertTrue(booleanValue.feature().name().startsWith("feature "));
      assertTrue(booleanValue.get());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInteractiveProviders() {
    new CompositeDataProvider<>(
        new DataProvider<Object>() {

          @Override
          public DataProvider<Object> update(Object object, ValueSet values) {
            throw new UnsupportedOperationException("This should not be called!");
          }

          @Override
          public boolean interactive() {
            return true;
          }

          @Override
          public ValueCache<Object> cache() {
            throw new UnsupportedOperationException("This should not be called!");
          }

          @Override
          public DataProvider<Object> set(UserCallback callback) {
            throw new UnsupportedOperationException("This should not be called!");
          }

          @Override
          public DataProvider<Object> set(ValueCache<Object> cache) {
            throw new UnsupportedOperationException("This should not be called!");
          }

          @Override
          public Set<Feature<?>> supportedFeatures() {
            throw new UnsupportedOperationException("This should not be called!");
          }

          @Override
          public DataProvider<Object> configure(Path config) {
            throw new UnsupportedOperationException("This should not be called!");
          }
        }
    );
  }

}