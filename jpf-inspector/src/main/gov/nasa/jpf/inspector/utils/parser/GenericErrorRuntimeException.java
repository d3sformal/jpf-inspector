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

package gov.nasa.jpf.inspector.utils.parser;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionFactory;

/**
 * Runtime wrapper for {@link JPFInspectorGenericErrorException}.
 * It is thrown by {@link ExpressionFactory} and caught and unwrapped in the ParserFasade
 * into the original non {@link RuntimeException}.
 */
public class GenericErrorRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -6788294963338418755L;

  private final JPFInspectorGenericErrorException exc;

  public GenericErrorRuntimeException (JPFInspectorGenericErrorException exc) {
    super(exc);
    this.exc = exc;
  }

  public JPFInspectorGenericErrorException getWrappedException () {
    return exc;
  }

}
