package com.sap.oss.phosphor.fosstars.nvd;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNVD extends NVD {

  private final Map<String, InputStream> content = new HashMap<>();

  @Override
  public List<String> jsonFiles() {
    return new ArrayList<>(content.keySet());
  }

  @Override
  InputStream open(String file) {
    return content.get(file);
  }

  public void add(String file, InputStream is) {
    content.put(file, is);
  }
}
