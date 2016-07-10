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
 * Contains classes that will only be compiled and used when jpf-symbc is also present. These are various
 * attribute adaptors that allow JPF Inspector to work with symbolic values more effectively.
 *
 * In particular, these classes permit to convert strings typed in by the user into jpf-symbc symbolic values.
 */
package gov.nasa.jpf.inspector.symbolic;