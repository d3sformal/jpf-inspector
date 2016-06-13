//
// Copyright (C) 2010 United States Government as represented by the
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
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;

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
  protected static final boolean DEBUG = false;

  private static final long serialVersionUID = 7537838000235914763L;

  final private int clientID;
  /**
   * Inspector interface which can be used for lazy retrieval of referenced items.
   * Although, we should probably indicate which items are retrieved lazily.
   */
  final private JPFInspectorBackEndInterface inspector;
  final private String stateExpr;

  protected ProgramStateEntry(int clientID, StateNodeInterface sni) {
    assert sni != null;

    this.clientID = clientID;
    this.inspector = sni.getInspector();
    this.stateExpr = sni.getStateExpr();

    assert inspector != null;
    assert stateExpr != null;
  }


  /**
   * Returns the constant "0". I don't know why this exists.
   */
  public int getClientID () {
    return clientID;
  }

  /**
   * Returns the Inspector server.
   */
  protected JPFInspectorBackEndInterface getInspector () {
    return inspector;
  }

  /**
   * Returns the expression that can be used to obtain given entry if used with the "print" command.
   */
  public String getStateExpr () {
    return stateExpr;
  }

  /**
   * See {@link PSEVisitor} and {@link CmdPrint.ValuePrinter}.
   */
  public abstract <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException;
}
