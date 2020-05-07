package com.sap.sgs.phosphor.fosstars.maven;

import java.io.IOException;
import java.io.InputStream;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * The class contains useful methods for working with Maven.
 */
public class MavenUtils {

  /**
   * Parses a pom.xml file.
   *
   * @param content The content of the pom.xml file.
   * @return A {@link Model} which represents the pom.xml file.
   * @throws IOException If something went wrong.
   */
  public static Model readModel(InputStream content) throws IOException {
    try {
      return new MavenXpp3Reader().read(content);
    } catch (XmlPullParserException e) {
      throw new IOException(e);
    }
  }

}
