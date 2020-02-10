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

import cd.go.artifact.webdav.annotation.Validatable;
import cd.go.artifact.webdav.utils.Util;

public abstract class ArtifactPlanConfig implements Validatable {

  abstract public String getSource();

  abstract public String getTarget();

  @Override
  public String toString() {
    return toJSON();
  }

  public static ArtifactPlanConfig fromJSON(String json) {
    return Util.GSON.fromJson(json, ArtifactPlanConfig.class);
  }
}
