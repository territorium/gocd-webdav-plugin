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

package cd.go.artifact.webdav;

/**
 * Enumerable that represents one of the messages that the server sends to the plugin
 */
public interface ArtifactRequest {

  String REQUEST_GET_PLUGIN_ICON           = "cd.go.artifact.get-icon";
  String REQUEST_GET_PLUGIN_CAPABILITIES   = "cd.go.artifact.get-capabilities";

  String REQUEST_STORE_CONFIG_METADATA     = "cd.go.artifact.store.get-metadata";
  String REQUEST_STORE_CONFIG_VIEW         = "cd.go.artifact.store.get-view";
  String REQUEST_STORE_CONFIG_VALIDATE     = "cd.go.artifact.store.validate";

  String REQUEST_PUBLISH_ARTIFACT_METADATA = "cd.go.artifact.publish.get-metadata";
  String REQUEST_PUBLISH_ARTIFACT_VIEW     = "cd.go.artifact.publish.get-view";
  String REQUEST_PUBLISH_ARTIFACT_VALIDATE = "cd.go.artifact.publish.validate";

  String REQUEST_FETCH_ARTIFACT_METADATA   = "cd.go.artifact.fetch.get-metadata";
  String REQUEST_FETCH_ARTIFACT_VIEW       = "cd.go.artifact.fetch.get-view";
  String REQUEST_FETCH_ARTIFACT_VALIDATE   = "cd.go.artifact.fetch.validate";

  String REQUEST_PUBLISH_ARTIFACT          = "cd.go.artifact.publish-artifact";
  String REQUEST_FETCH_ARTIFACT            = "cd.go.artifact.fetch-artifact";
}
