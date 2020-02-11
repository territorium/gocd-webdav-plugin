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

package cd.go.artifact.metadata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Metadata} class defines the metadata model. This usually is used for the metadata
 * request.
 * 
 * <pre>
 * [
 *   {
 *     "key": "Image",
 *     "metadata": {
 *       "required": true,
 *       "secure": false
 *     }
 *   },
 *   {
 *     "key": "Tag",
 *     "metadata": {
 *       "required": true,
 *       "secure": false
 *     }
 *   }
 * ]
 * </pre>
 */
public class Metadata {

  @Expose
  @SerializedName("key")
  private String key;


  @Expose
  @SerializedName("metadata")
  private MetadataField metadata;

  /**
   * Constructs an instance of {@link Metadata}.
   *
   * @param key
   * @param metadata
   */
  public Metadata(String key, MetadataField metadata) {
    this.key = key;
    this.metadata = metadata;
  }

  /**
   * Get the metadata key
   */
  public final String getKey() {
    return key;
  }

  /**
   * Get the metadata field type.
   */
  public final MetadataType getType() {
    return metadata.type();
  }

  /**
   * Return true if the metadata is required.
   */
  public final boolean isRequired() {
    return metadata.required();
  }

  /**
   * Return true if the metadata is secured.
   */
  public final boolean isSecure() {
    return metadata.secure();
  }

  /**
   * Get the list of {@link Metadata} from the class definition.
   *
   * @param clazz
   */
  public static List<Metadata> listOf(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    List<Metadata> list = new ArrayList<>();
    for (Field field : fields) {
      MetadataField metadata = field.getAnnotation(MetadataField.class);
      if (metadata != null) {
        list.add(new Metadata(metadata.key(), metadata));
      }
    }
    return list;
  }
}
