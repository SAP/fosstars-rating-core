package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class HandlingUntrustedDataTest {

  private static final HandlingUntrustedData PROVIDER = new HandlingUntrustedData();

  @Test
  public void testSupportedFeatures() {
    assertEquals(1, PROVIDER.supportedFeatures().size());
    assertTrue(PROVIDER.supportedFeatures().contains(HANDLING_UNTRUSTED_DATA_LIKELIHOOD));
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
    HandlingUntrustedData provider = new HandlingUntrustedData();
    provider.set(NoUserCallback.INSTANCE);
    ValueSet values = provider.fetchValuesFor(PROJECT);
    assertEquals(1, values.size());
    Optional<Value<Likelihood>> value = values.of(HANDLING_UNTRUSTED_DATA_LIKELIHOOD);
    assertTrue(value.isPresent());
    assertFalse(value.get().isUnknown());
    assertEquals(HIGH, value.get().get());
  }
}