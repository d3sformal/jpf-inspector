//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
package gov.nasa.jpf.inspector;

import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.JPFInspectorParallel;

import java.io.PrintStream;

/**
 * Class that should be used to obtain instances of the client and the server.
 *
 * The only implemented server is the {@link JPFInspectorParallel}.
 * The only implemented client is the {@link JPFInspectorClient}.
 * This class merely constructs those classes by calling constructors directly.
 */
public final class JPFInspectorFacade {
  /**
   * Creates an instance of the JPFInspector back-end (server part).
   * 
   * @param callBacks The client's callback handler where the server should print its output.
   * @return New instance of the server part of the JPFInspector.
   */
  public static JPFInspector getInspectorBackend (InspectorCallbacks callBacks) {
    return new JPFInspectorParallel(callBacks);
  }

  /**
   * Creates an instance of the JPFInspectorClient (the client part).
   *
   * @param target Name of the target (.jpf file name, without the .jpf extension). For logging purposes only.
   * @param out Stream where command output should be printed.
   * @return New instance of high level client part of the JPF Inspector.
   */
  public static JPFInspectorClientInterface getInspectorClient (String target, PrintStream out) {
    return new JPFInspectorClient(target, out);
  }
  /**
   * Creates an instance of the JPFInspectorClient (the client part).
   *
   * @param target Name of the target (.jpf file name, without the .jpf extension). For logging purposes only.
   * @param out Stream where command output should be printed.
   * @param callbackOut  Stream where callback output should be printed.
   * @return New instance of high level client part of the JPF Inspector.
   */
  public static JPFInspectorClientInterface getInspectorClient (String target, PrintStream out, PrintStream callbackOut) {
    return new JPFInspectorClient(target, out, callbackOut);
  }

}
