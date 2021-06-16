package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.junit.AssumptionViolatedException;
import org.junit.Test;

public class AbstractCachingDataProviderTest {

  @Test
  public void testProviderWithSingleFeature() throws IOException {
    CachingDataProviderForSingleFeature provider = new CachingDataProviderForSingleFeature();
    SubjectValueCache cache = new SubjectValueCache();
    provider.set(cache);

    assertFalse(provider.interactive());
    assertEquals(1, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));

    GitHubProject project = new GitHubProject("org", "test");
    for (int i = 0; i < 10; i++) {
      ValueSet values = new ValueHashSet();
      provider.update(project, values);
      assertEquals(1, values.size());
      assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
      assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
      assertEquals(42, (int) values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());

      // make sure that the cache is used
      assertEquals(1, provider.counter);

      ValueSet cachedValues = cache.get(project)
          .orElseThrow(() -> new AssumptionViolatedException("No cached value!"));
      assertEquals(1, cachedValues.size());
      assertTrue(cachedValues.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
      assertTrue(cachedValues.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
      assertEquals(42, (int) cachedValues.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    }
  }

  @Test
  public void testProviderWithMultipleFeatures() throws IOException {
    CachingDataProviderForMultipleFeatures provider = new CachingDataProviderForMultipleFeatures();
    SubjectValueCache cache = new SubjectValueCache();
    provider.set(cache);

    assertFalse(provider.interactive());
    assertEquals(2, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(provider.supportedFeatures().contains(SECURITY_REVIEW_DONE_EXAMPLE));

    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);
    assertEquals(2, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
    assertEquals(42, (int) values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    assertTrue(values.has(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).isPresent());
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().isUnknown());

    ValueSet cachedValues = cache.get(project)
        .orElseThrow(() -> new AssumptionViolatedException("No cached value!"));
    assertEquals(2, cachedValues.size());
    assertTrue(cachedValues.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(cachedValues.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
    assertEquals(42, (int) cachedValues.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    assertTrue(cachedValues.has(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(cachedValues.of(SECURITY_REVIEW_DONE_EXAMPLE).isPresent());
    assertTrue(cachedValues.of(SECURITY_REVIEW_DONE_EXAMPLE).get().isUnknown());

    // since the provider was called for the first time, it should have tried to fetch data
    assertEquals(1, provider.counter);

    provider.update(project, values);
    assertEquals(2, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE));
    assertTrue(values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isPresent());
    assertEquals(42, (int) values.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get().get());
    assertTrue(values.has(SECURITY_REVIEW_DONE_EXAMPLE));
    assertTrue(values.of(SECURITY_REVIEW_DONE_EXAMPLE).isPresent());
    assertFalse(values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().isUnknown());
    assertEquals(Boolean.TRUE, values.of(SECURITY_REVIEW_DONE_EXAMPLE).get().get());

    // since one of the values was unknown after the first call,
    // the provider should have attempted to fetch values one more time
    assertEquals(2, provider.counter);

    provider.update(project, values);

    // since all the values were known, only cached values should have been used
    // therefore, the provider should not have attempted to fetch data
    assertEquals(2, provider.counter);
  }

  private static class CachingDataProviderForSingleFeature extends AbstractCachingDataProvider {

    int counter = 0;

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return Collections.singleton(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    }

    @Override
    public boolean supports(Subject subject) {
      return subject instanceof GitHubProject;
    }

    @Override
    protected ValueSet fetchValuesFor(Subject object) {
      counter++;
      return ValueHashSet.from(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(42));
    }
  }

  private static class CachingDataProviderForMultipleFeatures extends AbstractCachingDataProvider {

    int counter = 0;

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return setOf(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, SECURITY_REVIEW_DONE_EXAMPLE);
    }

    @Override
    public boolean supports(Subject subject) {
      return subject instanceof GitHubProject;
    }

    @Override
    protected ValueSet fetchValuesFor(Subject object) {
      ValueSet values = new ValueHashSet();
      values.update(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(42));

      if (counter == 0) {
        values.update(SECURITY_REVIEW_DONE_EXAMPLE.unknown());
      } else {
        values.update(SECURITY_REVIEW_DONE_EXAMPLE.value(true));
      }

      counter++;
      return values;
    }
  }
}