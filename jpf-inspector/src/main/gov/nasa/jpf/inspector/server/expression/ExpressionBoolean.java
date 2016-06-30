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

package gov.nasa.jpf.inspector.server.expression;

/**
 * Abstract base class for boolean expressions in the syntax tree. Boolean expressions are used as
 * breakpoint hit conditions.
 */
public abstract class ExpressionBoolean implements ExpressionBooleanInterface {

  /**
   * {@inheritDoc}
   *
   * In the default implementation, this method returns null which means that no additional information is provided.
   */
  @Override
  public String getDetails (InspectorState state) {
    return null;
  }

}
