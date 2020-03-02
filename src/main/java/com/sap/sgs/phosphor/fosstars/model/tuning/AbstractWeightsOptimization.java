package com.sap.sgs.phosphor.fosstars.model.tuning;

import com.sap.sgs.phosphor.fosstars.model.AbstractVisitor;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.qa.FailedTestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerifier;
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

  // TODO: initialize a logger in child classes
  private static final Logger LOGGER = LogManager.getLogger(AbstractWeightsOptimization.class);

  /**
   * A score to be adjusted.
   */
  protected final Score score;

  /**
   * A verifier to be used to check test vectors.
   */
  final ScoreVerifier verifier;

  /**
   * A list of test vectors which defines a desirable behaviour of the score.
   */
  protected final List<TestVector> vectors;

  /**
   * A path where a serialized score should be stored to.
   */
  protected final String path;

  /**
   * Initialize a new instance.
   *
   * @param score A score to be tuned.
   * @param vectors A list of test vectors.
   * @param path A path where a serialized score should be stored to.
   */
  AbstractWeightsOptimization(Score score, List<TestVector> vectors, String path) {
    this.score = score;
    this.vectors = vectors;
    this.path = path;
    this.verifier = new ScoreVerifier(score, vectors);
  }

  /**
   * Returns a list of mutable weights which are used in the score.
   */
  List<MutableWeight> mutableWeights() {
    MutableWeightsCollector collector = new MutableWeightsCollector();
    score.accept(collector);
    return Collections.unmodifiableList(collector.weights);
  }

  /**
   * Starts weights optimization for the specified score.
   *
   * @throws VerificationFailedException If some test vectors still fail
   *                                     after the optimization was done.
   */
  public final void run() throws VerificationFailedException, IOException {
    optimize();

    LOGGER.info("Weights:");
    for (Score subScore : score.subScores()) {
      Optional<Weight> value = score.weightOf(subScore);
      if (value.isPresent()) {
        Weight weight = value.get();
        LOGGER.info("    {} -> {}", () -> String.format("%.2f", weight.value()), subScore::name);
      } else {
        LOGGER.info("    {}: no weight assigned", subScore.name());
      }
    }

    List<FailedTestVector> failedVectors = new ScoreVerifier(score, vectors).runImpl();
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
      RatingRepository.INSTANCE.store(score, path);
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
