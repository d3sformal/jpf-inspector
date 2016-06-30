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
package gov.nasa.jpf.inspector.interfaces;

/**
 * Represents a method of the {@link InspectorCallbacks} interface.
 */
public enum CallbackKind {
  CB_ANY,
  CB_STATE_CHANGE,
  CB_GENERIC_ERROR,
  CB_GENERIC_INFO,
  CB_BREAKPOINT_HIT,
  CB_CG_NEW_CHOICE,
  CB_CG_CHOICE_TO_USE,
  CB_CG_USED_CHOICE
}
