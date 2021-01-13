package com.sap.oss.phosphor.fosstars.model.feature.example;

/**
 * A collection of sample features.
 */
public class ExampleFeatures {

  /**
   * Private constructor, We don't need to create an instance of this class.
   */
  private ExampleFeatures() {

  }

  public static final NumberOfCommitsLastMonthExample NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE
      = new NumberOfCommitsLastMonthExample();

  public static final NumberOfContributorsLastMonthExample NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE
      = new NumberOfContributorsLastMonthExample();

  public static final SecurityReviewDoneExample SECURITY_REVIEW_DONE_EXAMPLE
      = new SecurityReviewDoneExample();

  public static final StaticCodeAnalysisDoneExample STATIC_CODE_ANALYSIS_DONE_EXAMPLE
      = new StaticCodeAnalysisDoneExample();
}
