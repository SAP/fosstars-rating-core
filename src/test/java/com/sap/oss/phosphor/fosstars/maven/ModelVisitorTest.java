package com.sap.oss.phosphor.fosstars.maven;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.DEPENDENCIES;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.MANAGEMENT;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.PROFILE;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.junit.Test;

public class ModelVisitorTest {

  @Test
  public void testWithOnePluginInDefaultBuild() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(
        "/com/sap/oss/phosphor/fosstars/data/github/MavenCheckStyleWithNoHttp.xml")) {

      browse(readModel(is), new ModelVisitor() {

        @Override
        public void accept(Plugin plugin, Set<Location> locations) {
          assertEquals("org.apache.maven.plugins", plugin.getGroupId());
          assertEquals("maven-checkstyle-plugin", plugin.getArtifactId());
          assertEquals(1, locations.size());
          assertTrue(locations.contains(BUILD));
        }

        @Override
        public void accept(ReportPlugin plugin, Set<Location> locations) {
          fail("Should not be reached!");
        }

        @Override
        public void accept(Dependency dependency, Set<Location> locations) {
          fail("Should not be reached!");
        }
      });
    }
  }

  @Test
  public void testWithOnePluginInProfileBuild() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(
        "/com/sap/oss/phosphor/fosstars/data/github/MavenWithFindSecBugsInProfilesBuild.xml")) {

      browse(readModel(is), new ModelVisitor() {

        @Override
        public void accept(Plugin plugin, Set<Location> locations) {
          assertEquals("com.github.spotbugs", plugin.getGroupId());
          assertEquals("spotbugs-maven-plugin", plugin.getArtifactId());
          assertEquals(2, locations.size());
          assertTrue(locations.contains(BUILD));
          assertTrue(locations.contains(PROFILE));
        }

        @Override
        public void accept(ReportPlugin plugin, Set<Location> locations) {
          fail("Should not be reached!");
        }

        @Override
        public void accept(Dependency dependency, Set<Location> locations) {
          fail("Should not be reached!");
        }
      });
    }
  }

  @Test
  public void testWithOnePluginInBuildPluginManagement() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(
        "/com/sap/oss/phosphor/fosstars/data/github/"
            + "MavenWithOwaspDependencyCheckInBuildPluginManagement.xml")) {

      browse(readModel(is), new ModelVisitor() {

        @Override
        public void accept(Plugin plugin, Set<Location> locations) {
          assertEquals("org.owasp", plugin.getGroupId());
          assertEquals("dependency-check-maven", plugin.getArtifactId());
          assertEquals(2, locations.size());
          assertTrue(locations.contains(BUILD));
          assertTrue(locations.contains(MANAGEMENT));
        }

        @Override
        public void accept(ReportPlugin plugin, Set<Location> locations) {
          fail("Should not be reached!");
        }

        @Override
        public void accept(Dependency dependency, Set<Location> locations) {
          fail("Should not be reached!");
        }
      });
    }
  }

  @Test
  public void testWithOnePluginInProfilesReporting() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(
        "/com/sap/oss/phosphor/fosstars/data/github/"
            + "MavenWithOwaspDependencyCheckInProfilesReporting.xml")) {

      browse(readModel(is), new ModelVisitor() {

        @Override
        public void accept(Plugin plugin, Set<Location> locations) {
          fail("Should not be reached!");
        }

        @Override
        public void accept(ReportPlugin plugin, Set<Location> locations) {
          assertEquals("org.owasp", plugin.getGroupId());
          assertEquals("dependency-check-maven", plugin.getArtifactId());
          assertEquals(2, locations.size());
          assertTrue(locations.contains(REPORTING));
          assertTrue(locations.contains(PROFILE));
        }

        @Override
        public void accept(Dependency dependency, Set<Location> locations) {
          fail("Should not be reached!");
        }
      });
    }
  }

  @Test
  public void testWithDependencies() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("PomWithDependencies.xml")) {

      browse(readModel(is), new ModelVisitor() {

        @Override
        public void accept(Plugin plugin, Set<Location> locations) {
          fail("We should not be here!");
        }

        @Override
        public void accept(ReportPlugin plugin, Set<Location> locations) {
          fail("We should not be here!");
        }

        @Override
        public void accept(Dependency dependency, Set<Location> locations) {
          assertNotNull(dependency);
          assertEquals("test.group", dependency.getGroupId());
          switch (dependency.getArtifactId()) {
            case "dependency-in-default-section":
              assertEquals(1, locations.size());
              assertTrue(locations.contains(DEPENDENCIES));
              break;
            case "dependency-in-profile":
              assertEquals(2, locations.size());
              assertTrue(locations.contains(DEPENDENCIES));
              assertTrue(locations.contains(PROFILE));
              break;
            default:
              fail("We should not be here!");
          }
        }
      });
    }
  }
}