package com.sap.oss.phosphor.fosstars.data.artifact;

import com.sap.oss.phosphor.fosstars.nvd.Matcher;
import com.sap.oss.phosphor.fosstars.nvd.data.CVE;
import com.sap.oss.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data matcher to search for a specific CVE in NVD database for an artifact.
 */
public class NvdEntryArtifactCveMatcher implements Matcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(NvdEntryArtifactCveMatcher.class);

  /**
   * A CVE id.
   */
  private final String cveId;

  /**
   * Initializes a new matcher.
   *
   * @param cve A CVE id.
   */
  private NvdEntryArtifactCveMatcher(String cve) {
    this.cveId = Objects.requireNonNull(cve, "Null is not a CVE id!");
  }

  /**
   * Creates a new matcher for a project.
   *
   * @param cve The CVE id.
   * @return The new matcher.
   */
  public static NvdEntryArtifactCveMatcher nvdEntryFrom(String cve) {
    return new NvdEntryArtifactCveMatcher(cve);
  }

  @Override
  public boolean match(NvdEntry entry) {
    Objects.requireNonNull(entry, "NVD entry can't be null!");
    CVE cve = entry.getCve();

    if (cve == null) {
      LOGGER.warn("No CVE in NVD entry");
      return false;
    }

    CveMetaData meta = cve.getCveDataMeta();
    if (meta == null) {
      LOGGER.warn("No metadata in NVD entry");
      return false;
    }

    String cveId = meta.getId();
    if (cveId == null) {
      LOGGER.warn("No CVE ID in NVD entry");
      return false;
    }

    return cveId.equals(this.cveId);
  }
}