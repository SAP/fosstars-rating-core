package com.sap.oss.phosphor.fosstars.advice;

import java.util.Optional;

/**
 * A context for making advice more detailed for a specific subject.
 */
public interface AdviceContext {

  /**
   * A context that provides no details.
   */
  AdviceContext EMPTY_ADVICE_CONTEXT = name -> Optional.empty();

  /**
   * Returns a value of a variable in the context.
   *
   * @param variable The variable name.
   * @return A value of the variable in the context if it's available.
   */
  Optional<String> resolve(String variable);
}
