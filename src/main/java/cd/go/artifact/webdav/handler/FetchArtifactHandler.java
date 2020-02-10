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

import static java.lang.String.format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Map;

import cd.go.artifact.webdav.Console;
import cd.go.artifact.webdav.RequestHandler;
import cd.go.artifact.webdav.WebDAV;
import cd.go.artifact.webdav.model.ArtifactStoreConfig;
import cd.go.artifact.webdav.utils.Util;

public class FetchArtifactHandler implements RequestHandler {

  private final Console      console;
  private final FetchRequest request;


  public FetchArtifactHandler(Console console, GoPluginApiRequest request) {
    this.console = console;
    this.request = FetchRequest.fromJSON(request.requestBody());
  }

  @Override
  public GoPluginApiResponse execute() {
    try {
      Map<String, String> artifactMap = request.getMetadata();
      validateMetadata(artifactMap);

      String workingDir = request.getAgentWorkingDir();
      String sourceFileToGet = artifactMap.get("Source");

      console.info("Retrieving file `%s` from WebDAV `%s`.", sourceFileToGet,
          request.getArtifactStoreConfig().getUrl());

      WebDAV webDav = new WebDAV(console, request.getArtifactStoreConfig().getUsername(),
          request.getArtifactStoreConfig().getPassword());
      String resource = String.format("%s/%s", request.getArtifactStoreConfig().getUrl(), sourceFileToGet);
      InputStream fileReader = webDav.getStream(resource);
      File outFile = new File(Paths.get(workingDir, sourceFileToGet).toString());
      OutputStream writer = new BufferedOutputStream(new FileOutputStream(outFile));

      int read_length = -1;

      while ((read_length = fileReader.read()) != -1) {
        writer.write(read_length);
      }

      writer.flush();
      writer.close();
      fileReader.close();

      console.info(String.format("Source `%s` successfully pulled from WebDAV `%s`.", sourceFileToGet,
          request.getArtifactStoreConfig().getUrl()));

      return DefaultGoPluginApiResponse.success("");
    } catch (Exception e) {
      final String message = format("Failed pull source file: %s", e);
      console.error(message);
      return DefaultGoPluginApiResponse.error(message);
    }
  }

  public void validateMetadata(Map<String, String> artifactMap) {
    if (artifactMap == null) {
      throw new RuntimeException(String.format(
          "Cannot fetch the source file from WebDAV: Invalid metadata received from the GoCD server. The artifact metadata is null."));
    }

    if (!artifactMap.containsKey("Source")) {
      throw new RuntimeException(String.format(
          "Cannot fetch the source file from WebDAV: Invalid metadata received from the GoCD server. The artifact metadata must contain the key `%s`.",
          "Source"));
    }
  }

  // TODO Diogomrorl: Maybe this can be moved to a separate file under model to keep coherence
  protected static class FetchRequest {

    @Expose
    @SerializedName("store_configuration")
    private ArtifactStoreConfig artifactStoreConfig;
    @Expose
    @SerializedName("artifact_metadata")
    private Map<String, String> metadata;

    @Expose
    @SerializedName("agent_working_directory")
    private String              agentWorkingDir;

    public FetchRequest() {}

    public FetchRequest(ArtifactStoreConfig artifactStoreConfig, Map<String, String> metadata, String agentWorkingDir) {
      this.artifactStoreConfig = artifactStoreConfig;
      this.metadata = metadata;
      this.agentWorkingDir = agentWorkingDir;
    }

    public ArtifactStoreConfig getArtifactStoreConfig() {
      return artifactStoreConfig;
    }

    public String getAgentWorkingDir() {
      return agentWorkingDir;
    }

    public Map<String, String> getMetadata() {
      return metadata;
    }

    public static FetchRequest fromJSON(String json) {
      return Util.GSON.fromJson(json, FetchRequest.class);
    }
  }
}
