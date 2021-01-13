package com.sap.oss.phosphor.fosstars.maven;

import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.DEPENDENCIES;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.MANAGEMENT;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.PROFILE;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;

import com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * The class contains useful methods for working with Maven.
 */
public class MavenUtils {

  /**
   * Parses a pom.xml file.
   *
   * @param content The content of the pom.xml file.
   * @return A {@link Model} which represents the pom.xml file.
   * @throws IOException If something went wrong.
   */
  public static Model readModel(InputStream content) throws IOException {
    try {
      return new MavenXpp3Reader().read(content);
    } catch (XmlPullParserException e) {
      throw new IOException(e);
    }
  }

  /**
   * Browse a POM file with a specified visitor.
   *
   * @param model The POM file to browse.
   * @param visitor The visitor.
   * @param <T> A type of the visitor.
   * @return The passed visitor.
   */
  public static <T extends ModelVisitor> T browse(Model model, T visitor) {
    Objects.requireNonNull(model, "Oh no! Model is null!");
    Objects.requireNonNull(visitor, "On no! Visitor is null!");

    visitBuild(model.getBuild(), visitor, EnumSet.noneOf(Location.class));
    visitReporting(model.getReporting(), visitor, EnumSet.noneOf(Location.class));
    visitProfiles(model.getProfiles(), visitor, EnumSet.noneOf(Location.class));
    visitDependenciesIn(model, visitor, EnumSet.noneOf(Location.class));

    return visitor;
  }

  /**
   * Visit a build section.
   *
   * @param build The build section.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitBuild(
      BuildBase build, ModelVisitor visitor, EnumSet<Location> locations) {

    if (build == null) {
      return;
    }

    visitPlugins(build.getPlugins(), visitor, in(locations, BUILD));
    visitPluginManagement(build.getPluginManagement(), visitor, in(locations, BUILD));
  }

  /**
   * Visit a list of plugins.
   *
   * @param plugins The plugins.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitPlugins(
      List<Plugin> plugins, ModelVisitor visitor, EnumSet<Location> locations) {

    if (plugins == null) {
      return;
    }

    plugins.forEach(plugin -> visitor.accept(plugin, in(locations)));
  }

  /**
   * Visit a plugin management section.
   *
   * @param pluginManagement The section to be visited.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitPluginManagement(
      PluginManagement pluginManagement, ModelVisitor visitor, EnumSet<Location> locations) {

    if (pluginManagement == null) {
      return;
    }

    pluginManagement.getPlugins()
        .forEach(plugin -> visitor.accept(plugin, in(locations, MANAGEMENT)));
  }

  /**
   * Visit a list of profiles.
   *
   * @param profiles The profiles.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitProfiles(
      List<Profile> profiles, ModelVisitor visitor, EnumSet<Location> locations) {

    if (profiles == null) {
      return;
    }

    profiles.forEach(profile -> visitProfile(profile, visitor, in(locations)));
  }

  /**
   * Visit a profile.
   *
   * @param profile The profile.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitProfile(
      Profile profile, ModelVisitor visitor, EnumSet<Location> locations) {

    if (profile == null) {
      return;
    }

    visitBuild(profile.getBuild(), visitor, in(locations, PROFILE));
    visitReporting(profile.getReporting(), visitor, in(locations, PROFILE));
  }

  /**
   * Visit a reporting section.
   *
   * @param reporting The reporting section.
   * @param visitor The visitor.
   * @param locations A set of locations.
   */
  private static void visitReporting(
      Reporting reporting, ModelVisitor visitor, EnumSet<Location> locations) {

    if (reporting == null) {
      return;
    }

    reporting.getPlugins().forEach(plugin -> visitor.accept(plugin, in(locations, REPORTING)));
  }

  /**
   * Visit dependencies in a model.
   *
   * @param model The model to be processed.
   * @param visitor The visitor to be applied.
   * @param locations A set of locations.
   */
  private static void visitDependenciesIn(
      Model model, ModelVisitor visitor, EnumSet<Location> locations) {

    if (model == null || model.getDependencies() == null) {
      return;
    }

    visit(model.getDependencies(), visitor, in(locations, DEPENDENCIES));
    visitDependenciesIn(model.getProfiles(), visitor, in(locations, DEPENDENCIES));
  }

  /**
   * Visit dependencies in profiles.
   *
   * @param profiles The profiles.
   * @param visitor The visitor to be applied.
   * @param locations A set of locations.
   */
  private static void visitDependenciesIn(
      List<Profile> profiles, ModelVisitor visitor, EnumSet<Location> locations) {

    if (profiles != null) {
      profiles.forEach(profile -> visitDependenciesIn(profile, visitor, locations));
    }
  }

  /**
   * Visit dependencies in a profile.
   *
   * @param profile The profile.
   * @param visitor The visitor to be applied.
   * @param locations A set of locations.
   */
  private static void visitDependenciesIn(
      Profile profile, ModelVisitor visitor, EnumSet<Location> locations) {

    if (profile != null) {
      visit(profile.getDependencies(), visitor, in(locations, PROFILE));
    }
  }

  /**
   * Visit dependencies.
   *
   * @param dependencies The dependencies to visit.
   * @param visitor The visitor to be applied.
   * @param locations A set of locations.
   */
  private static void visit(
      List<Dependency> dependencies, ModelVisitor visitor, EnumSet<Location> locations) {

    if (dependencies != null) {
      dependencies.forEach(dependency -> visitor.accept(dependency, locations));
    }
  }

  /**
   * Builds a new set of locations.
   *
   * @param locations A set of locations to be added to the resulting set.
   * @param rest An array of locations to be added to the resulting set.
   * @return A set of locations that contains all passed locations.
   */
  private static EnumSet<Location> in(
      EnumSet<Location> locations, Location... rest) {

    EnumSet<Location> set = EnumSet.copyOf(locations);
    set.addAll(Arrays.asList(rest));
    return set;
  }
}
