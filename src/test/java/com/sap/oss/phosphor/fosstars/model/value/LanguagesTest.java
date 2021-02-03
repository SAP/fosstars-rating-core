package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.OTHER;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

public class LanguagesTest {

  @Test
  public void testSorted() {
    Languages languages = Languages.of(JAVA, OTHER, C);
    Iterator<Language> iterator = languages.get().iterator();
    assertEquals(C, iterator.next());
    assertEquals(JAVA, iterator.next());
    assertEquals(OTHER, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testContains() {
    Languages languages = Languages.of(JAVA, C, OTHER);
    assertEquals(3, languages.size());
    assertTrue(languages.containsAnyOf(JAVA));
    assertTrue(languages.containsAnyOf(C));
    assertTrue(languages.containsAnyOf(OTHER));
    assertTrue(languages.containsAnyOf(Languages.of(C, PYTHON)));
  }

  @Test
  public void testToString() {
    Languages languages = Languages.of(JAVA, C);
    assertEquals("C, JAVA", languages.toString());
  }

  @Test
  public void testSerialization() throws IOException {
    Languages languages = Languages.of(JAVA, C, OTHER);
    Languages clone = Json.read(Json.toBytes(languages), Languages.class);
    assertEquals(languages, clone);
  }

  @Test
  public void testEqualsAndHashCode() {
    Languages one = Languages.of(C, PYTHON);
    assertEquals(one, one);

    Languages two = Languages.of(PYTHON, C);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());

    Languages three = Languages.of(JAVA, C);
    assertNotEquals(one, three);
    assertNotEquals(two, three);
  }

}