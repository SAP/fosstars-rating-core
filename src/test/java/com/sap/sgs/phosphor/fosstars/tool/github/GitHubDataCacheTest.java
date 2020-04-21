package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GitHubDataCacheTest {

  @Test
  public void testPut() {
    GitHubDataCache<String> cache = new GitHubDataCache<>();
    
    String data = "data to be cached";
    GitHubProject project = new GitHubProject(new GitHubOrganization(data), data);
    cache.put(project, data);
    assertEquals(1, cache.size());
    String cached = cache.get(project).orElseThrow(RuntimeException::new);
    assertEquals(data, cached);

    // fill out the cache
    for (int i = 1, cacheSize = cache.size(); cacheSize < cache.maxSize(); cacheSize++, i++) {
      project = new GitHubProject(String.format("org%d", i), String.format("project%d", i));
      data = String.format("data%d", i);
      cache.put(project, data);
    }

    assertEquals(cache.size(), cache.maxSize());

    // try to add one more item
    project = new GitHubProject("one", "more");
    data = "extra data";
    cache.put(project, data);
    assertEquals(cache.size(), cache.maxSize());
    cached = cache.get(project).orElseThrow(RuntimeException::new);
    assertEquals(data, cached);
  }

  @Test
  public void testClear() {
    GitHubDataCache<String> cache = new GitHubDataCache<>();

    String data = "data to be cached";
    GitHubProject project = new GitHubProject(new GitHubOrganization(data), data);
    cache.put(project, data);
    assertEquals(1, cache.size());
    String cached = cache.get(project).orElseThrow(RuntimeException::new);
    assertEquals(data, cached);

    // fill out the cache
    for (int i = 1, cacheSize = cache.size(); cacheSize < cache.maxSize(); cacheSize++, i++) {
      project = new GitHubProject(String.format("org%d", i), String.format("project%d", i));
      data = String.format("data%d", i);
      cache.put(project, data);
    }

    assertEquals(cache.size(), cache.maxSize());

    cache.clear();
    assertEquals(0, cache.size());
  }
}