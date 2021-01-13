package com.sap.oss.phosphor.fosstars.nvd;

import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;

/**
 * An interface for a matcher that checks if an entry from NVD satisfies a requirement.
 */
public interface Matcher {

  /**
   * Checks if an entry from NVD satisfies a requirement.
   *
   * @param entry The entry to be checked.
   * @return True if the requirement is met, false otherwise.
   */
  boolean match(NvdEntry entry);
}
