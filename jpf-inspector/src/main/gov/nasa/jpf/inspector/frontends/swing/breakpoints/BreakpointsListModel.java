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
