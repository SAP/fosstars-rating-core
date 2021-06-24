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
   * Set an adaptor for subjects.
   *
   * @param adaptor The adaptor.
   * @return The same calculator.
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
   * An adaptor that adapts a subject for a data provider.
   * For example, if a subject is not supported by a data provider, but the subject
   * has another subject inside, which is supported by the provider, then the adaptor can return it.
   */
  interface SubjectAdaptor {

    /**
     * Adapt a subject for a data provider.
     *
     * @param subject The subject.
     * @param provider The provider.
     * @return An adapted subject if available.
     */
    Optional<Subject> adapt(Subject subject, DataProvider provider);
  }
}
