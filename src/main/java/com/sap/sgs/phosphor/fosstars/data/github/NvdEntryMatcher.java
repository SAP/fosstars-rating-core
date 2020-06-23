package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.nvd.Matcher;
import com.sap.sgs.phosphor.fosstars.nvd.data.Affects;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.Configurations;
import com.sap.sgs.phosphor.fosstars.nvd.data.CpeMatch;
import com.sap.sgs.phosphor.fosstars.nvd.data.CpeUri;
import com.sap.sgs.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Node;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ProductData;
import com.sap.sgs.phosphor.fosstars.nvd.data.ReferenceLink;
import com.sap.sgs.phosphor.fosstars.nvd.data.References;
import com.sap.sgs.phosphor.fosstars.nvd.data.Vendor;
import com.sap.sgs.phosphor.fosstars.nvd.data.VendorData;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.net.URI;
import java.util.Arrays;
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
  private static final List<String> STOP_WORDS = Arrays.asList("wiki");

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
   * Checks if one of the {@link Configurations} matches the project. Also checks if any
   * {@link ReferenceLink} from {@link CVE} refers to the project.
   * 
   * @param configurations of type {@link Configurations}.
   * @param cve of type {@link CVE}.
   * @param project of type {@link GitHubProject}.
   * @return True if one of the configurations or the references matches the project details, false
   *         otherwise.
   */
  private static boolean match(Configurations configurations, CVE cve, GitHubProject project) {
    if (configurations == null || configurations.getNodes() == null) {
      return false;
    }

    boolean referenceMatch = match(cve, project); 
    return parseNodes(configurations.getNodes(), referenceMatch, project);
  }

  /**
   * Returns true if one of the entries in an Affects element match the project, false otherwise.
   * 
   * @param affects of type {@link Affects}.
   * @param project of type {@link GitHubProject}.
   * @return True if one of the affects matches the project details, false otherwise.
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
   * Checks if vendor data from an NVD entry matches to the project's owner and name.
   *
   * @param vendorData The vendor data.
   * @param project of type {@link GitHubProject}.
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
   * Checks if product data from an NVD entry matches to the project's name.
   *
   * @param productData The product data.
   * @param project of type {@link GitHubProject}.
   * @return True if the product data matches to the project's name, false otherwise.
   */
  private static boolean match(ProductData productData, GitHubProject project) {
    return match(project.name(), productData.getProductName());
  }

  /**
   * Checks if a string is similar to another. It goes through a series of controlled validations to
   * confirm this.
   * 
   * @param String one.
   * @param String two.
   * @return True if the validations part of this method is satisfied. Otherwise False.
   */
  private static boolean match(String one, String two) {
    if (one.equalsIgnoreCase(two)) {
      return true;
    }

    return jaroWinklerSimilarityCheck(one, two) && longestCommonSubsequenceCheck(one, two);
  }
  
  /**
   * Checks if one of the references contain the project's URL.
   * 
   * @param cve The list of type {@link CVE}.
   * @param project of type {@link GitHubProject}.
   * @return True if the project's URL is found, false otherwise.
   * @see The method needs to be enhanced in
   *      <a href="https://github.com/SAP/fosstars-rating-core/issues/243">Improve Reference
   *      check</a>
   */
  private static boolean match(CVE cve, GitHubProject project) {
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
   * Checks if the referenceUrl of type {@link URI} matches with the project's URL.
   * 
   * @param referenceUrl of type {@link URI}.
   * @param project of type {@link GitHubProject}.
   * @return True if the input URL matches the project's URL. Otherwise false.
   */
  static boolean match(URI referenceUrl, GitHubProject project) {
    return checkUrlHost(referenceUrl.getHost(), project)
        && checkUrlPath(referenceUrl.getPath(), project);
  }

  /**
   * Find the Longest Common Subsequence found between two strings. Then, check if the subsequence
   * score is greater than a certain threshold of the first string.
   * 
   * @param String one.
   * @param String two.
   * @return True if the subsequence score is greater than the threshold, Otherwise False.
   * @see <a href="https://www.geeksforgeeks.org/longest-common-subsequence-dp-4/">Longest Common
   *      Subsequence Problem</a>
   */
  private static boolean longestCommonSubsequenceCheck(String one, String two) {
    return LCS.apply(one, two) >= (one.length() * LCS_METRIC_THRESHOLD);
  }

  /**
   * Find the Jaro Winkler Similarity score between two strings. The, check if the score is greater
   * than a certain threshold.
   * 
   * @param String one.
   * @param String two.
   * @return True if the score is greater than the threshold, Otherwise False.
   * @see <a href="https://www.geeksforgeeks.org/jaro-and-jaro-winkler-similarity/">Jaro Winkler
   *      Similarity</a>
   */
  private static boolean jaroWinklerSimilarityCheck(String one, String two) {
    return JWS.apply(one, two) > JWS_METRIC_THRESHOLD;
  }

  /**
   * Checks if one of the cpeXXUri contains the project's and organization's names. If the check is
   * a success, it returns true immediately. If it could not match then check if the list of
   * references contains any information.
   * 
   * @param cpeMatch of type {@link CpeMatch}.
   * @param referenceMatch Indicates if there is a reference URL similar to project URL.
   * @param project of type {@link GitHubProject}.
   * @return True if one of the cpeXXUri contains the project's name. Otherwise False.
   */
  private static boolean projectCheck(CpeMatch cpeMatch, boolean referenceMatch,
      GitHubProject project) {
    CpeUri cpeUri = cpeMatch.getCpeUri();

    boolean productMatch = match(cpeUri.getProduct(), project.name());
    boolean vendorMatch = match(cpeUri.getVendor(), project.organization().name());

    // Check if product's name matches the project's name. Otherwise at least one reference URL
    // matches with the project's URL only if there is a vendor match.
    return productMatch || (referenceMatch && vendorMatch);
  }

  /**
   * This is a recursive method to parse through the list of nodes to find a match to the project's
   * and organization's names.
   * 
   * @param nodes List of type {@link Node}.
   * @param referenceMatch Indicates if there is a reference URL similar to project URL.
   * @param project of type {@link GitHubProject}.
   * @return True if there was a match. Otherwise False.
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
   * Checks if the input host matches with project's URL host.
   * 
   * @param host The host of the URL.
   * @param project of type {@link GitHubProject}.
   * @return True if the input host matches the project's URL host. Otherwise false.
   */
  private static boolean checkUrlHost(String host, GitHubProject project) {
    return host.equals(project.url().getHost());
  }

  /**
   * Matches the reference URL with the project's URL. The URL must not contain the pre-defined stop
   * words.
   * 
   * @param path The path of the URL.
   * @param project of type {@link GitHubProject}.
   * @return True if the project URL matches with the reference URL. Otherwise False.
   */
  private static boolean checkUrlPath(String path, GitHubProject project) {
    String delimiter = "/";
    return path != null
        && checkSplitPath(StringUtils.stripStart(path, delimiter).split(delimiter), project);
  }

  /**
   * Matches the split contents of the URL path. Each predefined position of the split path must
   * match with its respective counterpart. The path must not contain the pre-defined stop words.
   * 
   * @param path The split path of the URL.
   * @param project of type {@link GitHubProject}.
   * @return True if the reference path matches with the project path. Otherwise False.
   */
  private static boolean checkSplitPath(String[] path, GitHubProject project) {
    return path.length > 2 
        && path[0].equals(project.organization().name())
        && path[1].equals(project.name()) 
        && checkStopWords(path[2]);
  }

  /**
   * Check if the input word is a stop word.
   * 
   * @param word The input string.
   * @return True if the input string is not a stop word. Otherwise false.
   */
  private static boolean checkStopWords(String word) {
    return word != null && !STOP_WORDS.contains(word); 
  }
}