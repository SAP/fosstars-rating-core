package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.interactive.AbstractInteractiveDataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class SimpleCompositeDataProviderTest {

  private static final Feature<Integer> SOMETHING = new PositiveIntegerFeature("test feature");

  private static class TestNonInteractiveProvider extends AbstractDataProvider {

    @Override
    protected TestNonInteractiveProvider doUpdate(Subject subject, ValueSet values) {
      values.update(SOMETHING.value(1));
      return this;
    }

    @Override
    public boolean interactive() {
      return false;
    }

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return singleton(SOMETHING);
    }

    @Override
    public boolean supports(Subject subject) {
      return true;
    }
  }

  private static class TestInteractiveProvider extends AbstractInteractiveDataProvider {

    @Override
    public Set<Feature<?>> supportedFeatures() {
      return singleton(SOMETHING);
    }

    @Override
    public boolean supports(Subject subject) {
      return true;
    }

    @Override
    protected TestInteractiveProvider ask(Subject subject, ValueSet values) {
      values.update(SOMETHING.value(2));
      return this;
    }
  }

  private static class TestCallback implements UserCallback {

    @Override
    public boolean canTalk() {
      return true;
    }

    @Override
    public String ask() {
      return "test reply";
    }

    @Override
    public String ask(String question) {
      return "test reply";
    }

    @Override
    public void say(String phrase) {
      // do nothing
    }
  }

  @Test
  public void testSupportedFeatures() {
    SimpleCompositeDataProvider provider = SimpleCompositeDataProvider.forFeature(SOMETHING)
        .withDefaultValue(SOMETHING.value(42));
    assertEquals(1, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(SOMETHING));
  }

  @Test
  public void testUpdate() throws IOException {
    ValueSet values = new ValueHashSet();

    SimpleCompositeDataProvider provider = SimpleCompositeDataProvider.forFeature(SOMETHING)
        .withDefaultValue(SOMETHING.value(42));
    provider.update(PROJECT, values);
    assertValueIn(values, 42);

    provider = SimpleCompositeDataProvider.forFeature(SOMETHING)
        .withInteractiveProvider(new TestInteractiveProvider())
        .withDefaultValue(SOMETHING.value(42));
    provider.set(new TestCallback());
    provider.update(PROJECT, values);
    assertValueIn(values, 2);

    provider = SimpleCompositeDataProvider.forFeature(SOMETHING)
        .withInteractiveProvider(new TestInteractiveProvider())
        .withNonInteractiveProvider(new TestNonInteractiveProvider())
        .withDefaultValue(SOMETHING.value(42));
    provider.set(new TestCallback());
    provider.update(PROJECT, values);
    assertValueIn(values, 1);
  }

  private static void assertValueIn(ValueSet values, int expectedValue) {
    assertEquals(1, values.size());
    Optional<Value<Integer>> something = values.of(SOMETHING);
    assertTrue(something.isPresent());
    assertEquals(expectedValue, (int) something.get().get());
  }
}