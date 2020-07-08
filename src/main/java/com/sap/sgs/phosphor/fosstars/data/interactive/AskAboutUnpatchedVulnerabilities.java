package com.sap.sgs.phosphor.fosstars.data.interactive;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer.YES;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.tool.InputURL;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import java.util.Collections;
import java.util.Set;

/**
 * This data provider asks a user about unpatched vulnerabilities.
 */
public class AskAboutUnpatchedVulnerabilities<T> extends AbstractInteractiveDataProvider<T> {

  @Override
  protected AskAboutUnpatchedVulnerabilities<T> ask(T object, ValueSet values) {
    Vulnerabilities unpatchedVulnerabilities = new Vulnerabilities();

    Answer answer = new YesNoQuestion(
        callback, "Are you aware about any unpatched vulnerability in the project?").ask();

    if (answer == YES) {
      do {
        callback.say("[+] This is awesome! Or, no?..");
        callback.say("[?] Please give me a URL for the vulnerability");
        String id = new InputURL(callback).get().toString();
        unpatchedVulnerabilities.add(newVulnerability(id).make());
        answer = new YesNoQuestion(callback, "One more?").ask();
      } while (answer == YES);
    }

    Value<Vulnerabilities> vulnerabilities = knownVulnerabilities(values);
    vulnerabilities.get().add(unpatchedVulnerabilities);
    values.update(vulnerabilities);

    // TODO: store the entered vulnerabilities somewhere

    return this;
  }

  /**
   * Searches for
   * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES}
   * feature a set of values.
   *
   * @param values The set of value.
   * @return An existing value for the feature, or an empty value otherwise.
   */
  private static Value<Vulnerabilities> knownVulnerabilities(ValueSet values) {
    return values.of(VULNERABILITIES).orElseGet(() -> VULNERABILITIES.value(new Vulnerabilities()));
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return Collections.singleton(VULNERABILITIES);
  }
}
