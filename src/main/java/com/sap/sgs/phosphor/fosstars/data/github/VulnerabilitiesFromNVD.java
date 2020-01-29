package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.date;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_INTRODUCED_DATE;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Reference;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.nvd.data.DescriptionData;
import com.sap.sgs.phosphor.fosstars.nvd.data.NVDEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ReferenceData;
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
 */
public class VulnerabilitiesFromNVD extends AbstractGitHubDataProvider {

  /**
   * A list of vulnerabilities to be updated by this data provider.
   */
  private final Value<Vulnerabilities> vulnerabilities;

  /**
   * An interface to the NVD database.
   */
  private final NVD nvd;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param vulnerabilities A list of vulnerabilities to be updated by this data provider.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public VulnerabilitiesFromNVD(String where, String name, GitHub github, boolean mayTalk,
      Value<Vulnerabilities> vulnerabilities) {

    super(where, name, github, mayTalk);
    this.vulnerabilities = vulnerabilities;
    this.nvd = new NVD();
  }

  @Override
  public Value<Vulnerabilities> get(UserCallback callback) throws IOException {
    System.out.println("[+] Looking for vulnerabilities in NVD ...");
    nvd.download();
    for (NVDEntry entry : nvd.find(where, name)) {
      vulnerabilities.get().add(vulnerabilityFrom(entry));
    }
    return vulnerabilities;
  }

  /**
   * Converts an {@link NVDEntry} to a {@link Vulnerability}.
   *
   * @param entry The {@link NVDEntry} to be converted.
   * @return An instance of {@link Vulnerability}.
   */
  private static Vulnerability vulnerabilityFrom(NVDEntry entry) {
    String id = entry.getCve().getCVEDataMeta().getID();

    String description = entry.getCve().getDescription().getDescriptionData().stream()
        .map(DescriptionData::getValue).collect(Collectors.joining("\n\n"));

    // TODO: use CVSSv3 if it's available
    CVSS cvss = CVSS.v2(entry.getImpact().getBaseMetricV2().getCvssV2().getBaseScore());

    List<Reference> references = new ArrayList<>();
    for (ReferenceData r : entry.getCve().getReferences().getReferenceData()) {
      try {
        references.add(new Reference(r.getName(), new URL(r.getUrl())));
      } catch (MalformedURLException e) {
        System.out.printf("[!] Could not parse a URL from a reference in NVD: %s%n", e);
      }
    }

    // TODO: can we use CPEs to check if the issue has been fixed? what else can we use?
    Resolution resolution = Resolution.PATCHED;

    Date fixed = date(entry.getPublishedDate());

    return new Vulnerability(id, description, cvss, references, resolution, UNKNOWN_INTRODUCED_DATE,
        fixed);
  }

}
