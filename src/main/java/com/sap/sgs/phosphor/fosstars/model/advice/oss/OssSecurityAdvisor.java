package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import com.sap.sgs.phosphor.fosstars.model.advice.CompositeAdvisor;

/**
 * An advisor for security ratings for open-source projects.
 */
public class OssSecurityAdvisor extends CompositeAdvisor {

  /**
   * Create a new advisor.
   */
  public OssSecurityAdvisor() {
    super(new CodeqlScoreAdvisor(), new LgtmScoreAdvisor());
  }
}
