package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.data.github.NumberOfDependentProjectOnGitHub.numberFrom;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class NumberOfDependentProjectOnGitHubTest extends TestGitHubDataFetcherHolder {

  private static class TestProvider extends NumberOfDependentProjectOnGitHub {

    private String content;

    public TestProvider(GitHubDataFetcher fetcher) throws IOException {
      super(fetcher);
    }

    void set(String content) {
      this.content = content;
    }

    @Override
    Element loadFrontPageOf(GitHubProject project) {
      return Jsoup.parse(content);
    }
  }

  @Test
  public void testSupportedFeature() throws IOException {
    assertEquals(
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB,
        new NumberOfDependentProjectOnGitHub(fetcher).supportedFeature());
  }

  @Test
  public void testNumberFrom() {
    assertEquals(123, numberFrom("123"));
    assertEquals(1234567, numberFrom("1,234,567"));
    assertEquals(1234567, numberFrom("+ 1,234,567"));
    assertThrows(NumberFormatException.class, () -> numberFrom(""));
    assertThrows(NumberFormatException.class, () -> numberFrom("wrong"));
  }

  @Test
  public void testFetchValueFor() throws IOException {
    TestProvider provider = new TestProvider(fetcher);

    provider.set("<a href=\"/not/interesting\"><span title=\"42\"/></a>");
    Value<Integer> value = provider.fetchValueFor(PROJECT);
    assertTrue(value.isUnknown());

    provider.set(
        "<a href=\"/test/project/network/dependents?package_id=xyz\">nothing</a>");
    value = provider.fetchValueFor(PROJECT);
    assertTrue(value.isUnknown());

    provider.set(
        "<h2 class=\"h4 mb-3\">\n"
            + "<a href=\"/test/project/network/dependents?package_id=xyz\" "
            + "data-view-component=\"true\" class=\"Link--primary no-underline\">\n"
            + "Used by "
            + "<span title=\"423,038\" data-view-component=\"true\" class=\"Counter\">423k</span>\n"
            + "</a>"
            + "</h2>");
    value = provider.fetchValueFor(PROJECT);
    assertFalse(value.isUnknown());
    assertEquals(423038, (int) value.get());

    provider.set(
        "<a class=\"d-flex flex-items-center\" "
            + "href=\"/test/project/network/dependents?package_id=xyz\">\n"
            + "<ul class=\"hx_flex-avatar-stack list-style-none min-width-0\">\n"
            + "  <li class=\"hx_flex-avatar-stack-item\">\n"
            + "    <img class=\"avatar avatar-user\" height=\"32\" width=\"32\" />\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<span class=\"px-2 text-bold text-small no-wrap\">\n"
            + "  + 423,030\n"
            + "</span>\n"
            + "</a>");
    value = provider.fetchValueFor(PROJECT);
    assertFalse(value.isUnknown());
    assertEquals(423030, (int) value.get());
  }
}