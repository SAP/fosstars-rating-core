package com.sap.oss.phosphor.fosstars.nvd.data;

import java.util.Objects;

/**
 * An abstract class for CPE URIs.
 */
public abstract class AbstractCpeUri implements CpeUri {

  /**
   * The vendor of the project.
   */
  private final String vendor;

  /**
   * The product of the project.
   */
  private final String product;

  /**
   * Public constructor.
   */
  AbstractCpeUri(String cpeUri, int vendorIndex, int productIndex) {
    Objects.requireNonNull(cpeUri, "The uri cannot be null");

    if (vendorIndex > 3 || productIndex > 4 || productIndex <= vendorIndex) {
      throw new IllegalArgumentException("Oh no! The indexes don't seem to be correct!");
    }

    String[] splitContent = cpeUri.split(":");
    if (splitContent.length <= productIndex) {
      throw new IllegalArgumentException("Oh no! The CPE URI doesn't seem to be correct!");
    }

    this.vendor = splitContent[vendorIndex];
    this.product = splitContent[productIndex];
  }

  /**
   * Gets the vendor from the CPE URI.
   */
  @Override
  public String getVendor() {
    return vendor;
  }

  /**
   * Gets the product from the CPE URI.
   */
  @Override
  public String getProduct() {
    return product;
  }

  @Override
  public String toString() {
    return String.format("Vendor : %s\nProduct: %s\n\n", vendor, product);
  }
}