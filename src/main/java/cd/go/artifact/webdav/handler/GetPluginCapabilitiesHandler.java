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

package cd.go.artifact.webdav.handler;

import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import cd.go.artifact.webdav.utils.Util;

/**
 * The {@link GetPluginCapabilitiesHandler} is a request to the plugin to provide plugin
 * capabilities. Currently the plugin does not support any capabilities, so plugins should return an
 * empty JSON as a response. In the future, based on these capabilities GoCD would enable or disable
 * the plugin features for a user.
 * 
 * Server sends request an Empty request body.
 */
public class GetPluginCapabilitiesHandler {

  /**
   * The plugin is expected to return status 200 if it can understand the request, with an empty
   * response body
   */
  public GoPluginApiResponse execute() {
    Capabilities capabilities = new Capabilities();
    return DefaultGoPluginApiResponse.success(capabilities.toJSON());
  }

  public static class Capabilities {

    public Capabilities() {}

    public static Capabilities fromJSON(String json) {
      return Util.GSON.fromJson(json, Capabilities.class);
    }

    public String toJSON() {
      return Util.GSON.toJson(this);
    }
  }
}
