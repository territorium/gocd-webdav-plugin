
package cd.go.artifact.webdav.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import cd.go.artifact.webdav.WebDAV;

public class Main {

  // [cd.go.artifact.webdav] Found working dir(),
  // source(webapp2/target/smartio-(.*).war) and
  // target(nightly/develop/smartIO-Tomcat-$1-$BUILD.war)


  public static void main(String[] args) throws IOException {

    Path path = Paths.get("nightly/develop/smartIO-Web-20.x.42-18303.zip");
    System.out.println(path.getName(path.getNameCount() - 1));
    //
    // WebDAV webDAV = new WebDAV("", "");
    // String resource = String.format("%s/%s", "https://apps.tol.info/dav",
    // "nightly/develop/smartIO-Web-20.x.42-.zip");
    // InputStream fileReader = webDAV.getInputStream(resource);
    // File outFile = new File("/tmp", "smartIO-Web-20.x.42-.zip");
    // OutputStream writer = new BufferedOutputStream(new FileOutputStream(outFile));
    //
    // int read_length = -1;
    //
    // while ((read_length = fileReader.read()) != -1) {
    // writer.write(read_length);
    // }
    //
    // writer.flush();
    // writer.close();
    // fileReader.close();
  }

}
