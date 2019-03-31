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

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GoCD plugin for WevDAV
 */
@Extension
public class WebDAVPlugin implements GoPlugin {

  public static final String       EXTENSION_NAME         = "task";
  public static final List<String> SUPPORTED_API_VERSIONS = Arrays.asList("1.0");
  public static final Logger       LOGGER                 = Logger.getLoggerFor(WebDAVPlugin.class);


  public final static String REQUEST_CONFIGURATION   = "configuration";
  public final static String REQUEST_CONFIGURATION_2 = "go.plugin-settings.get-configuration";
  public final static String REQUEST_VALIDATION      = "validate";
  public final static String REQUEST_VALIDATION_2    = "go.plugin-settings.validate-configuration";
  public final static String REQUEST_TASK_VIEW       = "view";
  public final static String REQUEST_TASK_VIEW_2     = "go.plugin-settings.get-view";
  public final static String REQUEST_EXECUTION       = "execute";


  /**
   * The plugin identifier
   *
   * @see com.thoughtworks.go.plugin.api.GoPlugin#pluginIdentifier()
   */
  @Override
  public final GoPluginIdentifier pluginIdentifier() {
    return new GoPluginIdentifier(WebDAVPlugin.EXTENSION_NAME, WebDAVPlugin.SUPPORTED_API_VERSIONS);
  }

  /**
   * @see GoPlugin#initializeGoApplicationAccessor(GoApplicationAccessor)
   *
   * @param goApplicationAccessor
   */
  @Override
  public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
    // ignore
  }

  /**
   * Handles the request from GoCD Server
   *
   * @param goPluginApiRequest
   * @see GoPlugin#handle(GoPluginApiRequest)
   */
  @Override
  public GoPluginApiResponse handle(GoPluginApiRequest goPluginApiRequest) throws UnhandledRequestTypeException {
    switch (goPluginApiRequest.requestName()) {
      case REQUEST_CONFIGURATION:
      case REQUEST_CONFIGURATION_2:
        return handleConfiguration();

      case REQUEST_VALIDATION:
      case REQUEST_VALIDATION_2:
        return handleValidation(goPluginApiRequest);

      case REQUEST_TASK_VIEW:
      case REQUEST_TASK_VIEW_2:
        return handleView();

      case REQUEST_EXECUTION:
        try {
          return handleExecute(goPluginApiRequest);
        } catch (IOException e) {
          throw new UnhandledRequestTypeException("Couldn't find file to upload: " + e);
        }

      default:
    }
    return null;
  }

  /**
   * Handles the WebDAV plugin configuration.
   */
  protected final GoPluginApiResponse handleConfiguration() {
    HashMap<String, Object> config = new HashMap<>();
    HashMap<String, Object> filePath = new HashMap<>();
    filePath.put("display-order", "2");
    filePath.put("display-name", "File Path");
    filePath.put("required", true);
    config.put(Config.FILE_PATH_PROPERTY, filePath);

    HashMap<String, Object> fileName = new HashMap<>();
    fileName.put("display-order", "3");
    fileName.put("display-name", "File Name");
    fileName.put("required", true);
    config.put(Config.FILE_NAME_PROPERTY, fileName);
    return DefaultGoPluginApiResponse.success(Util.GSON.toJson(config));
  }

  /**
   * Validates the WebDAV plugin.
   *
   * @param request
   */
  protected final GoPluginApiResponse handleValidation(GoPluginApiRequest request) {
    Map<?, ?> configMap = (Map<?, ?>) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
    HashMap<String, String> errorMap = new HashMap<>();
    if (!Util.hasValidEntry(configMap, Config.FILE_PATH_PROPERTY)) {
      errorMap.put(Config.FILE_PATH_PROPERTY, "File Path cannot be empty");
    }
    if (!Util.hasValidEntry(configMap, Config.FILE_NAME_PROPERTY)) {
      errorMap.put(Config.FILE_NAME_PROPERTY, "File Name cannot be empty");
    }

    HashMap<String, Object> validationResult = new HashMap<>();
    validationResult.put("errors", errorMap);

    int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
    return new DefaultGoPluginApiResponse(responseCode, Util.GSON.toJson(validationResult));
  }

  /**
   * Validates the WebDAV plugin view.
   */
  protected final GoPluginApiResponse handleView() {
    HashMap<String, String> response = new HashMap<>();
    response.put("displayValue", "WebDAV File Upload");

    int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
    try {
      response.put("template", IOUtils.toString(getClass().getResourceAsStream("/task.template.html"), "UTF-8"));
      return new DefaultGoPluginApiResponse(responseCode, Util.GSON.toJson(response));
    } catch (IOException e) {
      String errorMessage = "Failed to find template: " + e.getMessage();
      response.put("exception", "Failed to find template: " + errorMessage);
      WebDAVPlugin.LOGGER.error(errorMessage, e);
    }

    return new DefaultGoPluginApiResponse(DefaultGoApiResponse.INTERNAL_ERROR, Util.GSON.toJson(response));
  }

  /**
   * Executes the WebDAV plugin.
   *
   * @param request
   */
  protected final GoPluginApiResponse handleExecute(GoPluginApiRequest request) throws IOException {
    Map<?, ?> executionRequest = (Map<?, ?>) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
    Config config = Config.of(executionRequest.get("config"));
    Context context = Context.of(executionRequest.get("context"));

    WebDAVExecutor executor = new WebDAVExecutor(context, JobConsoleLogger.getConsoleLogger());
    Result result = executor.execute(config);
    return new DefaultGoPluginApiResponse(result.responseCode(), Util.GSON.toJson(result.toMap()));
  }
}
