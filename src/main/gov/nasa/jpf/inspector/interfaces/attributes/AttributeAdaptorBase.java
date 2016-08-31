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

package gov.nasa.jpf.inspector.interfaces.attributes;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;

/**
 * Base class for attribute adaptors that allows them to gain access to the JPF Inspector.
 */
public interface AttributeAdaptorBase {
  /**
   * This method is called once, immediately after instantiation. Adaptors that need to use the Inspector instance
   * should store it as a member field. There is only ever a single Inspector client instance over the course of
   * Inspector run.
   * @param inspector The single JPF Inspector client.
   */
  void initialize(JPFInspectorClientInterface inspector);
}
