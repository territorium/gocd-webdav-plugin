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

package cd.go.artifact.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The {@link Resources} is an utility to read resources.
 */
public class Resources {

  /**
   * Read the resource as string.
   *
   * @param resource
   */
  public static String asString(String resource) {
    return new String(asBytes(resource), StandardCharsets.UTF_8);
  }

  /**
   * Read the resource as byte array.
   *
   * @param resource
   */
  public static byte[] asBytes(String resource) {
    try (InputStream stream = Resources.class.getResourceAsStream(resource)) {
      return readStream(stream);
    } catch (IOException e) {
      throw new RuntimeException("Could not find resource " + resource, e);
    }
  }

  /**
   * Read the bytes from an {@link InputStream}.
   *
   * @param stream
   * @throws IOException
   */
  private static byte[] readStream(InputStream stream) throws IOException {
    int length;
    byte[] buffer = new byte[8192];

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    while ((length = stream.read(buffer)) != -1) {
      output.write(buffer, 0, length);
    }
    return output.toByteArray();
  }
}

