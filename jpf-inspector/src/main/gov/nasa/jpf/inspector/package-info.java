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
 * JPF Inspector is a debugging tool for Java Pathfinder.
 *
 * You may attach the JPF Inspector to a program run using
 * the Java Pathfinder virtual machine to debug it and step through it. It supports breakpoints and single-step
 * execution (forward and backward) at different levels of granularity, and it allows the user to examine and modify
 * program state (threads, call stacks, and heap objects). Unlike with standard debuggers (GDB), it is also possible
 * to control thread scheduling explicitly.
 */
package gov.nasa.jpf.inspector;