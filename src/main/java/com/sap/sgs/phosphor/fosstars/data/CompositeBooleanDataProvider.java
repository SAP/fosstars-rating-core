package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This is a base class for a composite data provider which combines multiple data providers which
 * provide boolean feature values.
 */
public abstract class CompositeBooleanDataProvider extends CompositeDataProvider {

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   * @param providers Underlying data providers.
   */
  public CompositeBooleanDataProvider(String where, String name, GitHub github, boolean mayTalk,
      DataProvider... providers) {
    super(where, name, github, mayTalk, providers);
  }

  /**
   * A composite data providers which combines multiple data providers which provide boolean feature
   * values. Once a provider sets the feature to true, then the rest are not going to be called (OR
   * operator).<br/> An unknown value is returned only if all underlying providers returned an
   * unknown value.
   */
  public static class RequireOne extends CompositeBooleanDataProvider {

    /**
     * @param where A GitHub organization of user name.
     * @param name A name of a repository.
     * @param github An interface to the GitHub API.
     * @param mayTalk A flag which shows if the provider can communicate with a user or not.
     */
    public RequireOne(String where, String name, GitHub github,
        boolean mayTalk, DataProvider... providers) {

      super(where, name, github, mayTalk, providers);
    }

    @Override
    public Value get(UserCallback callback) throws IOException {
      if (providers.isEmpty()) {
        throw new IllegalStateException("No providers specified!");
      }

      boolean allUnknown = true;
      Feature feature = null;
      for (DataProvider provider : providers) {
        Value value = provider.get(callback);

        if (feature == null) {
          feature = value.feature();
        }

        if (!feature.equals(value.feature())) {
          throw new IllegalStateException("Providers return different features!");
        }

        if (value.isUnknown()) {
          continue;
        }

        allUnknown = false;
        Object object = value.get();
        if (object instanceof Boolean == false) {
          throw new IllegalStateException("Provider returned not a boolean value!");
        }

        if (((Boolean) object) == true) {
          return value;
        }
      }

      if (allUnknown) {
        return UnknownValue.of(feature);
      }

      return new BooleanValue(feature, false);
    }
  }
}
