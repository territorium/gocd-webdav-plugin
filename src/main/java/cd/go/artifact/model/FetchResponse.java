/*
 * Copyright (c) 2001-2019 Territorium Online Srl / TOL GmbH. All Rights Reserved.
 *
 * This file contains Original Code and/or Modifications of Original Code as defined in and that are
 * subject to the Territorium Online License Version 1.0. You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at http://www.tol.info/license/
 * and read it before using this file.
 *
 * The Original Code and all software distributed under the License are distributed on an 'AS IS'
 * basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND TERRITORIUM ONLINE HEREBY
 * DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT. Please see the License for
 * the specific language governing rights and limitations under the License.
 */

package cd.go.artifact.model;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import cd.go.artifact.ArtifactMessage;
import cd.go.artifact.util.JsonBuilder;

/**
 * The plugin can respond with a list of environment variables which will be set on the job, for all
 * further tasks in that job to use. It can also return an empty list ([]). Existing environment
 * variables will be overridden, if they are returned as a part of this response.
 * 
 * <pre>
 * [
 *   {
 *     "name": "IMAGE_ID",
 *     "value": "image1/v23",
 *     "secure": false
 *   },
 *   {
 *     "name": "MY_SECRET_ENV_VAR",
 *     "value": "some-password",
 *     "secure": true
 *   }
]
 * </pre>
 */
public class FetchResponse extends JsonBuilder {

  private final Map<String, Value> values = new HashMap<>();

  /**
   * Set a new environment value.
   *
   * @param name
   * @param value
   */
  public final void setValue(String name, String value) {
    setValue(name, value, false);
  }

  /**
   * Set a new environment value.
   *
   * @param name
   * @param value
   * @param secure
   */
  public final void setValue(String name, String value, boolean secure) {
    values.put(name, new Value(value, secure));
  }

  /**
   * Builds the instance as {@link JsonObject}.
   */
  public final JsonArray build() {
    JsonArrayBuilder array = Json.createArrayBuilder();
    for (String name : values.keySet()) {
      JsonObjectBuilder object = Json.createObjectBuilder();
      object.add(ArtifactMessage.ENV_NAME, name);
      object.add(ArtifactMessage.ENV_VALUE, values.get(name).value);
      object.add(ArtifactMessage.ENV_SECURE, values.get(name).secure);
      array.add(object);
    }
    return array.build();
  }

  /**
   * Parses the values from a {@link JsonObject}.
   * 
   * @param json
   */
  @Override
  public final void parse(JsonValue json) {
    JsonArray array = json.asJsonArray();
    for (int index = 0; index < array.size(); index++) {
      JsonObject object = array.getJsonObject(index);
      String name = object.getString(ArtifactMessage.ENV_NAME);
      String value = object.getString(ArtifactMessage.ENV_VALUE);
      boolean secure = object.getBoolean(ArtifactMessage.ENV_SECURE);
      values.put(name, new Value(value, secure));
    }
  }

  /**
   * The {@link Value} class.
   */
  private class Value {

    private final String  value;
    private final boolean secure;

    /**
     * Constructs an instance of {@link Value}.
     *
     * @param value
     * @param secure
     */
    private Value(String value, boolean secure) {
      this.value = value;
      this.secure = secure;
    }
  }
}