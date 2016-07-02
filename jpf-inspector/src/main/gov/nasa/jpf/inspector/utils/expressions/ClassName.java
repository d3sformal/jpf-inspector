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

    Matcher matcher = classNamePattern.matcher(cn);
    Boolean nameMatch = matcher.matches();
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
