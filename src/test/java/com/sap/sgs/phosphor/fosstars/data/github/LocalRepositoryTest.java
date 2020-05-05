package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

public class LocalRepositoryTest {

  @Test
  public void testSerialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    LocalRepository repository = new LocalRepository(
        Paths.get("."), new Date(), null);
    LocalRepository clone = mapper.readValue(
        mapper.writeValueAsBytes(repository), LocalRepository.class);
    assertEquals(repository.updated(), clone.updated());
    assertEquals(repository.path().toAbsolutePath(), clone.path().toAbsolutePath());

    repository = new LocalRepository(
        Paths.get("."), new Date(), mock(Repository.class));
    clone = mapper.readValue(
        mapper.writeValueAsBytes(repository), LocalRepository.class);
    assertEquals(repository.updated(), clone.updated());
    assertEquals(repository.path().toAbsolutePath(), clone.path().toAbsolutePath());
  }

}