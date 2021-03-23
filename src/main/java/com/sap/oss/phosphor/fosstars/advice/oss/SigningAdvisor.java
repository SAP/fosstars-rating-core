package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.net.MalformedURLException;
import java.util.List;

/**
 * An advisor for features related to signing.
 */
public class SigningAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public SigningAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    return adviceForFeature(
        usedValues, SIGNS_ARTIFACTS, subject, context, SigningAdvisor::disabledArtifactSigning);
  }

  /**
   * Checks if a value tells that artifact signing is disabled.
   *
   * @param value The value.
   * @return True if artifact signing is disabled, false otherwise.
   */
  private static boolean disabledArtifactSigning(Value<?> value) {
    return SIGNS_ARTIFACTS.equals(value.feature())
        && !value.isUnknown()
        && Boolean.FALSE.equals(value.get());
  }
}
