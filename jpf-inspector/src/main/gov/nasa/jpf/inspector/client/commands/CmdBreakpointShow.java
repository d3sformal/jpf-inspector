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

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the "show breakpoint" command that prints all existing breakpoints.
 */
public class CmdBreakpointShow extends ClientCommand {

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    List<BreakpointStatus> bps = inspector.getBreakpoints();
    for (BreakpointStatus bp : bps) {
      outStream.println(breakpointToString(bp));
    }
    if (bps.isEmpty()) {
      outStream.println("No breakpoints.");
    }
  }

  @Override
  public String getNormalizedCommand () {
    return "show breakpoint";
  }

  /**
   * Transforms breakpoint representation into the string.
   * 
   * @param bp Information about a breakpoint.
   * @return String representation of the brekapoint status.
   */
  public static String breakpointToString (BreakpointStatus bp) {
    StringBuilder bpText = new StringBuilder(80);
    bpText.append(bp.getBPID());
    bpText.append(" : ");
    if (bp.getName() != null && !bp.getName().equals("")) {
      bpText.append(" name=");
      bpText.append(bp.getName());
    }
    bpText.append(" state=");
    bpText.append(bp.getState().toString());

    bpText.append(" hits=");
    bpText.append(bp.getHitCounter());

    bpText.append(" hitsTotal=");
    bpText.append(bp.getHitCounterTotal());

    if (bp.bpHitCountLowerBound() != null || bp.bpHitCountUpperBound() != null) {
      bpText.append(' ');
      Integer lowerBound = bp.bpHitCountLowerBound();
      Integer upperBound = bp.bpHitCountUpperBound();
      if (lowerBound != null && (!lowerBound.equals(BreakpointCreationInformation.DEFAULT_LOWER_BOUND))) {
        bpText.append(lowerBound);
        bpText.append("<=");
      }
      bpText.append("hit_count");
      if (upperBound != null && (!upperBound.equals(BreakpointCreationInformation.DEFAULT_UPPER_BOUND))) {
        bpText.append("<=");
        bpText.append(upperBound);
      }
    }

    bpText.append(' ');
    bpText.append(bp.getNormalizedBreakpointExpression());

    return bpText.toString();
  }

}
