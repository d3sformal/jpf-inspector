//
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

package gov.nasa.jpf.inspector.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user-defined command alias, such as "c" for "continue".
 */
public class CommandAlias {
  private String key;
  private String value;
  private int numberOfRequiredParameters;
  private boolean usesFullArgumentLine;
  public static final String FULL_ARGUMENT_PATTERN = "{*}";

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public int getNumberOfRequiredParameters() {
    return numberOfRequiredParameters;
  }

  private static Pattern patternNumericArgument = Pattern.compile("\\{[0-9]\\}");

  public CommandAlias(String key, String value) {
    this.key = key;
    this.value = value;
    Matcher patternMatcher =  patternNumericArgument.matcher(value);
    int count = 0;
    while (patternMatcher.find()) {
      count++;
    }
    this.numberOfRequiredParameters = count;
    this.usesFullArgumentLine = value.contains(FULL_ARGUMENT_PATTERN);
  }

  public boolean usesFullArgumentLine() {
    return usesFullArgumentLine;
  }
}
