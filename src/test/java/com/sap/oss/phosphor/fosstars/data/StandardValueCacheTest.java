package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import org.junit.Test;

public class StandardValueCacheTest {

  @Test
  public void testStoreAndLoad() throws IOException {
    Path tmp = Files.createTempFile(StandardValueCacheTest.class.getCanonicalName(), "test");
    String filename = tmp.toString();
    StandardValueCache cache = new StandardValueCache();
    try {
      String url = "https://github.com/unknown/project";
      cache.put(url, new BooleanValue(OssFeatures.HAS_SECURITY_TEAM, true));
      cache.put(url, new IntegerValue(OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS, 42));

      url = "https://github.com/fosstars/model";
      cache.put(url, new BooleanValue(OssFeatures.HAS_SECURITY_TEAM, false));
      cache.put(url, new IntegerValue(OssFeatures.NUMBER_OF_GITHUB_STARS, 10));

      cache.store(filename);

      byte[] content = Files.readAllBytes(Paths.get(filename));
      assertNotNull(content);
      assertTrue(content.length > 0);

      StandardValueCache anotherCache = StandardValueCache.load(filename);
      assertEquals(cache, cache);
      assertTrue(cache.equals(anotherCache) && anotherCache.equals(cache));
      assertEquals(cache.hashCode(), anotherCache.hashCode());
    } finally {
      Files.delete(tmp);
    }
  }

  @Test
  public void testPutAndGet() {
    StandardValueCache cache = new StandardValueCache();
    assertEquals(0, cache.size());
    Optional<ValueSet> someValueSet = cache.get("test");
    assertFalse(someValueSet.isPresent());

    testPutAndGet(cache, "first", NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(42));
    assertEquals(1, cache.size());

    testPutAndGet(cache, "first", NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(24));
    assertEquals(1, cache.size());

    testPutAndGet(cache, "second", NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(24));
    assertEquals(2, cache.size());

    testPutAndGet(cache, "second", NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(7));
    assertEquals(2, cache.size());

    testPutAndGet(cache, "second", NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0));
    assertEquals(2, cache.size());
  }

  private static <T> void testPutAndGet(StandardValueCache cache, String key, Value<T> value) {
    cache.put(key, value);

    Optional<ValueSet> someValueSet = cache.get(key);
    assertTrue(someValueSet.isPresent());
    ValueSet values = someValueSet.get();
    assertTrue(values.has(value.feature()));
    assertTrue(values.of(value.feature()).isPresent());
    assertEquals(value, values.of(value.feature()).get());

    Optional<Value<T>> someValue = cache.get(key, value.feature());
    assertTrue(someValue.isPresent());
    assertEquals(value, someValue.get());
  }

  @Test
  public void testExpiration() throws InterruptedException {
    StandardValueCache cache = new StandardValueCache();

    Value<Integer> value = NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0);
    Date inTwoSecond = new Date(System.currentTimeMillis() + 2 * 1000);
    cache.put("test", value, inTwoSecond);
    Optional<Value<Integer>> something
        = cache.get("test", NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
    assertTrue(something.isPresent());
    Thread.sleep(5000); // sleep for 5 seconds
    something = cache.get("test", NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
    assertFalse(something.isPresent());
  }
}
