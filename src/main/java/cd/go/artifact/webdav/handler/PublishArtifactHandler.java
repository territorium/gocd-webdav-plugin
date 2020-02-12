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

package cd.go.artifact.webdav.handler;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cd.go.artifact.Console;
import cd.go.artifact.RequestHandler;
import cd.go.artifact.util.Build;
import cd.go.artifact.util.FileMapper;
import cd.go.artifact.webdav.WebDAV;
import cd.go.artifact.webdav.model.ArtifactPlanConfig;
import cd.go.artifact.webdav.model.PublishRequest;
import cd.go.artifact.webdav.model.PublishResponse;
import cd.go.artifact.webdav.model.WebDavStoreConfig;

/**
 * The {@link PublishArtifactHandler} is a request to the plugin to publish an
 * artifact to the specified artifact store.
 * 
 * The request body will contain the following JSON elements:
 * 
 * <pre>
 * {
 *   "environment_variables":{
 *     "GO_PIPELINE_NAME":"build",
 *     "GO_TRIGGER_USER":"admin",
 *     "FOO": "bar"
 *   },
 *   "artifact_plan":{
 *     "configuration":{
 *       "BuildFile":"",
 *       "Image":"gocd/gocd-demo",
 *       "Tag":"v${GO_PIPELINE_COUNTER}"
 *     },
 *     "id":"app-image",
 *     "storeId":"dockerhub"
 *   },
 *   "artifact_store":{
 *     "configuration":{
 *       "RegistryURL":"https://index.docker.io/v1/",
 *       "Username":"boohoo",
 *       "Password":"password"
 *     },
 *     "id":"dockerhub"
 *   },
 *   "agent_working_directory":"/Users/varshavs/gocd/agent/pipelines/build"}
 * </pre>
 */
public class PublishArtifactHandler implements RequestHandler {

  private final Console        console;
  private final PublishRequest request;


  /**
   * Constructs an instance of {@link PublishArtifactHandler}.
   *
   * @param console
   * @param request
   */
  public PublishArtifactHandler(Console console, GoPluginApiRequest request) {
    this.console = console;
    this.request = PublishRequest.fromJSON(request.requestBody());
  }

  /**
   * The plugin is expected to return a json as shown. This json is written into
   * a file called <plugin-id>.json on the agent and uploaded as a Build
   * Artifact to the GoCD server to a directory called
   * pluggable-artifact-metadata. This directory is never removed as part of
   * cleaning GoCD artifacts.
   * 
   * The plugin is expected to return status 200 if it can understand the
   * request.
   * 
   * <pre>
   * {
   *   "metadata":{
   *     "location":"gocd/gocd-demo:v23"
   *   }
   * }
   * </pre>
   */
  @Override
  public GoPluginApiResponse execute() {
    String workingDir = request.getWorkingDir();
    ArtifactPlanConfig planConfig = request.getArtifactPlan().getPlanConfig();
    WebDavStoreConfig storeConfig = request.getArtifactStore().getStoreConfig();

    String source = planConfig.getSource();
    String target = planConfig.getTarget();

    try {
      WebDAV webDav = new WebDAV(storeConfig.getUsername(), storeConfig.getPassword());
      if (!target.isEmpty()) {
        webDav.createDirectories(storeConfig.getUrl(), target);
      }

      Map<String, String> params = Collections.singletonMap("BUILD", Build.getBuildNumber());

      List<String> destinations = new ArrayList<>();
      for (FileMapper mapper : FileMapper.list(source, workingDir)) {
        String path = mapper.remap(target, params);
        destinations.add(path);
        if (mapper.getFile().isFile()) {
          webDav.uploadFile(storeConfig.getUrl(), path, mapper.getFile(), console);
        } else {
          webDav.createDirectories(storeConfig.getUrl(), path);
          for (File file : mapper.getFile().listFiles()) {
            String relativePath = String.format("%s/%s", path, file.getName());
            webDav.uploadFiles(storeConfig.getUrl(), relativePath, file, console);
          }
        }
      }

      console.info("Source file '%s' pushed to WebDAV '%s'.", source, storeConfig.getUrl());

      PublishResponse response = new PublishResponse();
      response.addMetadata("Location", destinations.get(0));
      return DefaultGoPluginApiResponse.success(response.toJSON());
    } catch (Exception e) {
      console.error("Failed to publish %s: %s", request.getArtifactPlan(), e);
      console.logStackTrace(e, "Failed to publish %s", request.getArtifactPlan());
      return DefaultGoPluginApiResponse
          .error(String.format("Failed to publish %s: %s", request.getArtifactPlan(), e.getMessage()));
    }
  }
}
