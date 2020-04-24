package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.nvd.Matcher;
import com.sap.sgs.phosphor.fosstars.nvd.data.Affects;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVEDataMeta;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ProductData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Vendor;
import com.sap.sgs.phosphor.fosstars.nvd.data.VendorData;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a matcher that check if vendor data from an NVD entry matched to
 * a project's owner and name.
 */
public class VendorDataMatcher implements Matcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(VendorDataMatcher.class);

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
  public static VendorDataMatcher with(GitHubProject project) {
    return new VendorDataMatcher(project);
  }

  /**
   * Initializes a new matcher.
   *
   * @param project A project to be checked.
   */
  private VendorDataMatcher(GitHubProject project) {
    this.project = Objects.requireNonNull(project, "Null is not a project!");
  }

  @Override
  public boolean match(NvdEntry entry) {
    Objects.requireNonNull(entry, "NVD entry can't be null!");

    CVE cve = entry.getCve();
    if (cve == null) {
      LOGGER.warn("No CVE in NVD entry");
      return false;
    }

    CVEDataMeta meta = cve.getCveDataMeta();
    if (meta == null) {
      LOGGER.warn("No metadata in NVD entry");
      return false;
    }

    String cveId = meta.getID();
    if (cveId == null) {
      LOGGER.warn("No CVE ID in NVD entry");
      cveId = "unknown";
    }

    Affects affects = cve.getAffects();
    if (affects == null) {
      LOGGER.warn("No affects in NVD entry for {}", cveId);
      return false;
    }

    Vendor cveVendor = affects.getVendor();
    if (cveVendor == null) {
      LOGGER.warn("No vendor in NVD entry for {}", cveId);
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
