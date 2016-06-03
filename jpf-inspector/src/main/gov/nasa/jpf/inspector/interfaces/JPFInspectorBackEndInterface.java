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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

/**
 * Interface provided by JPFInspector back-end ("server") part. Low level interface.
 */
public interface JPFInspectorBackEndInterface extends
        CommandsInterface,
        BreakPointManagerInterface,
        ProgramStateInterface,
        ChoiceGeneratorsInterface {

  /**
   * New JPF can be bound only if previous one has finished (stops execution) or no JPF is bound.
   * 
   * @param jpf
   *        Instance of the JPF that should the Inspector use. JPF mustn't be running. (The {@link JPF#run()} method is not called.)
   * @throws JPFInspectorGenericErrorException -Error when connection new JPF occur (previous connected JPF instance is still running, ..)
   */
  void bindWithJPF(JPF jpf) throws JPFInspectorGenericErrorException;

  InspectorCallBacks getCallBack();

}
