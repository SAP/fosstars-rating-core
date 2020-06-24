package com.sap.sgs.phosphor.fosstars.nvd.data;

/**
 * An interface for CPE URI.
 */
public interface CpeUri {

  /**
   * Gets the vendor from the CPE URI.
   */
  public String getVendor();

  /**
   * Gets the product from the CPE URI.
   */
  public String getProduct();
}
