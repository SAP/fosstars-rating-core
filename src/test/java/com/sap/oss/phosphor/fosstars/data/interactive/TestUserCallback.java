package com.sap.oss.phosphor.fosstars.data.interactive;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a callback for testing purposes.
 * It can answer a number of questions with pre-defined answers.
 */
public class TestUserCallback implements UserCallback {

  private static final Logger LOGGER = LogManager.getLogger(TestUserCallback.class);

  /**
   * An iterator over pre-defined answers.
   */
  private Iterator<String> iterator;

  /**
   * Initialize a callback with a number of answers.
   * @param answers The answers.
   */
  TestUserCallback(String... answers) {
    iterator = Arrays.asList(answers).iterator();
  }

  @Override
  public boolean canTalk() {
    return true;
  }

  @Override
  public String ask() {
    if (!iterator.hasNext()) {
      throw new IllegalStateException("I don't know the answer any more!");
    }
    String answer = iterator.next();
    LOGGER.info("answer: {}", answer);
    return answer;
  }

  @Override
  public String ask(String question) {
    LOGGER.info("question: {}", question);
    return ask();
  }

  @Override
  public void say(String phrase) {
    LOGGER.info("message: {}", phrase);
  }
}
