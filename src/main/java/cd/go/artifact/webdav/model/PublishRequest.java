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

package cd.go.artifact.webdav.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import cd.go.artifact.webdav.utils.Util;

/**
 * 
 * The {@link PublishRequest} implements the data structure for a publish request.
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
public class PublishRequest {

  @Expose
  @SerializedName("agent_working_directory")
  private String              workingDir;

  @Expose
  @SerializedName("artifact_store")
  private ArtifactStore       artifactStore;

  @Expose
  @SerializedName("artifact_plan")
  private ArtifactPlan        artifactPlan;

  @Expose
  @SerializedName("environment_variables")
  private Map<String, String> environment;

  /**
   * Constructs an instance of {@link PublishRequest}.
   */
  public PublishRequest() {}

  /**
   * Constructs an instance of {@link PublishRequest}.
   *
   * @param artifactStore
   * @param artifactPlan
   * @param agentWorkingDir
   */
  public PublishRequest(ArtifactStore artifactStore, ArtifactPlan artifactPlan, String agentWorkingDir) {
    this.workingDir = agentWorkingDir;
    this.artifactStore = artifactStore;
    this.artifactPlan = artifactPlan;
  }

  /**
   * Get the agents working directory.
   */
  public final String getWorkingDir() {
    return workingDir;
  }

  /**
   * Get the related {@link ArtifactStore}.
   */
  public final ArtifactStore getArtifactStore() {
    return artifactStore;
  }

  /**
   * Get the {@link ArtifactPlan}.
   */
  public final ArtifactPlan getArtifactPlan() {
    return artifactPlan;
  }

  /**
   * Get the environment.
   */
  public final Map<String, String> getEnvironment() {
    return environment;
  }

  @Override
  public final int hashCode() {
    int result = workingDir != null ? workingDir.hashCode() : 0;
    result = 31 * result + (artifactStore != null ? artifactStore.hashCode() : 0);
    result = 31 * result + (artifactPlan != null ? artifactPlan.hashCode() : 0);
    return result;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PublishRequest))
      return false;

    PublishRequest that = (PublishRequest) o;

    if (workingDir != null ? !workingDir.equals(that.workingDir) : that.workingDir != null)
      return false;
    if (artifactStore != null ? !artifactStore.equals(that.artifactStore) : that.artifactStore != null)
      return false;
    return artifactPlan != null ? artifactPlan.equals(that.artifactPlan) : that.artifactPlan == null;
  }

  public static PublishRequest fromJSON(String json) {
    return Util.GSON.fromJson(json, PublishRequest.class);
  }
}
