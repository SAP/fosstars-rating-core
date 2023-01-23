package com.sap.oss.phosphor.fosstars.data;

import static org.junit.Assert.assertFalse;

import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.InnerSourceRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.nvd.TestNVD;
import java.io.IOException;
import org.junit.Test;

public class DataProviderSelectorTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testProvidersForOssSecurityRating() throws IOException {
    DataProviderSelector selector = new DataProviderSelector(fetcher, new TestNVD());
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    assertFalse(selector.providersFor(rating).isEmpty());
    for (Feature<?> feature : rating.allFeatures()) {
      assertFalse(selector.providersFor(feature).isEmpty());
    }
  }

  @Test
  public void testProvidersForOssRulesOfPlayRating() throws IOException {
    DataProviderSelector selector = new DataProviderSelector(fetcher, new TestNVD());
    OssRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);
    assertFalse(selector.providersFor(rating).isEmpty());
    for (Feature<?> feature : rating.allFeatures()) {
      assertFalse(selector.providersFor(feature).isEmpty());
    }
  }

  @Test
  public void testProvidersForInnerSourceRulesOfPlayRating() throws IOException {
    DataProviderSelector selector = new DataProviderSelector(fetcher, new TestNVD());
    InnerSourceRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(InnerSourceRulesOfPlayRating.class);
    assertFalse(selector.providersFor(rating).isEmpty());
    for (Feature<?> feature : rating.allFeatures()) {
      assertFalse(selector.providersFor(feature).isEmpty());
    }
  }
}