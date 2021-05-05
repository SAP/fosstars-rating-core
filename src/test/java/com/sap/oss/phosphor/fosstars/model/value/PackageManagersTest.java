package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.OTHER;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.PIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class PackageManagersTest {

  @Test
  public void testSorted() {
    PackageManagers packageManagers = PackageManagers.from(PIP, MAVEN, NPM);
    List<PackageManager> list = packageManagers.list();
    assertEquals(3, list.size());
    assertEquals(MAVEN, list.get(0));
    assertEquals(NPM, list.get(1));
    assertEquals(PIP, list.get(2));
  }

  @Test
  public void testContainsAndAdd() {
    PackageManagers packageManagers = PackageManagers.from(PIP, MAVEN, NPM);
    assertTrue(packageManagers.containsAny(PIP));
    assertTrue(packageManagers.containsAny(MAVEN));
    assertTrue(packageManagers.containsAny(NPM));
    assertFalse(packageManagers.containsAny(OTHER));

    packageManagers.add(OTHER);
    assertTrue(packageManagers.containsAny(OTHER));
  }

  @Test
  public void testToString() {
    PackageManagers packageManagers = PackageManagers.from(PIP, MAVEN);
    assertEquals("MAVEN, PIP", packageManagers.toString());
  }

  @Test
  public void testSerialization() throws IOException {
    PackageManagers packageManagers = PackageManagers.from(PIP, MAVEN);
    PackageManagers clone = Json.read(Json.toBytes(packageManagers), PackageManagers.class);
    assertEquals(packageManagers, clone);
  }

  @Test
  public void testEqualsAndHashCode() {
    PackageManagers one = PackageManagers.from(PIP, MAVEN);
    assertEquals(one, one);

    PackageManagers two = PackageManagers.from(PIP, MAVEN);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());

    PackageManagers three = PackageManagers.from(PIP, OTHER);
    assertNotEquals(one, three);
    assertNotEquals(two, three);
  }

  @Test
  public void testContainsAnyWithEmpty() {
    assertFalse(PackageManagers.from(PIP, MAVEN).containsAny(PackageManagers.empty()));
  }
}