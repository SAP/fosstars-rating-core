package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.nvd.Matcher;
import com.sap.oss.phosphor.fosstars.nvd.data.Affects;
import com.sap.oss.phosphor.fosstars.nvd.data.CVE;
import com.sap.oss.phosphor.fosstars.nvd.data.Configurations;
import com.sap.oss.phosphor.fosstars.nvd.data.CpeMatch;
import com.sap.oss.phosphor.fosstars.nvd.data.CpeUri;
import com.sap.oss.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.oss.phosphor.fosstars.nvd.data.Node;
import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.oss.phosphor.fosstars.nvd.data.ProductData;
import com.sap.oss.phosphor.fosstars.nvd.data.ReferenceLink;
import com.sap.oss.phosphor.fosstars.nvd.data.References;
import com.sap.oss.phosphor.fosstars.nvd.data.Vendor;
import com.sap.oss.phosphor.fosstars.nvd.data.VendorData;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data matcher to search for entries in NVD for a GitHub project.
 */
public class NvdEntryMatcher implements Matcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(NvdEntryMatcher.class);

  /**
   * The threshold for the {@link JaroWinklerSimilarity} check.
   */
  private static final double JWS_METRIC_THRESHOLD = 0.92;

  /**
   * The threshold for the {@link LongestCommonSubsequence} check.
   */
  private static final double LCS_METRIC_THRESHOLD = 0.875;

  /**
   * Instance of {@link LongestCommonSubsequence}.
   */
  private static final LongestCommonSubsequence LCS = new LongestCommonSubsequence();

  /**
   * Instance of {@link JaroWinklerSimilarity}.
   */
  private static final JaroWinklerSimilarity JWS = new JaroWinklerSimilarity();

  /**
   * A project to be checked.
   */
  private final GitHubProject project;

  /**
   * A black list of words, which should not be present when checking reference URLs.
   */
  private static final List<String> STOP_WORDS = Collections.singletonList("wiki");

  /**
   * Creates a new matcher for a project.
   *
   * @param project The project.
   * @return The new matcher.
   */
  public static NvdEntryMatcher entriesFor(GitHubProject project) {
    return new NvdEntryMatcher(project);
  }

  /**
   * Initializes a new matcher.
   *
   * @param project A project to be checked.
   */
  private NvdEntryMatcher(GitHubProject project) {
    this.project = Objects.requireNonNull(project, "Null is not a project!");
  }

  @Override
  public boolean match(NvdEntry entry) {
    Objects.requireNonNull(entry, "NVD entry can't be null!");

    Configurations configurations = entry.getConfigurations(); 
    CVE cve = entry.getCve();

    if (match(configurations, cve, project)) {
      return true;
    }

    if (cve == null) {
      LOGGER.warn("No CVE in NVD entry");
      return false;
    }

    CveMetaData meta = cve.getCveDataMeta();
    if (meta == null) {
      LOGGER.warn("No metadata in NVD entry");
      return false;
    }

    String cveId = meta.getId();
    if (cveId == null) {
      LOGGER.warn("No CVE ID in NVD entry");
      return false;
    }

    return match(cve.getAffects(), project);
  }

  /**
   * Checks if {@link Configurations} match the project. Also checks if any
   * {@link ReferenceLink} from {@link CVE} refers to the project.
   * 
   * @param configurations The configuration to be checked.
   * @param cve The CVE to be checked.
   * @param project The project to be checked.
   * @return True if the configurations or the references match the project, false otherwise.
   */
  private static boolean match(Configurations configurations, CVE cve, GitHubProject project) {
    if (configurations == null || configurations.getNodes() == null) {
      return false;
    }

    boolean referenceMatch = matchReferences(cve, project);
    return parseNodes(configurations.getNodes(), referenceMatch, project);
  }

  /**
   * Checks if one of the entries in an {@link Affects} element matches a project.
   * 
   * @param affects The {@link Affects} element to be checked.
   * @param project The project.
   * @return Returns true if one of the entries in the {@link Affects} element matches the project,
   *         false otherwise.
   */
  private static boolean match(Affects affects, GitHubProject project) {
    if (affects == null) {
      return false;
    }

    Vendor cveVendor = affects.getVendor();
    if (cveVendor == null) {
      return false;
    }

    for (VendorData vendorData : cveVendor.getVendorData()) {
      if (match(vendorData, project)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if vendor data from an NVD entry matches a project.
   *
   * @param vendorData The vendor data.
   * @param project The project to be checked.
   * @return True if the vendor data matches the project, false otherwise.
   */
  private static boolean match(VendorData vendorData, GitHubProject project) {
    if (!match(project.organization().name(), vendorData.getVendorName())) {
      return false;
    }

    for (ProductData productData : vendorData.getProduct().getProductData()) {
      if (match(productData, project)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if product data from an NVD entry matches to a project.
   *
   * @param productData The product data.
   * @param project The project.
   * @return True if the product data matches the project, false otherwise.
   */
  private static boolean match(ProductData productData, GitHubProject project) {
    return match(project.name(), productData.getProductName());
  }

  /**
   * Checks if a string is similar to another one.
   *
   * @param one First string.
   * @param two Second string.
   * @return True if the strings are similar, false otherwise.
   */
  private static boolean match(String one, String two) {
    if (one.equalsIgnoreCase(two)) {
      return true;
    }

    return jaroWinklerSimilarityCheck(one, two) && longestCommonSubsequenceCheck(one, two);
  }

  /**
   * Checks if a {@link URI} matches a project.
   *
   * @param referenceUrl The {@link URI}.
   * @param project The project.
   * @return True if the input URL matches the project's URL, false otherwise.
   */
  static boolean match(URI referenceUrl, GitHubProject project) {
    return checkUrlHost(referenceUrl.getHost(), project)
        && checkUrlPath(referenceUrl.getPath(), project);
  }
  
  /**
   * Checks if one of the references in a {@link CVE} contains refers to a project.
   * 
   * @param cve The {@link CVE} to be checked.
   * @param project The project.
   * @return True if a reference to the project is found, false otherwise.
   */
  private static boolean matchReferences(CVE cve, GitHubProject project) {
    if (cve == null) {
      return false;
    }

    References references = cve.getReferences();
    if (references == null || references.getReferenceData() == null) {
      return false;
    }

    for (ReferenceLink reference : references.getReferenceData()) {
      Optional<URI> referenceUrl = reference.url();
      if (referenceUrl.isPresent() && match(referenceUrl.get(), project)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if two strings are similar by looking for the longest common sub-sequence.
   * 
   * @param one First string.
   * @param two Second string.
   * @return True if the strings are similar, false otherwise.
   * @see <a href="https://www.geeksforgeeks.org/longest-common-subsequence-dp-4/">Longest Common
   *      Subsequence Problem</a>
   */
  private static boolean longestCommonSubsequenceCheck(String one, String two) {
    return LCS.apply(one, two) >= (one.length() * LCS_METRIC_THRESHOLD);
  }

  /**
   * Checks if two strings are similar by calculating Jaro Winkler Similarity score.
   * 
   * @param one First string.
   * @param two Second string.
   * @return True if the strings are similar, false otherwise.
   * @see <a href="https://www.geeksforgeeks.org/jaro-and-jaro-winkler-similarity/">Jaro Winkler
   *      Similarity</a>
   */
  private static boolean jaroWinklerSimilarityCheck(String one, String two) {
    return JWS.apply(one, two) > JWS_METRIC_THRESHOLD;
  }

  /**
   * Checks if a {@link CpeMatch} matches a project.
   * 
   * @param cpeMatch The {@link CpeMatch} to be checked.
   * @param referenceMatch Indicates if there is a reference URL similar to project URL.
   * @param project The project.
   * @return True if the {@link CpeMatch} matches the project, false otherwise.
   */
  private static boolean projectCheck(CpeMatch cpeMatch, boolean referenceMatch,
      GitHubProject project) {

    Optional<CpeUri> cpeUri = cpeMatch.getCpeUri();
    if (!cpeUri.isPresent()) {
      return false;
    }

    boolean productMatch = match(cpeUri.get().getProduct(), project.name());
    boolean vendorMatch = match(cpeUri.get().getVendor(), project.organization().name());

    // check if product's name matches the project's name,
    // or at least one reference URL matches with the project's URL
    // and vendor matches with the organization as well
    return productMatch || (referenceMatch && vendorMatch);
  }

  /**
   * Checks if one of the nodes matches with a project.
   * 
   * @param nodes The nodes to be checked.
   * @param referenceMatch Indicates if there is a reference URL similar to project URL.
   * @param project The project.
   * @return True a matching node is found, false otherwise.
   */
  private static boolean parseNodes(List<Node> nodes, boolean referenceMatch,
      GitHubProject project) {

    if (nodes == null) {
      return false;
    }

    for (Node node : nodes) {
      if (node.getCpeMatches() == null) {
        continue;
      }

      for (CpeMatch cpeMatch : node.getCpeMatches()) {
        if (cpeMatch == null) {
          continue;
        }

        if (projectCheck(cpeMatch, referenceMatch, project)) {
          return true;
        }
      }

      if (parseNodes(node.getChildren(), referenceMatch, project)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if a host name matches with project's URL host.
   * 
   * @param host The host name.
   * @param project The project.
   * @return True if the host name matches with the project, false otherwise.
   */
  private static boolean checkUrlHost(String host, GitHubProject project) {
    return host.equals(project.scm().getHost());
  }

  /**
   * Checks if a path matches with project's URL path.
   *
   * @param path The path to be checked.
   * @param project The project.
   * @return True if the path matches with the project, false otherwise.
   */
  private static boolean checkUrlPath(String path, GitHubProject project) {
    String delimiter = "/";
    return path != null
        && checkSplitPath(StringUtils.stripStart(path, delimiter).split(delimiter), project);
  }

  /**
   * Checks if a path matches with project's URL path.
   *
   * @param path The path to be checked.
   * @param project The project.
   * @return True if the path matches with the project, false otherwise.
   */
  private static boolean checkSplitPath(String[] path, GitHubProject project) {
    return path.length > 2 
        && path[0].equals(project.organization().name())
        && path[1].equals(project.name()) 
        && notStopWord(path[2]);
  }

  /**
   * Check if a string is not a stop word.
   * 
   * @param word The string.
   * @return True if the string is not a stop word, false otherwise.
   */
  private static boolean notStopWord(String word) {
    return word != null && !STOP_WORDS.contains(word); 
  }
}
