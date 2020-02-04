package com.sap.sgs.phosphor.fosstars.data;

import java.util.Scanner;

/**
 * A {@link UserCallback} which interacts with a user via terminal.
 */
public class Terminal implements UserCallback {

  @Override
  public boolean canTalk() {
    return true;
  }

  @Override
  public String ask() {
    System.out.print(">>> ");
    return new Scanner(System.in).nextLine();
  }

  @Override
  public String ask(String question) {
    System.out.println(question);
    System.out.print(">>> ");
    return new Scanner(System.in).nextLine();
  }

  @Override
  public void say(String phrase) {
    System.out.println(phrase);
  }
}
