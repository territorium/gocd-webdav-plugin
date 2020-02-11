
package cd.go.artifact.metadata;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MetadataFieldAdapter implements JsonSerializer<MetadataField> {

  @Override
  public JsonElement serialize(MetadataField field, Type type, JsonSerializationContext context) {
    JsonObject json = new JsonObject();
    json.add("required", new JsonPrimitive(field.required()));
    json.add("secure", new JsonPrimitive(field.secure()));
    return json;
  }
}
