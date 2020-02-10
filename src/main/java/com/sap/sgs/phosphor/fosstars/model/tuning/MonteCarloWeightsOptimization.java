package com.sap.sgs.phosphor.fosstars.model.tuning;

import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.qa.FailedTestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.weight.MutableWeight;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This class applies Monte Carlo method to search for weights which can be used in a rating, so
 * that the rating passes test vectors. In other words, the class just tries random mutableWeights
 * to find a right combination.
 */
public class MonteCarloWeightsOptimization extends AbstractWeightsOptimization {

  private static final double STEP = 0.01;

  /**
   * A random generator for selecting weights.
   */
  private final Random weightIndexGenerator = new Random();

  /**
   * A random generator for selecting how a weight is modified on each iteration of Monte Carlo
   * method.
   */
  private final Random coin = new Random();

  public MonteCarloWeightsOptimization(Rating rating, List<TestVector> vectors, String path) {
    super(rating, vectors, path);
  }

  /**
   * Search for weights by applying Monte Carlo method. On each iteration, it picks up a random
   * weight, change a bit, and run the test vectors. The procedure stops when all the test vectors
   * pass. Theoretically, it can be an infinite process (highly depends on test vectors).
   */
  public void optimize() {
    List<MutableWeight> weights = mutableWeights();
    while (!passTestVectors()) {
      MutableWeight weight = randomFrom(weights);
      double oldValue = weight.value();
      double v = coin.nextBoolean() ? oldValue - STEP : oldValue + STEP;
      if (v > Weight.MAX) {
        v = Weight.MAX;
      }
      if (v <= Weight.MIN) {
        v = Weight.MIN + STEP;
      }
      weight.value(v);
    }
  }

  /**
   * Returns a random weight from a list of mutableWeights.
   */
  private MutableWeight randomFrom(List<MutableWeight> weights) {
    int n = weightIndexGenerator.nextInt(weights.size());
    return weights.get(n);
  }

  /**
   * Checks if the rating passes all the test vectors.
   *
   * @return True if all test vector are passes, and false otherwise.
   */
  private boolean passTestVectors() {
    List<FailedTestVector> failedVectors = verifier.runImpl();
    return failedVectors.isEmpty();
  }

}
