package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
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
    MarkdownHeaderReference reference = Markdown.reference()
        .to(header)
        .withCaption(Markdown.string("Test"));
    assertEquals("[Test](#this-is-a-header)", reference.make());
  }
  
  @Test
  public void testSectionReference() {
    MarkdownHeader header = Markdown.header().level(1).withCaption("This is a header");
    MarkdownSection section = Markdown.section().with(header).thatContains("Text");
    MarkdownHeaderReference reference = Markdown.reference()
        .to(section)
        .withCaption(Markdown.string("Test"));
    assertEquals("[Test](#this-is-a-header)", reference.make());
  }

  @Test
  public void testMakeLink() throws MalformedURLException {
    assertEquals(
        "[link text](https://github.com/SAP/fosstars-rating-core)",
        Markdown.link()
            .to("https://github.com/SAP/fosstars-rating-core")
            .withCaption("link text")
            .make());
    assertEquals(
        "[link text](https://github.com/SAP/fosstars-rating-core)",
        Markdown.link()
            .to(new URL("https://github.com/SAP/fosstars-rating-core"))
            .withCaption("link text")
            .make());
    assertEquals(
        "[link text](../../local/path/to/file.md)",
        Markdown.link()
            .to("../../local/path/to/file.md")
            .withCaption("link text")
            .make());
  }

  @Test
  public void testStringFormat() {
    assertEquals(
        "This is a test string.",
        Markdown.string("This is a %s string.", "test").make());
    assertEquals("", Markdown.string("").make());
    assertEquals("%s", Markdown.string("%s").make());
    assertEquals("%\"", Markdown.string("%\"").make());
  }

  @Test
  public void testBold() {
    assertEquals(
        "**This is a bold text.**",
        Markdown.bold("This is a bold text.").make());
    assertEquals(
        "This is a **bold** string.",
        Markdown.string("This is a %s string.", Markdown.bold("bold")).make());
  }

  @Test
  public void testTemplate() {
    assertEquals(
        "This is a test string.",
        Markdown.template("This is a %s string.", Markdown.string("test")).make());
  }
}
