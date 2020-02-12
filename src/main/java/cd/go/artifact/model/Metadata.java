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

package cd.go.artifact.model;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import cd.go.artifact.ArtifactMessage;
import cd.go.artifact.util.JsonBuilder;


/**
 * The {@link Metadata} provides the response for the server.
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
public class Metadata extends JsonBuilder {

  private Map<String, String> values = new HashMap<>();

  /**
   * Constructs an instance of {@link Metadata}.
   */
  public Metadata() {}

  /**
   * Get the metadata for a publish response.
   */
  public final Map<String, String> getMetadata() {
    return values;
  }

  /**
   * Add a metadata for a publish response.
   */
  public final void addMetadata(String key, String value) {
    this.values.put(key, value);
  }

  /**
   * Builds the JSON as string.
   */
  @Override
  public final JsonObject build() {
    JsonObjectBuilder metadata = Json.createObjectBuilder();
    for (String key : getMetadata().keySet()) {
      metadata.add(key, "" + getMetadata().get(key));
    }

    JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add(ArtifactMessage.METADATA, metadata);
    return builder.build();
  }

  /**
   * Builds a {@link Metadata} from a JSON object.
   * 
   * @param json
   */
  @Override
  public final void parse(JsonValue json) {
    JsonObject object = json.asJsonObject();
    for (String key : object.keySet()) {
      addMetadata(key, object.getString(key));
    }
  }
}
