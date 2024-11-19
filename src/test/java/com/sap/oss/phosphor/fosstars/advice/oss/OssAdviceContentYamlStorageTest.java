package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COLLABORATORS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.AdviceContent;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OssAdviceContentYamlStorageTest {

  @Test
  public void testDefault() throws MalformedURLException {
    List<AdviceContent> advice =
        OssAdviceContentYamlStorage.DEFAULT.adviceFor(USES_CODEQL_CHECKS, EMPTY_OSS_CONTEXT);
    assertFalse(advice.isEmpty());
    assertFalse(advice.get(0).text().isEmpty());
    assertFalse(advice.get(0).links().isEmpty());

    assertTrue(
        OssAdviceContentYamlStorage.DEFAULT
            .adviceFor(NUMBER_OF_COLLABORATORS, EMPTY_OSS_CONTEXT)
            .isEmpty());
  }
}
