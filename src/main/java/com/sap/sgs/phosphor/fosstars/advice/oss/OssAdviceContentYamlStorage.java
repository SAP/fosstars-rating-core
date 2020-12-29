package com.sap.sgs.phosphor.fosstars.advice.oss;

import com.sap.sgs.phosphor.fosstars.advice.AdviceContent;
import com.sap.sgs.phosphor.fosstars.advice.AdviceContentYamlStorage;
import com.sap.sgs.phosphor.fosstars.advice.AdviceContext;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The class provides advices
 * for {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures}. It is based
 * on {@link AdviceContentYamlStorage}. The class requires a {@link OssAdviceContext} that
 * helps with creating detailed advices for open-source projects.
 */
public class OssAdviceContentYamlStorage {

  /**
   * A path to a resource that contains the default advices.
   */
  private static final String RESOURCE_PATH
      = "com/sap/sgs/phosphor/fosstars/advice/oss/OssAdvices.yml";

  /**
   * A default instance of the storage that contains advices loaded from {@link #RESOURCE_PATH}.
   */
  public static final OssAdviceContentYamlStorage DEFAULT;

  static {
    try {
      DEFAULT = OssAdviceContentYamlStorage.loadFromResource(
          RESOURCE_PATH, RatingRepository.INSTANCE.rating(OssSecurityRating.class));
    } catch (IOException e) {
      throw new UncheckedIOException("Could not load advices", e);
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
   * Returns advices for a feature in a specified context for an open-source project.
   *
   * @param feature The feature.
   * @param context The context.
   * @return A list of advices.
   */
  public List<AdviceContent> advicesFor(Feature<?> feature, OssAdviceContext context) {
    return adviceContentYamlStorage.advicesFor(feature, context);
  }

  /**
   * Loads advices from a resource for a specified rating.
   *
   * @param path A path to the resource.
   * @param rating The rating.
   * @return An instance of {@link OssAdviceContentYamlStorage}.
   * @throws IOException If the advices couldn't be loaded.
   */
  public static OssAdviceContentYamlStorage loadFromResource(String path, Rating rating)
      throws IOException {

    return new OssAdviceContentYamlStorage(AdviceContentYamlStorage.loadFromResource(path, rating));
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
    };

    @Override
    public Optional<String> resolve(String variable) {
      switch (variable) {
        case "LGTM_PROJECT_LINK":
          return lgtmProjectLink();
        default:
          return Optional.empty();
      }
    }

    /**
     * Returns a link to a project on LGTM if it's available.
     *
     * @return A link to a project on LGTM.
     */
    public abstract Optional<String> lgtmProjectLink();
  }

}
