package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CODE_OF_CONDUCT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CodeOfConductGuidelinInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() throws IOException {
    CodeOfConductGuidelineInfo provider = new CodeOfConductGuidelineInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_CODE_OF_CONDUCT));
    assertTrue(provider.supportedFeatures()
        .contains(HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE));
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
  }

}