package com.sap.sgs.phosphor.fosstars.data;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class AbstractCachingDataProviderTest {

  @Test
  public void testProviderWithSingleFeature() throws IOException {
    CachingDataProviderForSingleFeature provider = new CachingDataProviderForSingleFeature();
    provider.set(new StandardValueCache());

    assertFalse(provider.interactive());
    assertEquals(1, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));

    for (int i = 0; i < 10; i++) {
      ValueSet values = new ValueHashSet();
      provider.update("test", values);
      assertEquals(1, values.size());
      assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
      assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
      assertEquals(42, values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());

      // make sure that the cache is used
      assertEquals(1, provider.counter);
    }
  }

  @Test
  public void testProviderWithMultipleFeatures() throws IOException {
    CachingDataProviderForMultipleFeatures provider = new CachingDataProviderForMultipleFeatures();
    provider.set(new StandardValueCache());

    assertFalse(provider.interactive());
    assertEquals(2, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(provider.supportedFeatures().contains(SECURITY_REVIEW_DONE_EXAMPLE));

    ValueSet values = new ValueHashSet();
    provider.update("test", values);
    assertEquals(2, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
    assertEquals(42, values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    assertTrue(values.has(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).isPresent());
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().isUnknown());

    // since the provider was called for the first time, it should have tried to fetch data
    assertEquals(1, provider.counter);

    provider.update("test", values);
    assertEquals(2, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
    assertEquals(42, values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    assertTrue(values.has(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).isPresent());
    assertFalse(values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().isUnknown());
    assertEquals(Boolean.TRUE, values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().get());

    // since one of the values was unknown after the first call,
    // the provider should have attempted to fetch values one more time
    assertEquals(2, provider.counter);

    provider.update("test", values);

    // since all the values were known, only cached values should have been used
    // therefore, the provider should not have attempted to fetch data
    assertEquals(2, provider.counter);
  }

  private static class CachingDataProviderForSingleFeature
      extends AbstractCachingDataProvider<String> {

    int counter = 0;

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    protected Set<Feature> supportedFeatures() {
      return Collections.singleton(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    }

    @Override
    protected Set<Value> fetchValuesFor(String object) {
      counter++;
      return Collections.singleton(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(42));
    }
  }

  private static class CachingDataProviderForMultipleFeatures
      extends AbstractCachingDataProvider<String> {

    int counter = 0;

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    protected Set<Feature> supportedFeatures() {
      return setOf(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, SECURITY_REVIEW_DONE_EXAMPLE);
    }

    @Override
    protected Set<Value> fetchValuesFor(String object) {
      Set<Value> values = new HashSet<>();
      values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(42));

      if (counter == 0) {
        values.add(SECURITY_REVIEW_DONE_EXAMPLE.unknown());
      } else {
        values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(true));
      }

      counter++;
      return values;
    }
  }
}