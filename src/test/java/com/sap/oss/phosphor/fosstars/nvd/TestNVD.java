package com.sap.oss.phosphor.fosstars.nvd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class TestNVD extends NVD {

  private final Map<String, byte[]> content = new HashMap<>();

  @Override
  public void download() {
    // do nothing
  }

  @Override
  public List<String> jsonFiles() {
    return new ArrayList<>(content.keySet());
  }

  @Override
  InputStream open(String file) {
    return new ByteArrayInputStream(content.get(file));
  }

  public void add(String file, InputStream is) throws IOException {
    add(file, IOUtils.toByteArray(is));
  }

  public void add(String file, byte[] bytes) {
    content.put(file, bytes);
  }
}
