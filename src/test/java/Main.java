import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

import info.tol.gocd.webdav.WebDAVExecutor;

public class Main {

  private static final String URL = "[WEB_DAV_URL]";

  public static void main(String[] args) throws Exception {
    String file = "test/test.txt";

    Sardine sardine = SardineFactory.begin();

    WebDAVExecutor.createDirectories(sardine, URL, file);

    byte[] data = FileUtils.readFileToByteArray(new File("/tmp/test.txt"));
    sardine.put(String.format("%s/%s", URL, file), data);

    List<DavResource> resources = sardine.list(URL);
    for (DavResource res : resources) {
      System.out.println(res.getName());
    }
  }
}
