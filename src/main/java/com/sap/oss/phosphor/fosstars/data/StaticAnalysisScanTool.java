package com.sap.oss.phosphor.fosstars.data;

import java.util.Map;

/**
 * This is an interface of an Static Analysis Scan Tool (SAST) check data provider.
 */
public interface StaticAnalysisScanTool {

  /**
   * Checks if a GitHub action triggers a SAST.
   *
   * @param githubAction A config for the action.
   * @return True if the action triggers a SAST, false otherwise.
   */
  boolean triggersScan(Map<?, ?> githubAction);

  /**
   * Checks if a GitHub action runs on pull requests.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  boolean runsOnPullRequests(Map<?, ?> githubAction);
}
