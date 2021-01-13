package com.sap.oss.phosphor.fosstars.model.tuning;

import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Tunable;
import com.sap.oss.phosphor.fosstars.model.Weight;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectorResult;
import com.sap.oss.phosphor.fosstars.model.qa.Verifier;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Weight optimization with CMA-ES algorithm.
 *
 * @see <a href="https://en.wikipedia.org/wiki/CMA-ES">CMA-ES algorithm on Wikipedia</a>
 */
public class TuningWithCMAES extends AbstractTuning {

  /**
   * Minimal weight to be assigned.
   * TODO: minimal weight should be specified for particular scores
   */
  private static final double MIN_WEIGHT = 0.1;

  /**
   * A penalty for a failed test vector.
   */
  private static final double PENALTY = 100;

  /**
   * An accuracy for comparing values of the fitness function.
   */
  private static final double ACCURACY = 0.01;

  /**
   * A fitness function.
   */
  private final FitnessFunction fitnessFunction;

  /**
   * Initializes a new {@link TuningWithCMAES}.
   *
   * @param object An object to be tuned.
   * @param verifier A verifier.
   */
  public TuningWithCMAES(Tunable object, Verifier verifier) {
    super(object, verifier);
    fitnessFunction = new FitnessFunction(verifier, object.parameters());
  }

  /**
   * Runs the CMA-ES optimization algorithm with particular parameters.
   *
   * @param step A step value for the algorithm.
   * @param maxIterations Max number of iterations.
   * @param samplesPerIteration A number of samples per iteration, at least two, generally > 4
   * @param candidateSamplesPerIteration A number of candidate samples per iteration (lambda)
   *        smaller values, for example 10, lead to more local search behavior
   *        larger values, for example 10n, may render the search more global
   * @return The solution.
   */
  private PointValuePair optimize(double step, int maxIterations, int samplesPerIteration,
      int candidateSamplesPerIteration) {

    // try to find the best value
    double stopFitness = FitnessFunction.MIN;

    // using the additional active CMA update is considered as the default variant nowadays
    boolean isActiveCMA = true;

    // random generator
    RandomGenerator random = new MersenneTwister();

    boolean generateStatistics = false;
    int checkFeasibleCount = 0;
    ConvergenceChecker<PointValuePair> checker = null;

    CMAESOptimizer optimizer = new CMAESOptimizer(maxIterations, stopFitness, isActiveCMA,
        samplesPerIteration, checkFeasibleCount, random, generateStatistics, checker);

    int n = fitnessFunction.numberOfWeights();

    // starting point
    double[] startWeights = new double[n];

    // bounds for weights
    double[] lowerWeightsBound = new double[n];
    double[] upperWeightsBound = new double[n];

    // initial step sizes
    double[] steps = new double[fitnessFunction.numberOfWeights()];

    for (int i = 0; i < n; i++) {
      double weight = fitnessFunction.weight(i);
      startWeights[i] = weight < MIN_WEIGHT ? MIN_WEIGHT : fitnessFunction.weight(i);
      lowerWeightsBound[i] = MIN_WEIGHT;
      upperWeightsBound[i] = Weight.MAX;
      steps[i] = 0.1;
    }

    /*
     * The hierarchy of optimizers:
     *
     *    CMAESOptimizer
     *      MultivariateOptimizer
     *        BaseMultivariateOptimizer
     *          BaseOptimizer
     *
     * Each of them required a number of specific implementations of OptimizationData
     */
    return optimizer.optimize(
        // required by BaseOptimizer
        // are these parameters required?
        // we have specified them already in the constructor of CMAESOptimizer
        MaxEval.unlimited(), MaxIter.unlimited(),

        // required by BaseMultivariateOptimizer
        new InitialGuess(startWeights), new SimpleBounds(lowerWeightsBound, upperWeightsBound),

        // required by MultivariateOptimizer
        new ObjectiveFunction(fitnessFunction), GoalType.MINIMIZE,

        // required by CMAESOptimizer
        new CMAESOptimizer.Sigma(steps),
        new CMAESOptimizer.PopulationSize(candidateSamplesPerIteration)
    );
  }

