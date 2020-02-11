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

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.annotation.Load;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.info.PluginContext;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Collections;
import java.util.Properties;

import cd.go.artifact.ArtifactRequest;
import cd.go.artifact.Console;
import cd.go.artifact.handler.GetPluginCapabilitiesHandler;
import cd.go.artifact.handler.GetPluginIconHandler;
import cd.go.artifact.handler.GetViewHandler;
import cd.go.artifact.webdav.handler.FetchArtifactHandler;
import cd.go.artifact.webdav.handler.FetchArtifactMetadataHandler;
import cd.go.artifact.webdav.handler.FetchArtifactValidationHandler;
import cd.go.artifact.webdav.handler.PublishArtifactHandler;
import cd.go.artifact.webdav.handler.PublishArtifactMetadataHandler;
import cd.go.artifact.webdav.handler.PublishArtifactValidationHandler;
import cd.go.artifact.webdav.handler.StoreConfigMetadataHandler;
import cd.go.artifact.webdav.handler.StoreConfigValidationHandler;
import cd.go.artifact.webdav.utils.Util;

@Extension
public class WebDAVPlugin implements GoPlugin {

  private static final String             EXTENSION         = "artifact";
  private static final String             VERSION           = "1.0";
  private static final GoPluginIdentifier PLUGIN_IDENTIFIER =
      new GoPluginIdentifier(EXTENSION, Collections.singletonList(VERSION));


  private static final Logger LOGGER      = Logger.getLoggerFor(WebDAVPlugin.class);
  private static final String LOGGER_NAME = "go.processor.artifact.console-log";


  private Console console;

  @Load
  public void onLoad(PluginContext ctx) {
    final Properties properties = Util.getPluginProperties();
    LOGGER.info(
        String.format("Loading plugin %s[%s].", properties.getProperty("name"), properties.getProperty("pluginId")));
  }

  @Override
  public GoPluginIdentifier pluginIdentifier() {
    return WebDAVPlugin.PLUGIN_IDENTIFIER;
  }

  @Override
  public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
    console = Console.of(WebDAVPlugin.LOGGER_NAME, WebDAVPlugin.LOGGER, WebDAVPlugin.VERSION,
        WebDAVPlugin.PLUGIN_IDENTIFIER, accessor);
  }

  @Override
  public GoPluginApiResponse handle(GoPluginApiRequest request) {
    try {
      switch (request.requestName()) {
        case ArtifactRequest.REQUEST_GET_PLUGIN_ICON:
          return new GetPluginIconHandler("/plugin-icon.png", "image/png").execute();
        case ArtifactRequest.REQUEST_GET_PLUGIN_CAPABILITIES:
          return new GetPluginCapabilitiesHandler().execute();

        case ArtifactRequest.REQUEST_STORE_CONFIG_METADATA:
          return new StoreConfigMetadataHandler().execute();
        case ArtifactRequest.REQUEST_STORE_CONFIG_VIEW:
          return new GetViewHandler("/artifact-store.template.html").execute();
        case ArtifactRequest.REQUEST_STORE_CONFIG_VALIDATE:
          return new StoreConfigValidationHandler(request).execute();

        case ArtifactRequest.REQUEST_FETCH_ARTIFACT_METADATA:
          return new FetchArtifactMetadataHandler().execute();
        case ArtifactRequest.REQUEST_FETCH_ARTIFACT_VIEW:
          return new GetViewHandler("/artifact-fetch.template.html").execute();
        case ArtifactRequest.REQUEST_FETCH_ARTIFACT_VALIDATE:
          return new FetchArtifactValidationHandler().execute();

        case ArtifactRequest.REQUEST_PUBLISH_ARTIFACT_METADATA:
          return new PublishArtifactMetadataHandler().execute();
        case ArtifactRequest.REQUEST_PUBLISH_ARTIFACT_VIEW:
          return new GetViewHandler("/artifact-publish.template.html").execute();
        case ArtifactRequest.REQUEST_PUBLISH_ARTIFACT_VALIDATE:
          return new PublishArtifactValidationHandler(request).execute();

        case ArtifactRequest.REQUEST_FETCH_ARTIFACT:
          return new FetchArtifactHandler(console, request).execute();
        case ArtifactRequest.REQUEST_PUBLISH_ARTIFACT:
          return new PublishArtifactHandler(console, request).execute();

        default:
          throw new UnhandledRequestTypeException(request.requestName());
      }
    } catch (Exception e) {
      LOGGER.error("Error while executing request " + request.requestName(), e);
      throw new RuntimeException(e);
    }
  }
}
