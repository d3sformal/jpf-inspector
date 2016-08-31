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
 * Contains classes that represent nodes of the program state hierarchy.
 *
 * I could try to be funny and call this "hierarchy 4" but it's not really funny that I felt the need to add either
 * another system of classes that represent absolutely the same thing. I guess the DRY principle is crying somewhere
 * in a corner.
 *
 * To be fair, we did need these nodes to inherit from {@link javax.swing.tree.TreeNode} _and_ a representation based
 * on object type rather than its relationship to the parent was more helpful in this case because we need indefinite
 * recursion.
 */
package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;