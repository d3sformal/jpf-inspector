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

package gov.nasa.jpf.inspector.utils.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a comma-separated list of strings.
 * This is used as arguments to custom hit conditions.
 */
public class Expressions  {
  private final List<String> expressions;

  public Expressions() {
    expressions = new ArrayList<>();
  }
  public void insert(String argument) {
    expressions.add(0, argument.trim());
  }

  @Override
  public String toString() {
    String s = "";
    for (int i = 0; i < expressions.size(); i++) {
      String arg = expressions.get(i);
      s += arg;
      if (i < expressions.size() - 1) {
        s += ", ";
      }
    }
    return s;
  }

  public String[] toStringArray() {
    return expressions.toArray(new String[expressions.size()]);
  }
}
