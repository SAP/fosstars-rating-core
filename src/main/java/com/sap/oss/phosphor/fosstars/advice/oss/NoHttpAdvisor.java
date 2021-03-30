package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.net.MalformedURLException;
import java.util.List;

/**
 * An advisor for features related to NoHttp tool.
 */
public class NoHttpAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public NoHttpAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    return adviceForFeature(usedValues, USES_NOHTTP, subject, context, NoHttpAdvisor::noHttpTool);
  }

  /**
   * Checks if a value tells that NoHttp tool is not used.
   *
   * @param value The value.
   * @return True if NoHttp tool is not used, false otherwise.
   */
  private static boolean noHttpTool(Value<?> value) {
    return USES_NOHTTP.equals(value.feature())
        && !value.isUnknown()
        && Boolean.FALSE.equals(value.get());
  }
}