  @Override
  public void runTuning() {
    double[] steps = { 0.1, 0.25, 0.05 };
    int[] differentMaxIterations = { 100, 1000, 10000 };
    int[] differentSamplesPerIteration = { 4, 8, 10, 16 };
    int[] differentCandidateSamplesPerIteration = { 10, 50, 100 };

    PointValuePair currentSolution = null;
    List<? extends Parameter> parameters = object.parameters();

    main_loop:
    for (double step : steps) {
      for (int maxIterations : differentMaxIterations) {
        for (int samplesPerIteration : differentSamplesPerIteration) {
          for (int candidateSamplesPerIteration : differentCandidateSamplesPerIteration) {
            PointValuePair solution = optimize(step, maxIterations, samplesPerIteration,
                candidateSamplesPerIteration);

            double[] point = solution.getPoint();
            for (int i = 0; i < point.length; i++) {
              parameters.get(i).value(point[i]);
            }
            List<TestVectorResult> testVectorResults = verifier.run();
            String indexes = testVectorResults.stream()
                .map(v -> String.format("#%d", v.index))
                .collect(Collectors.joining(", "));

            if (isBetter(currentSolution, solution)) {
              logger.info("Hooray! Found a better value of the fitness function!");
              logger.info("Fitness function = {}, failed {} test vectors: {}",
                  String.format("%.3f", solution.getValue()), testVectorResults.size(), indexes);
              logger.info("Algorithm parameters:");
              logger.info("    max iterations = {}", maxIterations);
              logger.info("    samples per iteration = {}", samplesPerIteration);
              logger.info("    candidate samples per iteration = {}",
                  candidateSamplesPerIteration);
              logger.info("    step = {}", step);
              currentSolution = solution;
            }

            if (testVectorResults.isEmpty()) {
              // TODO: introduce a system property which controls early break
              logger.info("Found a solution which passes all test vectors");
              break main_loop;
            }
          }
        }
      }
    }
  }

  /**
   * Checks if a new solution is better than the current one.
   * A new solution is better its value of the fitness function is lower,
   * or there is currently no solution.
   *
   * @param currentSolution The old solution.
   * @param newSolution The new solution.
   * @return True if the new solution is better than the old solution, false otherwise.
   */
  private static boolean isBetter(PointValuePair currentSolution, PointValuePair newSolution) {
    if (currentSolution == null) {
      return true;
    }
    double oldValue = currentSolution.getValue();
    double newValue = newSolution.getValue();
    return oldValue > newValue && oldValue - newValue > ACCURACY;
  }

  /**
   * This is a fitness function for minimization.
   * For each test vector, the function does the following:
   * <ul>
   *   <li>Adds a penalty if the test vector failed.</li>
   *   <li>Calculates a mean value for the expected score interval,
   *       and adds an absolute difference between the mean and actual score value.</li>
   * </ul>
   */
  private static final class FitnessFunction implements MultivariateFunction {

    /**
     * A minimal value of the function.
     */
    static final double MIN = 0;

    /**
     * A list of weights which may be adjusted.
     */
    private final List<? extends Parameter> parameters;

    /**
     * A verifier for checking the test vectors.
     */
    private final Verifier verifier;

    /**
     * Initialize a fitness function.
     *
     * @param verifier A verifier.
     * @param parameters A list of weights which may be adjusted.
     */
    FitnessFunction(Verifier verifier, List<? extends Parameter> parameters) {
      this.verifier = verifier;
      this.parameters = parameters;
    }

    @Override
    public double value(double[] parameters) {
      if (parameters.length != this.parameters.size()) {
        throw new IllegalArgumentException(String.format(
            "The number of parameters (%d) doesn't match with the number of weights (%d)",
            parameters.length, this.parameters.size()));
      }

      for (int i = 0; i < parameters.length; i++) {
        this.parameters.get(i).value(parameters[i]);
      }

      double value = MIN;
      List<TestVectorResult> results = verifier.run();
      for (TestVectorResult result : results) {
        if (result.failed()) {
          value += PENALTY;
        } else if (!result.scoreValue.isNotApplicable()) {
          value += Math.abs(result.vector.expectedScore().mean() - result.scoreValue.get());
        }
      }

      return value;
    }

    /**
     * Get the number of weights which may be adjusted.
     *
     * @return The number of weights which may be adjusted.
     */
    int numberOfWeights() {
      return parameters.size();
    }

    /**
     * Return a weight by its index.
     *
     * @param i An index of the weight.
     * @return The weight.
     */
    double weight(int i) {
      return parameters.get(i).value();
    }
  }

}
