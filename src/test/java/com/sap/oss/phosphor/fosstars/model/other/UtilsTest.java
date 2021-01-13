package com.sap.oss.phosphor.fosstars.model.other;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import org.junit.Test;

public class UtilsTest {

  @Test
  public void date() {
    Date date;

    date = Utils.date("Jan 12, 1952");
    assertNotNull(date);

    date = Utils.date("2018-01-29T17:29Z");
    assertNotNull(date);
  }
}