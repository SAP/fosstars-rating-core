package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_INNERSOURCE_TOPIC;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.util.Arrays;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class GithubInnerSourceTopicDataProviderTest {

  private GithubInnerSourceTopicDataProvider testee;
  private GitHubDataFetcher fetcher;

  @Before
  public void setUp() throws Exception {
    fetcher = Mockito.mock(GitHubDataFetcher.class);
    testee = new GithubInnerSourceTopicDataProvider(fetcher);
  }

  @Test
  public void testSupportedFeatures() throws Exception {
    Set<Feature<?>> features =  new GithubInnerSourceTopicDataProvider(fetcher).supportedFeatures();
    Assert.assertEquals(1, features.size());
    Assert.assertTrue(features.contains(HAS_INNERSOURCE_TOPIC));
  }

  @Test
  public void testNoDash() throws Exception {
    testTopic(true, "innersource");
  }

  @Test
  public void testDash() throws Exception {
    testTopic(true, "inner-source");
  }

  @Test
  public void testAny() throws Exception {
    testTopic(false, "any-topic");
  }

  @Test
  public void testNone() throws Exception {
    testTopic(false);
  }

  private void testTopic(boolean expectedValue, String... topics) throws Exception {
    GitHubProject project = Mockito.mock(GitHubProject.class);

    Mockito.when(fetcher.githubTopicsFor(project)).thenReturn(Arrays.asList(topics));

    ValueSet values = testee.fetchValuesFor(project);
    Assert.assertEquals(1, values.size());

    Value<?> value = values.iterator().next();

    Assert.assertEquals(expectedValue, value.get());
  }
}
