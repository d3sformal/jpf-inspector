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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StackFrame;

/**
 * The attribute manager interface handles the "set_attr" commands.
 *
 * The attribute manager itself has more capabilities, but these don't need to be exposed to the client.
 */
public interface AttributeManagerInterface {
  void setAttributeValue(String expression) throws JPFInspectorException;
  String getAttachmentAttributes(ElementInfo ei, int index);
  String getAttachmentAttributes(ElementInfo ei, FieldInfo fieldInfo);
  String getAttachmentAttributes(StackFrame sf, int index);
}
