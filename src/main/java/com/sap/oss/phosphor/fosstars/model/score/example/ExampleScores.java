package com.sap.oss.phosphor.fosstars.model.score.example;

/**
 * A collection of sample scores.
 */
public class ExampleScores {

  /**
   * Private constructor, We don't need to create an instance of this class.
   */
  private ExampleScores() {

  }

  public static final ProjectActivityScoreExample PROJECT_ACTIVITY_SCORE_EXAMPLE
      = new ProjectActivityScoreExample();

  public static final SecurityTestingScoreExample SECURITY_TESTING_SCORE_EXAMPLE
      = new SecurityTestingScoreExample();

  public static final SecurityScoreExample SECURITY_SCORE_EXAMPLE
      = new SecurityScoreExample();
}
