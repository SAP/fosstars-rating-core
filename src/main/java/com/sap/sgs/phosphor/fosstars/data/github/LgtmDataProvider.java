package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GitHub;

/**
 * The data provider gathers info about how a project uses static analysis with LGTM.
 * In particular, it tires to fill out the following features:
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}</li>
 * </ul>
 */
public class LgtmDataProvider extends AbstractGitHubDataProvider {

  /**
   * For parsing JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public LgtmDataProvider(GitHub github) {
    super(github);
  }

  @Override
  protected LgtmDataProvider doUpdate(GitHubProject project, ValueSet values) {
    JsonNode json = lgtmProjectInfo(project);
    values.update(usesLgtm(json));
    values.update(worstLgtmGrade(json));
    return this;
  }

  /**
   * Loads info about the project from LGTM.
   *
   * @return The info about the project.
   */
  private JsonNode lgtmProjectInfo(GitHubProject project) {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://lgtm.com/api/v1.0/projects/g/%s", project.path());
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return MAPPER.readTree(httpResponse.getEntity().getContent());
      }
    } catch (IOException e) {
      logger.warn("Couldn't fetch data from lgtm.com!", e);
      return MAPPER.createObjectNode();
    }
  }

  /**
   * Returns an HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Parses a JSON response from the LGTM API
   * and tries to figure out if the project has been analysed.
   *
   * @param json The JSON response from LGTM to be parsed.
   * @return A value of the {@link OssFeatures#USES_LGTM} feature.
   */
  private static Value<Boolean> usesLgtm(JsonNode json) {
    return new BooleanValue(USES_LGTM, json.has("id"));
  }

  /**
   * Parses a JSON response from the LGTM API
   * and tries to figure out the worst grade for the project.
   *
   * @param json The JSON response from LGTM to be parsed.
   * @return A value of the {@link OssFeatures#WORST_LGTM_GRADE} feature.
   */
  private static Value<LgtmGrade> worstLgtmGrade(JsonNode json) {
    LgtmGrade worstGrade = null;
    if (json.has("languages") && json.get("languages").isArray()) {
      for (JsonNode item : json.get("languages")) {
        if (!item.has("grade")) {
          continue;
        }

        LgtmGrade grade = LgtmGrade.parse(item.get("grade").asText());
        if (worstGrade == null || grade.compareTo(worstGrade) > 0) {
          worstGrade = grade;
        }
      }
    }

    if (worstGrade != null) {
      return WORST_LGTM_GRADE.value(worstGrade);
    }

    return UnknownValue.of(WORST_LGTM_GRADE);
  }

}
