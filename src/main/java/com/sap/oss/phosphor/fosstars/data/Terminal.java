package com.sap.oss.phosphor.fosstars.data;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link UserCallback} which interacts with a user via terminal.
 */
public class Terminal implements UserCallback {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(Terminal.class);

  @Override
  public boolean canTalk() {
    return true;
  }

  @Override
  public String ask() {
    LOGGER.info(">>> ");
    return new Scanner(System.in).nextLine();
  }

  @Override
  public String ask(String question) {
    LOGGER.info(question);
    LOGGER.info(">>> ");
    return new Scanner(System.in).nextLine();
  }

  @Override
  public void say(String phrase) {
    LOGGER.info(phrase);
  }
}
