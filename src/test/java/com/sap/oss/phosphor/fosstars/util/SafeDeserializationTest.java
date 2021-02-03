package com.sap.oss.phosphor.fosstars.util;

import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.sap.oss.phosphor.test.AnotherData;
import com.sap.oss.phosphor.test.Entity;
import java.io.IOException;
import org.junit.Test;

public class SafeDeserializationTest {

  @Test
  public void testWithProhibitedClass() throws IOException {
    Entity entity = new Entity(new AnotherData());

    try {
      Json.mapper().readValue(Json.toBytes(entity), Entity.class);
      fail("Deserialization should fail");
    } catch (InvalidTypeIdException e) {
      // ok
    }

    try {
      Yaml.mapper().readValue(Json.toBytes(entity), Entity.class);
      fail("Deserialization should fail");
    } catch (InvalidTypeIdException e) {
      // ok
    }

    Deserialization.allow(AnotherData.class.getCanonicalName());
    Json.mapper().readValue(Json.toBytes(entity), Entity.class);
    Yaml.mapper().readValue(Json.toBytes(entity), Entity.class);
  }

}