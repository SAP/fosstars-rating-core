package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import java.util.Objects;

/**
 * Ask a questions and offers yes/no answers.
 */
public class YesNoQuestion {

  /**
   * Acceptable answers.
   */
  public enum Answer {
    YES, NO
  }

  /**
   * A callback to interact with a user.
   */
  private final UserCallback callback;

  /**
   * A question for a user.
   */
  private final String question;

  /**
   * Initializes a new {@link YesNoQuestion}.
   *
   * @param callback A callback to interact with a user.
   * @param question A question for a user.
   */
  public YesNoQuestion(UserCallback callback, String question) {
    Objects.requireNonNull(callback, "Hey! User callback can't be null!");
    Objects.requireNonNull(question, "Hey! Question can't be null!");
    this.callback = callback;
    this.question = question;
  }

  /**
   * Asks a user a question and reads their reply.
   *
   * @return The user's answer.
   */
  public Answer ask() {
    String reply;
    while (true) {
      reply = callback.ask(String.format("[?] %s (yes/no)", question));
      switch (reply.toLowerCase().trim()) {
        case "yes":
        case "y":
          return Answer.YES;
        case "no":
        case "n":
          return Answer.NO;
        default:
          callback.say(String.format(
              "What the heck is '%s'? I didn't get it ... please try again?%n", reply));
      }
    }
  }

}
