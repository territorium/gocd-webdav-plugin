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

package cd.go.artifact.webdav.handler;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import cd.go.artifact.Console;
import cd.go.artifact.RequestHandler;
import cd.go.artifact.webdav.WebDAV;
import cd.go.artifact.webdav.model.FetchRequest;
import cd.go.artifact.webdav.model.WebDavStoreConfig;

public class FetchArtifactHandler implements RequestHandler {

  private final Console      console;
  private final FetchRequest request;


  public FetchArtifactHandler(Console console, GoPluginApiRequest request) {
    this.console = console;
    // GsonBuilder builder = new GsonBuilder();
    this.request = FetchRequest.fromJSON(request.requestBody());
  }

  @Override
  public GoPluginApiResponse execute() {
    String workingDir = request.getAgentWorkingDir();
    WebDavStoreConfig storeConfig = request.getArtifactStoreConfig();
    String url = storeConfig.getUrl();

    try {
      Map<String, String> metadata = request.getMetadata();
      String relativePath = FetchArtifactHandler.validateLocation(metadata);
      String resource = String.format("%s/%s", url, relativePath);

      console.info("Retrieving file '%s' from WebDAV '%s'.", relativePath, url);

      WebDAV webDAV = new WebDAV(storeConfig.getUsername(), storeConfig.getPassword());
      try (InputStream reader = webDAV.getInputStream(resource)) {
        Path path = Paths.get(relativePath);
        File file = new File(workingDir, path.getName(path.getNameCount() - 1).toString());
        file.getParentFile().mkdirs();
        try (OutputStream writer = new BufferedOutputStream(new FileOutputStream(file))) {
          int read_length = -1;
          while ((read_length = reader.read()) != -1) {
            writer.write(read_length);
          }
          writer.flush();
        }
      }
      console.info("Source '%s' successfully pulled from WebDAV '%s'.", relativePath, url);

      return DefaultGoPluginApiResponse.success("");
    } catch (Exception e) {
      String message = String.format("Failed pull source file: %s", e);
      console.error(message);
      return DefaultGoPluginApiResponse.error(message);
    }
  }

  private static String validateLocation(Map<String, String> metadata) {
    if (metadata == null) {
      throw new RuntimeException(String.format(
          "Cannot fetch the source file from WebDAV: Invalid metadata received from the GoCD server. The artifact metadata is null."));
    }
    if (!metadata.containsKey("Location")) {
      throw new RuntimeException(String.format(
          "Cannot fetch the source file from WebDAV: Invalid metadata received from the GoCD server. The artifact metadata must contain the key `%s`.",
          "Source"));
    }
    return metadata.get("Location");
  }
}
