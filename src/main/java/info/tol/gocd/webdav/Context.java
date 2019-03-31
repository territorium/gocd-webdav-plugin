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

package info.tol.gocd.webdav;

import java.util.Map;

public class Context {

  private final String              workingDir;
  private final Map<String, String> environmentVariables;

  @SuppressWarnings("unchecked")
  private Context(Map<String, ?> context) {
    workingDir = (String) context.get("workingDirectory");
    environmentVariables = (Map<String, String>) context.get("environmentVariables");
  }

  public String getWorkingDir() {
    return workingDir;
  }

  public String getEnvironmentVariable(String name) {
    return environmentVariables.get(name);
  }

  @SuppressWarnings("unchecked")
  public static Context of(Object object) {
    return new Context((Map<String, ?>) object);
  }
}
