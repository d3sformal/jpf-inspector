//
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

/**
 * Contains classes of the JPF Inspector server.
 *
 * JPF Inspector consists of the client and of the server.
 *
 * Methods of the server classes are called either from the client (whenever the user executes a command) or from the
 * JPF thread (when called by the {@link gov.nasa.jpf.inspector.server.jpf.InspectorListener}). Methods that are called
 * from the JPF thread must be annotated as such.
 */
package gov.nasa.jpf.inspector.server;