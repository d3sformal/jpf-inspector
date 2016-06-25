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
import gov.nasa.jpf.inspector.server.callbacks.InspectorServerCallbacks;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

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

  /**
   * Gets the server-side serializer of callbacks. When this serializer's methods are called, new callbacks are sent
   * to the client.
   */
  InspectorServerCallbacks getServerCallbacks();


  /**
   * Blocks until JPF is connected with the Inspector. If JPF is already connected to the Inspector,
   * it returns immediately.
   */
  void waitUntilJpfBecomesConnected();

  Instruction getCurrentInstruction();

  /**
   * This may violate encapsulation, but, on the other hand, the Inspector's codebase is already too fragmented and
   * adding more interfaces and yet another hierarchy of data objects isn't worth it, but perhaps we'll think of an
   * alternative.
   *
   * It is the caller's responsibility to only use the VM when the VM is paused.
   */
  VM getVM();
}
