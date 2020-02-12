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

package cd.go.artifact.handler;


import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Base64;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import cd.go.artifact.RequestHandler;
import cd.go.artifact.util.Resources;

/**
 * The {@link GetPluginIconHandler} is expected to return the icon for the plugin, so as to make it
 * easy for users to identify the plugin.
 * 
 * The server will not provide a request body.
 * </pre>
 */
public class GetPluginIconHandler implements RequestHandler {

  private final String icon;
  private final String contentType;

  /**
   * Constructs an instance of {@link GetPluginIconHandler}.
   *
   * @param icon
   * @param contentType
   */
  public GetPluginIconHandler(String icon, String contentType) {
    this.icon = icon;
    this.contentType = contentType;
  }

  /**
   * The plugin is expected to return status 200 if it can understand the request.
   * 
   * <pre>
   * { "content_type": "image/svg+xml", "data": "PHN2ZyB2ZXJzaW9u..." }
   */
  @Override
  public GoPluginApiResponse execute() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add("content_type", contentType);
    builder.add("data", Base64.getEncoder().encodeToString(Resources.asBytes(icon)));
    return DefaultGoPluginApiResponse.success(builder.build().toString());
  }
}
