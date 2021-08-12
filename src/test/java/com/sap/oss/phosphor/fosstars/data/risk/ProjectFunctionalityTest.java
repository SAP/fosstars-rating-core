package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.OTHER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class ProjectFunctionalityTest {

  private static final ProjectFunctionality PROVIDER = new ProjectFunctionality();

  @Test
  public void testSupportedFeatures() {
    assertEquals(1, PROVIDER.supportedFeatures().size());
    assertTrue(PROVIDER.supportedFeatures().contains(FUNCTIONALITY));
  }

  @Test
  public void testInteractive() {
    assertFalse(PROVIDER.interactive());
  }

  @Test
  public void testSupports() {
    assertTrue(PROVIDER.supports(PROJECT));
  }

  @Test
  public void testFetchValuesFor() throws IOException {
    ProjectFunctionality provider = new ProjectFunctionality();
    provider.set(NoUserCallback.INSTANCE);
    ValueSet values = provider.fetchValuesFor(PROJECT);
    assertEquals(1, values.size());
    Optional<Value<Functionality>> value = values.of(FUNCTIONALITY);
    assertTrue(value.isPresent());
    assertFalse(value.get().isUnknown());
    assertEquals(OTHER, value.get().get());
  }
}