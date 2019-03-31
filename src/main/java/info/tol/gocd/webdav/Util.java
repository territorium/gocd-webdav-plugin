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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

import java.util.Map;

public class Util {

  public static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().create();


  public static boolean hasValidEntry(Map<?, ?> map, String key) {
    return map.containsKey(key) && (((Map<?, ?>) map.get(key)).get("value") != null)
        && !((String) ((Map<?, ?>) map.get(key)).get("value")).trim().isEmpty();
  }


  /**
   * Print the whole stacktrace of the {@link Exception}.
   * 
   * @param console
   * @param exception
   */
  public static void printStackTrace(JobConsoleLogger console, Exception exception) {
    for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
      console.printLine("   at: " + stackTraceElement.toString());
    }
  }

  /**
   * Print the whole stacktrace of the {@link Exception}.
   * 
   * @param console
   * @param exception
   * @param message
   * @param arguments
   */
  public static void printStackTrace(JobConsoleLogger console, Exception exception, String message,
      Object... arguments) {
    console.printLine(String.format(message, arguments));
    for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
      console.printLine("   at: " + stackTraceElement.toString());
    }
  }


  /**
   * Creates a JSON Response.
   *
   * @param responseCode
   * @param response
   */
  public static GoPluginApiResponse toJSON(final int responseCode, Object response) {
    final String json = response == null ? null : new GsonBuilder().create().toJson(response);
    return new GoPluginApiResponse() {

      @Override
      public int responseCode() {
        return responseCode;
      }

      @Override
      public Map<String, String> responseHeaders() {
        return null;
      }

      @Override
      public String responseBody() {
        return json;
      }
    };
  }
}
