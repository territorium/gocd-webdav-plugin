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

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;


class ConsoleLogger implements Console {

  private final Logger                logger;
  private final GoApplicationAccessor accessor;

  private ConsoleLogger(Logger logger, GoApplicationAccessor accessor) {
    this.logger = logger;
    this.accessor = accessor;
  }

  public void info(String message, Object... arguments) {
    doLog(new ConsoleLogMessage(ConsoleLogger.LogLevel.INFO, message, arguments));
  }

  public void error(String message, Object... arguments) {
    doLog(new ConsoleLogMessage(ConsoleLogger.LogLevel.ERROR, message, arguments));
  }

  public void logStackTrace(Exception exception, String message, Object... arguments) {
    error(message, arguments);
    for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
      error("   at: %s", stackTraceElement.toString());
    }
  }

  private void doLog(ConsoleLogMessage message) {
    DefaultGoApiRequest request =
        new DefaultGoApiRequest(Global.SEND_CONSOLE_LOG, Global.API_VERSION, Global.PLUGIN_IDENTIFIER);
    request.setRequestBody(message.toJSON());

    GoApiResponse response = accessor.submit(request);
    if (response.responseCode() != DefaultGoApiResponse.SUCCESS_RESPONSE_CODE) {
      logger.error(String.format("Failed to submit console log: %s", response.responseBody()));
    }
  }

  public static Console of(Logger logger, GoApplicationAccessor accessor) {
    return new ConsoleLogger(logger, accessor);
  }

  static class ConsoleLogMessage {

    public LogLevel logLevel;
    public String   message;

    public ConsoleLogMessage(LogLevel logLevel, String message, Object... arguments) {
      this.logLevel = logLevel;
      this.message = (arguments.length == 0) ? message : String.format(message, arguments);
    }

    public String toJSON() {
      return new Gson().toJson(this);
    }
  }

  private enum LogLevel {
    INFO,
    ERROR
  }
}
