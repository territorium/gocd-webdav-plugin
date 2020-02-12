/*
 * Copyright (c) 2001-2019 Territorium Online Srl / TOL GmbH. All Rights Reserved.
 *
 * This file contains Original Code and/or Modifications of Original Code as defined in and that are
 * subject to the Territorium Online License Version 1.0. You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at http://www.tol.info/license/
 * and read it before using this file.
 *
 * The Original Code and all software distributed under the License are distributed on an 'AS IS'
 * basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND TERRITORIUM ONLINE HEREBY
 * DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT. Please see the License for
 * the specific language governing rights and limitations under the License.
 */

package cd.go.artifact.util;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * The {@link JsonBuilder} is an utility class to build JSON based data.
 */
public abstract class JsonBuilder {

  /**
   * Builds the instance as {@link JsonObject}.
   */
  public abstract JsonValue build();

  /**
   * Parses the values from a {@link JsonValue}.
   * 
   * @param json
   */
  public abstract void parse(JsonValue json);

  /**
   * Parses the values from a named attribute of the {@link JsonValue}.
   * 
   * @param json
   * @param nama
   */
  public final void parse(JsonObject json, String name) {
    parse(json.get(name));
  }

  /**
   * Build the JSON as string.
   */
  @Override
  public final String toString() {
    return build().toString();
  }


  /**
   * Builds a {@link JsonObject} from a text.
   * 
   * @param json
   */
  public static JsonObject of(String json) {
    return Json.createReader(new StringReader(json)).readObject();
  }
}
