package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.F_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.TYPESCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.VISUALBASIC;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LanguageTest {

  @Test
  public void testParse() {
    assertEquals(GO, Language.parse("Go"));
    assertEquals(GO, Language.parse("Golang"));
    assertEquals(JAVA, Language.parse("Java"));
    assertEquals(C_SHARP, Language.parse("C#"));
    assertEquals(F_SHARP, Language.parse("F#"));
    assertEquals(JAVASCRIPT, Language.parse("JavaScript"));
    assertEquals(TYPESCRIPT, Language.parse("TypeScript"));
    assertEquals(VISUALBASIC, Language.parse("VisualBasic"));
    assertEquals(CPP, Language.parse("C++"));
    assertEquals(C, Language.parse("C"));
  }

}