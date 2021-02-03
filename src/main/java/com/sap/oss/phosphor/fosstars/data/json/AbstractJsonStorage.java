package com.sap.oss.phosphor.fosstars.data.json;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a base class for all storage classes in this package.
 */
class AbstractJsonStorage {

  /**
   * Checks if a URL is valid and uses HTTPS.
   *
   * @param url A URL to be checked.
   * @throws IllegalArgumentException If the URL is not valid or doesn't use HTTPS.
   */
  static void checkHttps(String url) {
    URL u;
    try {
      u = new URL(url);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Could not parse URL!", e);
    }

    if (!"https".equalsIgnoreCase(u.getProtocol())) {
      throw new IllegalArgumentException("Only HTTPS schema supported!");
    }
  }

  /**
   * Loads an instance of a specified class from a JSON resource or file.
   *
   * @param path A path to the resource or file.
   * @param clazz The class.
   * @return The loaded object.
   * @throws IOException If something went wrong.
   */
  static <T> T load(String path, Class<T> clazz) throws IOException {
    File file = new File(path);
    T storage;

    if (file.exists()) {
      storage = Json.mapper().readValue(file, clazz);
    } else {
      storage = loadFromResource(path, clazz);
    }

    if (storage == null) {
      throw new IOException(String.format(
          "Could not load info bug bounty programs from %s", path));
    }

    return storage;
  }

  /**
   * Tries to load an instance of a specified class from a JSON resource.
   *
   * @param path A path to the resource.
   * @param clazz The class.
   * @return The loaded object.
   * @throws IOException If something went wrong.
   */
  static <T> T loadFromResource(String path, Class<T> clazz) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return Json.mapper().readValue(is, clazz);
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("Resource '%s' not found!", path));
  }

}
