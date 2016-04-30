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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;

import java.util.List;

public interface BreakPointManagerInterface {

  /**
   * @return Gets list with currently existing Breakpoints. Never gets null, if no breakpoint exists, then empty list is returned.
   */
  public List<BreakPointStatus> getBreakPoints ();

  /**
   * Creates new (if breakpoint ID is set {@link BreakPointCreate#BP_ID_NOT_DEFINED} or modifies existing breakpoint ( with same breakpoint ID)
   * 
   * @param newBP Structure with new information about new BP.
   * @return Get informations about state of the created/modified breakpoint or null if error takes place.
   */
  public BreakPointStatus createBreakPoint (BreakPointCreate newBP) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * Creates new assertion (if breakpoint ID is set {@link BreakPointCreate#BP_ID_NOT_DEFINED} or modifies existing assertion ( with same ID)
   * 
   * @param newAssert Structure with new information about new Assert.
   * @return Get informations about state of the created/modified assertion or null if error takes place.
   */
  public AssertStatus createAssert (AssertCreate newAssert) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * 
   * @param bpID Identification of the Breakpoint to delete
   * @return true if Breakpoint with given bpID exists, false if BP not found.
   */
  public boolean deleteBreakPoint (int bpID);

}
