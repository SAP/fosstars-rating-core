package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class ProjectUsageTest {

  private static final ProjectUsage PROVIDER = new ProjectUsage();

  @Test
  public void testSupportedFeatures() {
    assertEquals(1, PROVIDER.supportedFeatures().size());
    assertTrue(PROVIDER.supportedFeatures().contains(PROJECT_USAGE));
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
    ProjectUsage provider = new ProjectUsage();
    provider.set(NoUserCallback.INSTANCE);
    ValueSet values = provider.fetchValuesFor(PROJECT);
    assertEquals(1, values.size());
    Optional<Value<Quantity>> value = values.of(PROJECT_USAGE);
    assertTrue(value.isPresent());
    assertFalse(value.get().isUnknown());
    assertEquals(A_LOT, value.get().get());
  }
}