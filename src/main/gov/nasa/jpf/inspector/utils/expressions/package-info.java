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
 * The classes in this package represent user-typed expressions in the expression syntax.
 *
 * These classes are created from the ANTLR grammar. They store their data as strings but they have the capability
 * to be compared to actual ClassInfo/MethodInfo etc. instances. This comparison is string-wise, but the expressions
 * may have wildcards which is taken into account by the classes (they use regular expressions).
 *
 * The comparisons are cached so that we don't do too much regular expression parsing.
 *
 * See the documentation for what wildcards are allowed.
 */
package gov.nasa.jpf.inspector.utils.expressions;