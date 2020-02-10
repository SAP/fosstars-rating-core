package com.sap.sgs.phosphor.fosstars.model.tuning;

import com.sap.sgs.phosphor.fosstars.model.AbstractVisitor;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.qa.FailedTestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.RatingVerifier;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.weight.MutableWeight;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for weight optimization tasks.
 */
public abstract class AbstractWeightsOptimization {

  private static final Logger LOGGER = LogManager.getLogger(AbstractWeightsOptimization.class);

  /**
   * A rating to be adjusted.
   */
  protected final Rating rating;

  /**
   * A verifier to be used to check test vectors.
   */
  final RatingVerifier verifier;

  /**
   * A list of test vectors which defines a desirable behaviour of the rating.
   */
  protected final List<TestVector> vectors;

  /**
   * A path where a serialized rating should be stored to.
   */
  protected final String path;

  /**
   * Initialize a new instance.
   *
   * @param rating A rating to be tuned.
   * @param vectors A list of test vectors.
   * @param path A path where a serialized rating should be stored to.
   */
  AbstractWeightsOptimization(Rating rating, List<TestVector> vectors, String path) {
    this.rating = rating;
    this.vectors = vectors;
    this.path = path;
    this.verifier = new RatingVerifier(rating, vectors);
  }

  /**
   * Returns a list of mutable weights which are used in the rating.
   */
  List<MutableWeight> mutableWeights() {
    MutableWeightsCollector collector = new MutableWeightsCollector();
    rating.accept(collector);
    return Collections.unmodifiableList(collector.weights);
  }

  /**
   * Starts weights optimization for the specified rating.
   *
   * @throws VerificationFailedException If some test vectors still fail
   *                                     after the optimization was done.
   * @throws IOException If something went wrong during storing the rating to a file.
   */
  public final void run() throws VerificationFailedException, IOException {
    optimize();

    LOGGER.info("Weights:");
    Score securityScore = rating.score();
    for (Score score : securityScore.subScores()) {
      Optional<Weight> value = securityScore.weightOf(score);
      if (value.isPresent()) {
        Weight weight = value.get();
        LOGGER.info("    {} -> {}", () -> String.format("%.2f", weight.value()), score::name);
      } else {
        LOGGER.info("    {}: no weight assigned", score.name());
      }
    }

    List<FailedTestVector> failedVectors = new RatingVerifier(rating, vectors).runImpl();
    for (FailedTestVector failedVector : failedVectors) {
      LOGGER.info("Test vector #{} failed", failedVector.index);
      LOGGER.info("  reason: {}", failedVector.reason);
      LOGGER.info("  test vector:");
      LOGGER.info("  alias: {}", failedVector.vector.alias());
      LOGGER.info("    expected score: {}", failedVector.vector.expectedScore());
      LOGGER.info("    expected label: {}", failedVector.vector.expectedLabel());
      LOGGER.info("    features:");
      for (Value value : failedVector.vector.values()) {
        LOGGER.info("      {}: {}",
            value.feature(), value.isUnknown() ? "unknown" : value.get());
      }
    }

    if (failedVectors.isEmpty()) {
      LOGGER.info("Gut gemacht, all test vectors passed!");
      RatingRepository.INSTANCE.store(rating, path);
    } else {
      LOGGER.warn("{} test vector{} failed!",
          failedVectors.size(), failedVectors.size() == 1 ? "" : "s");
      throw new VerificationFailedException("Some test vectors failed!");
    }
  }

  /**
   * Runs an optimization algorithm.
   */
  abstract void optimize();

  /**
   * A visitor which collects mutable weights.
   */
  private static class MutableWeightsCollector extends AbstractVisitor {

    private final List<MutableWeight> weights = new ArrayList<>();

    @Override
    public void visit(Weight weight) {
      if (weight instanceof MutableWeight) {
        weights.add((MutableWeight) weight);
      } else {
        throw new IllegalArgumentException("The weight is not mutable!");
      }
    }
  }
}
