package com.sap.oss.phosphor.fosstars.model.value;

/**
 * Shows how OWASP Dependency Check can be used in a project.
 */
public enum OwaspDependencyCheckUsage {

  MANDATORY, OPTIONAL, NOT_USED;

  @Override
  public String toString() {
    switch (this) {
      case NOT_USED:
        return "Not used";
      case OPTIONAL:
        return "Optional";
      case MANDATORY:
        return "Mandatory";
      default:
        return super.toString();
    }
  }
}