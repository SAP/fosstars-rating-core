package com.sap.oss.phosphor.fosstars.maven;

import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;

/**
 * An implementation of {@link ModelVisitor} that does nothing.
 */
public abstract class AbstractModelVisitor implements ModelVisitor {

  @Override
  public void accept(Plugin plugin, Set<Location> locations) {
    // do nothing
  }

  @Override
  public void accept(ReportPlugin plugin, Set<Location> locations) {
    // do nothing
  }

  @Override
  public void accept(Dependency dependency, Set<Location> locations) {
    // do nothing
  }
}
