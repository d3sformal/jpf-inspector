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

package gov.nasa.jpf.inspector.server.jpf;


import gov.nasa.jpf.search.Search;

/**
 * When using JPF Inspector, your search class MUST implement this interface. Only search classes should
 * implement this interface.
 *
 * See {@link DFSearchInspector} for a reference implementation, or use {@link SearchWrapper} if you don't
 * want to create your own Search subclass.
 */
public interface SearchInspectorExtension {

  /**
   * Implementor should use this method to remember the reference to JPF Inspector.
   * @param inspector The running JPF Inspector.
   */
  void setInspector(JPFInspector inspector);
  
  /**
   * The implementor's terminate method (overriden from the class {@link Search}) must
   * contain the code:
   * <pre>
   * {@code
   * if (inspector != null) {
   *   inspector.getStopHolder().terminating();
   * }
   * }
   * </pre>
   */
  void terminate();
}