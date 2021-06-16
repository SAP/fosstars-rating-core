package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.Subject;
import java.io.IOException;
import java.util.Optional;

/**
 * A calculator that calculates a rating for a project.
 */
public interface RatingCalculator {

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback The interface for interacting with a user.
   * @return The same calculator.
   */
  RatingCalculator set(UserCallback callback);

  /**
   * Set a cache for the calculator.
   *
   * @param cache The cache.
   * @return The same calculator.
   */
  RatingCalculator set(ValueCache<Subject> cache);

  /**
   *
   * @param adaptor
   * @return
   */
  RatingCalculator set(SubjectAdaptor adaptor);

  /**
   * Calculate a rating and assign it to a subject.
   *
   * @param subject The subject.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  RatingCalculator calculateFor(Subject subject) throws IOException;

  /**
   *
   */
  interface SubjectAdaptor {

    /**
     *
     * @param subject
     * @param provider
     * @return
     */
    Optional<Subject> adapt(Subject subject, DataProvider provider);
  }
}
