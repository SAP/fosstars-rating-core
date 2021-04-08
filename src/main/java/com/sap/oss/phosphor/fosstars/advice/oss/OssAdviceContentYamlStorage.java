package com.sap.oss.phosphor.fosstars.advice.oss;

import com.sap.oss.phosphor.fosstars.advice.AdviceContent;
import com.sap.oss.phosphor.fosstars.advice.AdviceContentYamlStorage;
import com.sap.oss.phosphor.fosstars.advice.AdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The class provides advice
 * for {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures}. It is based
 * on {@link AdviceContentYamlStorage}. The class requires a {@link OssAdviceContext} that
 * helps with creating detailed advice for open-source projects.
 */
public class OssAdviceContentYamlStorage {

  /**
   * A path to a resource that contains the default advice.
   */
  private static final String RESOURCE_PATH
      = "com/sap/oss/phosphor/fosstars/advice/oss/OssAdvice.yml";

  /**
   * A default instance of the storage that contains advice loaded from {@link #RESOURCE_PATH}.
   */
  public static final OssAdviceContentYamlStorage DEFAULT;

  static {
    try {
      DEFAULT = OssAdviceContentYamlStorage.loadFrom(RESOURCE_PATH);
    } catch (IOException e) {
      throw new UncheckedIOException("Could not load advice", e);
    }
  }

  /**
   * An underlying advice storage.
   */
  private final AdviceContentYamlStorage adviceContentYamlStorage;

  /**
   * Create a new storage.
   *
   * @param adviceContentYamlStorage An underlying advice storage.
   */
  private OssAdviceContentYamlStorage(AdviceContentYamlStorage adviceContentYamlStorage) {
    Objects.requireNonNull(adviceContentYamlStorage, "Oh no! Storage is nulL!");
    this.adviceContentYamlStorage = adviceContentYamlStorage;
  }

  /**
   * Returns advice for a feature in a specified context for an open-source project.
   *
   * @param feature The feature.
   * @param context The context.
   * @return A list of advice.
   * @throws MalformedURLException If the method couldn't parse URLs.
   */
  public List<AdviceContent> adviceFor(Feature<?> feature, AdviceContext context)
      throws MalformedURLException {

    return adviceContentYamlStorage.adviceFor(feature, context);
  }

  /**
   * Loads advice from a resource.
   *
   * @param path A path to the resource.
   * @return An instance of {@link OssAdviceContentYamlStorage}.
   * @throws IOException If the advice couldn't be loaded.
   */
  public static OssAdviceContentYamlStorage loadFrom(String path) throws IOException {
    return new OssAdviceContentYamlStorage(AdviceContentYamlStorage.loadFrom(path));
  }

  /**
   * A context for advice for open-source projects.
   */
  public abstract static class OssAdviceContext implements AdviceContext {

    /**
     * A context that provides no details about open-source projects.
     */
    public static final OssAdviceContext EMPTY_OSS_CONTEXT = new OssAdviceContext() {

      @Override
      public Optional<String> lgtmProjectLink() {
        return Optional.empty();
      }

      @Override
      public Optional<String> suggestSecurityPolicyLink() {
        return Optional.empty();
      }
    };

    @Override
    public Optional<String> resolve(String variable) {
      switch (variable) {
        case "LGTM_PROJECT_LINK":
          return lgtmProjectLink();
        case "SUGGEST_SECURITY_POLICY_ON_GITHUB":
          return suggestSecurityPolicyLink();
        default:
          return Optional.empty();
      }
    }

    /**
     * If available, returns a link to a project on LGTM.
     *
     * @return A link to a project on LGTM.
     */
    public abstract Optional<String> lgtmProjectLink();

    /**
     * If available, returns a link for suggesting a security policy for a subject.
     *
     * @return A link for suggesting a security policy.
     */
    public abstract Optional<String> suggestSecurityPolicyLink();
  }

}
