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

/**
 * 
 * The {@link ArtifactStore} class.
 * 
 * <pre>
 * <artifactStore id="dockerhub" pluginId="cd.go.artifact.docker.registry">
 *         <property>
 *           <key>RegistryURL</key>
 *           <value>https://index.docker.io/v1/</value>
 *         </property>
 *         <property>
 *           <key>Username</key>
 *           <value>boohoo</value>
 *         </property>
 *         <property>
 *           <key>Password</key>
 *           <value>password</value>
 *         </property>
 *       </artifactStore>
 * </pre>
 */
public class ArtifactStore {

  @Expose
  @SerializedName("id")
  private String              id;

  @Expose
  @SerializedName("configuration")
  private ArtifactStoreConfig storeConfig;

  /**
   * Constructs an instance of {@link ArtifactStore}.
   */
  public ArtifactStore() {}

  /**
   * Constructs an instance of {@link ArtifactStore}.
   */
  public ArtifactStore(String id, ArtifactStoreConfig storeConfig) {
    this.id = id;
    this.storeConfig = storeConfig;
  }

  /**
   * Get the id of the {@link ArtifactStoreConfig}
   */
  public final String getId() {
    return id;
  }

  /**
   * Get the {@link ArtifactStoreConfig}
   */
  public final ArtifactStoreConfig getStoreConfig() {
    return storeConfig;
  }

  /**
   * Returns a hash code value for the object.
   */
  @Override
  public final int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (storeConfig != null ? storeConfig.hashCode() : 0);
    return result;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public final boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ArtifactStore))
      return false;

    ArtifactStore that = (ArtifactStore) o;

    if (id != null ? !id.equals(that.id) : that.id != null)
      return false;
    return storeConfig != null ? storeConfig.equals(that.storeConfig) : that.storeConfig == null;
  }
}
