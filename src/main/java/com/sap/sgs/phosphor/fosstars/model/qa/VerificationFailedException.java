package com.sap.sgs.phosphor.fosstars.model.qa;

/**
 * An exception which indicates that a verification procedure for a rating failed.
 */
public class VerificationFailedException extends Exception {

  public VerificationFailedException() {
    super("One of the test vectors failed!");
  }

  public VerificationFailedException(String format, Object... params) {
    super(String.format(format, params));
  }
}
