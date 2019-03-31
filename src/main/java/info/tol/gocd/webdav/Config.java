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

public class Config {

  public static final String FILE_PATH_PROPERTY = "FilePath";
  public static final String FILE_NAME_PROPERTY = "FileName";


  private final String filePath;
  private final String fileName;

  private Config(Map<?, ?> config) {
    filePath = getValue(config, Config.FILE_PATH_PROPERTY);
    fileName = getValue(config, Config.FILE_NAME_PROPERTY);
  }

  private String getValue(Map<?, ?> config, String property) {
    Map<?, ?> map = (Map<?, ?>) config.get(property);
    if (map == null) {
      return null;
    }
    return (String) map.get("value");
  }

  public String getFilePath() {
    return filePath;
  }

  public String getFileName() {
    return fileName;
  }

  public static Config of(Object config) {
    return new Config((Map<?, ?>) config);
  }
}
