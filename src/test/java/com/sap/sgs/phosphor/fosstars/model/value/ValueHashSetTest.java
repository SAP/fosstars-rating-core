package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COLLABORATORS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import org.junit.Test;

public class ValueHashSetTest {

  @Test
  public void empty() {
    assertEquals(0, ValueHashSet.empty().toArray().length);
  }

  @Test
  public void has() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    assertEquals(1, values.toArray().length);
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_THREE_MONTHS));
  }

  @Test
  public void update() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    assertEquals(1, values.toArray().length);
    assertEquals(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10), values.toArray()[0]);
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(20));
    assertEquals(1, values.toArray().length);
    assertEquals(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(20), values.toArray()[0]);
  }

  @Test
  public void toArray() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    values.update(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(5));
    Value[] array = values.toArray();
    assertNotNull(array);
    assertEquals(2, array.length);
  }

  @Test
  public void unknown() {
    ValueSet values = ValueHashSet.unknown(NUMBER_OF_WATCHERS_ON_GITHUB, NUMBER_OF_GITHUB_STARS,
        NUMBER_OF_COLLABORATORS);
    assertEquals(3, values.size());
    assertTrue(values.has(NUMBER_OF_WATCHERS_ON_GITHUB));
    assertTrue(values.has(NUMBER_OF_GITHUB_STARS));
    assertTrue(values.has(NUMBER_OF_COLLABORATORS));
    assertFalse(values.has(HAS_SECURITY_TEAM));
    assertTrue(values.of(NUMBER_OF_WATCHERS_ON_GITHUB).isPresent());
    assertTrue(values.of(NUMBER_OF_GITHUB_STARS).isPresent());
    assertTrue(values.of(NUMBER_OF_COLLABORATORS).isPresent());
    assertFalse(values.of(HAS_SECURITY_TEAM).isPresent());
    assertEquals(UnknownValue.of(NUMBER_OF_WATCHERS_ON_GITHUB), values.of(
        NUMBER_OF_WATCHERS_ON_GITHUB).get());
    assertEquals(UnknownValue.of(NUMBER_OF_GITHUB_STARS), values.of(NUMBER_OF_GITHUB_STARS).get());
    assertEquals(UnknownValue.of(NUMBER_OF_COLLABORATORS), values.of(NUMBER_OF_COLLABORATORS).get());
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    values.update(HAS_SECURITY_TEAM.value(true));

    ObjectMapper mapper = new ObjectMapper();
    byte[] bytes = mapper.writeValueAsBytes(values);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    System.out.println(new String(bytes));

    ValueSet clone = mapper.readValue(bytes, ValueSet.class);
    assertEquals(values, clone);
  }
}