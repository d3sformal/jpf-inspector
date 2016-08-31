///
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
///

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.ConfigChangeListener;

/**
 * This config change listener is used to handle the rare situation where JPF fails to initialize, for example, because
 * the application class has no main method. In this case, we must end the Inspector's run as well.
 */
public class InspectorConfigChangeListener implements ConfigChangeListener {
  private final JPFInspector jpfInspector;

  public InspectorConfigChangeListener(JPFInspector jpfInspector) {

    this.jpfInspector = jpfInspector;
  }

  @Override
  public void propertyChanged(Config conf, String key, String oldValue, String newValue) {

  }

  @Override
  public void jpfRunTerminated(Config conf) {
    jpfInspector.notifyJPFFinished();
  }
}
