package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.GroupedMarkdownElements;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.Markdown;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownElement;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownList;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownString;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class MarkdownBuilderTest {

  @Test
  public void testSimpleOrderedList() {
    List<MarkdownElement> elements = asList(
        Markdown.string("one"), Markdown.string("two"), Markdown.string("three"));
    MarkdownList list = Markdown.orderedListOf(elements);
    String expected =
              "1.  one\n"
            + "1.  two\n"
            + "1.  three\n";
    assertEquals(expected, list.make());
  }

  @Test
  public void testSimpleUnorderedList() {
    List<MarkdownElement> elements = asList(
        Markdown.string("one"), Markdown.string("two"), Markdown.string("three"));
    MarkdownList list = Markdown.unorderedListOf(elements);
    String expected =
              "*  one\n"
            + "*  two\n"
            + "*  three\n";
    assertEquals(expected, list.make());
  }

  @Test
  public void testNestedLists() {
    List<MarkdownElement> elements = new ArrayList<>();
    elements.add(Markdown.string("one"));

    MarkdownString two = Markdown.string("two");
    MarkdownList nestedList = Markdown.unorderedListOf(
        asList(Markdown.string("xyz"), Markdown.string("abc")));
    GroupedMarkdownElements group = Markdown.group(two, nestedList);
    elements.add(group);

    elements.add(Markdown.string("three"));

    MarkdownList list = Markdown.orderedListOf(elements);

    String expected =
              "1.  one\n"
            + "1.  two\n"
            + "    *  xyz\n"
            + "    *  abc\n"
            + "1.  three\n";
    assertEquals(expected, list.make());
  }
}
