package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.date;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_INTRODUCED_DATE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Reference;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.nvd.data.DescriptionData;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ReferenceData;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.kohsuke.github.GitHub;

/**
 * This data provider looks for vulnerabilities in NVD.
 * TODO: This class doesn't talk to GitHub. Instead, it uses a local storage
 *       which contains info about known security teams.
 *       VulnerabilitiesFromNvd may be converted to a data provider.
 */
public class VulnerabilitiesFromNvd extends AbstractGitHubDataProvider {

  /**
   * An interface to the NVD database.
   */
  private final NVD nvd;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public VulnerabilitiesFromNvd(GitHub github) {
    super(github);
    this.nvd = new NVD();
  }

  @Override
  protected VulnerabilitiesFromNvd doUpdate(GitHubProject project, ValueSet values)
      throws IOException {

    logger.info("Looking for vulnerabilities in NVD ...");

    Value<Vulnerabilities> vulnerabilities = knownVulnerabilities(values);

    nvd.download();
    for (NvdEntry entry : nvd.find(project.organization().name(), project.name())) {
      vulnerabilities.get().add(vulnerabilityFrom(entry));
    }
    values.update(vulnerabilities);

    return this;
  }

  /**
   * Converts an {@link NvdEntry} to a {@link Vulnerability}.
   *
   * @param entry The {@link NvdEntry} to be converted.
   * @return An instance of {@link Vulnerability}.
   */
  private Vulnerability vulnerabilityFrom(NvdEntry entry) {
    String id = entry.getCve().getCveDataMeta().getID();

    String description = entry.getCve().getDescription().getDescriptionData().stream()
        .map(DescriptionData::getValue).collect(Collectors.joining("\n\n"));

    // TODO: use CVSSv3 if it's available
    CVSS cvss = CVSS.v2(entry.getImpact().getBaseMetricV2().getCvssV2().getBaseScore());

    List<Reference> references = new ArrayList<>();
    for (ReferenceData r : entry.getCve().getReferences().getReferenceData()) {
      try {
        references.add(new Reference(r.getName(), new URL(r.getUrl())));
      } catch (MalformedURLException e) {
        logger.warn("Could not parse a URL from a reference in NVD", e);
      }
    }

    // TODO: can we use CPEs to check if the issue has been fixed? what else can we use?
    Resolution resolution = Resolution.PATCHED;

    Date fixed = date(entry.getPublishedDate());

    return new Vulnerability(
        id, description, cvss, references, resolution, UNKNOWN_INTRODUCED_DATE, fixed);
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
}
