package com.sap.sgs.phosphor.fosstars.model.advice;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class AdviceContentStorageTest {

  @Test
  public void testLoadFromResource() throws IOException {
    Subject project = new GitHubProject("test", "project");

    AdviceContentStorage storage = new AdviceContentStorage(
        "com/sap/sgs/phosphor/fosstars/model/advice/AdviceContentStorageTest.yml");

    List<AdviceContent> advices = storage.advicesFor(SECURITY_REVIEW_DONE_EXAMPLE);
    assertEquals(2, advices.size());
    for (int i = 0; i < 2; i++) {
      assertFalse(advices.get(i).text().isEmpty());
      assertFalse(advices.get(0).links().isEmpty());
    }

    advices = storage.advicesFor(STATIC_CODE_ANALYSIS_DONE_EXAMPLE);
    assertEquals(1, advices.size());
    assertFalse(advices.get(0).text().isEmpty());
    assertFalse(advices.get(0).links().isEmpty());

    advices = storage.advicesFor(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    assertTrue(advices.isEmpty());
  }
}