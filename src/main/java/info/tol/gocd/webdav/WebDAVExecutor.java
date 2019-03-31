/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package info.tol.gocd.webdav;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class WebDAVExecutor {

  private final Context          context;
  private final JobConsoleLogger console;


  private final Sardine sardine;

  /**
   * Creates a new {@link WebDAVExecutor}.
   *
   * @param context
   * @param console
   */
  public WebDAVExecutor(Context context, JobConsoleLogger console) {
    this.context = context;
    this.console = console;
    sardine = WebDAVExecutor.createSardine(context);
  }

  /**
   * Get the {@link Context}.
   */
  protected final Context getContext() {
    return context;
  }

  /**
   * Get the {@link JobConsoleLogger}.
   */
  protected final JobConsoleLogger getConsole() {
    return console;
  }

  /**
   * Get the {@link Sardine}.
   */
  protected final Sardine getSardine() {
    return sardine;
  }


  /**
   * Executes the upload
   * 
   * @param config
   */
  public Result execute(Config config) {
    String url = getContext().getEnvironmentVariable("WEBDAV_URL");
    File file = new File(context.getWorkingDir(), config.getFilePath());
    String resource = String.format("%s/%s", url, config.getFileName());

    getConsole().printLine("Start WebDAV executor");
    try {
      createDirectories(url, config.getFileName());

      if (file.isFile()) {
        boolean uploaded = uploadFile(resource, file);
        return new Result(uploaded, "Artifact uploaded to WebDAV");
      } else {
        Arrays.asList(file.listFiles()).forEach(f -> uploadFiles(resource, f));
        return new Result(true, "Artifacts uploaded to WebDAV");
      }

    } catch (Exception e) {
      getConsole().printLine("Coudln't execute command: " + e);
      for (StackTraceElement stackTraceElement : e.getStackTrace()) {
        getConsole().printLine("   at: " + stackTraceElement.toString());
      }
      return new Result(false, "Failed to upload artifact to WebDAV. Check URL, artifact path...");
    } finally {
      getConsole().printLine("Stop WebDAV executor");
    }
  }

  /**
   * Upload file to WebDAV
   *
   * @param url
   * @param username
   * @param password
   * @param workingDir
   * @param config
   * @param console
   */
  protected final boolean uploadFile(String url, File file) {
    getConsole().printLine(String.format("Upload file %s", url));

    try (InputStream stream = new FileInputStream(file)) {
      byte[] data = IOUtils.toByteArray(stream);
      getSardine().put(url, data);
      return true;
    } catch (IOException e) {
      Util.printStackTrace(getConsole(), e, "Couldn't upload file %s", url);
    }
    return false;
  }


  /**
   * Upload all files recursively
   * 
   * @param url
   * @param file
   */
  protected void uploadFiles(String url, File file) {
    String resource = String.format("%s/%s", url, file.getName());
    if (file.isDirectory()) {
      createDirectory(resource);
      Arrays.asList(file.listFiles()).forEach(f -> uploadFiles(resource, f));
    } else {
      uploadFile(resource, file);
    }
  }

  /**
   * Create all directories recursively.
   *
   * @param url
   * @param path
   */
  protected final void createDirectories(String url, String path) {
    for (String name : path.split("/")) {
      if (!name.contains(".")) {
        url = String.format("%s/%s", url, name);
        createDirectory(url);
      }
    }
  }


  /**
   * Create a remote directory if it not exists.
   * 
   * @param resource
   */
  public boolean createDirectory(String resource) {
    try {
      if (!getSardine().exists(resource)) {
        getConsole().printLine("Create directory " + resource);
        getSardine().createDirectory(resource);
        return true;
      }
    } catch (IOException e) {
      Util.printStackTrace(getConsole(), e, "Couldn't create directory %s", resource);
    }
    return false;
  }

  /**
   * Create a new instance of {@link Sardine}.
   *
   * @param context
   */
  private static Sardine createSardine(Context context) {
    String user = context.getEnvironmentVariable("WEBDAV_USERNAME");
    String pass = context.getEnvironmentVariable("WEBDAV_PASSWORD");
    return ((user == null) || (pass == null)) ? SardineFactory.begin() : SardineFactory.begin(user, pass);
  }
}
