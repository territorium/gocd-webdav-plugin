/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cd.go.artifact.webdav;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cd.go.artifact.Console;

public class WebDAV {

  private final Sardine sardine;

  public WebDAV(String username, String password) {
    this.sardine = SardineFactory.begin(username, password);;
  }

  protected final Sardine getSardine() {
    return sardine;
  }

  public final InputStream getInputStream(String url) throws IOException {
    return getSardine().get(url);
  }

  public final void uploadFile(String url, String path, File file, Console console) throws IOException {
    try (InputStream stream = new FileInputStream(file)) {
      console.info(String.format(">>> %s/%s", url, path));
      getSardine().put(String.format("%s/%s", url, path), IOUtils.toByteArray(stream));
    }
  }

  public final void uploadFiles(String url, String path, File file, Console console) throws IOException {
    if (file.isDirectory()) {
      String resource = String.format("%s/%s", path, file.getName());
      createDirectory(resource);
      for (File f : file.listFiles()) {
        uploadFiles(url, resource, f, console);
      }
    } else {
      uploadFile(url, path, file, console);
    }
  }

  /**
   * Create all directories recursively.
   *
   * @param url
   * @param path
   */
  public final void createDirectories(String url, String path) {
    for (String name : path.split("/")) {
      if (!name.contains(".")) {
        url = String.format("%s/%s", url, name);
        createDirectory(url);
      }
    }
  }

  private boolean createDirectory(String resource) {
    try {
      if (!getSardine().exists(resource)) {
        getSardine().createDirectory(resource);
        return true;
      }
    } catch (IOException e) {}
    return false;
  }
}
