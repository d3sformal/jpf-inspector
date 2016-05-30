//
//Copyright (C) 2010 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.
//
//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.
//
//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
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

  public FileName(String fileName) {
    assert fileName != null;

    this.fileName = fileName;
    String  fileNameProcessed = ClassName.convertName2RegExp(fileName);
    fileNamePattern = Pattern.compile(fileNameProcessed);

    fileNameMatches = new HashMap<String, Boolean>();
  }

  final public String getFileName() {
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

    Matcher m = fileNamePattern.matcher(fiName);
    Boolean nameMatch = m.matches();
    fileNameMatches.put(fiName, nameMatch);
    return nameMatch;
  }
  
  /**
   * 
   * @return Gets true of can represent only single file, false otherwise. 
   * 
   * <br>Note: Checks whether file name contains "*" char.
   */
  public boolean representsSingleFile() {
    return ClassName.singleEntryName(fileName);
  }


}
