package com.sap.sgs.phosphor.fosstars.data.lgtm;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Objects;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * The data provider gathers info about how a project uses static analysis with LGTM.
 * In particular, it tires to fill out the following features:
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM}</li>
 * </ul>
 */
public class LgtmDataProvider implements DataProvider {

  /**
   * For parsing JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A GitHub organization of user name.
   */
  protected final String where;

  /**
   * A name of a repository.
   */
  protected final String name;

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization or a user name (can't be null).
   * @param name A name of a repository (can"t be null).
   */
  public LgtmDataProvider(String where, String name) {
    Objects.requireNonNull(where,
        "Oh no! You gave me a null instead of an organization or user name!");
    Objects.requireNonNull(name,
        "Oh no! You gave me a null instead of a project name!");

    this.where = where;
    this.name = name;
  }

  @Override
  public DataProvider update(ValueSet values) {
    JsonNode json = lgtmProjectInfo();
    values.update(usesLgtm(json));
    values.update(worstLgtmGrade(json));
    return this;
  }

  @Override
  public DataProvider set(UserCallback callback) {
    return this;
  }

  /**
   * Loads info about the project from LGTM.
   *
   * @return The info about the project.
   */
  private JsonNode lgtmProjectInfo() {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://lgtm.com/api/v1.0/projects/g/%s/%s", where, name);
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return MAPPER.readTree(httpResponse.getEntity().getContent());
      }
    } catch (IOException e) {
      System.out.printf("[x] Couldn't fetch data from lgtm.com!%n");
      System.out.printf("[x]     %s%n", e);
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

  /**
   * The main() method exists for demo and testing purposes.
   */
  public static void main(String... args) {
    String where = args.length > 0 ? args[0] : "apache";
    String name = args.length > 1 ? args[1] : "nifi";
    ValueHashSet values = new ValueHashSet();

    System.out.printf("[+] check project: https://github.com/%s/%s%n", where, name);
    new LgtmDataProvider("apache", "nifi").update(values);

    System.out.printf("[+] Uses LGTM:   %s%n",
        values.of(USES_LGTM).orElse(UnknownValue.of(USES_LGTM)));
    System.out.printf("[+] Worst grade: %s%n",
        values.of(WORST_LGTM_GRADE).orElse(UnknownValue.of(WORST_LGTM_GRADE)));
  }
}
