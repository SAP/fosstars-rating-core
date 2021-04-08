package com.sap.oss.phosphor.fosstars.advice;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.AdviceContentYamlStorage.RawAdviceContent;
import com.sap.oss.phosphor.fosstars.advice.AdviceContentYamlStorage.RawLink;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class AdviceContentYamlStorageTest {

  @Test
  public void testLoadFromResource() throws IOException {
    AdviceContentYamlStorage storage = AdviceContentYamlStorage.loadFrom(
        "com/sap/oss/phosphor/fosstars/advice/AdviceContentStorageTest.yml");

    List<AdviceContent> advice = storage.adviceFor(
        SECURITY_REVIEW_DONE_EXAMPLE, OssAdviceContext.EMPTY_ADVICE_CONTEXT);

    assertEquals(2, advice.size());
    for (int i = 0; i < 2; i++) {
      assertFalse(advice.get(i).text().isEmpty());
      assertFalse(advice.get(i).links().isEmpty());
    }

    advice = storage.adviceFor(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, EMPTY_OSS_CONTEXT);
    assertEquals(3, advice.size());
    assertFalse(advice.get(0).text().isEmpty());
    assertFalse(advice.get(0).links().isEmpty());
    assertFalse(advice.get(1).text().isEmpty());
    assertTrue(advice.get(1).links().isEmpty());
    assertFalse(advice.get(2).text().isEmpty());
    assertTrue(advice.get(2).links().isEmpty());

    advice = storage.adviceFor(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, EMPTY_OSS_CONTEXT);
    assertTrue(advice.isEmpty());
  }

  @Test
  public void testLoadFromFile() throws IOException {
    String path = "TestAdvice.yml";
    String content =
          "---\n"
        + "Security review status (example):\n"
        + "  - advice: test1\n"
        + "    links: []\n"
        + "  - advice: test2\n"
        + "Static code analysis status (example):\n"
        + "  - advice: test3\n"
        + "    links:\n"
        + "      - name: link5\n"
        + "        url: https://test/5\n";
    Files.write(Paths.get(path), content.getBytes());
    try {
      AdviceContentYamlStorage storage = AdviceContentYamlStorage.loadFrom(path);

      List<AdviceContent> advice = storage.adviceFor(
          SECURITY_REVIEW_DONE_EXAMPLE, OssAdviceContext.EMPTY_ADVICE_CONTEXT);

      assertEquals(2, advice.size());
      for (int i = 0; i < advice.size(); i++) {
        assertFalse(advice.get(i).text().isEmpty());
        assertTrue(advice.get(i).links().isEmpty());
      }

      advice = storage.adviceFor(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, EMPTY_OSS_CONTEXT);
      assertEquals(1, advice.size());
      assertFalse(advice.get(0).text().isEmpty());
      assertFalse(advice.get(0).links().isEmpty());

      advice = storage.adviceFor(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, EMPTY_OSS_CONTEXT);
      assertTrue(advice.isEmpty());
    } finally {
      FileUtils.forceDeleteOnExit(new File(path));
    }
  }

  @Test(expected = IOException.class)
  public void testWithNotExistingPath() throws IOException {
    AdviceContentYamlStorage.loadFrom("does_not_exist.yml");
  }

  @Test
  public void testRawContentVariables() {
    RawAdviceContent content = new RawAdviceContent(
        "Test ${FIRST_VARIABLE} variable",
        asList(
            new RawLink("First link ${SECOND_VARIABLE}}", "https://test"),
            new RawLink("Second link", "${THIRD_VARIABLE}}")));

    Set<String> variables = content.variables();
    assertEquals(3, variables.size());
    assertTrue(variables.contains("FIRST_VARIABLE"));
    assertTrue(variables.contains("SECOND_VARIABLE"));
    assertTrue(variables.contains("THIRD_VARIABLE"));
  }

  @Test
  public void testYamlSerialization() throws IOException {
    RawAdviceContent fullContent = new RawAdviceContent(
        "Test",
        asList(
            new RawLink("First", "https://test/first"),
            new RawLink("Second", "https://test/second")));
    RawAdviceContent clone = Yaml.read(Yaml.toBytes(fullContent), RawAdviceContent.class);
    assertEquals(fullContent, clone);

    RawAdviceContent noLinks = new RawAdviceContent("No links", null);
    clone = Yaml.read(Yaml.toBytes(noLinks), RawAdviceContent.class);
    assertEquals(noLinks, clone);
  }
}