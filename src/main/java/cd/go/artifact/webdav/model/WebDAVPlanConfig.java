
package cd.go.artifact.webdav.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.Optional;

import cd.go.artifact.webdav.annotation.MetadataField;

/**
 * The {@link WebDAVPlanConfig} get plan config for WebDAV.
 * 
 * <pre>
 * {
 *   "configuration":{
 *     "Source": "",
 *     "Destination": "gocd/gocd-demo",
 *   },
 *   "id":"app-image",
 *   "storeId":"dockerhub"
 * }
 * </pre>
 */
public class WebDAVPlanConfig extends ArtifactPlanConfig {

  @Expose
  @SerializedName("Source")
  @MetadataField(key = "Source")
  private String source;

  @Expose
  @SerializedName("Destination")
  @MetadataField(key = "Destination")
  private String target;

  public WebDAVPlanConfig(String source, Optional<String> destination) {
    this.source = source;
    this.target = destination.orElse("");
  }

  @Override
  public String getSource() {
    return source;
  }

  @Override
  public String getTarget() {
    return target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WebDAVPlanConfig that = (WebDAVPlanConfig) o;
    return Objects.equals(source, that.source) && Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, target);
  }
}
