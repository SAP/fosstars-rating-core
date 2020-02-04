package com.sap.sgs.phosphor.fosstars.data.lgtm;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORSE_LGTM_GRADE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class LgtmDataProviderTest {

  @Test
  public void testIfProjectExist() throws IOException {
    LgtmDataProvider provider = new LgtmDataProvider("org", "project");
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("LgtmProjectInfoReply.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM));
      assertTrue(values.of(USES_LGTM).isPresent());
      assertFalse(values.of(USES_LGTM).get().isUnknown());
      assertEquals(USES_LGTM.value(true), values.of(USES_LGTM).get());

      assertTrue(values.has(WORSE_LGTM_GRADE));
      assertTrue(values.of(WORSE_LGTM_GRADE).isPresent());
      assertFalse(values.of(WORSE_LGTM_GRADE).get().isUnknown());
      assertEquals(WORSE_LGTM_GRADE.value(LgtmGrade.C), values.of(WORSE_LGTM_GRADE).get());
    }
  }

  @Test
  public void testIfProjectDoesNotExist() throws IOException {
    LgtmDataProvider provider = new LgtmDataProvider("org", "project");
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("LgtmProjectDoesNotExistReply.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM));
      assertTrue(values.of(USES_LGTM).isPresent());
      assertFalse(values.of(USES_LGTM).get().isUnknown());
      assertEquals(USES_LGTM.value(false), values.of(USES_LGTM).get());

      assertTrue(values.has(WORSE_LGTM_GRADE));
      assertTrue(values.of(WORSE_LGTM_GRADE).isPresent());
      assertTrue(values.of(WORSE_LGTM_GRADE).get().isUnknown());
    }
  }

}