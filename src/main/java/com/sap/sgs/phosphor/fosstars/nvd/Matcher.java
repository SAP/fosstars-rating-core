package com.sap.sgs.phosphor.fosstars.nvd;

import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;

public interface Matcher {

  boolean match(NvdEntry entry, String vendor, String product);
}
