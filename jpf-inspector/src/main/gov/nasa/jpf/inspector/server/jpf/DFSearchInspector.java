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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.search.DFSearch;

/**
 * The DFSearchInspector is a subclass of DFSearch and has the same functionality, except that it also
 * implements the {@link SearchInspectorExtension} interface which is neccessary in order for the Search class
 * to work with the Inspector.
 *
 * This is the default search class when using the Inspector.
 *
 * You may use this class as a reference implementation for your own search classes if you don't want to use
 * the {@link SearchWrapper} search class which handles interfacing with the Inspector for you.
 */
public class DFSearchInspector extends DFSearch implements SearchInspectorExtension {
  private JPFInspector inspector = null;

  public DFSearchInspector (Config config, VM vm) {
    super(config, vm);
  }

  @Override
  public void setInspector (JPFInspector inspector) {
    this.inspector = inspector;
  }

  @Override
  public void terminate () {
    super.terminate();
    if (inspector != null) {
      inspector.getStopHolder().terminating();
    }
  }
}
