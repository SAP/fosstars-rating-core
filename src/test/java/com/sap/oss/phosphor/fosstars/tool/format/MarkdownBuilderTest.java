package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.GroupedMarkdownElements;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.Markdown;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownElement;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownHeader;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownHeaderReference;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownList;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter.MarkdownSection;
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

  @Test
  public void testHeaders() {
    assertEquals("# Test", Markdown.header().level(1).withCaption("Test").make());
    assertEquals("## Test", Markdown.header().level(2).withCaption("Test").make());
    assertEquals("### Test", Markdown.header().level(3).withCaption("Test").make());
  }

  @Test
  public void testSection() {
    MarkdownHeader header = Markdown.header().level(1).withCaption("Header");
    MarkdownSection section = Markdown.section().with(header).thatContains("Text");
    String expected =
              "# Header\n"
            + "\n"
            + "Text\n";
    assertEquals(expected, section.make());
  }

  @Test
  public void testChoice() {
    MarkdownString firstOption = Markdown.string("one");
    MarkdownString secondOption = Markdown.string("two");
    assertEquals(
        "one",
        Markdown.choose(firstOption).when(() -> true).otherwise(secondOption).make());
    assertEquals(
        "two",
        Markdown.choose(firstOption).when(() -> false).otherwise(secondOption).make());
  }

  @Test
  public void testFormattedRuleIdentifier() {
    assertEquals("**[rl-test-1]**", Markdown.rule(Markdown.string("rl-test-1")).make());
    assertEquals("", Markdown.rule(MarkdownString.EMPTY).make());
  }

  @Test
  public void testHeaderReference() {
    MarkdownHeader header = Markdown.header().level(1).withCaption("This is a header");
    MarkdownSection section = Markdown.section().with(header).thatContains("Text");
    MarkdownHeaderReference reference = Markdown.reference()
        .to(section)
        .withCaption(Markdown.string("Test"));
    assertEquals("[Test](#this-is-a-header)", reference.make());
  }
}
