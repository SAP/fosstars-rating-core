package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COLLABORATORS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.advice.AdviceContent;
import java.util.List;
import org.junit.Test;

public class OssAdviceContentStorageTest {

  @Test
  public void testDefault() {
    List<AdviceContent> advices = OssAdviceContentStorage.DEFAULT.advicesFor(USES_LGTM_CHECKS);
    assertFalse(advices.isEmpty());
    assertFalse(advices.get(0).text().isEmpty());
    assertFalse(advices.get(0).links().isEmpty());

    assertTrue(OssAdviceContentStorage.DEFAULT.advicesFor(NUMBER_OF_COLLABORATORS).isEmpty());
  }
}