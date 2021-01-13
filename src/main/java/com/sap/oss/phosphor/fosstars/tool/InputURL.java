package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Reads a URL provided by a user.
 */
public class InputURL {

  /**
   * A callback to interact with a user.
   */
  private final UserCallback callback;

  /**
   * Initializes a new {@link InputURL}.
   *
   * @param callback A callback to interact with a user.
   */
  public InputURL(UserCallback callback) {
    Objects.requireNonNull(callback, "Hey! User callback can't be null!");
    this.callback = callback;
  }

  /**
   * Ask a user to provide a URL.
   *
   * @return A URL provided by a user.
   */
  public URL get() {
    String string;
    URL url;
    while (true) {
      string = callback.ask();
      if (string != null) {
        string = string.trim();
      }
      if (string == null || string.isEmpty()) {
        callback.say("[!] Hmm ... Looks like an empty string. Please try again ...");
        continue;
      }
      try {
        url = new URL(string);
      } catch (MalformedURLException e) {
        callback.say("[!] Hmm ... Looks like it's not a URL. Please try again ...");
        continue;
      }
      break;
    }
    return url;
  }
}
