
package cd.go.artifact.webdav.utils;

import java.io.File;
import java.io.IOException;

import cd.go.artifact.webdav.WebDAV;

public class Main {

  // [cd.go.artifact.webdav] Found working dir(),
  // source(webapp2/target/smartio-(.*).war) and
  // target(nightly/develop/smartIO-Tomcat-$1-$BUILD.war)


  public static void main(String[] args) throws IOException {
    WebDAV dav = new WebDAV("https://apps.tol.info/dav");
    // for (DavResource r : s.list("https://apps.tol.info/dav/packages")) {
    // System.out.println(r.getName() + "\t= " + r.getContentLength());
    // }

    dav.pushAll("packages", new File("/data/smartIO/develop/installer/repo"));
  }
}
