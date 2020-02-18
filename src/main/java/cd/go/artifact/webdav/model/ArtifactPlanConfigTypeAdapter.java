
package cd.go.artifact.webdav.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Optional;

import cd.go.artifact.webdav.utils.Util;

public class ArtifactPlanConfigTypeAdapter
    implements JsonDeserializer<ArtifactPlanConfig>, JsonSerializer<ArtifactPlanConfig> {

  @Override
  public ArtifactPlanConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    if (isBuildFileConfig(jsonObject)) {
      return new WebDavPlanConfig(jsonObject.get("Source").getAsString(), parseDestination(jsonObject));
    } else {
      throw new JsonParseException("Ambiguous or unknown json. `Source` property must be specified.");
    }
  }

  private Optional<String> parseDestination(JsonObject jsonObject) {
    JsonElement destination = jsonObject.get("Destination");
    if (destination != null && Util.isNotBlank(destination.getAsString())) {
      return Optional.of(destination.getAsString());
    }
    return Optional.empty();
  }

  @Override
  public JsonElement serialize(ArtifactPlanConfig src, Type typeOfSrc, JsonSerializationContext context) {
    if (src instanceof WebDavPlanConfig) {
      return context.serialize(src, WebDavPlanConfig.class);
    }
    throw new JsonIOException("Unknown type of ArtifactPlanConfig");
  }

  private boolean isBuildFileConfig(JsonObject jsonObject) {
    return containsSourceFileProperty(jsonObject);
  }

  private boolean containsSourceFileProperty(JsonObject jsonObject) {
    return jsonObject.has("Source") && isPropertyNotBlank(jsonObject);
  }

  private boolean isPropertyNotBlank(JsonObject jsonObject) {
    try {
      JsonElement jsonElement = jsonObject.get("Source");
      return Util.isNotBlank(jsonElement.getAsString());
    } catch (UnsupportedOperationException e) {
      return false;
    }
  }
}
