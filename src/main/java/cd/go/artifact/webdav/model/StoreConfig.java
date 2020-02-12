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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import cd.go.artifact.util.JsonBuilder;

/**
 * The {@link StoreConfig} class defines the specific configuration of the store.
 */
public class StoreConfig extends JsonBuilder /* implements Validatable */ {

  private String url;
  private String username;
  private String password;

  /**
   * Get the WebDAV resource.
   */
  public final String getUrl() {
    return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
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
   * Builds the instance as {@link JsonObject}.
   */
  @Override
  public final JsonObject build() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add("URL", url);
    builder.add("Username", username);
    builder.add("Password", password);
    return builder.build();
  }

  /**
   * Parses the values from a {@link JsonObject}.
   * 
   * @param json
   */
  @Override
  public final void parse(JsonValue json) {
    JsonObject object = json.asJsonObject();
    this.url = object.getString("URL");
    this.username = object.getString("Username");
    this.password = object.getString("Password");
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
    if (!(o instanceof StoreConfig))
      return false;

    StoreConfig that = (StoreConfig) o;

    if (url != null ? !url.equals(that.url) : that.url != null)
      return false;
    if (username != null ? !username.equals(that.username) : that.username != null)
      return false;
    return password != null ? password.equals(that.password) : that.password == null;
  }
}
