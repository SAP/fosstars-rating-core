package com.sap.sgs.phosphor.fosstars.model;

/**
 * This is a list of all registered versions of ratings.
 */
public enum Version {

  /**
   * A sample security rating for demo purposes only.
   */
  SECURITY_RATING_EXAMPLE_1_1(
      "com/sap/sgs/phosphor/fosstars/model/rating/example/SecurityRatingExample_1_1.json"),

  /**
   * A security rating for open-source projects.
   */
  OSS_SECURITY_RATING_1_0(
      "com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating_1_0.json");

  /**
   * A path to a JSON file which contains a serialized rating.
   */
  public final String path;

  /**
   * @param path the path of the JSON file which contains the serialized rating.
   */
  Version(String path) {
    this.path = path;
  }
}
