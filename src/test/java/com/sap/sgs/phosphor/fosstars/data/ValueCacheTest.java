package com.sap.sgs.phosphor.fosstars.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class ValueCacheTest {

  @Test
  public void store() throws IOException {
    Path tmp = Files.createTempFile(ValueCacheTest.class.getCanonicalName(), "test");
    String filename = tmp.toString();
    ValueCache cache = new ValueCache();
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

      ValueCache anotherCache = ValueCache.load(filename);
      assertEquals(cache, anotherCache);
    } finally {
      Files.delete(tmp);
    }
  }
}
