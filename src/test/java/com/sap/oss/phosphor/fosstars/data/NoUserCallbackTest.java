package com.sap.oss.phosphor.fosstars.data;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NoUserCallbackTest {

  @Test
  public void testCanNotTalk() {
    assertFalse(NoUserCallback.INSTANCE.canTalk());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCanNotAsk() {
    NoUserCallback.INSTANCE.ask();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCanNotAskQuestion() {
    NoUserCallback.INSTANCE.ask("hmm?");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCanNotSay() {
    NoUserCallback.INSTANCE.say("oops");
  }

}