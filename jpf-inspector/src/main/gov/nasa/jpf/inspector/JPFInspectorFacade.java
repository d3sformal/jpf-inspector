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

package gov.nasa.jpf.inspector;

import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.server.jpf.JPFInspectorParallel;

import java.io.PrintStream;

/**
 * Class that should be used to obtain instances of the client and the server.
 *
 * The only implemented server is the {@link JPFInspectorParallel}.
 * The only implemented client is the {@link JPFInspectorClient}.
 * This class merely constructs those classes by calling constructors directly.
 */
public class JPFInspectorFacade {

  /**
   * Creates an instance of the JPFInspector back-end (server part).
   * 
   * @param callBacks Where error and other notification should be reported.
   * @return New instance of the server part of the JPFInspector.
   */
  public static JPFInspectorBackEndInterface getInspectorBackend (InspectorCallBacks callBacks) {
    return new JPFInspectorParallel(callBacks);
  }

  /**
   * @param target Name of the target (.jpf file name, without the .jpf extension). For logging purposes only.
   * @param out Stream where command output should be printed.
   * @return New instance of high level client part of the JPF Inspector.   *
   */
  public static JPFInspectorClientInterface getInspectorClient (String target, PrintStream out) {
    return new JPFInspectorClient(target, out);
  }

}
