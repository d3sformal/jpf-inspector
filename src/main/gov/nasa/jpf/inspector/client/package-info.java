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
 * Contains classes of the JPF Inspector client.
 *
 * JPF Inspector consists of the client and of the server.
 *
 * The client is created by the frontend (a graphical shell panel or the command-line shell). The frontend takes
 * commands from the user and passes them to the client. The client parses them and sends them to the server. The
 * server executes them and returns the result to the client and the client sends the result to the output stream.
 */
package gov.nasa.jpf.inspector.client;