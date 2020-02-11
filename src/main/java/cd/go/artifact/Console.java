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

package cd.go.artifact;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;

public interface Console {

  public void info(String message, Object... arguments);

  public void error(String message, Object... arguments);

  public void logStackTrace(Exception exception, String message, Object... arguments);

  public static ConsoleLogger of(String name, Logger logger, String version, GoPluginIdentifier identifier,
      GoApplicationAccessor accessor) {
    return new ConsoleLogger(name, logger, version, identifier, accessor);
  }

}
