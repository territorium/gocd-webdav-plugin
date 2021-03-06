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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cd.go.artifact.Console;

/**
 * The {@link WebDAV} is an abstraction to the {@link Sardine} backend.
 */
public class WebDAV {

  private final String  url;
  private final Console console;
  private final Sardine sardine;

  /**
   * Constructs an instance of {@link WebDAV} without authentication.
   *
   * @param url
   * @param console
   */
  public WebDAV(String url, Console console) {
    this(url, console, null, null);
  }

  /**
   * Constructs an instance of {@link WebDAV} with authentication.
   *
   * @param url
   * @param console
   * @param username
   * @param password
   */
  public WebDAV(String url, Console console, String username, String password) {
    this.url = url;
    this.console = console;
    this.sardine = SardineFactory.begin(username, password);
  }

  /**
   * Get the URL for this connection.
   */
  public final String getUrl() {
    return url;
  }

  /**
   * Get the reference to the {@link Sardine} implementation.
   */
  protected final Sardine getSardine() {
    return sardine;
  }

  /**
   * Get the full URL to the resource.
   * 
   * @param path
   */
  protected final String getResource(String path) {
    return String.format("%s/%s", url, path);
  }

  /**
   * Return <code>true</code> if the resource already exists.
   *
   * @param path
   */
  public final boolean exists(String path) throws IOException {
    return getSardine().exists(getResource(path));
  }

  /**
   * Make all directories for the provided path.
   * 
   * @param path
   */
  public final void mkdirs(String path) throws IOException {
    List<String> paths = new ArrayList<>();
    for (String name : path.split("/")) {
      if (!name.contains(".")) {
        paths.add(name);
        String resource = getResource(String.join("/", paths));
        if (!getSardine().exists(resource)) {
          getSardine().createDirectory(resource);
        }
      }
    }
  }

  /**
   * Fetches (PULL) a resource from remote storage.
   *
   * @param path
   */
  public final InputStream pull(String path) throws IOException {
    return getSardine().get(getResource(path));
  }

  /**
   * Publish (PUSH) a file to the remote storage.
   *
   * @param path
   * @param file
   */
  public final void push(String path, File file) throws IOException {
    try (InputStream stream = new FileInputStream(file)) {
      console.info("Push file '%s' to '%s'", file, path);
      getSardine().put(getResource(path), stream);
    }
  }

  /**
   * Push all local files recursively to the remote storage.
   *
   * @param path
   * @param directory
   */
  public final void pushAll(String path, File directory) throws IOException {
    for (File file : WebDAV.listSorted(directory)) {
      String newPath = String.format("%s/%s", path, file.getName());
      if (file.isDirectory()) {
        if (!exists(newPath)) {
          console.info("Create remote directory '%s'", newPath);
          getSardine().createDirectory(getResource(newPath));
        }
        pushAll(newPath, file);
      } else {
        push(newPath, file);
      }
    }
  }

  private static final List<File> listSorted(File directory) {
    return Arrays.asList(directory.listFiles()).stream().sorted(DIRECTORY_FIRST).collect(Collectors.toList());
  }

  private static final Comparator<File> DIRECTORY_FIRST = new Comparator<File>() {

    @Override
    public int compare(File o1, File o2) {
      if (o1.isDirectory() && !o2.isDirectory())
        return -1;
      if (!o1.isDirectory() && o2.isDirectory())
        return 1;
      if (o1.isDirectory() && o2.isDirectory())
        return 0;

      // due bug in mod_dav on filename paths
      return Integer.valueOf(o1.getName().length()).compareTo(Integer.valueOf(o2.getName().length()));
    }
  };
}
