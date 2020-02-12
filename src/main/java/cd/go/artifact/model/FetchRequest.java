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

import java.util.Map;

import javax.json.JsonObject;

import cd.go.artifact.ArtifactMessage;
import cd.go.artifact.util.JsonBuilder;
import cd.go.artifact.webdav.model.StoreConfig;

/**
 * The {@link FetchRequest} is a request to the plugin to fetch an artifact from the specified
 * artifact store.
 * 
 * <pre>
 * {
 *   "fetch_artifact_configuration":{
 *     "image_name": "release-candidate"
 *   },
 *   "artifact_metadata":{
 *     "image":"gocd/gocd-demo:v23",
 *     "digest":"sha256:f7840887b6f09f531935329a4ad1f6176866675873a8b3eed6a5894573da8247"
 *   },
 *   "store_configuration":{
 *     "RegistryURL":"https://index.docker.io/v1/",
 *     "Username":"boohoo",
 *     "Password":"password"
 *   },
 *   "agent_working_directory":"/Users/varshavs/gocd/agent/pipelines/build"
 * }
 * </pre>
 */
public class FetchRequest {

  private String            agentWorkingDir;

  private final StoreConfig storeConfig = new StoreConfig();
  private final FetchConfig fetchConfig = new FetchConfig();
  private final Metadata    metadata    = new Metadata();


  public final StoreConfig getStoreConfig() {
    return storeConfig;
  }

  public final FetchConfig getFetchConfig() {
    return fetchConfig;
  }

  public final String getAgentWorkingDir() {
    return agentWorkingDir;
  }

  public final Map<String, String> getMetadata() {
    return metadata.getMetadata();
  }

  /**
   * Builds a {@link Metadata} from a JSON string.
   * 
   * @param text
   */
  public static FetchRequest of(String text) {
    JsonObject json = JsonBuilder.of(text);
    FetchRequest request = new FetchRequest();
    request.metadata.parse(json, ArtifactMessage.ARTIFACT_METADATA);
    request.storeConfig.parse(json, ArtifactMessage.STORE_CONFIGURATION);
    request.fetchConfig.parse(json, ArtifactMessage.FETCH_ARTIFACT_CONFIGURATION);
    request.agentWorkingDir = json.getString(ArtifactMessage.AGENT_WORKING_DIRECTORY);
    return request;
  }
}