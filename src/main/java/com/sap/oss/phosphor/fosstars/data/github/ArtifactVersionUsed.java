package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;

import com.sap.oss.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * This data provider collects version information from GitHub releases.
 * FIXME: not able to add this to the
 *  {@link com.sap.oss.phosphor.fosstars.tool.github.DataProviderSelector}
 *  use this as workaround see {@link ArtifactVersionUsed}
 */
public class ArtifactVersionUsed<T> extends AbstractDataProvider<T> {

  //  @Override
  //  protected ValueSet fetchValuesFor(String version) throws IOException {
  //    logger.info("Got parameter: {}", version);
  //    String setVersion = getSetVersion();
  //    logger.info("Found version: {}", setVersion);
  //    return new ValueHashSet(ARTIFACT_VERSION.value(setVersion == null ? "" : setVersion));
  //  }

  private String getSetVersion() {
    String setVersion = System.getenv("FEATURE_VALUE_ARTIFACT_VERSION");
    if (setVersion == null) {
      setVersion = System.getProperty("feature-value.artifact-version");
    }
    return setVersion;
  }

  @Override
  public boolean interactive() {
    return false;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return Collections.singleton(ARTIFACT_VERSION);
  }

  @Override
  protected AbstractDataProvider<T> doUpdate(T version, ValueSet values)
      throws IOException {

    //    logger.info("Got parameter: {}", version);
    String setVersion = getSetVersion();
    logger.info("Found version: {}", setVersion);
    values.update(ARTIFACT_VERSION.value(setVersion == null ? "" : setVersion));

    return this;
  }
}
