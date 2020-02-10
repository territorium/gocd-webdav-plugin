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
import java.util.Optional;

/**
 * The {@link ArtifactPlan} class.
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
public class ArtifactPlan {

  @Expose
  @SerializedName("id")
  private String             id;

  @Expose
  @SerializedName("storeId")
  private String             storeId;

  @Expose
  @SerializedName("configuration")
  private ArtifactPlanConfig planConfig;

  /**
   * Constructs an instance of {@link ArtifactPlan}.
   */
  public ArtifactPlan() {}

  /**
   * Constructs an instance of {@link ArtifactPlan}.
   *
   * @param id
   * @param storeId
   * @param sourceFile
   * @param destination
   */
  public ArtifactPlan(String id, String storeId, String sourceFile, Optional<String> destination) {
    this.id = id;
    this.storeId = storeId;
    this.planConfig = new WebDAVPlanConfig(sourceFile, destination);
  }

  /**
   * Get the {@link ArtifactPlan} id.
   */
  public final String getId() {
    return id;
  }

  /**
   * Get the id of the related {@link ArtifactStore}.
   */
  public final String getStoreId() {
    return storeId;
  }

  /**
   * Get the related {@link ArtifactStoreConfig}.
   */
  public final ArtifactPlanConfig getPlanConfig() {
    return planConfig;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (storeId != null ? storeId.hashCode() : 0);
    result = 31 * result + (planConfig != null ? planConfig.hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ArtifactPlan))
      return false;

    ArtifactPlan that = (ArtifactPlan) o;

    if (id != null ? !id.equals(that.id) : that.id != null)
      return false;
    if (storeId != null ? !storeId.equals(that.storeId) : that.storeId != null)
      return false;
    return planConfig != null ? planConfig.equals(that.planConfig) : that.planConfig == null;
  }

  @Override
  public String toString() {
    return String.format("Artifact[id=%s, storeId=%s, artifactPlanConfig=%s]", getId(), getStoreId(),
        planConfig.toString());
  }
}
