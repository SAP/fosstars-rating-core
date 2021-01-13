package com.sap.oss.phosphor.fosstars.data;

/**
 * An interface which allows communicating with a user.
 */
public interface UserCallback {

  /**
   * Checks if the user callback can interact with a user.
   *
   * @return True is the user callback can interact with a user, false otherwise
   */
  boolean canTalk();

  /**
   * Asks a user for a reply.
   *
   * @return User's reply.
   */
  String ask();

  /**
   * Prints a question to a user, and waits for his reply.
   *
   * @param question The questions to be asked.
   * @return User's reply.
   */
  String ask(String question);

  /**
   * Prints a message to a user.
   *
   * @param phrase The message for user.
   */
  void say(String phrase);
}
