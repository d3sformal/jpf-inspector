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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * Represents an evaluated program state expression.
 */
public interface StateNodeInterface {

  JPFInspector getInspector();

  int getReferenceDepth();

  String getStateExpr();

  /**
   * Evaluates the evaluated expression again, this time resulting in a {@link ProgramStateEntry} which is mostly
   * just a container of strings.
   *
   * The name "toHierarchy3" is temporary (I promise). Previously, this method was called "getResultExpression" which
   * was confusing because the method that transformed Expression classes (hierarchy 1) to state expressions (hierarchy 2)
   * was also called "getResultExpression".
   *
   * @param name TODO how does this work? does it even have any meaning?
   * @param clientID This is always zero, I think. I don't know what it means.
   */
  ProgramStateEntry toHierarchy3(String name, int clientID) throws JPFInspectorException;
}
