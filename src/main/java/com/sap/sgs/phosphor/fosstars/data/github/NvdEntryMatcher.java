package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.nvd.Matcher;
import com.sap.sgs.phosphor.fosstars.nvd.data.Affects;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.Configurations;
import com.sap.sgs.phosphor.fosstars.nvd.data.CpeMatch;
import com.sap.sgs.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Node;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ProductData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Vendor;
import com.sap.sgs.phosphor.fosstars.nvd.data.VendorData;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data matcher to search for entries in NVD for a GitHub project.
 */
public class NvdEntryMatcher implements Matcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(NvdEntryMatcher.class);

  /**
   * A project to be checked.
   */
  private final GitHubProject project;

  /**
   * Creates a new matcher for a project.
   *
   * @param project The project.
   * @return The new matcher.
   */
  public static NvdEntryMatcher entriesFor(GitHubProject project) {
    return new NvdEntryMatcher(project);
  }

  /**
   * Initializes a new matcher.
   *
   * @param project A project to be checked.
   */
  private NvdEntryMatcher(GitHubProject project) {
    this.project = Objects.requireNonNull(project, "Null is not a project!");
  }

  @Override
  public boolean match(NvdEntry entry) {
    Objects.requireNonNull(entry, "NVD entry can't be null!");

    if (match(entry.getConfigurations())) {
      return true;
    }

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

    return match(cve.getAffects());
  }

  /**
   * Returns true if one of the configurations matches the project, false otherwise.
   */
  private boolean match(Configurations configurations) {
    if (configurations == null || configurations.getNodes() == null) {
      return false;
    }

    for (Node node : configurations.getNodes()) {
      if (node.getCpeMatches() == null) {
        continue;
      }

      for (CpeMatch cpeMatch : node.getCpeMatches()) {
        if (cpeMatch == null) {
          continue;
        }

        if (match(cpeMatch.getCpe22Uri()) || match(cpeMatch.getCpe23Uri())) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true if a string matches the project, false otherwise.
   */
  private boolean match(String string) {
    return string != null && string.toLowerCase().contains(project.name());
  }

  /**
   * Returns true if one of the entries in an Affects element match the project, false otherwise.
   */
  private boolean match(Affects affects) {
    if (affects == null) {
      return false;
    }

    Vendor cveVendor = affects.getVendor();
    if (cveVendor == null) {
      return false;
    }

    for (VendorData vendorData : cveVendor.getVendorData()) {
      if (match(vendorData)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if vendor data from an NVD entry matches to the project's owner and name.
   *
   * @param vendorData The vendor data.
   * @return True if the vendor data matches the project, false otherwise.
   */
  private boolean match(VendorData vendorData) {
    if (!match(project.organization().name(), vendorData.getVendorName())) {
      return false;
    }

    for (ProductData productData : vendorData.getProduct().getProductData()) {
      if (match(productData)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if product data from an NVD entry matches to the project's owner and name.
   *
   * @param productData The produce data.
   * @return True if the product data matches to the project's owner and name, false otherwise.
   */
  private boolean match(ProductData productData) {
    return match(project.name(), productData.getProductName());
  }

  /**
   * Returns true if one of two strings includes the other one.
   */
  private static boolean match(String one, String two) {
    one = one.toLowerCase().trim();
    two = two.toLowerCase().trim();
    return one.contains(two) || two.contains(one);
  }
}
