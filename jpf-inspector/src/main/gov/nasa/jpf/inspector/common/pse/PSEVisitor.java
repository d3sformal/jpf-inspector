//
// Copyright (C) 2011 United States Government as represented by the
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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.client.commands.CmdPrint;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

/**
 * This visitor is implemented only by a visitor in the {@link CmdPrint} class and it is used to print program
 * state entries to the console.
 */
public interface PSEVisitor<T> {

  T visitPSEHeapEntryList(PSEHeapEntryList entry) throws JPFInspectorException;

  T visitPSEMethod(PSEMethod entry) throws JPFInspectorException;

  T visitPSEThread(PSEThread entry) throws JPFInspectorException;

  T visitPSEVariableArray(PSEVariableArray entry) throws JPFInspectorException;

  T visitPSEVariableObject(PSEVariableObject entry) throws JPFInspectorException;

  T visitPSEVariablePrimitive(PSEVariablePrimitive entry) throws JPFInspectorException;
}
