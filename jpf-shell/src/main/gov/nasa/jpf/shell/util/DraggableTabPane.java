/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */
package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.util.tabtearing.TabTransferData;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A JTabbedPane that has the added feature of tab tearing.<br>
 * ie.) tabs can be dragged away into their own frame and then recombined into 
 * a single frame. This class isn't directly used by the jpf-shell project.
 * Instead it helps to separate the ugly swing code from the ugly jpf-shell code
 * in {@link gov.nasa.jpf.shell.basicshell.ShellTabbedPane}.
 *
 */
public class DraggableTabPane extends JTabbedPane implements DragSourceListener,
                                                             DragGestureListener,
                                                             DropTargetListener {

  //Used for Drag and Drop support
  public static final String NAME = "DraggableTabPaneTransferData";
  public static final DataFlavor TAB_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);

  protected Component hack_lastDropOperationComponent;
  private int possibleDest = -1;

  protected DraggableTabPane() {
    super();
    new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
    new DragSource().createDefaultDragGestureRecognizer(this,
                                                        DnDConstants.ACTION_COPY_OR_MOVE,
                                                        this);
  }

  @Override
  public Dimension getPreferredSize(){
    Dimension preferredSize = super.getPreferredSize();
    Integer tabrunoverlay =  (Integer) UIManager.get("TabbedPane.tabRunOverlay");
    int x = 0;
    for(int i = 0; i < possibleDest; i++){
      x += getTabComponentWidth(i) - 2*tabrunoverlay;
    }
    preferredSize.width = Math.max(preferredSize.width, x);
    return preferredSize;
  }

  private int getTabComponentWidth(int i){
    Insets tabinsets = (Insets) UIManager.get("TabbedPane.tabInsets");
    Insets tabareainsets = (Insets) UIManager.get("TabbedPane.tabAreaInsets");
    int padding = tabinsets.left + tabinsets.right + tabareainsets.left + tabareainsets.right;

    return padding + getTabComponentAt(i).getWidth();
  }

  private int indexOfDrop(Point dropLocation){
    int i = indexAtLocation(dropLocation.x, dropLocation.y);
    if (i != -1) {
      //Found a location to drop this component into
      return i;
    } else if(getTabCount() == 0) {
      //If the shell is brand spanking new.
      return 0;
    } else if(dropLocation.y <= getTabComponentAt(0).getHeight()) {
      //Add this tab onto the end
      return getTabCount();
    } else {
      return -1;
    }
  }

  private static boolean isDragDropAcceptable(List<DataFlavor> flavs) {
    return flavs.contains(TAB_FLAVOR);
  }

  /**
   * This does the work on including an arrow during DnD to see where the
   * panel is going to drop.
   */
  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);

    if (getTabCount() == 0) {return;}

    if (possibleDest >= 0 && possibleDest <= getTabCount()) {

      Insets tabinsets = (Insets) UIManager.get("TabbedPane.tabInsets");
      Insets tabareainsets = (Insets) UIManager.get("TabbedPane.tabAreaInsets");
      Integer tabrunoverlay =  (Integer) UIManager.get("TabbedPane.tabRunOverlay");
      int padding = tabinsets.left + tabinsets.right + tabareainsets.left + tabareainsets.right;
      int x = 0;
      for(int i = 0; i < possibleDest; i++){
        x += padding + getTabComponentAt(i).getWidth() - 2*tabrunoverlay;
      }
      Graphics2D g2d = (Graphics2D) g.create();

      Component tc = getTabComponentAt(0);
      int y = tc.getY();

      //Lets draw an arrow!!
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      final int ARROW_WIDTH = 14;
      final int BASE_WIDTH = ARROW_WIDTH/2;
      final int ARROW_LENGTH = tc.getHeight();
      final int HEAD_TO_SHAFT_LENGTH = ARROW_LENGTH/2;

      //Fill in the arrow
      GradientPaint fillPaint = new GradientPaint(0, 0, Color.WHITE, 0, ARROW_LENGTH, Color.BLUE);
      g2d.setPaint(fillPaint);
      g2d.fillPolygon(new int[]{x, x - ARROW_WIDTH/2, x - BASE_WIDTH/2, x - BASE_WIDTH/2, x + BASE_WIDTH/2, x + BASE_WIDTH/2, x + ARROW_WIDTH/2},
                      new int[]{ARROW_LENGTH, HEAD_TO_SHAFT_LENGTH, HEAD_TO_SHAFT_LENGTH, 0, 0, HEAD_TO_SHAFT_LENGTH, HEAD_TO_SHAFT_LENGTH},
                      BASE_WIDTH);

      //Outline the arrow
      g2d.setColor(Color.BLACK);
      g2d.drawPolygon(new int[]{x, x - ARROW_WIDTH/2, x - BASE_WIDTH/2, x - BASE_WIDTH/2, x + BASE_WIDTH/2, x + BASE_WIDTH/2, x + ARROW_WIDTH/2},
                      new int[]{ARROW_LENGTH, HEAD_TO_SHAFT_LENGTH, HEAD_TO_SHAFT_LENGTH, 0, 0, HEAD_TO_SHAFT_LENGTH, HEAD_TO_SHAFT_LENGTH},
                      BASE_WIDTH);

      g2d.dispose();
    }

  }

  //----- From DragGestureListener
  /**
   * Decide if the gesture is worth starting a DnD operation over.
   * Just as long the mouse is clicked on a tab, then a drag is started
   */
  @Override
  public void dragGestureRecognized(DragGestureEvent dge) {
    //Check if there is a comp that is being dragged
    Point compPt = dge.getDragOrigin();
    int index = indexAtLocation(compPt.x, compPt.y);
    if ( index != -1) {
      Component comp =  getComponentAt(index);
      dge.startDrag(DragSource.DefaultMoveDrop, new TabTransferrable(
              getTitleAt(index), getIconAt(index), getToolTipTextAt(index),
              getTabComponentAt(index), comp), this);

      //Remove the panel since the tab is being dragged away
      remove(comp);
    }
  }

  
  //--- From DropTargetListener
  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
    if (isDragDropAcceptable(dtde.getCurrentDataFlavorsAsList())) {
      dtde.acceptDrag(dtde.getDropAction());
    } else {
      dtde.rejectDrag();
    }
  }
  /**
   * Use this event to draw the destination marker for the TabPane
   *
   * @param dtde
   */
  @Override
  public void dragOver(DropTargetDragEvent dtde) {
    if (isDragDropAcceptable(dtde.getCurrentDataFlavorsAsList())) {
      try {
        TabTransferData ttd = (TabTransferData) dtde.getTransferable().getTransferData(TAB_FLAVOR);
        int index = indexOfDrop(dtde.getLocation());
        if (index >= 0) {
          possibleDest = indexOfDrop(dtde.getLocation());
          repaint();
        }
      } catch (UnsupportedFlavorException ex) {
        Logger.getLogger(DraggableTabPane.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(DraggableTabPane.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  /***
   * A component is coming in for landing here
   * @param dtde
   */
  @Override
  public void drop(DropTargetDropEvent dtde) {
    hack_lastDropOperationComponent = null;
    if (!isDragDropAcceptable(dtde.getCurrentDataFlavorsAsList())) {
      dtde.dropComplete(false);
      return;
    }

    int indexOfDrop = indexOfDrop(dtde.getLocation());
    if (indexOfDrop < 0) {
      possibleDest = -1;
      dtde.dropComplete(false);
      repaint();
      return;
    }

    try {
      TabTransferData tdd = (TabTransferData) dtde.getTransferable().getTransferData(TAB_FLAVOR);
      if (indexOfDrop == getTabCount()) {
        addTab(tdd.getTitle(), tdd.getIcon(), tdd.getComponent(), tdd.getTooltip());
      }else{
        try{
          insertTab(tdd.getTitle(), tdd.getIcon(), tdd.getComponent(), tdd.getTooltip(), indexOfDrop);
        }catch (ArrayIndexOutOfBoundsException oobe){
          oobe.printStackTrace();
        }
      }
      setTabComponentAt(indexOfDrop, tdd.getTabComponent());
      setSelectedIndex(indexOfDrop);
      hack_lastDropOperationComponent = tdd.getTabComponent();
    } catch (UnsupportedFlavorException ex) {
      Logger.getLogger(DraggableTabPane.class.getName()).log(Level.SEVERE, null, ex);
      dtde.dropComplete(false);
    } catch (IOException ex) {
      Logger.getLogger(DraggableTabPane.class.getName()).log(Level.SEVERE, null, ex);
      dtde.dropComplete(false);
    }

    dtde.dropComplete(true);
    possibleDest = -1;
    repaint();
  }
  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {}
  @Override
  public void dragExit(DropTargetEvent dte) {possibleDest = -1; repaint();}

  //----- From DragSourceListener --
  // These are called when this panel is the source of the DnD
  @Override
  public void dragDropEnd(DragSourceDropEvent dsde) {}
  @Override
  public void dragEnter(DragSourceDragEvent dsde) {}
  @Override
  public void dragOver(DragSourceDragEvent dsde) {}
  @Override
  public void dropActionChanged(DragSourceDragEvent dsde) {}
  @Override
  public void dragExit(DragSourceEvent dse) {}

}

class TabTransferrable implements Transferable {

  private TabTransferData data;

  TabTransferrable(String title, Icon icon, String tooltip, Component tabComponent, Component st) {
    data = new TabTransferData(title, icon, tooltip, tabComponent, st);
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{DraggableTabPane.TAB_FLAVOR};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavor.getHumanPresentableName().equals(DraggableTabPane.NAME);
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws
          UnsupportedFlavorException, IOException {
    if (!isDataFlavorSupported(flavor)) {
      throw new UnsupportedFlavorException(flavor);
    }
    return data;
  }
}
