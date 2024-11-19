package com.sap.oss.phosphor.fosstars.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NoUserCallbackTest {

  @Test
  public void testCanNotTalk() {
    assertFalse(NoUserCallback.INSTANCE.canTalk());
  }

  @Test
  public void testCanNotAsk() {
    assertThrows(UnsupportedOperationException.class, () -> NoUserCallback.INSTANCE.ask());
  }

  @Test
  public void testCanNotAskQuestion() {
    assertThrows(UnsupportedOperationException.class, () -> NoUserCallback.INSTANCE.ask("hmm?"));
  }

  @Test
  public void testCanNotSay() {
    assertThrows(UnsupportedOperationException.class, () -> NoUserCallback.INSTANCE.say("oops"));
  }
}
