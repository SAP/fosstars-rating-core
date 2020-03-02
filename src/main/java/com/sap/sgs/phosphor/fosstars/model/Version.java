package com.sap.sgs.phosphor.fosstars.model;

import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;

/**
 * This is a list of all registered versions of ratings.
 */
public enum Version {

  /**
   * A sample security rating for demo purposes only.
   */
  SECURITY_RATING_EXAMPLE_1_1(SecurityRatingExample.class),

  /**
   * A security rating for open-source projects.
   */
  OSS_SECURITY_RATING_1_0(OssSecurityRating.class);

  /**
   * A class that represents a rating.
   */
  public final Class clazz;

  /**
   * Initializes a new version.
   *
   * @param clazz A class that represents a rating.
   */
  Version(Class clazz) {
    this.clazz = clazz;
  }
}
