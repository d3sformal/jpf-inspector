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
 * This package contains "program state entries", classes that represent information that is passed from the client console to the Inspector server. See the base class,
 * {@link gov.nasa.jpf.inspector.common.pse.ProgramStateEntry}, for details.
 *
 * This package also contains the {@link gov.nasa.jpf.inspector.common.pse.PSEVisitor} interface
 * that the command "print" uses to print the entries out.
 */
package gov.nasa.jpf.inspector.common.pse;