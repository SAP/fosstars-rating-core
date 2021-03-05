package com.sap.oss.phosphor.fosstars.advice;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.AdviceContentYamlStorage.RawAdviceContent;
import com.sap.oss.phosphor.fosstars.advice.AdviceContentYamlStorage.RawLink;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class AdviceContentYamlStorageTest {

  @Test
  public void testLoadFromResource() throws IOException {
    AdviceContentYamlStorage storage = AdviceContentYamlStorage.loadFromResource(
        "com/sap/oss/phosphor/fosstars/advice/AdviceContentStorageTest.yml",
        RatingRepository.INSTANCE.rating(SecurityRatingExample.class));

    List<AdviceContent> advice = storage.advicesFor(
        SECURITY_REVIEW_DONE_EXAMPLE, OssAdviceContext.EMPTY_ADVICE_CONTEXT);

    assertEquals(2, advice.size());
    for (int i = 0; i < 2; i++) {
      assertFalse(advice.get(i).text().isEmpty());
      assertFalse(advice.get(0).links().isEmpty());
    }

    advice = storage.advicesFor(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, EMPTY_OSS_CONTEXT);
    assertEquals(1, advice.size());
    assertFalse(advice.get(0).text().isEmpty());
    assertFalse(advice.get(0).links().isEmpty());

    advice = storage.advicesFor(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, EMPTY_OSS_CONTEXT);
    assertTrue(advice.isEmpty());
  }

  @Test
  public void testRawContentVariables() {
    RawAdviceContent content = new RawAdviceContent(
        "Test ${FIRST_VARIABLE} variable",
        Arrays.asList(
            new RawLink("First link ${SECOND_VARIABLE}}", "https://test"),
            new RawLink("Second link", "${THIRD_VARIABLE}}")));

    Set<String> variables = content.variables();
    assertEquals(3, variables.size());
    assertTrue(variables.contains("FIRST_VARIABLE"));
    assertTrue(variables.contains("SECOND_VARIABLE"));
    assertTrue(variables.contains("THIRD_VARIABLE"));
  }
}