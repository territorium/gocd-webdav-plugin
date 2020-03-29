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

import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import cd.go.artifact.RequestHandler;

public class FetchArtifactMetadataHandler implements RequestHandler {

  public GoPluginApiResponse execute() {
    JsonArrayBuilder array = Json.createArrayBuilder();
    JsonObjectBuilder meta = Json.createObjectBuilder();
    meta.add("required", true);
    meta.add("secure", false);

    JsonObjectBuilder object = Json.createObjectBuilder();
    object.add("key", "target");
    object.add("metadata", meta);
    array.add(object);

    meta = Json.createObjectBuilder();
    meta.add("required", false);
    meta.add("secure", false);

    object = Json.createObjectBuilder();
    object.add("metadata", meta);
    array.add(object);

    return DefaultGoPluginApiResponse.success(array.build().toString());
  }
}

