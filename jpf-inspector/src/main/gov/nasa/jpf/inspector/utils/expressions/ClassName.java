//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.utils.expressions;

import gov.nasa.jpf.vm.ClassInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a class by name extracted from the user input by the server expression parser.
 */
public class ClassName {
  private final String className;

  private final Pattern classNamePattern;
  
  private final Map<String, Boolean> classNamesMatches; /// For each tested loaded class name in JPF holds whether matches given pattern or not.
  
  public ClassName(String className) {
    assert className != null;

    this.className = className;
    String classNameProcessed = ClassName.convertName2RegExp(className);
    classNamePattern = Pattern.compile(classNameProcessed);
    
    classNamesMatches = new HashMap<>();
  }
  
  public final String getClassName() {
    return className;
  }

  /**
   * Returns true if this expression represents the specified class.
   * Comparison is done based on string names, except that the class name expression might not be a simple name
   * but can be more complex (see the documentation on expressions; this is taken into account here).
   *
   * Performs caching.
   * @param ci The class that should be tested for name equality with this expression.
   */
  public boolean isSameClass(ClassInfo ci) {
    String cn = ci.getName();
    
    Boolean wasTestedBefore = classNamesMatches.get(cn);
    if (wasTestedBefore != null) {
      return wasTestedBefore;
    }

    Matcher m = classNamePattern.matcher(cn);
    Boolean nameMatch = m.matches();
    classNamesMatches.put(cn, nameMatch);
    return nameMatch;
  }
  
  
  /*
   * Converts name of the class of file into regular expression patter.
   * <br>
   * The name may contain * to mark (eventually empty) number of arbitrary characters.
   * Note that "." (and other special regexp characters) are escaped 
   *   so that the * has special meaning.
   * <br>  
   * Examples:
   * <br> "MyEX*.java" is converted into MyEx.*\.java.
   * 
   * @param name Name to convert
   * @return regular expresison pattern
   */
  public static String convertName2RegExp(String name) {
    
    // Conversions ... we permit only * in regexp
    //      '\' -> "\\"
    //      '[' -> "\["
    //      ']' -> "\]"
    //      '}' -> "\{"
    //      '{' -> "\}"
    //      '(' -> "\("
    //      ')' -> "\)"
    //      '+' -> "\+"
    //      '^' -> "\^"
    //      '$' -> "\$"
    //      '?' -> "\?"
    //      ',' -> "\,"
    //      '&' -> "\&"
    //      '|' -> "\|"
    name = name.replace( "\\", "\\\\");
    name = name.replace( "[",  "\\[");
    name = name.replace( "]",  "\\]");
    name = name.replace( "}",  "\\}");
    name = name.replace( "{",  "\\{");
    name = name.replace( "(",  "\\(");
    name = name.replace( ")",  "\\)");
    name = name.replace( "+",  "\\+");
    name = name.replace( "^",  "\\^");
    name = name.replace( "$",  "\\$");
    name = name.replace( "?",  "\\?");
    name = name.replace( ",",  "\\,");
    name = name.replace( "&",  "\\&");
    name = name.replace( "|",  "\\|");

    //      '.' -> "\." not any char
    //      '*' -> ".*"
    name = name.replace( ".",  "\\.");
    name = name.replace( "*",  ".*");
    return "^" + name + "\\z";
  }
  
  /**
   * Checks whether given name represents single entry (class or file) or 
   *   it may match with more then one input.
   * 
   * <br>Test whether original name contains "*".
   * 
   */
  public static boolean singleEntryName(String name) {
    return ( ! name.contains("*") );
  }
}
