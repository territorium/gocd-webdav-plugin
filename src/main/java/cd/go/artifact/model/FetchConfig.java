
package cd.go.artifact.model;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import cd.go.artifact.util.JsonBuilder;

/**
 * The {@link FetchConfig} get plan config for WebDAV.
 * 
 * <pre>
 * {
 *   "fetch_artifact_configuration":{
 *     "target": ""
 *   },
 *   ...
 * }
 * </pre>
 */
public class FetchConfig extends JsonBuilder {


  private String target;
  private String unpack;

  public final String getTarget() {
    return target;
  }

  public final String getUnpack() {
    return unpack;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    FetchConfig that = (FetchConfig) o;
    return Objects.equals(target, that.target) && Objects.equals(unpack, that.unpack);
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, unpack);
  }

  /**
   * Builds the instance as {@link JsonObject}.
   */
  public final JsonValue build() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add("target", target);
    builder.add("unpack", unpack);
    return builder.build();
  }

  /**
   * Parses the values from a {@link JsonValue}.
   * 
   * @param json
   */
  public final void parse(JsonValue json) {
    JsonObject object = json.asJsonObject();
    target = object.getString("target");
    unpack = object.getString("unpack");
  }
}
