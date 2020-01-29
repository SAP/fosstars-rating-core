package com.sap.sgs.phosphor.fosstars.common;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

  /**
   * Checks if a URL is valid and uses HTTPS.
   *
   * @param url A URL to be checked.
   * @throws IllegalArgumentException If the URL is not valid or doesn't use HTTPS.
   */
  public static void checkHttps(String url) {
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
