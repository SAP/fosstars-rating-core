package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COLLABORATORS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class ValueHashSetTest {

  @Test
  public void testCreateEmptyValueHashSet() {
    assertEquals(0, ValueHashSet.empty().size());
  }

  @Test
  public void testCreateNotEmptyValueHashSet() {
    ValueSet values = new ValueHashSet(
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(5));
    assertEquals(2, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_THREE_MONTHS));
    assertTrue(values.has(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS));
  }

  @Test
  public void testHasValue() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    assertEquals(1, values.size());
    assertTrue(values.has(NUMBER_OF_COMMITS_LAST_THREE_MONTHS));
  }

  @Test
  public void testUpdate() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    assertEquals(1, values.size());
    assertEquals(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10), values.iterator().next());
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(20));
    assertEquals(1, values.size());
    assertEquals(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(20), values.iterator().next());
  }

  @Test
  public void testToSet() {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    values.update(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(5));
    Set<Value<?>> set = values.toSet();
    assertNotNull(set);
    assertEquals(2, set.size());
  }

  @Test
  public void testUnknown() {
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
    assertEquals(
        UnknownValue.of(NUMBER_OF_COLLABORATORS),
        values.of(NUMBER_OF_COLLABORATORS).get());
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ValueSet values = ValueHashSet.empty();
    values.update(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(10));
    values.update(HAS_SECURITY_TEAM.value(true));

    byte[] bytes = Json.toBytes(values);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    System.out.println(new String(bytes));

    ValueSet clone = Json.read(bytes, ValueSet.class);
    assertEquals(values, clone);
  }
}