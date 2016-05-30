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

package gov.nasa.jpf.inspector.utils.parser;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

/**
 * This exception is thrown when the expression parser attempts to parse an int, long, char or string,
 * but fails because the user's input is incorrect.
 *
 * @author Alf *
 */
public class JPFInspectorRuntimeParsingException extends RuntimeException {

  private static final long serialVersionUID = -8957248709476152417L;

  private final JPFInspectorParsingErrorException pee;

  public JPFInspectorRuntimeParsingException (JPFInspectorParsingErrorException pee) {
    super();
    this.pee = pee;
  }

  public JPFInspectorParsingErrorException getParsingErrorException () {
    return pee;
  }

}
