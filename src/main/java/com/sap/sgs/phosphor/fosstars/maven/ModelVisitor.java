package com.sap.sgs.phosphor.fosstars.maven;

import java.util.Set;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;

/**
 * A visitor for visiting elements in a POM file.
 */
public interface ModelVisitor {

  /**
   * Known locations of elements in a POM file.
   */
  enum Location {
    BUILD, REPORTING, PROFILE, MANAGEMENT
  }

  /**
   * Visit a plugin.
   *
   * @param plugin The plugin.
   * @param locations A set of locations that tells where the plugin is located.
   */
  void accept(Plugin plugin, Set<Location> locations);

  /**
   * Visit a report plugin.
   *
   * @param plugin The plugin.
   * @param locations A set of locations that tells where the plugin is located.
   */
  void accept(ReportPlugin plugin, Set<Location> locations);
}
