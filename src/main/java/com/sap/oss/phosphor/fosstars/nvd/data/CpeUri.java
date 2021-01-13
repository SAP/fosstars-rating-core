package com.sap.oss.phosphor.fosstars.nvd.data;

/**
 * An interface for CPE URI.
 */
public interface CpeUri {

  /**
   * Get a vendor.
   *
   * @return The vendor from the CPE URI.
   */
  String getVendor();

  /**
   * Get a product.
   *
   * @return The product from the CPE URI.
   */
  String getProduct();
}
