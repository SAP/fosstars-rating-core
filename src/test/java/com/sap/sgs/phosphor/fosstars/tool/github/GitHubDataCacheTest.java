package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;

public class GitHubDataCacheTest {

  @Test
  public void testPut() throws IOException {
    GitHubDataCache<String> gitHubDataCache = new GitHubDataCache<String>();
    
    String test = "test1";
    GitHubProject project = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project, test);
    assertTrue(gitHubDataCache.size() == 1);
    assertTrue(gitHubDataCache.get(project).get().equals(test));

    GitHubProject project2 = new GitHubProject(new GitHubOrganization(test), test);
    test = "test2";
    gitHubDataCache.put(project2, test);
    assertTrue(gitHubDataCache.size() == 1);
    assertTrue(gitHubDataCache.get(project2).get().equals(test));

    test = "test3";
    GitHubProject project3 = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project3, test);
    assertTrue(gitHubDataCache.size() == 2);
    assertTrue(gitHubDataCache.get(project3).get().equals(test));

    test = "test4";
    GitHubProject project4 = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project4, test);
    assertTrue(gitHubDataCache.size() == 3);
    assertTrue(gitHubDataCache.get(project4).get().equals(test));

    test = "test5";
    GitHubProject project5 = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project5, test);
    assertTrue(gitHubDataCache.size() == 4);
    assertTrue(gitHubDataCache.get(project5).get().equals(test));

    test = "test6";
    GitHubProject project6 = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project6, test);
    assertTrue(gitHubDataCache.size() == 5);
    assertTrue(gitHubDataCache.get(project6).get().equals(test));

    test = "test7";
    GitHubProject project7 = new GitHubProject(new GitHubOrganization(test), test);
    gitHubDataCache.put(project7, test);
    assertTrue(gitHubDataCache.size() == 5);
    assertTrue(gitHubDataCache.get(project7).get().equals(test));
  }
}