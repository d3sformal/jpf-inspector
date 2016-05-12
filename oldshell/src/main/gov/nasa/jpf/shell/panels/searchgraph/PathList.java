package gov.nasa.jpf.shell.panels.searchgraph;

import java.awt.event.*;
import javax.swing.event.*;

import gov.nasa.jpf.shell.ShellManager;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

public class PathList extends JPanel implements ListDataListener, MouseListener {

  private static final int PADDING = 5;
  private DefaultListModel model;
  private ArrayList<PathItem<NodeInfo>> nodes = new ArrayList<PathItem<NodeInfo>>();
  private ArrayList<Integer> selectedIndicies = new ArrayList<Integer>();
  private ArrayList<ListSelectionListener> selectionListeners = new ArrayList<ListSelectionListener>();

  public PathList(DefaultListModel path) {
    this.model = path;
    this.model.addListDataListener(this);

    ListDataEvent listDataEvent = new ListDataEvent(model, ListDataEvent.INTERVAL_ADDED, 0, model.getSize());
    intervalAdded(listDataEvent);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent me){
        Component componentAt = getComponentAt(me.getPoint());
        PathItem pr = componentAt instanceof PathItem ? (PathItem)componentAt : null;
        setSelectedValue(null, false);
      }
    });

  }

  //---------- Selection Methods. Warning: These do not fire events!! ----------
  public NodeInfo[] getSelectedValues() {
    NodeInfo[] nodeInfo = new NodeInfo[selectedIndicies.size()];
    int i = 0;
    for (Integer index : selectedIndicies) {
      nodeInfo[i] = nodes.get(index).getItem();
      i++;
    }
    return nodeInfo;
  }

  public void setSelectedValue(NodeInfo n, boolean select) {
    for (Integer index : selectedIndicies) {
      nodes.get(index).setSelected(false);
    }
    selectedIndicies.clear();

    if (n == null) { return; }

    setSelected(nodes.indexOf(getPathItem(n)), select);
    repaint();
  }


  public void setSelected(int indexOfTarget, boolean select) {
    if (select) {
      selectedIndicies.add(indexOfTarget);
      Collections.sort(selectedIndicies);
    } else {
      selectedIndicies.remove((Object)indexOfTarget); //Make sure that we remove
                                                      //the value, not the index
    }
    nodes.get(indexOfTarget).setSelected(select);
  }


  public void clearSelection(){
    ArrayList<Integer> arrayList = new ArrayList<Integer>(selectedIndicies);
    selectedIndicies.clear();
    for (Integer index : arrayList) {
      nodes.get(index).setSelected(false);
    }
  }

  public void intervalAdded(ListDataEvent lde) { contentsChanged(lde); }
  public void intervalRemoved(ListDataEvent lde) { contentsChanged(lde); }

  public void contentsChanged(final ListDataEvent lde) {
    //Make sure that we're in the right thread.
    if (SwingUtilities.isEventDispatchThread() == false) {
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            contentsChanged(lde);
          }
        });
      } catch (InterruptedException ex) {
        Logger.getLogger(PathList.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(PathList.class.getName()).log(Level.SEVERE, null, ex);
      }
      return;
    }

    removeAll();
    nodes.clear();
    selectedIndicies.clear();
    SpringLayout springLayout = new SpringLayout();
    PathItem previous = null;
    int height = PADDING;
    int width = 0;
    for(int i = 0; i < model.getSize(); i++){
      NodeInfo n = (NodeInfo)model.get(i);
      PathItem<NodeInfo> pathRenderer = new PathItem<NodeInfo>( n );
      pathRenderer.addMouseListener(this);
      nodes.add(pathRenderer);

      springLayout.putConstraint(SpringLayout.WEST, pathRenderer, PADDING,
                                  SpringLayout.WEST, this);

      springLayout.putConstraint(SpringLayout.EAST, pathRenderer, PADDING,
                                  SpringLayout.EAST, this);

      if (previous == null) {
        springLayout.putConstraint(SpringLayout.NORTH, pathRenderer, PADDING,
                                   SpringLayout.NORTH, this);
      }else{
        springLayout.putConstraint(SpringLayout.NORTH, pathRenderer, PADDING,
                                   SpringLayout.SOUTH, previous);
      }
      previous = pathRenderer;
      add(pathRenderer);
      height += pathRenderer.getMinimumSize().height;
      width = Math.max(width, pathRenderer.getMinimumSize().width);
    }
    height += PADDING*model.getSize();
    width += 2*PADDING;
    setSize(width, height);
    setPreferredSize(getSize());

    setLayout(springLayout);
    setSelectedValue(null, false); //Select nothing
    repaint();
  }

  public void mouseClicked(MouseEvent me) {
    Component componentAt = me.getComponent();
    if (componentAt instanceof PathItem == false) {
      ShellManager.getManager().getLogger().info( "Non PathItem item in the list: " +
              componentAt.getClass().getName());
      return;
    }
    PathItem pr = (PathItem)componentAt;
    int indexOfTarget = nodes.indexOf(pr);

    int firstIndex, lastIndex;

    if (me.isShiftDown()) {
      int start = selectedIndicies.isEmpty() ? 0 : selectedIndicies.get(0);
      int end = indexOfTarget;
      firstIndex = Math.min(start, end);
      lastIndex = Math.max(start, end);
      clearSelection();
      for(int i = firstIndex; i <= lastIndex; i++){ setSelected(i, true); }
    }else if (me.isControlDown()){
      firstIndex = lastIndex = indexOfTarget;
      setSelected(indexOfTarget, !selectedIndicies.contains(indexOfTarget) );
    } else {
      firstIndex = lastIndex = indexOfTarget;
      if (!selectedIndicies.isEmpty()){
        firstIndex = selectedIndicies.get(0);
        lastIndex = selectedIndicies.get(selectedIndicies.size() - 1);
      }
      clearSelection();
      setSelected(indexOfTarget, true);
    }

    ListSelectionEvent lse = new ListSelectionEvent(this, firstIndex, lastIndex, false);
    for (ListSelectionListener listSelectionListener : selectionListeners) {
      listSelectionListener.valueChanged(lse);
    }

  }

  private PathItem getPathItem(NodeInfo ei){
    for (PathItem<NodeInfo> pi : nodes) {
      if (pi.getItem() == ei) { return pi; }
    }
    return null;
  }

  public void removeListSelectionListener(ListSelectionListener lse){
    selectionListeners.remove(lse);
  }

  public void addListSelectionListener(ListSelectionListener lse){
    selectionListeners.add(lse);
  }

  public void mousePressed(MouseEvent me){}
  public void mouseReleased(MouseEvent me){}
  public void mouseEntered(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}


}
