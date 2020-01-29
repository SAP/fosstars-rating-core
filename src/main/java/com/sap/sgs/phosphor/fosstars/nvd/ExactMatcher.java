package com.sap.sgs.phosphor.fosstars.nvd;

import com.sap.sgs.phosphor.fosstars.nvd.data.Affects;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVEDataMeta;
import com.sap.sgs.phosphor.fosstars.nvd.data.Cve;
import com.sap.sgs.phosphor.fosstars.nvd.data.NVDEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ProductData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Vendor;
import com.sap.sgs.phosphor.fosstars.nvd.data.VendorData;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExactMatcher implements Matcher {

  private static final Logger LOGGER = LogManager.getLogger(ExactMatcher.class);

  @Override
  public boolean match(NVDEntry entry, String vendor, String product) {
    Objects.requireNonNull(entry, "NVD entry can't be null!");
    Objects.requireNonNull(vendor, "Vendor can't be null!");
    Objects.requireNonNull(product, "Product can't be null!");

    Cve cve = entry.getCve();
    if (cve == null) {
      LOGGER.warn("No CVE in NVD entry");
      return false;
    }

    CVEDataMeta meta = cve.getCVEDataMeta();
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
      if (vendor.equalsIgnoreCase(vendorData.getVendorName())) {
        for (ProductData productData : vendorData.getProduct().getProductData()) {
          if (product.equalsIgnoreCase(productData.getProductName())) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
