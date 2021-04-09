package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory.WITH_EMPTY_CONTEXT;
import static com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.DEFAULT;
import static com.sap.oss.phosphor.fosstars.util.Config.loadDefaultYamlConfigIfAvailable;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An advisor for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating}.
 */
public class OssRulesOfPlayAdvisor extends AbstractOssAdvisor {

  /**
   * OSS Rules of Play rating.
   */
  private static final OssRulesOfPlayRating RATING
      = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);

  public OssRulesOfPlayAdvisor() throws IOException {
    super(storage(), WITH_EMPTY_CONTEXT);
  }

  /**
   * Create a new advisor with default advice.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public OssRulesOfPlayAdvisor(OssAdviceContextFactory contextFactory) {
    super(DEFAULT, contextFactory);
  }

  /**
   * Create a new advisor.
   *
   * @param path A path to advice.
   * @param contextFactory A factory that provides contexts for advice.
   * @throws IOException If advice could not be loaded.
   */
  public OssRulesOfPlayAdvisor(String path, OssAdviceContextFactory contextFactory)
      throws IOException {

    super(OssAdviceContentYamlStorage.loadFrom(path, RATING), contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    List<Advice> advice = new ArrayList<>();
    for (Feature<?> feature : RATING.allFeatures()) {
      if (feature instanceof BooleanFeature == false) {
        throw new IllegalStateException(
            String.format("Oops! Not a boolean feature: %s", feature.name()));
      }
      advice.addAll(
          adviceForBooleanFeature(usedValues, (BooleanFeature) feature, subject, context));
    }

    return advice;
  }

  /**
   * Load an advice storage.
   *
   * @return An advice storage.
   * @throws IOException If an advice storage could not be loaded.
   */
  private static OssAdviceContentYamlStorage storage() throws IOException {
    Optional<Path> path = loadDefaultYamlConfigIfAvailable(OssRulesOfPlayAdvisor.class);
    if (path.isPresent()) {
      return OssAdviceContentYamlStorage.loadFrom(path.get().toString(), RATING);
    }

    return DEFAULT;
  }
}
