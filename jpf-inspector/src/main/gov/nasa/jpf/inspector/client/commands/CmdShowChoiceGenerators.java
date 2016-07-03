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

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "show choice_generators" command that displays the suppression status of threads and the notification
 * action of both types of choice generators.
 */
public class CmdShowChoiceGenerators extends ClientCommand {

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

    // Choice generators
    ChoiceGeneratorsInterface.CGNotificationSpecification[] cgNotificationStatus = inspector.getCGNotificationStatus();
    for(ChoiceGeneratorsInterface.CGNotificationSpecification spec : cgNotificationStatus) {
      String status = "";
      switch (spec.getNotificationType()) {
        case CG_TYPE_DATA:       status += "Data      "; break;
        case CG_TYPE_SCHEDULING: status += "Scheduling"; break;
      }
      status += " choice generators' mode ";
      switch (spec.getNotificationMode()) {
        case CG_MODE_PRINT: status += "'print'"; break;
        case CG_MODE_ASK:   status += "'ask'  "; break;
      }
      status += " is ";
      if (spec.isNotificationEnabled() == true) {
        status += "ENABLED.";
      } else if (spec.isNotificationEnabled() == false) {
        status += "disabled.";
      }
      outStream.println(status);
    }

    outStream.println();

    Integer[] suppressedThreads = inspector.getSuppressedThreads();
    if (suppressedThreads.length == 0) {
      outStream.println("No threads are suppressed.");
    } else {
      for (int threadIndex : suppressedThreads) {
        outStream.println("Suppressing thread " + threadIndex + ".");
      }
    }
  }

  @Override
  public String getNormalizedCommand() {
    return "show choice_generators";
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return true;
  }
}
