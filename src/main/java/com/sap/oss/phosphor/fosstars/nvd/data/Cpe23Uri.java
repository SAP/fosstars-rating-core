package com.sap.oss.phosphor.fosstars.nvd.data;

/**
 * Extended class from {@link AbstractCpeUri}, highlighting the 2.3 CPE format.
 * 
 * @see <a href="https://csrc.nist.gov/schema/cpe/2.3/cpe-dictionary_2.3.xsd">CPE 2.3 schema</a>
 */
public class Cpe23Uri extends AbstractCpeUri {

  public Cpe23Uri(String cpeUri) {
    super(cpeUri, 3, 4);
  }
}