package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class GitHubProjectTest {

  @Test
  public void testBasics() {
    GitHubProject project = new GitHubProject("org", "test");
    assertEquals("org", project.organization().name());
    assertEquals("test", project.name());
    assertEquals("https://github.com/org/test", project.scm().toString());
    assertEquals("pkg:github/org/test", project.purl());
    assertEquals("org/test", project.path());
  }

  @Test
  public void testJsonSerialization() throws IOException {
    GitHubOrganization apache = new GitHubOrganization("apache");
    GitHubProject project = new GitHubProject(apache, "nifi");
    project.set(
        new RatingValue(
            new ScoreValue(ExampleScores.SECURITY_SCORE_EXAMPLE),
            SecurityLabelExample.OKAY));
    byte[] bytes = Json.toBytes(project);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    GitHubProject clone = Json.read(bytes, GitHubProject.class);
    assertNotNull(clone);
    assertEquals(project, clone);
    assertEquals(project.hashCode(), clone.hashCode());
  }

  @Test
  public void testJsonSerializationWithList() throws IOException {
    GitHubProject org = new GitHubProject("org", "test");
    List<GitHubProject> list = Collections.singletonList(org);
    ObjectMapper mapper = Json.mapper();
    TypeReference<List<GitHubProject>> typeReference
        = new TypeReference<List<GitHubProject>>() {};
    byte[] bytes = mapper.writerFor(typeReference).writeValueAsBytes(list);
    List<GitHubProject> clone = mapper.readValue(bytes, typeReference);
    assertEquals(list, clone);
  }

  @Test
  public void testJsonSerializationWithUnknownFields() throws IOException {
    String content = "{\n"
        + "  \"type\" : \"GitHubProject\",\n"
        + "  \"organization\" : {\n"
        + "    \"type\" : \"GitHubOrganization\",\n"
        + "    \"name\" : \"apache\",\n"
        + "    \"ratingValue\" : null,\n"
        + "    \"ratingValueDate\" : null\n"
        + "  },\n"
        + "  \"name\" : \"nifi\","
        + "  \"ratingValue\" : null,"
        + "  \"ratingValueDate\" : null,"
        + "  \"extra\" : \"something\""
        + "}";
    GitHubProject project = Json.read(content.getBytes(), GitHubProject.class);
    assertEquals("apache", project.organization().name());
    assertEquals("nifi", project.name());
    assertFalse(project.ratingValue().isPresent());
    assertFalse(project.ratingValueDate().isPresent());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    GitHubProject project = new GitHubProject("org", "test");
    GitHubProject clone = Yaml.read(Yaml.toBytes(project), GitHubProject.class);
    assertEquals(project, clone);
  }

  @Test
  public void testYamlSerializationWithUnknownFields() throws IOException {
    String content = "---\n"
        + "type: \"GitHubProject\"\n"
        + "organization:\n"
        + "  type: \"GitHubOrganization\"\n"
        + "  name: \"org\"\n"
        + "  ratingValue: null\n"
        + "  ratingValueDate: null\n"
        + "name: \"test\"\n"
        + "ratingValue: null\n"
        + "ratingValueDate: null\n"
        + "extra: something\n";
    GitHubProject project = Yaml.read(content.getBytes(), GitHubProject.class);
    assertEquals("org", project.organization().name());
    assertEquals("test", project.name());
    assertFalse(project.ratingValue().isPresent());
    assertFalse(project.ratingValueDate().isPresent());
  }

  @Test
  public void testParsingFromUrl() throws IOException {
    GitHubProject parsed = GitHubProject.parse("http://github.com/SAP/fosstars-rating-core");
    assertEquals(parsed.name(), "fosstars-rating-core");
    assertEquals(parsed.organization().name(), "SAP");
    assertEquals(parsed.path(), "SAP/fosstars-rating-core");
  }

  @Test
  public void testParsingFromUrlEndsWithDotGit() throws IOException {
    GitHubProject parsed = GitHubProject.parse("http://github.com/SAP/fosstars-rating-core.git");
    assertEquals(parsed.name(), "fosstars-rating-core");
    assertEquals(parsed.organization().name(), "SAP");
    assertEquals(parsed.path(), "SAP/fosstars-rating-core");
  }

  @Test
  public void testEqualsAndHashCode() throws IOException {
    GitHubProject firstProject = GitHubProject.parse("https://github.com/test/first");
    GitHubProject theSameProject = GitHubProject.parse("https://github.com/test/first");
    assertTrue(firstProject.equals(theSameProject) && theSameProject.equals(firstProject));
    assertEquals(firstProject.hashCode(), theSameProject.hashCode());

    GitHubProject secondProject = new GitHubProject(new GitHubOrganization("test"), "second");
    assertNotEquals(firstProject, secondProject);

    GitHubProject thirdProject = new GitHubProject(new GitHubOrganization("another"), "first");
    assertNotEquals(firstProject, thirdProject);
  }
}
