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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.Serializable;

/**
 * Abstract class that holds properties common to all program state entries.
 *
 * A program state entry is created by the server in response to the "print" command and sent back. The
 * print command then prints the program state entry as a string. That is the only use of this class.
 *
 * This class is exposed as public API to custom hit conditions, therefore all of its methods (and the methods
 * of its subclasses) should be well-documented.
 */
public abstract class ProgramStateEntry implements Serializable {

  private static final long serialVersionUID = 7537838000235914763L;

  /**
   * See {@link PSEVisitor} and {@link ValuePrinter}.
   */
  public abstract <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException;
}
