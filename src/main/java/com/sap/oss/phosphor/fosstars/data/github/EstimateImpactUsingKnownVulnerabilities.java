package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V2;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V3;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.util.Set;

/**
 * <p>This data provider tries to estimate possible impact on confidentiality, integrity and
 * availability (CIA) if a security problem appears in an open source project.
 * The provider uses info about known vulnerabilities in open source projects.
 * It fills out the following features:</p>
 * <ul>
 *   <li>{@link OssRiskFeatures#CONFIDENTIALITY_IMPACT}</li>
 *   <li>{@link OssRiskFeatures#INTEGRITY_IMPACT}</li>
 *   <li>{@link OssRiskFeatures#AVAILABILITY_IMPACT}</li>
 * </ul>
 */
public class EstimateImpactUsingKnownVulnerabilities extends AbstractDataProvider {

  /**
   * The provider tries to estimate CIA only if the number of vulnerabilities is more than this.
   */
  private static final int KNOWN_VULNERABILITIES_THRESHOLD = 10;

  /**
   * An underlying data provider that provides info about vulnerabilities.
   */
  private final InfoAboutVulnerabilities infoAboutVulnerabilities;

  /**
   * Initialize a new data provider.
   *
   * @param infoAboutVulnerabilities An underlying data provider
   *                                 that provides info about vulnerabilities.
   */
  public EstimateImpactUsingKnownVulnerabilities(
      InfoAboutVulnerabilities infoAboutVulnerabilities) {

    requireNonNull(infoAboutVulnerabilities, "Hey! The underlying data provider can't be null!");
    this.infoAboutVulnerabilities = infoAboutVulnerabilities;
  }

  @Override
  protected EstimateImpactUsingKnownVulnerabilities doUpdate(Subject subject, ValueSet values)
      throws IOException {

    infoAboutVulnerabilities.update(subject, values);
    Value<Vulnerabilities> vulnerabilities = values.of(VULNERABILITIES_IN_PROJECT).orElseThrow(
        () -> new IllegalStateException(
            "Oops! The underlying provider could not provide info about vulnerabilities!"));

    Impact worstConfidentialityImpact = null;
    Impact worstIntegrityImpact = null;
    Impact worstAvailableImpact = null;

    if (vulnerabilities.get().size() >= KNOWN_VULNERABILITIES_THRESHOLD) {
      logger.info("Found enough vulnerabilities for estimating potential CIA impact");
      for (Vulnerability vulnerability : vulnerabilities.get()) {
        if (!vulnerability.cvss().isPresent()) {
          continue;
        }
        worstConfidentialityImpact = set(
            worstConfidentialityImpact,
            vulnerability.cvss().get().confidentialityImpact().orElse(null));
        worstIntegrityImpact = set(
            worstIntegrityImpact,
            vulnerability.cvss().get().integrityImpact().orElse(null));
        worstAvailableImpact = set(
            worstAvailableImpact,
            vulnerability.cvss().get().availabilityImpact().orElse(null));
      }
    } else {
      logger.info("Not enough info about vulnerabilities to estimate potential CIA impact");
    }

    set(CONFIDENTIALITY_IMPACT, worstConfidentialityImpact, values);
    set(INTEGRITY_IMPACT, worstIntegrityImpact, values);
    set(AVAILABILITY_IMPACT, worstAvailableImpact, values);

    return this;
  }

  /**
   * Set a feature in a value set.
   *
   * @param feature The feature.
   * @param impact A value.
   * @param values The value set.
   */
  private static void set(Feature<Impact> feature, Impact impact, ValueSet values) {
    values.update(impact == null
        ? feature.unknown()
        : feature.value(impact).explain("Estimated using info about vulnerabilities"));
  }

  /**
   * Choose between two impacts.
   *
   * @param currentImpact The first impact.
   * @param cvssImpact The second impact.
   * @return The first impact if it is greater than the second impact, otherwise the second one.
   */
  private static Impact set(Impact currentImpact, Object cvssImpact) {
    Impact newImpact = null;
    if (V2.Impact.COMPLETE == cvssImpact || V3.Impact.HIGH == cvssImpact) {
      newImpact = Impact.HIGH;
    } else if (V2.Impact.PARTIAL == cvssImpact) {
      newImpact =  Impact.MEDIUM;
    } else if (V3.Impact.LOW == cvssImpact) {
      newImpact =  Impact.LOW;
    } else if (V2.Impact.NONE == cvssImpact || V3.Impact.NONE == cvssImpact) {
      newImpact =  Impact.NEGLIGIBLE;
    }

    if (newImpact == null) {
      return currentImpact;
    }

    if (currentImpact == null) {
      return newImpact;
    }

    return newImpact.compareTo(currentImpact) <= 0 ? currentImpact : newImpact;
  }

  @Override
  public boolean interactive() {
    return false;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(CONFIDENTIALITY_IMPACT, INTEGRITY_IMPACT, AVAILABILITY_IMPACT);
  }

  @Override
  public boolean supports(Subject subject) {
    return infoAboutVulnerabilities.supports(subject);
  }
}
