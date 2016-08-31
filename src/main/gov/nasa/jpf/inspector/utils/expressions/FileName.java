//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.utils.expressions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Represents the expression syntax expression for a file name.
 */
public class FileName {

  private String fileName;
  private Pattern fileNamePattern;

  private Map<String, Boolean> fileNameMatches; /// For each tested loaded class name in JPF holds whether matches given pattern or not.

  /**
   * Creates a new {@link FileName} filter that matches a filename mask.
   *
   * @param fileName The filter to use (for example, "hel*" would accept the file "hello.java")
   */
  public FileName(String fileName) {
    assert fileName != null;

    this.fileName = fileName;
    String  fileNameProcessed = ClassName.convertName2RegExp(fileName);
    fileNamePattern = Pattern.compile(fileNameProcessed);

    fileNameMatches = new HashMap<>();
  }

  public final String getFileName() {
    return fileName;
  }

  /**
   * Test whether given file name (actual file name - without '*' characters) match
   *  provided file name pattern.
   * <br>
   * Example:
   * <br> Pattern represented by {@link FileName} class "MyEx*le.java"
   * <br> fiName parameter "MyExample.java" --> true
   * <br> fiName parameter "MyEx.java"      --> false
   *  
   * @param fiName File name to test with represented file name pattern. Cann't be null.
   * @return true if file names match (or are equal)
   */
  public boolean isSameFile(String fiName) {
    assert fiName != null;
    
    Boolean wasTestedBefore = fileNameMatches.get(fiName);
    if (wasTestedBefore != null) {
      return wasTestedBefore;
    }

    Matcher matcher = fileNamePattern.matcher(fiName);
    Boolean nameMatch = matcher.matches();
    fileNameMatches.put(fiName, nameMatch);
    return nameMatch;
  }
  
  /**
   * 
   * @return Gets true of can represent only single file, false otherwise. 
   * 
   * <br>Note: Checks whether file name contains "*" char.
   */
  @SuppressWarnings("unused") // Preserved for similarity to ClassName and FieldName
  public boolean representsSingleFile() {
    return ClassName.singleEntryName(fileName);
  }
}
