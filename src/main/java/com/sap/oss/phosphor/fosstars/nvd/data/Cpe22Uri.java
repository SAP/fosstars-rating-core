package com.sap.oss.phosphor.fosstars.nvd.data;

/**
 * Extended class from {@link AbstractCpeUri}, highlighting the 2.2 CPE format.
 * 
 * @see <a href="https://csrc.nist.gov/schema/cpe/2.2/cpe-dictionary_2.2.xsd">CPE 2.2 schema</a>
 */
public class Cpe22Uri extends AbstractCpeUri {

  public Cpe22Uri(String cpeUri) {
    super(cpeUri, 2, 3);
  }
}