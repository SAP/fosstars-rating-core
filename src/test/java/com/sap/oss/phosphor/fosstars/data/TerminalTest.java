package com.sap.oss.phosphor.fosstars.data;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TerminalTest {

  @Test
  public void canTalk() {
    assertTrue(new Terminal().canTalk());
  }
}