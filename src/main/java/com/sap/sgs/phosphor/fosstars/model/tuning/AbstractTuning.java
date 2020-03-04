package com.sap.sgs.phosphor.fosstars.model.tuning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Tunable;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectorResult;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.qa.Verifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for tuning procedures.
 */
public abstract class AbstractTuning {

  /**
   * An ObjectMapper for serialization and deserialization ratings to JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

  /**
   * An object to be tuned.
   */
  final Tunable object;

  /**
   * A verifier to be used.
   */
  final Verifier verifier;

  /**
   * A path where a serialized tuned object should be stored to.
   */
  protected final String path;

  /**
   * Initialize a new tuner.
   *
   * @param object An object to be tuned.
   * @param verifier A verifier to be used during tuning.
   * @param path A path where a serialized tuned object should be stored to.
   */
  AbstractTuning(Tunable object, Verifier verifier, String path) {
    this.object = Objects.requireNonNull(object, "Object can't be null!");
    this.verifier = Objects.requireNonNull(verifier, "Verifier can't be null!");
    this.path = Objects.requireNonNull(path, "Path can't be null!");
  }

  /**
   * Runs tuning.
   *
   * @throws VerificationFailedException If some test vectors still fail
   *                                     after the tuning was done.
   */
  public final void run() throws VerificationFailedException, IOException {
    runTuning();

    boolean success = true;
    List<TestVectorResult> results = verifier.run();
    for (TestVectorResult result : results) {
      if (result.failed()) {
        success = false;

        logger.info("Test vector #{} failed", result.index);
        logger.info("  reason: {}", result.message);
        logger.info("  test vector:");
        logger.info("  alias: {}", result.vector.alias());
        logger.info("    expected score: {}", result.vector.expectedScore());
        logger.info("    expected label: {}", result.vector.expectedLabel());
        logger.info("    features:");

        for (Value value : result.vector.values()) {
          logger.info("      {}: {}",
              value.feature(), value.isUnknown() ? "unknown" : value.get());
        }
      }
    }

    if (success) {
      logger.info("Gut gemacht, all test vectors passed!");
      Files.write(
          Paths.get(path),
          MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(object));
    } else {
      logger.warn("{} test vector{} failed!",
          results.size(), results.size() == 1 ? "" : "s");
      throw new VerificationFailedException("Some test vectors failed!");
    }
  }

  /**
   * Runs an optimization algorithm.
   */
  abstract void runTuning();
}
