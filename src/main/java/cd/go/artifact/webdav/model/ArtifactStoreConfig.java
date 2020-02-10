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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cd.go.artifact.webdav.annotation.MetadataField;
import cd.go.artifact.webdav.annotation.MetadataFieldAdapter;
import cd.go.artifact.webdav.annotation.Validatable;
import cd.go.artifact.webdav.utils.Util;

/**
 * The {@link ArtifactStoreConfig} class defines the specific configuration of the store.
 */
public class ArtifactStoreConfig implements Validatable {

  @Expose
  @SerializedName("URL")
  @MetadataField(key = "URL", required = true)
  private String url;

  @Expose
  @SerializedName("Username")
  @MetadataField(key = "Username")
  private String username;

  @Expose
  @SerializedName("Password")
  @MetadataField(key = "Password", secure = true)
  private String password;


  /**
   * Constructs an instance of {@link ArtifactStoreConfig}.
   */
  public ArtifactStoreConfig() {}

  /**
   * Constructs an instance of {@link ArtifactStoreConfig}.
   *
   * @param url
   * @param username
   * @param password
   */
  public ArtifactStoreConfig(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  /**
   * Get the WebDAV resource.
   */
  public final String getUrl() {
    return url;
  }

  /**
   * Get the WebDAV username.
   */
  public final String getUsername() {
    return username;
  }

  /**
   * Get the WebDAV password.
   */
  public final String getPassword() {
    return password;
  }

  /**
   * Returns a hash code value for the object.
   */
  @Override
  public final int hashCode() {
    int result = url != null ? url.hashCode() : 0;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public final boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ArtifactStoreConfig))
      return false;

    ArtifactStoreConfig that = (ArtifactStoreConfig) o;

    if (url != null ? !url.equals(that.url) : that.url != null)
      return false;
    if (username != null ? !username.equals(that.username) : that.username != null)
      return false;
    return password != null ? password.equals(that.password) : that.password == null;
  }


  public static ArtifactStoreConfig fromJSON(String json) {
    return Util.GSON.fromJson(json, ArtifactStoreConfig.class);
  }
}
