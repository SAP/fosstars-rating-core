package com.sap.sgs.phosphor.fosstars.nvd;

import com.sap.sgs.phosphor.fosstars.nvd.data.NVDEntry;

public interface Matcher {

  boolean match(NVDEntry entry, String vendor, String product);
}
