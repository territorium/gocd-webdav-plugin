/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cd.go.artifact.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The {@link Archive} provides utilities to handle different kind of archives..
 */
public abstract class Archive {

  /**
   * Uncompress the provided ZIP-file.
   * 
   * @param file
   */
  public static LocalDateTime unzip(File file) throws IOException {
    String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
    File target = new File(file.getParentFile(), name);
    return Archive.unzip(file, target);
  }


  /**
   * Uncompress the provided ZIP-file to the target location
   * 
   * @param file
   * @param target
   */
  public static LocalDateTime unzip(File file, File target) throws IOException {
    LocalDateTime local = null;
    try (ZipInputStream stream = new ZipInputStream(new FileInputStream(file))) {
      ZipEntry entry = stream.getNextEntry();
      while (entry != null) {
        LocalDateTime date = Instant.ofEpochMilli(entry.getTime()).atOffset(ZoneOffset.UTC).toLocalDateTime();
        if (local == null || date.isAfter(local))
          local = date;

        File newFile = newFile(target, entry);
        if (entry.isDirectory()) {
          newFile.mkdirs();
        } else {
          newFile.getParentFile().mkdirs();
          writeToFile(newFile, stream);
        }
        stream.closeEntry();
        entry = stream.getNextEntry();
      }
    }
    return local;
  }

  private static File newFile(File target, ZipEntry entry) throws IOException {
    File file = new File(target, entry.getName());
    String targetPath = target.getCanonicalPath();
    String targetFilePath = file.getCanonicalPath();

    if (!targetFilePath.startsWith(targetPath + File.separator)) {
      throw new IOException("Entry is outside of the target dir: " + entry.getName());
    }
    return file;
  }

  /**
   * Write the {@link InputStream} to {@link File}
   *
   * @param file
   * @param stream
   */
  private static void writeToFile(File file, InputStream stream) throws IOException {
    byte[] buffer = new byte[4096];
    try (FileOutputStream output = new FileOutputStream(file)) {
      int len;
      while ((len = stream.read(buffer)) > 0) {
        output.write(buffer, 0, len);
      }
    }
  }
}
