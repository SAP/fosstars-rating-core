package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.HAS_EXPLANATION;
import static com.sap.oss.phosphor.fosstars.TestUtils.NO_EXPLANATION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.github.CodeScanAlertsInfo;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class UsesCodeScanTest  extends TestGitHubDataFetcherHolder {

  private static final String CODE_SCAN_RESPONSE_BODY_TEMPLATE
        = "{\n"
        + "  \"data\": {\n"
        + "    \"repository\": {\n"
        + "      \"codeScanAlerts\": {\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}";

  private static final String CODE_SCAN_RESPONSE_BODY_WITH_ERROR
        = "{\n"
        + "  \"errors\": [\n"
        + "    {\n"
        + "      \"type\": \"SOME_ERROR\",\n"
        + "      \"path\": [\n"
        + "        \"something\"\n"
        + "      ],\n"
        + "      \"locations\": [\n"
        + "        {\n"
        + "          \"line\": 1,\n"
        + "          \"column\": 1\n"
        + "        }\n"
        + "      ],\n"
        + "      \"message\": \"This is a error message.\"\n"
        + "    }\n"
        + "  ]\n"
        + "}";

  @Test
  public void testWithCodeScanAlerts() throws IOException {
    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);
    
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    when(client.execute(any(HttpGet.class)))
            .thenReturn(response)
            .thenThrow(new AssertionError("Only one GET request was expected!"));
    
    HttpEntity entity = mock(HttpEntity.class);
    when(entity.getContent()).thenReturn(
            IOUtils.toInputStream(
                String.format(CODE_SCAN_RESPONSE_BODY_TEMPLATE)));
    
    statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);
    
    response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(response.getEntity()).thenReturn(entity);
    
    when(client.execute(any(HttpPost.class)))
            .thenReturn(response)
            .thenThrow(new AssertionError("Only one POST request was expected!"));
    
    CodeScanAlertsInfo provider = spy(new CodeScanAlertsInfo(fetcher));  
    when(provider.httpClient()).thenReturn(client);
    
    GitHubProject project = new GitHubProject("test", "project");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, RUNS_CODEQL_SCANS, true, NO_EXPLANATION);
  }

  @Test
  public void testWithDisabledAlerts() throws IOException {
    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(404);
    
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    when(client.execute(any(HttpGet.class)))
    .thenReturn(response)
        .thenThrow(new AssertionError("Only one GET request was expected!"));
        
    HttpEntity entity = mock(HttpEntity.class);
    when(entity.getContent()).thenReturn(
            IOUtils.toInputStream(CODE_SCAN_RESPONSE_BODY_WITH_ERROR));
    
    statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);
    
    response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(response.getEntity()).thenReturn(entity);
    
    when(client.execute(any(HttpPost.class)))
            .thenReturn(response)
            .thenThrow(new AssertionError("Only one POST request was expected!"));

    CodeScanAlertsInfo provider = spy(new CodeScanAlertsInfo(fetcher));    
    when(provider.httpClient()).thenReturn(client);
    
    GitHubProject project = new GitHubProject("test", "project");
    ValueSet values = provider.fetchValuesFor(project);
    
    checkValue(values, RUNS_CODEQL_SCANS, false, HAS_EXPLANATION);
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected,
        Consumer<Value<Boolean>> additionalCheck) {

    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertEquals(expected, value.get());
    additionalCheck.accept(value);
  }
}
