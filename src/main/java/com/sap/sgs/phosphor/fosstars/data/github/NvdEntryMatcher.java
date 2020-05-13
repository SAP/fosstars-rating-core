package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.nvd.Matcher;
import com.sap.sgs.phosphor.fosstars.nvd.data.Affects;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.Configurations;
import com.sap.sgs.phosphor.fosstars.nvd.data.CpeMatch;
import com.sap.sgs.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Node;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.nvd.data.ProductData;
import com.sap.sgs.phosphor.fosstars.nvd.data.Vendor;
import com.sap.sgs.phosphor.fosstars.nvd.data.VendorData;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.List;
import java.util.Objects;
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
   * The threshold for the project to be checked.
   */
  private static final double METRIC_THRESHOLD = 0.9;

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

    if (match(entry.getConfigurations())) {
      return true;
    }

    CVE cve = entry.getCve();
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

    return match(cve.getAffects());
  }

  /**
   * Returns true if one of the configurations matches the project, false otherwise.
   */
  private boolean match(Configurations configurations) {
    if (configurations == null || configurations.getNodes() == null) {
      return false;
    }

    return parseNodes(configurations.getNodes());
  }

  /**
   * Returns true if a string matches the project, false otherwise.
   */
  private boolean match(String string) {
    return match(project.name(), string.trim());
  }

  /**
   * Returns true if one of the entries in an Affects element match the project, false otherwise.
   */
  private boolean match(Affects affects) {
    if (affects == null) {
      return false;
    }

    Vendor cveVendor = affects.getVendor();
    if (cveVendor == null) {
      return false;
    }

    for (VendorData vendorData : cveVendor.getVendorData()) {
      if (match(vendorData)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if vendor data from an NVD entry matches to the project's owner and name.
   *
   * @param vendorData The vendor data.
   * @return True if the vendor data matches the project, false otherwise.
   */
  private boolean match(VendorData vendorData) {
    if (!match(project.organization().name(), vendorData.getVendorName())) {
      return false;
    }

    for (ProductData productData : vendorData.getProduct().getProductData()) {
      if (match(productData)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if product data from an NVD entry matches to the project's name.
   *
   * @param productData The produce data.
   * @return True if the product data matches to the project's name, false otherwise.
   */
  private boolean match(ProductData productData) {
    return match(project.name(), productData.getProductName());
  }

  /**
   * Checks if a string is similar to another. It goes through series of controlled validations to
   * confirm this.
   * 
   * @param String one.
   * @param String two.
   * @return True if the validations part of this method is satisfied. Otherwise False.
   */
  private boolean match(String one, String two) {
    if (one.equalsIgnoreCase(two)) {
      return true;
    }

    return jaroWinklerSimilarityCheck(one, two) && longestCommonSubsequenceCheck(one, two);
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
    return LCS.apply(one, two) >= (one.length() * METRIC_THRESHOLD);
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
    return JWS.apply(one, two) > METRIC_THRESHOLD;
  }

  /**
   * Checks if one of the cpeXXUri contains the project's name.
   * 
   * @param cpeMatch of type {@link CpeMatch}.
   * @return True if one of the cpeXXUri contains the project's name. Otherwise False.
   */
  private boolean cpeUriCheck(CpeMatch cpeMatch) {
    return match(productFrom23Uri(cpeMatch.getCpe23Uri()))
        || match(productFrom22Uri(cpeMatch.getCpe22Uri()));
  }

  /**
   * Returns product name from {@link CpeMatch#getCpe23Uri()}.
   */
  private static String productFrom23Uri(String cpe23Uri) {
    return stringAtIndex(cpe23Uri, ":", 4);
  }

  /**
   * Returns product name from {@link CpeMatch#getCpe22Uri()}.
   */
  private static String productFrom22Uri(String cpe22Uri) {
    return stringAtIndex(cpe22Uri, ":", 3);
  }

  /**
   * Returns the substring present at the index after the split by the given delimiter pattern.
   * 
   * @param content The String to be split.
   * @param pattern The delimiter to be used.
   * @param index The index of the sub-string which should be returned.
   * @return The sub-string if the index is valid and the content is not null. Otherwise returns an
   *         empty string.
   */
  private static String stringAtIndex(String content, String pattern, int index) {
    if (content != null) {
      String[] splitContent = content.split(pattern);
      if (splitContent.length > index) {
        return splitContent[index];
      }
    }

    return StringUtils.EMPTY;
  }


  /**
   * This is a recursive method to parse through the list of nodes to find a match to the project's
   * name.
   * 
   * @param nodes List of type {@link Node}.
   * @return True if there was a match. Otherwise False.
   */
  private boolean parseNodes(List<Node> nodes) {
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

        if (cpeUriCheck(cpeMatch)) {
          return true;
        }
      }

      if (parseNodes(node.getChildren())) {
        return true;
      }
    }
    return false;
  }
}