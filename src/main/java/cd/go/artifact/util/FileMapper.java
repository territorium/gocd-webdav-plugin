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

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link FileMapper} is a utility class that allows to list all files matching the provided
 * file pattern. The instances of {@link FileMapper} allow to re-map the name to another relative
 * path.
 */
public class FileMapper {

  private static final Pattern PARAMS = Pattern.compile("\\$(([0-9]+)|([a-zA-Z][a-zA-Z_0-9]*))");


  private final File         file;
  private final List<String> indexes;

  /**
   * Constructs an instance of {@link FileMapper}.
   *
   * @param File
   * @param groups
   */
  private FileMapper(File file, List<String> groups) {
    this.file = file;
    this.indexes = groups;
  }

  /**
   * Gets the {@link #path}.
   */
  public final File getFile() {
    return file;
  }

  /**
   * Replaces the indexed or named placeholder's with the the parameter values.
   *
   * @param target
   */
  public String remap(String target) {
    StringBuffer buffer = new StringBuffer();

    int offset = 0;
    Matcher matcher = PARAMS.matcher(target);
    while (matcher.find()) {
      int index = (matcher.group(2) == null) ? -1 : Integer.parseInt(matcher.group(2));
      buffer.append(target.substring(offset, matcher.start(1) - 1));
      if (index > 0 && index <= indexes.size()) {
        buffer.append(indexes.get(index - 1));
      }
      offset = matcher.end(1);
    }
    buffer.append(target.substring(offset, target.length()));
    return buffer.toString();
  }


  /**
   * Processes the input path from the offset to calculate the specific path and the groups.
   *
   * @param file
   * @param path
   * @param groups
   * @param mappings
   */
  private static void list(File directory, Path path, List<String> groups, List<FileMapper> mappings) {
    if (directory.isDirectory()) {
      String filepattern = path.getName(0).toString().replace('%', '*');
      Pattern pattern = Pattern.compile("^" + filepattern + "$");
      for (File file : directory.listFiles(new PatternFilter(pattern))) {
        List<String> values = new ArrayList<String>(groups);
        Matcher matcher = pattern.matcher(file.getName());
        if (matcher.find()) {
          for (int index = 0; index < matcher.groupCount(); index++) {
            values.add(matcher.group(index + 1));
          }
        }

        if (path.getNameCount() == 1) {
          mappings.add(new FileMapper(file, values));
        } else {
          list(file, path.subpath(1, path.getNameCount()), values, mappings);
        }
      }
    }
  }

  /**
   * Resolve the input pattern on the current working directory, to find all matching files.
   *
   * @param pattern
   */
  public static List<FileMapper> list(String pattern, String workingDir) {
    List<FileMapper> matches = new ArrayList<>();
    Path path = Paths.get(pattern);
    list(new File(workingDir), path, Collections.emptyList(), matches);
    return matches;
  }

  /**
   * The {@link PatternFilter} class implements a {@link FilenameFilter} using a {@link Pattern} to
   * find the valid files.
   */
  private static class PatternFilter implements FilenameFilter {

    private final Pattern pattern;

    /**
     * Constructs an instance of {@link PatternFilter}.
     *
     * @param pattern
     */
    private PatternFilter(Pattern pattern) {
      this.pattern = pattern;
    }

    /**
     * Tests if a specified file should be included in a file list.
     * 
     * @param dir
     * @param name
     */
    @Override
    public final boolean accept(File dir, String name) {
      return pattern.matcher(name).find();
    }
  }
}
