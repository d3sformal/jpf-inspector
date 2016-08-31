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

import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * Represents a node in the expressions syntax tree, if the node is used to retrieve state of the SuT
 * (variables, fields, ...)
 * 
 * Expressions are created typically by the parser.
 * 
 * The {@link ExpressionStateRootNode#getResultExpression(JPFInspector, InspectorState)} can be used to retrieve representation of the current state. *
 * 
 * @author Alfifi
 */
public interface ExpressionStateInterface extends ExpressionNodeInterface {
}
