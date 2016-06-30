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

package gov.nasa.jpf.inspector.frontends.swing.breakpoints;

import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * List model for the Breakpoint Manager GUI panel.
 */
public class BreakpointsListModel extends AbstractListModel<BreakpointStatus> {
  private static final long serialVersionUID = 4657226277578293353L;
  private List<BreakpointStatus> shownBreakpoints = new ArrayList<>();

  @Override
  public int getSize() {
    return shownBreakpoints.size();
  }

  @Override
  public BreakpointStatus getElementAt(int index) {
    return shownBreakpoints.get(index);
  }

  public void update(List<BreakpointStatus> newBreakpoints) {
    int oldIndex = 0;
    for (int newIndex = 0; newIndex < newBreakpoints.size(); newIndex++) {
      if (oldIndex >= shownBreakpoints.size()) {
        // Adding at the end.
        shownBreakpoints.add(newBreakpoints.get(newIndex));
        fireIntervalAdded(this, shownBreakpoints.size() -1, shownBreakpoints.size()-1);
        oldIndex++;
      } else {
        int oldId = shownBreakpoints.get(oldIndex).getBPID();
        int newId = newBreakpoints.get(newIndex).getBPID();
        if (oldId == newId) {
          shownBreakpoints.set(oldIndex, newBreakpoints.get(newIndex));
          fireContentsChanged(this, oldIndex, oldIndex);
          oldIndex++;
        }
        else {
          // Removing
          shownBreakpoints.remove(oldIndex);
          fireIntervalRemoved(this, oldIndex, oldIndex);
          //noinspection AssignmentToForLoopParameter
          newIndex--;
        }
      }
    }
    // Remove any remaining ones
    for (int i = oldIndex; i < shownBreakpoints.size(); i++) {
      shownBreakpoints.remove(oldIndex);
      fireIntervalRemoved(this, oldIndex, oldIndex);
    }
  }
}
