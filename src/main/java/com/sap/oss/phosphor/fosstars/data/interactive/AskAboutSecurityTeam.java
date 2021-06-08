package com.sap.oss.phosphor.fosstars.data.interactive;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.tool.YesNoSkipQuestion;
import com.sap.oss.phosphor.fosstars.tool.YesNoSkipQuestion.Answer;
import java.util.Collections;
import java.util.Set;

/**
 * This data provider asks a user about security teams.
 */
public class AskAboutSecurityTeam extends AbstractInteractiveDataProvider {

  @Override
  protected AskAboutSecurityTeam ask(Subject subject, ValueSet values) {
    String question = String.format(
        "Does '%s' have a security team? Say yes, no, or skip, please.", subject.purl());

    Answer answer = new YesNoSkipQuestion(callback, question).ask();
    switch (answer) {
      case YES:
        values.update(new BooleanValue(HAS_SECURITY_TEAM, true));
        break;
      case NO:
        values.update(new BooleanValue(HAS_SECURITY_TEAM, false));
        break;
      case SKIP:
        break;
      default:
        throw new IllegalArgumentException(
            String.format("Hey! You gave me an unexpected answer: %s", answer));
    }

    // TODO: store the answer in the SecurityTeamStorage

    return this;
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return Collections.singleton(HAS_SECURITY_TEAM);
  }
}
