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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.client.commands.CmdPrint;

/**
 * This visitor is implemented only by a visitor in the {@link CmdPrint} class and it is used to print program
 * state entries to the console.
 */
public interface PSEVisitor<T> {

  T visitPSEHeapEntryList(PSEHeapEntryList entry);

  T visitPSEMethod(PSEMethod entry);

  T visitPSEThread(PSEThread entry);

  T visitPSEVariableArray(PSEVariableArray entry);

  T visitPSEVariableObject(PSEVariableObject entry);

  T visitPSEVariablePrimitive(PSEVariablePrimitive entry);
}
