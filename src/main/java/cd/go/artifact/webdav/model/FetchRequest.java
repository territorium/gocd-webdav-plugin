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

package cd.go.artifact.webdav.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import cd.go.artifact.webdav.utils.Util;

/**
 * The {@link FetchRequest} class.
 */
public class FetchRequest {

  @Expose
  @SerializedName("store_configuration")
  private WebDavStoreConfig   artifactStoreConfig;

  @Expose
  @SerializedName("artifact_metadata")
  private Map<String, String> metadata;

  @Expose
  @SerializedName("agent_working_directory")
  private String              agentWorkingDir;

  public FetchRequest() {}

  public FetchRequest(WebDavStoreConfig artifactStoreConfig, Map<String, String> metadata, String agentWorkingDir) {
    this.artifactStoreConfig = artifactStoreConfig;
    this.metadata = metadata;
    this.agentWorkingDir = agentWorkingDir;
  }

  public WebDavStoreConfig getArtifactStoreConfig() {
    return artifactStoreConfig;
  }

  public String getAgentWorkingDir() {
    return agentWorkingDir;
  }

  public Map<String, String> getMetadata() {
    return metadata;
  }

  public static FetchRequest fromJSON(String json) {
    return Util.GSON.fromJson(json, FetchRequest.class);
  }
}