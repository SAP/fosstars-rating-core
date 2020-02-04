package com.sap.sgs.phosphor.fosstars.data;

/**
 * A fake {@link UserCallback}.
 */
public class NoUserCallback implements UserCallback {

  public static final NoUserCallback INSTANCE = new NoUserCallback();

  private NoUserCallback() {}

  @Override
  public boolean canTalk() {
    return false;
  }

  @Override
  public String ask() {
    throw new UnsupportedOperationException("I can't talk!");
  }

  @Override
  public String ask(String question) {
    throw new UnsupportedOperationException("I can't talk!");
  }

  @Override
  public void say(String phrase) {
    throw new UnsupportedOperationException("I can't talk!");
  }
}
