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

package gov.nasa.jpf.inspector.exceptions;

import gov.nasa.jpf.vm.VM;

/**
 * @author Alf
 * 
 */
public class JPFInspectorNoVMConnected extends JPFInspectorException {

  private static final long serialVersionUID = -3191601258737187433L;

  private JPFInspectorNoVMConnected() {
    super("The JPF virtual machine is not loaded (JPF is not connected).");
    // This shouldn't actually ever happen - it's more like a failsafe.
  }

  /**
   * @param vm Checks if given VM is not null.
   * @throws JPFInspectorNoVMConnected
   */
  public static void checkVM (VM vm) throws JPFInspectorNoVMConnected {
    if (vm == null) {
      throw new JPFInspectorNoVMConnected();
    }

  }
}
