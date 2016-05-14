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

/**
 * 
 * Generic predecessor of all exception thrown by JPF-Inspector (by the {@link JPFInspectorBackEndInterface})
 * 
 * Successors reports invalid usage of the Inspector.
 * 
 * (All other exceptions thrown by {@link JPFInspectorBackEndInterface} are internal back-end errors)
 * 
 * @author Alf
 * 
 */
public class JPFInspectorException extends Exception {

  private static final long serialVersionUID = -1872156091293046263L;

  protected JPFInspectorException (String msg) {
    super((msg != null ? ("ERR: " + msg) : "ERROR"));
  }
}
