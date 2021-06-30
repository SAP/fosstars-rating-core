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
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An advisor for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating}.
 */
public class OssRulesOfPlayAdvisor extends AbstractOssAdvisor {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(OssRulesOfPlayAdvisor.class);

  /**
   * OSS Rules of Play rating.
   */
  private static final OssRulesOfPlayRating RATING
      = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);

  /**
   * Create a new advisor.
   *
   * @throws IOException If advice could not be loaded.
   */
  public OssRulesOfPlayAdvisor() throws IOException {
    super(storage(), WITH_EMPTY_CONTEXT);
  }

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   * @throws IOException If advice could not be loaded.
   */
  public OssRulesOfPlayAdvisor(OssAdviceContextFactory contextFactory) throws IOException {
    super(storage(), contextFactory);
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

    super(OssAdviceContentYamlStorage.loadFrom(path), contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    List<Value<Boolean>> toBeAdvised = new ArrayList<>();
    toBeAdvised.addAll(OssRulesOfPlayScore.findViolatedRulesIn(usedValues));
    toBeAdvised.addAll(OssRulesOfPlayScore.findWarningsIn(usedValues));

    List<Advice> advice = new ArrayList<>();
    for (Feature<?> feature : RATING.allFeatures()) {
      if (!BooleanFeature.class.isAssignableFrom(feature.getClass())) {
        throw new IllegalStateException(
            String.format("Oops! Not a boolean feature: %s", feature.name()));
      }

      advice.addAll(adviceForFeature(usedValues, feature, subject, context, toBeAdvised::contains));
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
      LOGGER.info("Found a config for the advisor: {}", path.get());
      return OssAdviceContentYamlStorage.loadFrom(path.get().toString());
    }

    return DEFAULT;
  }
}
