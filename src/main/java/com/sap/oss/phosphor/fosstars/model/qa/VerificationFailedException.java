package com.sap.oss.phosphor.fosstars.model.qa;

/**
 * An exception which indicates that a verification procedure for a rating failed.
 */
public class VerificationFailedException extends Exception {

  /**
   * Creates an exception with a generic message.
   */
  public VerificationFailedException() {
    super("One of the test vectors failed!");
  }

  /**
   * Creates an exception with a specified message.
   *
   * @param format A format string.
   * @param params A number of parameters for the format string.
   */
  public VerificationFailedException(String format, Object... params) {
    super(String.format(format, params));
  }
}
