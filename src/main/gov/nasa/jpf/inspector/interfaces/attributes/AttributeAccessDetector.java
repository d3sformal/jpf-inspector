///
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
///

package gov.nasa.jpf.inspector.interfaces.attributes;

import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.vm.Instruction;

/**
 * An attribute adaptor that determines additional instructions that should trigger an attribute access hit condition.
 */
public interface AttributeAccessDetector extends AttributeAdaptorBase {
  boolean detectRead(Instruction impendingInstruction, FieldName fieldName);
  boolean detectRead(Instruction impendingInstruction, String localVariable);
  boolean detectWrite(Instruction impendingInstruction, FieldName fieldName);
  boolean detectWrite(Instruction impendingInstruction, String localVariable);
}
