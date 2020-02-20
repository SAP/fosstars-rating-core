package com.sap.sgs.phosphor.fosstars.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a base class for all storage classes in this package.
 */
class AbstractJsonStorage {

  /**
   * An ObjectMapper for serialization and deserialization.
   */
  static final ObjectMapper MAPPER = new ObjectMapper();

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

}
