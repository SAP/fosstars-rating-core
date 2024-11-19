package com.sap.oss.phosphor.fosstars.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TerminalTest {

  @Test
  public void canTalk() {
    assertTrue(new Terminal().canTalk());
  }
}
