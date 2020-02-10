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

import java.util.HashMap;
import java.util.Map;


/**
 * The {@link PublishResponse} provides the response for the server.
 * 
 * <pre>
 * {
 *   "metadata":{
 *     "image":"gocd/gocd-demo:v23",
 *     "digest":"sha256:f7840887b6f09f531935329a4ad1f6176866675873a8b3eed6a5894573da8247"
 *   }
 * }
 * </pre>
 */
public class PublishResponse {

  private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();


  @Expose
  @SerializedName("metadata")
  private Map<String, Object> metadata = new HashMap<>();

  /**
   * Constructs an instance of {@link PublishResponse}.
   */
  public PublishResponse() {}

  /**
   * Get the metadata for a publish response.
   */
  public final Map<String, Object> getMetadata() {
    return metadata;
  }

  /**
   * Add a metadata for a publish response.
   */
  public final void addMetadata(String key, Object value) {
    this.metadata.put(key, value);
  }

  /**
   * Get the JSON string for the {@link PublishResponse}.
   */
  public final String toJSON() {
    return GSON.toJson(this);
  }
}
