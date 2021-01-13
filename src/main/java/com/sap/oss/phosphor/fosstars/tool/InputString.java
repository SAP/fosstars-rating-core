package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import java.util.Objects;

/**
 * Reads a string provided by a user.
 */
public class InputString {

  /**
   * A callback to interact with a user.
   */
  private final UserCallback callback;

  /**
   * Initializes a new {@link InputString}.
   *
   * @param callback A callback to interact with a user.
   */
  public InputString(UserCallback callback) {
    Objects.requireNonNull(callback, "Hey! User callback can't be null!");
    this.callback = callback;
  }

  /**
   * Asks a user to provide a string.
   *
   * @return A string provider by a user.
   */
  public String get() {
    String string;
    while (true) {
      string = callback.ask();
      if (string != null) {
        string = string.trim();
      }
      if (string == null || string.isEmpty()) {
        callback.say("[!] Hmm ... Looks like an empty string. Please try again ...");
      } else {
        break;
      }
    }
    return string;
  }
}
