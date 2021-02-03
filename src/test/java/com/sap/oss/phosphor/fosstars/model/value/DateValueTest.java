package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.DateFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class DateValueTest {

  @Test
  public void testGet() {
    Value<Date> one = new DateValue(OssFeatures.PROJECT_START_DATE, new Date(1));
    assertFalse(one.isUnknown());
    assertNotNull(one.get());
    assertEquals(new Date(1), one.get());
    assertEquals(OssFeatures.PROJECT_START_DATE, one.feature());
  }

  @Test
  public void testEquals() {
    final Value<Date> one = new DateValue(OssFeatures.PROJECT_START_DATE, new Date(1));
    final Value<Date> two = new DateValue(OssFeatures.PROJECT_START_DATE, new Date(1));
    final Value<Date> three = new DateValue(OssFeatures.PROJECT_START_DATE, new Date(2));
    final Value<Date> four = new DateValue(OssFeatures.FIRST_COMMIT_DATE, new Date(1));

    assertEquals(one, one);
    assertEquals(one.hashCode(), one.hashCode());

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    assertNotEquals(one, three);
    assertNotEquals(one.hashCode(), three.hashCode());

    assertNotEquals(one, four);
    assertNotEquals(one.hashCode(), four.hashCode());

    assertNotEquals(three, four);
    assertNotEquals(three.hashCode(), four.hashCode());
  }

  @Test
  public void testSerialization() throws IOException {
    DateValue dateValue = new DateValue(new DateFeature("test"), new Date());
    byte[] bytes = Json.toBytes(dateValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    DateValue clone = Json.read(bytes, DateValue.class);
    assertNotNull(clone);
    assertEquals(dateValue, clone);
    assertEquals(dateValue.hashCode(), clone.hashCode());
  }

}