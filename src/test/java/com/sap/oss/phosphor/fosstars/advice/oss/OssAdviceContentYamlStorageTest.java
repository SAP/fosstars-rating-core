package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext.EMPTY_OSS_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COLLABORATORS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.AdviceContent;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.Test;

public class OssAdviceContentYamlStorageTest {

  @Test
  public void testDefault() throws MalformedURLException {
    List<AdviceContent> advice = OssAdviceContentYamlStorage.DEFAULT.adviceFor(
        USES_LGTM_CHECKS, EMPTY_OSS_CONTEXT);
    assertFalse(advice.isEmpty());
    assertFalse(advice.get(0).text().isEmpty());
    assertFalse(advice.get(0).links().isEmpty());

    assertTrue(OssAdviceContentYamlStorage.DEFAULT.adviceFor(
        NUMBER_OF_COLLABORATORS, EMPTY_OSS_CONTEXT).isEmpty());
  }
}