package com.sap.oss.phosphor.fosstars.data.interactive;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import java.util.EnumSet;

/**
 * Ask a question and offers options from an enum.
 */
public class SelectFromEnum<T extends Enum<T>> {

  /**
   * A callback to interact with a user.
   */
  private final UserCallback callback;

  /**
   * A question for a user.
   */
  private final String question;

  /**
   * A class of an enum with options.
   */
  private final Class<T> enumClass;

  /**
   * Initializes a new {@link SelectFromEnum}.
   *
   * @param callback A callback to interact with a user.
   * @param question A question for a user.
   * @param enumClass A class of an enum with options.
   */
  public SelectFromEnum(UserCallback callback, String question, Class<T> enumClass) {
    requireNonNull(callback, "Hey! User callback can't be null!");
    requireNonNull(question, "Hey! Question can't be null!");
    requireNonNull(question, "Hey! Options can't be null!");

    this.callback = callback;
    this.question = question;
    this.enumClass = enumClass;
  }

  /**
   * Asks a user a question and reads their reply.
   *
   * @return The user's answer.
   */
  public T ask() {
    String reply;
    while (true) {
      EnumSet<T> options = EnumSet.allOf(enumClass);
      callback.say(format("[?] %s", question));
      callback.say(format("    Options: %s",
          options.stream().map(Enum::toString).map(String::toLowerCase).collect(joining(", "))));
      reply = callback.ask();
      reply = reply.trim().toLowerCase();
      if (reply.isEmpty()) {
        callback.say("[!] Hmm ... Looks like an empty string ... Please try again.");
        continue;
      }
      for (T option : options) {
        if (option.toString().toLowerCase().equals(reply)) {
          return option;
        }
      }

      callback.say(format("[!] What the heck is '%s'? Please try again?%n", reply));
    }
  }

}
